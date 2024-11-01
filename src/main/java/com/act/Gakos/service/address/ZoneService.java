package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.ZoneDto;
import com.act.Gakos.entity.address.Zone;
import com.act.Gakos.repository.address.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public Zone addZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    public List<Zone> addZonesBulk(List<Zone> zones) {
        return zoneRepository.saveAll(zones);
    }


    public boolean existsByNameAndRegionId(String name, Long regionId) {
        return zoneRepository.existsByNameAndRegionId(name, regionId);
    }

    // New method for paginated and searchable zones
    public Page<Zone> getZones(String searchTerm, int page, int size, Sort name) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchTerm == null || searchTerm.isEmpty()) {
            return zoneRepository.findAll(pageable);
        }
        return zoneRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
    }

    public Page<ZoneDto> searchZones(String searchTerm, int page, int size, Sort name) {
        Pageable pageable = PageRequest.of(page, size, name);
        Page<Zone> zonePage;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            // If a search term is provided, search by name or description
            zonePage = zoneRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
        } else {
            // If no search term, get all zones
            zonePage = zoneRepository.findAll(pageable);
        }

        // Convert the list of Zone entities to ZoneDto
        List<ZoneDto> zoneDtos = zonePage.getContent()
                .stream()
                .map(this::convertToDto) // Create a method to convert Zone to ZoneDto
                .collect(Collectors.toList());

        return new PageImpl<>(zoneDtos, pageable, zonePage.getTotalElements());
    }

    // Method to convert Zone entity to ZoneDto
    private ZoneDto convertToDto(Zone zone) {
        return new ZoneDto(
                zone.getId(),
                zone.getName(),
                zone.getDescription()
        );
    }
}
