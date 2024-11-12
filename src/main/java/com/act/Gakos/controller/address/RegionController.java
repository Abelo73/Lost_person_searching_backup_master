package com.act.Gakos.controller.address;

import com.act.Gakos.dto.address.RegionDto;
import com.act.Gakos.entity.address.Region;
import com.act.Gakos.repository.address.RegionRepository;
import com.act.Gakos.service.address.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    private final static Logger log = LoggerFactory.getLogger(RegionController.class);

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



    @GetMapping
    public ResponseEntity<Page<Region>> getRegions(
            @RequestParam(value = "countryId", required = false) Integer countryId,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Region> regions;

        if (countryId != null) {
            // If countryId is provided, fetch regions by countryId
            List<Region> regionList = regionService.getRegionsByCountryId(countryId);
            regions = new PageImpl<>(regionList, PageRequest.of(page, size), regionList.size());
        } else {
            // Existing logic to get regions by country (if no countryId is provided)
            regions = regionService.getRegionsByCountry("Ethiopia", searchTerm, page, size, Sort.by("name").ascending());
        }

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

    @GetMapping("/search")
    public Page<RegionDto> searchRegionByCountryId(
            @RequestParam(value = "country", required = false) Long country,
            @RequestParam(value = "sort", defaultValue = "name", required = false) String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Sort sorting = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sorting);
        return regionService.searchRegionByCountryId(country, pageable);
    }

    @PostMapping("/search")
    public ResponseEntity<Region> saveRegion(@RequestBody Region region) {
        Region createdRegion = regionService.addRegion(region);
        return new ResponseEntity<>(createdRegion, HttpStatus.CREATED);
    }


//    @PutMapping("search/{id}")
//    public ResponseEntity<RegionDto> updateRegion(
//            @PathVariable Integer id,
//            @RequestBody RegionDto regionDto) {
//        RegionDto updatedRegion = regionService.updateRegion(id, regionDto);
//        return ResponseEntity.ok(updatedRegion);
//    }

    @PutMapping("search/{id}")
    public ResponseEntity<RegionDto> updateRegion(
            @PathVariable Integer id,
            @RequestBody RegionDto regionDto) {

        log.info("checking for id:{} ", id);

        // Check if the ID is valid (null or zero should be invalid in most cases)
        if (id == null || id <= 0) {
            log.info("Id is not null or greater than zero");
            return ResponseEntity.badRequest().body(null);  // Or provide a meaningful error message
        }

        try {
            RegionDto updatedRegion = regionService.updateRegion(id, regionDto);
            log.info("Updating region data: {}", updatedRegion);
            if (updatedRegion == null) {
                log.info("Updating region data is null");
                return ResponseEntity.notFound().build();  // If region is not found
            }
            log.info("Updated successfully: {}", updatedRegion);
            return ResponseEntity.ok(updatedRegion);  // Return the updated region
        } catch (IllegalArgumentException e) {
            // This catches cases where, for example, the region is not found
            log.info("Region is not found");
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // For other unexpected exceptions
            log.info("Other error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);  // Or provide a message indicating failure
        }
    }

    @DeleteMapping("search/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Integer id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
