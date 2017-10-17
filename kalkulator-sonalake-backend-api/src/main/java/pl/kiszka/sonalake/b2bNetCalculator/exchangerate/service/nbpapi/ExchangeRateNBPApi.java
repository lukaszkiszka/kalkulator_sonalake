package pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service.nbpapi;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service.ExchangeRateService;

@Service
@Slf4j
class ExchangeRateNBPApi implements ExchangeRateService {

	@Value("${api.nbp.exchangerate.url}")
	private String apiURL;

	private final RestTemplate restTemplate;

	public ExchangeRateNBPApi(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	private Optional<BigDecimal> validateResponseAndExtractResult(
			ResponseEntity<ExchangeRatesResponse> responseEntity) {
		if (HttpStatus.OK.equals(responseEntity.getStatusCode()) && responseEntity.hasBody()) {

			ExchangeRatesResponse response = responseEntity.getBody();

			log.debug("NBP API result = {}", response);

			if (response == null || CollectionUtils.isEmpty(response.getRates())
					|| response.getRates().get(0) == null) {
				return Optional.empty();
			}
			String rate = response.getRates().get(0).getMid();

			return Optional.of(new BigDecimal(rate));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<BigDecimal> getExchangeRate(String currencyCode) {

		log.info("currencyCode = [ {} ], apiURL= [ {} ]", currencyCode, apiURL);

		try {

			ResponseEntity<ExchangeRatesResponse> responseEntity = this.restTemplate.getForEntity(apiURL,
					ExchangeRatesResponse.class, currencyCode);

			return validateResponseAndExtractResult(responseEntity);

		} catch (Exception e) {

			log.error("Exception while call NBP API", e);
			return Optional.empty();

		}

	}

}
