package pl.kiszka.sonalake.b2bNetCalculator.exchangerate.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandExchangeRateService extends HystrixCommand<Optional<BigDecimal>> {

	private final String currencyCode;
	private ExchangeRateService exchangeRateService;

	public CommandExchangeRateService(ExchangeRateService exchangeRateService, String currencyCode) {
		super(HystrixCommandGroupKey.Factory.asKey("nbpApi"));

		this.exchangeRateService = exchangeRateService;
		this.currencyCode = currencyCode;

	}

	@Override
	protected Optional<BigDecimal> run() throws Exception {

		return exchangeRateService.getExchangeRate(currencyCode);

	}

	@Override
	protected Optional<BigDecimal> getFallback() {
		return Optional.empty();
	}

}
