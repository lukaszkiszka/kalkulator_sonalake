package pl.kiszka.sonalake.b2bNetCalculator.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface NetCalculatorService {

	String POLAND_COUNTRY_NAME = "Poland";

	List<String> getAvailableCountries();

	Optional<BigDecimal> calculateNetProfit(String countryName, BigDecimal netDailyRate);

}
