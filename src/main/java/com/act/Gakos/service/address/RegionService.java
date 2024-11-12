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
import java.util.Objects;
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

//    public Region updateRegion(Integer id, RegionDto updatedRegionDto) {
//        // Fetch the existing region
//        Region existingRegion = regionRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id " + id));
//
//        boolean isUpdated = false;
//
//        // Check and update the name if different and not null
//        if (updatedRegionDto.getName() != null && !Objects.equals(existingRegion.getName(), updatedRegionDto.getName())) {
//            log.info("Updating region name from '{}' to '{}'", existingRegion.getName(), updatedRegionDto.getName());
//            existingRegion.setName(updatedRegionDto.getName());
//            isUpdated = true;
//        }
//
//        // Check and update the description if different and not null
//        if (updatedRegionDto.getDescription() != null && !Objects.equals(existingRegion.getDescription(), updatedRegionDto.getDescription())) {
//            log.info("Updating region description from '{}' to '{}'", existingRegion.getDescription(), updatedRegionDto.getDescription());
//            existingRegion.setDescription(updatedRegionDto.getDescription());
//            isUpdated = true;
//        }
//
//        // Check and update the country if different and not null
//        if (updatedRegionDto.getCountryId() != null && (existingRegion.getCountry() == null || !Objects.equals(existingRegion.getCountry().getId(), updatedRegionDto.getCountryId()))) {
//            Country newCountry = countryRepository.findById(updatedRegionDto.getCountryId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Country not found with id " + updatedRegionDto.getCountryId()));
//            log.info("Updating region country from '{}' to '{}'", existingRegion.getCountry().getId(), updatedRegionDto.getCountryId());
//            existingRegion.setCountry(newCountry.getId());
//            isUpdated = true;
//        }
//
//        // Save only if an update occurred
//        if (isUpdated) {
//            log.info("Saving updated region with id {} and data {}", id, existingRegion);
//            return regionRepository.save(existingRegion);
//        } else {
//            log.info("No changes detected for region with id {}, update skipped.", id);
//            return existingRegion;
//        }
//
//    }

    public Region updateRegion(Integer id, RegionDto updatedRegionDto) {
        // Fetch the existing region
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id " + id));

        boolean isUpdated = false;

        // Check and update name
        if (updatedRegionDto.getName() != null && !Objects.equals(existingRegion.getName(), updatedRegionDto.getName())) {
            log.info("Updating region name from '{}' to '{}'", existingRegion.getName(), updatedRegionDto.getName());
            existingRegion.setName(updatedRegionDto.getName());
            isUpdated = true;
        }

        // Check and update description
        if (updatedRegionDto.getDescription() != null && !Objects.equals(existingRegion.getDescription(), updatedRegionDto.getDescription())) {
            log.info("Updating region description from '{}' to '{}'", existingRegion.getDescription(), updatedRegionDto.getDescription());
            existingRegion.setDescription(updatedRegionDto.getDescription());
            isUpdated = true;
        }

        // Check and update country
        if (updatedRegionDto.getCountryId() != null && (existingRegion.getCountry() == null || !Objects.equals(existingRegion.getCountry().getId(), updatedRegionDto.getCountryId()))) {
            Country newCountry = countryRepository.findById(updatedRegionDto.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Country not found with id " + updatedRegionDto.getCountryId()));
            log.info("Updating region country from '{}' to '{}'", existingRegion.getCountry() != null ? existingRegion.getCountry().getId() : null, updatedRegionDto.getCountryId());
            existingRegion.setCountry(newCountry);
            log.info("New Country: {}", newCountry);  // Log the new country to ensure it's correct
            isUpdated = true;
        }

        // Save only if an update occurred
        if (isUpdated) {
            log.info("Saving updated region with id {} and data {}", id, existingRegion);
            return regionRepository.save(existingRegion);
        } else {
            log.info("No changes detected for region with id {}, update skipped.", id);
            return existingRegion;
        }
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
