package com.act.Gakos.repository.address;

import com.act.Gakos.entity.address.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    boolean existsByCountryName(String countryName);

    Country findByCountryName(String countryName);

//    Page<Country> findByCountryName(String countryName, Pageable pageable);
}
