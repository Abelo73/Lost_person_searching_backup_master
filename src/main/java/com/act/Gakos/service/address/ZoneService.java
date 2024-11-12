package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.ZoneDto;
import com.act.Gakos.entity.address.Zone;
import com.act.Gakos.repository.address.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;
    private static final Logger logger = LoggerFactory.getLogger(ZoneService.class);


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


    public List<ZoneDto> getZonesByRegionId(Long regionId) {
        List<Zone> zones = zoneRepository.findByRegionId(regionId);
        return zones.stream()
                .map(zone -> new ZoneDto(zone.getId(), zone.getName(), zone.getDescription(), zone.getRegionName())) // Assuming ZoneDto has an ID and Name
                .collect(Collectors.toList());
    }

    // Method to convert Zone entity to ZoneDto
//    private ZoneDto convertToDto(Zone zone) {
//        return new ZoneDto(
//                zone.getId(),
//                zone.getName(),
//                zone.getDescription(),
//                zone.getRegionName()
//        );
//    }


    public Page<ZoneDto> searchZoneByRegionId(Long regionId, Pageable pageable) {
        return zoneRepository.searchZoneByRegionId(regionId, pageable);
    }


    public ZoneDto getZoneById(Long id) {
        logger.info("Retrieving zone by ID: {}", id);
        Optional<Zone> zoneOpt = zoneRepository.findById(id);
        if (zoneOpt.isPresent()) {
            return convertToDto(zoneOpt.get());
        }
        return null;
    }

    public Zone updateZone(Long id, Zone updatedZoneData) {
        logger.info("Updating zone with ID: {}", id);
        return zoneRepository.findById(id).map(zone -> {
            zone.setName(updatedZoneData.getName());
            zone.setDescription(updatedZoneData.getDescription());
            zone.setRegion(updatedZoneData.getRegion());
            Zone savedZone = zoneRepository.save(zone);
            logger.info("Zone updated successfully: {}", savedZone);
            return savedZone;
        }).orElse(null);
    }

    public boolean deleteZone(Long id) {
        logger.info("Attempting to delete zone with ID: {}", id);
        if (zoneRepository.existsById(id)) {
            zoneRepository.deleteById(id);
            logger.info("Zone deleted successfully with ID: {}", id);
            return true;
        } else {
            logger.warn("Zone with ID: {} not found", id);
            return false;
        }
    }

    // Convert Zone entity to ZoneDto (if not already present)
    private ZoneDto convertToDto(Zone zone) {
        return new ZoneDto(
                zone.getId(),
                zone.getName(),
                zone.getDescription(),
                zone.getRegionName());
    }
}
