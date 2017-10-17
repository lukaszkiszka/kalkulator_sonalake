package pl.kiszka.sonalake.b2bNetCalculator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class DefaultNetProfitAlgorithm implements NetProfitAlgorithm {

	@Override
	public BigDecimal calculate(BigDecimal netDailyRate, int taxRate, BigDecimal fixedCost) {

		log.info("input: netDailyRate=[ {} ], taxRate=[ {} ], fixedCost=[ {} ]", netDailyRate, taxRate, fixedCost);

		BigDecimal monthRate = netDailyRate.multiply(NetProfitAlgorithm.WORKING_MONTH_DAYS);
		log.debug("monthRate=[ {} ]", monthRate);

		BigDecimal taxCost = monthRate.multiply(BigDecimal.valueOf((double) taxRate / 100));
		log.debug("taxCost=[ {} ]", taxCost);

		BigDecimal results = monthRate.subtract(taxCost).subtract(fixedCost);
		log.debug("results=[ {} ]", results);

		return results;

	}

}
