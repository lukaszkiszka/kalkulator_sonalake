package pl.kiszka.sonalake.b2bNetCalculator.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;

public interface NetCalculatorController {
	
	ResponseEntity<List<String>> getAvailableCountries();
	ResponseEntity<BigDecimal> calculateNetCost(CalculateNetCostRequest request);

}
