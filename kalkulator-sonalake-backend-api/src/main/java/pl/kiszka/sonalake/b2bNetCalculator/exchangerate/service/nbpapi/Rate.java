package pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service.nbpapi;

import lombok.Data;

@Data
public class Rate {

	private String no;
	private String effectiveDate;
	private String mid;

	public Rate() {};

}
