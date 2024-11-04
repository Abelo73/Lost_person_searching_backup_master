package com.act.Gakos.service;

import com.act.Gakos.dto.address.RegionDto;
import com.act.Gakos.entity.address.Region;
import com.act.Gakos.repository.address.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    // Add a new region


    // Get paginated list of regions with optional search by name or description
//    public Page<Region> getRegions(String searchTerm, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        if (searchTerm != null && !searchTerm.isEmpty()) {
//            return regionRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
//        } else {
//            return regionRepository.findAll(pageable);
//        }
//    }


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
        return new RegionDto(
                region.getId(),
                region.getName(),
                region.getDescription());
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
}
