package pl.kiszka.sonalake.b2bNetCalculator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import pl.kiszka.sonalake.b2bNetCalculator.entity.CountryContractCost;

public interface CountryContractCostRepository extends Repository<CountryContractCost, Long> {

	@Query("SELECT t.countryName FROM CountryContractCost t")
	List<String> findAllCountryName();
	Optional<CountryContractCost> findByCountryName(String countryName);

}
