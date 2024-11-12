package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.RegionDto;
import com.act.Gakos.entity.address.Country;
import com.act.Gakos.entity.address.Region;
import com.act.Gakos.exceptions.ResourceNotFoundException;
import com.act.Gakos.repository.address.CountryRepository;
import com.act.Gakos.repository.address.RegionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionService {

    private final static Logger log = LoggerFactory.getLogger(RegionService.class); // Use RegionService class for logging

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CountryRepository countryRepository;



    public Page<RegionDto> getRegionsReg(String searchTerm, int page, int size, Sort name) {
        Pageable pageable = PageRequest.of(page, size, name);

        Page<Region> regionPage;


        if (searchTerm != null && !searchTerm.isEmpty()) {
            regionPage = regionRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
        } else {
            regionPage = regionRepository.findAll(pageable);
        }

        List<RegionDto> regionDtos = regionPage.getContent()
                .stream()
                .map(this::convertToDto) // Create a method to convert Region to RegionDto
                .collect(Collectors.toList());

        return new PageImpl<>(regionDtos, pageable, regionPage.getTotalElements());
    }

    private RegionDto convertToDto(Region region) {
        Integer countryId = (region.getCountry() != null) ? region.getCountry().getId() : null;

        return new RegionDto(
                region.getId(),
                region.getName(),
                region.getDescription(),
                countryId  // Use null if country is not set
        );
    }


//    private RegionDto convertToDto(Region region) {
//        return new RegionDto(
//                region.getId(),
//                region.getName(),
//                region.getDescription(),
//                region.getCountry().getId()
//        );
//    }




    // Get a single region by ID
    public Optional<Region> getRegionById(Integer id) {
        return regionRepository.findById(id);
    }

    public Region addRegion(Region region) {
        // Check for existing region by name
        if (regionRepository.existsByName(region.getName())) {
            throw new IllegalArgumentException("Region with name " + region.getName() + " already exists.");
        }
        return regionRepository.save(region);
    }



    // Get regions by country with optional search term and pagination
    public Page<Region> getRegionsByCountry(String country, String searchTerm, int page, int size, Sort name) {
        PageRequest pageRequest = PageRequest.of(page, size);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            // Search by country and search term if provided
            return regionRepository.findByCountryAndNameContainingIgnoreCase(country, searchTerm, pageRequest);
        } else {
            // Search by country only
            return regionRepository.findByCountry(country, pageRequest);
        }
    }

    public List<Region> getRegionsByCountryId(Integer countryId) {
        return regionRepository.findByCountryId(countryId);
    }

    public Page<RegionDto> searchRegionByCountryId(Long countryId, Pageable pageable) {
        return regionRepository.searchRegionByCountryId(countryId, pageable);
    }

    // Update region method
// Update region method
    @Transactional
    public RegionDto updateRegion(Integer id, RegionDto regionDto) {
        log.info("Attempting to update region with ID: {}", id);

        // Find existing region by ID
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Region with ID {} not found", id);
                    return new ResourceNotFoundException("Region not found with id " + id);
                });

        log.info("Found existing region: {}", existingRegion);

        // Update fields
        existingRegion.setName(regionDto.getName());
        existingRegion.setDescription(regionDto.getDescription());
        log.debug("Updated region name to: {}, description to: {}", regionDto.getName(), regionDto.getDescription());

        // Fetch the country using countryId from RegionDto
        Country country = countryRepository.findById(regionDto.getCountryId())
                .orElseThrow(() -> {
                    log.error("Country with ID {} not found", regionDto.getCountryId());
                    return new ResourceNotFoundException("Country not found with id: " + regionDto.getCountryId());
                });

        log.info("Found country with ID: {}", country.getId());

        // Set the Country object to the Region
        existingRegion.setCountry(country.getId());
        log.info("saving country id to :{}", country.getId());

        // Save the updated region
        Region savedRegion = regionRepository.save(existingRegion);
        log.info("Region updated successfully: {}", savedRegion);

        // Convert to DTO and return
        return convertToDto(savedRegion);
    }



    // Delete region method
    @Transactional
    public void deleteRegion(Integer id) {
        if (!regionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Region not found with id " + id);
        }
        regionRepository.deleteById(id);
    }
}
