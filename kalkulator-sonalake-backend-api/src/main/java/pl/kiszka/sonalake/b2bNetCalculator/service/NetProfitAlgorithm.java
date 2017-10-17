package pl.kiszka.sonalake.b2bNetCalculator.service;

import java.math.BigDecimal;

public interface NetProfitAlgorithm {

	static final BigDecimal WORKING_MONTH_DAYS = new BigDecimal(22);

	BigDecimal calculate(BigDecimal netDailyRate, int taxRate, BigDecimal fixedCost);

}
