package pl.kiszka.sonalake.b2bNetCalculator.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.kiszka.sonalake.b2bNetCalculator.entity.CountryContractCost;
import pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service.CommandExchangeRateService;
import pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service.ExchangeRateService;
import pl.kiszka.sonalake.b2bNetCalculator.repository.CountryContractCostRepository;

@Service
@Slf4j
class DefaultNetCalculatorService implements NetCalculatorService {

	private ExchangeRateService exchangeRateService;
	private CountryContractCostRepository countryContractCostRepository;
	private NetProfitAlgorithm netProfitAlgorithm;

	@Autowired
	public DefaultNetCalculatorService(ExchangeRateService exchangeRateService,
			CountryContractCostRepository countryContractCostRepository, NetProfitAlgorithm netProfitAlgorithm) {

		this.exchangeRateService = exchangeRateService;
		this.countryContractCostRepository = countryContractCostRepository;
		this.netProfitAlgorithm = netProfitAlgorithm;

	}

	@Override
	public List<String> getAvailableCountries() {

		List<String> countries = countryContractCostRepository.findAllCountryName();

		if (log.isDebugEnabled()) {
			countries.stream().map(Object::toString).forEach(log::debug);
		}

		return countries;

	}

	private boolean countryIsPoland(String countryName) {
		return NetCalculatorService.POLAND_COUNTRY_NAME.equals(countryName);
	
	}

	private Optional<BigDecimal> calculateExchangeRate(String countryName, String currencyCode) {
		
		log.info("input countryName=[ {} ], currencyCode=[ {} ]", countryName, currencyCode);
	
		if (countryIsPoland(countryName)) {
			return Optional.of(BigDecimal.ONE);
		} else {
			return new CommandExchangeRateService(exchangeRateService, currencyCode).execute();
		}
	}

	@Override
	public Optional<BigDecimal> calculateNetProfit(String countryName, BigDecimal netDailyRate) {

		log.info("countryName = [ {} ], grossProfit = [ {} ]", countryName, netDailyRate);

		try {

			Optional<CountryContractCost> optionalCostEntity = countryContractCostRepository
					.findByCountryName(countryName);

			if (optionalCostEntity.isPresent()) {

				CountryContractCost costEntity = optionalCostEntity.get();

				BigDecimal netProfit = netProfitAlgorithm.calculate(netDailyRate, costEntity.getTaxRate(),
						costEntity.getFixedCosts());

				Optional<BigDecimal> optionalExchangeRate = calculateExchangeRate(countryName,
						costEntity.getCurrencyCode());

				if (optionalExchangeRate.isPresent()) {

					return Optional
							.of(netProfit.multiply(optionalExchangeRate.get()).setScale(2, BigDecimal.ROUND_HALF_EVEN));

				} else {

					log.error("calculateExchangeRate not return value");
					return Optional.empty();

				}

			} else {

				log.error("Not found entity by countryName=[{}]", countryName);
				return Optional.empty();

			}

		} catch (Exception e) {
			log.error("Exception while evaluate calculateNetProfit", e);
			return Optional.empty();
		}

	}

}
