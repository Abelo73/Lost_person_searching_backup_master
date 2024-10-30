package com.act.Gakos.service.address;

import com.act.Gakos.entity.address.Country;
import com.act.Gakos.repository.address.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final static Logger logger = LoggerFactory.getLogger(CountryService.class);

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    public Page<Country> getAllCountry(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }


//    public Page<Country> getCountryByCountryName(String countryName, Pageable pageable){
//        return countryRepository.findByCountryName(countryName, pageable);
//    }

//    public Country addCountry(Country country) {
//
//        if (countryRepository.findByCountryName(country.getCountryName())){
//            logger.info("Country with name '{}' already exists, skipping registartion." , country.getCountryName());
//        }
//        logger.info("Saving new country: {} ", country);
//
//        return countryRepository.save(country);

//    }

    public Country addCountry(Country country) {

        // Check if country with the same name already exists
        Country existingCountry = countryRepository.findByCountryName(country.getCountryName());

        if (existingCountry != null) {
            logger.info("Country with name '{}' already exists, skipping registration.", country.getCountryName());
            return null; // Return null if country already exists
        }

        logger.info("Saving new country: {}", country);
        return countryRepository.save(country); // Save new country if it does not exist
    }
}
