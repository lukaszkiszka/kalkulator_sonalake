package pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateService {

	Optional<BigDecimal> getExchangeRate(String currencyCode);

}
