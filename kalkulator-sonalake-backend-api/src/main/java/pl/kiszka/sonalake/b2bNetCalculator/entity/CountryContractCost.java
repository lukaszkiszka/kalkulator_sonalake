package pl.kiszka.sonalake.b2bNetCalculator.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_country_contract_cost", indexes = {

		@Index(name = "unique_field", columnList = "country_name,currency_code", unique = true) })
public class CountryContractCost implements Serializable {

	private static final long serialVersionUID = 6358856365635599199L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "country_name")
	private String countryName;
	@Column(name = "currency_code")
	private String currencyCode;
	@Column(name = "tax_rate")
	private int taxRate;
	@Column(name = "fixed_costs")
	private BigDecimal fixedCosts;

}
