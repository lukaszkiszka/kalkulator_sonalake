package pl.kiszka.sonalake.b2bNetCalculator;

import java.util.Collections;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCircuitBreaker
@EnableSwagger2
public class SonalakeCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SonalakeCalculatorApplication.class, args);

	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("pl.kiszka.sonalake.b2bNetCalculator.controller")).build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Net profit calculator", "Calculate net profit from contract", "1.0", "",
				new Contact("Lukasz Kiszka", "http://dataworm.pl/", "lukasz.kiszka@outlook.com"), null, null, Collections.emptyList());
	}

	@Bean
	ServletRegistrationBean h2servletRegistration() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
		registrationBean.addUrlMappings("/h2/*");
		return registrationBean;
	}

}
