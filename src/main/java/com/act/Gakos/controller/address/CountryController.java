package com.act.Gakos.controller.address;

import com.act.Gakos.entity.address.Country;
import com.act.Gakos.service.address.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
public class CountryController {


    private final CountryService countryService;


    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }


    @GetMapping("/country")
    public Page<Country> getAllCountry(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
//        Pageable pageable = PageRequest.of(page, size);

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

}
