package com.act.Gakos.controller.address;

import com.act.Gakos.dto.address.RegionDto;
import com.act.Gakos.entity.address.Region;
import com.act.Gakos.repository.address.RegionRepository;
import com.act.Gakos.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionRepository regionRepository;

    // Endpoint to add a new region
    @PostMapping
    public ResponseEntity<Region> addRegion(@RequestBody Region region) {
        Region createdRegion = regionService.addRegion(region);
        return new ResponseEntity<>(createdRegion, HttpStatus.CREATED);
    }

    @GetMapping("/search/reg")
    public ResponseEntity<Page<RegionDto>> getRegionsReg(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<RegionDto> regions = regionService.getRegionsReg(searchTerm, page, size, Sort.by("name").ascending());
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    // Endpoint to get a specific region by ID
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Integer id) {
        Optional<Region> region = regionService.getRegionById(id);
        return region.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to get paginated regions by country with optional search by name or description
    @GetMapping
    public ResponseEntity<Page<Region>> getRegions(
            @RequestParam(value = "country", defaultValue = "Ethiopia") String country,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Region> regions = regionService.getRegionsByCountry(country, searchTerm, page, size, Sort.by("name").ascending());
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }


    @PostMapping("/bulk")
    public ResponseEntity<List<String>> addRegionsBulk(@RequestBody List<Region> regions) {
        List<String> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (Region region : regions) {
            if (regionRepository.existsByName(region.getName())) {
                errors.add("Duplicate region: " + region.getName());
            } else {
                regionRepository.save(region);
                results.add(region.getName());
            }
        }

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT); // 409 Conflict for duplicates
        }

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }



}
