package pl.kiszka.sonalake.b2bNetCalculator.controller;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CalculateNetCostRequest {

	private String countryName;
	private BigDecimal	 netDailyRate;

}
