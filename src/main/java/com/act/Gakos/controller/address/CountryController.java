package com.act.Gakos.controller.address;

import com.act.Gakos.entity.address.Country;
import com.act.Gakos.service.address.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/address")
public class CountryController {


    private final CountryService countryService;
    private final static Logger logger = LoggerFactory.getLogger(CountryController.class);



    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }


    @GetMapping("/country")
    public Page<Country> getAllCountry(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("countryName").ascending());

        Page<Country> countries = countryService.getAllCountry(pageable);
        return countries;

    }

    @PostMapping("/country")
    public ResponseEntity<?> addCountry(@RequestBody Country country) {

        Country savedCountry = countryService.addCountry(country);
        if (savedCountry == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Country with this name already exists.");
        }

        return ResponseEntity.ok(savedCountry);
    }

    @GetMapping("/country/{id}")
    public ResponseEntity<?> getCountryById(@PathVariable Integer id) {
        logger.info("Fetching country with ID: {}", id);
        Optional<Country> country = countryService.getCountryById(id);
        return country.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Country with ID '{}' not found", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @PutMapping("/country/{id}")
    public ResponseEntity<?> updateCountry(@PathVariable Integer id, @RequestBody Country country) {
        logger.info("Updating country with ID: {}", id);
        Optional<Country> updatedCountry = countryService.updateCountry(id, country);
        return updatedCountry.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Country with ID '{}' not found for update", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @DeleteMapping("/country/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable Integer id) {
        logger.info("Deleting country with ID: {}", id);
        boolean isDeleted = countryService.deleteCountry(id);
        if (isDeleted) {
            logger.info("Country with ID '{}' deleted successfully", id);
            return ResponseEntity.ok("Country deleted successfully");
        } else {
            logger.warn("Country with ID '{}' not found for deletion", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Country not found");
        }
    }

}
