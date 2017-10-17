package pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service.nbpapi;

import java.util.List;

import lombok.Data;

@Data
public class ExchangeRatesResponse {

	private char table;
	private String currency;
	private String code;
	private List<Rate> rates;
	
	public ExchangeRatesResponse() {};
}
