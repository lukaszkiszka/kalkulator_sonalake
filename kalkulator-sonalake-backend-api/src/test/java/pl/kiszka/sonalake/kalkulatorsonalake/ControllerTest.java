package pl.kiszka.sonalake.kalkulatorsonalake;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import pl.kiszka.sonalake.b2bNetCalculator.SonalakeCalculatorApplication;
import pl.kiszka.sonalake.b2bNetCalculator.controller.CalculateNetCostRequest;
import pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service.ExchangeRateService;
import pl.kiszka.sonalake.b2bNetCalculator.service.NetCalculatorService;
import pl.kiszka.sonalake.b2bNetCalculator.service.NetProfitAlgorithm;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SonalakeCalculatorApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class ControllerTest {

	@MockBean
	private ExchangeRateService exchangeRateService;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private NetProfitAlgorithm netProfitAlgorithm;

	@Autowired
	private NetCalculatorService netCalculatorService;

	private HttpEntity<CalculateNetCostRequest> createHttpEntity(String countryName, BigDecimal netDailyRate) {
		CalculateNetCostRequest calculateNetCostRequest = new CalculateNetCostRequest();
		calculateNetCostRequest.setCountryName(countryName);
		calculateNetCostRequest.setNetDailyRate(netDailyRate);
		HttpEntity<CalculateNetCostRequest> requestContent = new HttpEntity<>(calculateNetCostRequest);
		return requestContent;
	}

	private ResponseEntity<BigDecimal> callCalculate(HttpEntity<CalculateNetCostRequest> requestContent,
			String currency) {

		given(this.exchangeRateService.getExchangeRate(currency)).willReturn(Optional.of(new BigDecimal(4)));
		return this.restTemplate.postForEntity("/calculator/calculate", requestContent, BigDecimal.class);

	}

	@Test
	public void controllerShouldCalculateNetProfitForGermany() {

		HttpEntity<CalculateNetCostRequest> requestContent = createHttpEntity("Germany", new BigDecimal(100));
		ResponseEntity<BigDecimal> response = callCalculate(requestContent, "EUR");
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody());
		assertEquals(0, response.getBody().compareTo(new BigDecimal(3840)));

		log.info("Results is {}", response.getBody());

	}

	@Test
	public void controllerShouldCalculateNetProfitForPoland() {

		HttpEntity<CalculateNetCostRequest> requestContent = createHttpEntity("Poland", new BigDecimal(100));
		ResponseEntity<BigDecimal> response = callCalculate(requestContent, "PLN");
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody());
		assertEquals(0, response.getBody().compareTo(new BigDecimal(582)));

		log.info("Results is {}", response.getBody());

	}

	@Test
	public void controllerShouldCalculateNetProfitForGreatBritain() {

		HttpEntity<CalculateNetCostRequest> requestContent = createHttpEntity("Great Britain", new BigDecimal(100));
		ResponseEntity<BigDecimal> response = callCalculate(requestContent, "GBP");
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody());
		assertEquals(0, response.getBody().compareTo(new BigDecimal(4200)));

		log.info("Results is {}", response.getBody());

	}

	@Test
	public void controllerShouldReturnBadRequestForWrongCountry() {

		HttpEntity<CalculateNetCostRequest> requestContent = createHttpEntity("China", new BigDecimal(100));
		ResponseEntity<BigDecimal> response = callCalculate(requestContent, "GBP");
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

		log.info("Results is {}", response.getStatusCode());

	}

	@Test
	public void controllerShouldReturnBadRequestForZeroNetDailyRate() {

		HttpEntity<CalculateNetCostRequest> requestContent = createHttpEntity("Great Britain", new BigDecimal(0));
		ResponseEntity<BigDecimal> response = callCalculate(requestContent, "GBP");
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

		log.info("Results is {}", response.getStatusCode());

	}

	@Test
	public void shouldReturnBadRequestForNegativeNetDailyRate() {

		HttpEntity<CalculateNetCostRequest> requestContent = createHttpEntity("Great Britain", new BigDecimal(-1));
		ResponseEntity<BigDecimal> response = callCalculate(requestContent, "GBP");
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

		log.info("Results is {}", response.getStatusCode());

	}

	@Test
	public void controllerShouldReturnThreeCounties() {

		ResponseEntity<String[]> response = this.restTemplate.getForEntity("/calculator/countries", String[].class);

		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(3, response.getBody().length);

		log.info("Results is {}", Arrays.toString(response.getBody()));

	}

	@Test
	public void algorithmShouldReturnCorrectResult() throws Exception {

		BigDecimal results = netProfitAlgorithm.calculate(new BigDecimal(100), 19, new BigDecimal(1200));
		assertNotNull(results);
		assertEquals(0, results.compareTo(new BigDecimal(582)));

	}

	@Test
	public void serviceShouldReturnThreeCountries() {

		List<String> countries = netCalculatorService.getAvailableCountries();
		assertTrue(!CollectionUtils.isEmpty(countries));
		assertEquals(3, countries.size());

		log.info("Results is {}", countries);

	}

	@Test
	public void serviceShouldCalculateCorrectResultForGermany() {

		given(this.exchangeRateService.getExchangeRate("EUR")).willReturn(Optional.of(new BigDecimal(4)));

		Optional<BigDecimal> results = netCalculatorService.calculateNetProfit("Germany", new BigDecimal(100));

		assertNotNull(results);
		assertTrue(results.isPresent());
		assertEquals(0, results.get().compareTo(new BigDecimal(3840)));

		log.info("Results is {}", results.get());

	}

	@Test
	public void serviceShouldReturnEmptyValueWhenCountryNotInDatabase() {

		given(this.exchangeRateService.getExchangeRate("EUR")).willReturn(Optional.of(new BigDecimal(4)));

		Optional<BigDecimal> results = netCalculatorService.calculateNetProfit("China", new BigDecimal(100));

		assertNotNull(results);
		assertFalse(results.isPresent());

	}

	@Test
	public void serviceShouldReturnEmptyValueWhenExchangeRateServiceReturnEmpty() {

		given(this.exchangeRateService.getExchangeRate("EUR")).willReturn(Optional.empty());

		Optional<BigDecimal> results = netCalculatorService.calculateNetProfit("Germany", new BigDecimal(100));

		assertNotNull(results);
		assertFalse(results.isPresent());

	}

}
