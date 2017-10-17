package pl.kiszka.sonalake.b2bNetCalculator.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;
import pl.kiszka.sonalake.b2bNetCalculator.service.NetCalculatorService;

@RestController
@RequestMapping("/calculator")
@Slf4j
class DefaultNetCalculatorController implements NetCalculatorController {

	private NetCalculatorService netCalculatorService;

	@Autowired
	public DefaultNetCalculatorController(NetCalculatorService netCalculatorService) {
		this.netCalculatorService = netCalculatorService;
	}

	@Override
	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, path = "/countries")
	public ResponseEntity<List<String>> getAvailableCountries() {

		return new ResponseEntity<List<String>>(netCalculatorService.getAvailableCountries(), HttpStatus.OK);

	}

	private boolean isValidCalculateNetCostRequest(CalculateNetCostRequest request) {

		boolean isInvalidCountryName = netCalculatorService.getAvailableCountries().stream()
				.noneMatch(elem -> elem.equals(request.getCountryName()));

		return !(request == null || Strings.isNullOrEmpty(request.getCountryName()) || request.getNetDailyRate() == null
				|| BigDecimal.ZERO.compareTo(request.getNetDailyRate()) >= 0 || isInvalidCountryName);

	}

	@Override
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/calculate")
	public ResponseEntity<BigDecimal> calculateNetCost(@RequestBody CalculateNetCostRequest request) {

		log.info("request body = {}", request);

		if (!isValidCalculateNetCostRequest(request)) {
			log.warn("Request is not valid");
			return new ResponseEntity<BigDecimal>(HttpStatus.BAD_REQUEST);
		}

		Optional<BigDecimal> netProfit = netCalculatorService.calculateNetProfit(request.getCountryName(),
				request.getNetDailyRate());

		if (netProfit.isPresent()) {
			return new ResponseEntity<BigDecimal>(netProfit.get(), HttpStatus.OK);
		} else {

			return new ResponseEntity<BigDecimal>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
