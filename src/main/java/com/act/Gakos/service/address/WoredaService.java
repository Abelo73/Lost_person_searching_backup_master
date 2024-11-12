package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.WoredaDto;
import com.act.Gakos.entity.address.Woreda;
import com.act.Gakos.entity.address.Zone;
import com.act.Gakos.repository.address.WoredaRepository;
import com.act.Gakos.repository.address.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WoredaService {

    private final static Logger logger = LoggerFactory.getLogger(WoredaService.class);

    @Autowired
    private WoredaRepository woredaRepository;

    @Autowired
    private ZoneRepository zoneRepository;




    public Woreda addWoreda(Woreda woreda) {
        Woreda existingWoreda = woredaRepository.findByWoredaName(woreda.getName());

        if (existingWoreda != null){
            logger.info("Woreda with name '{}' already exists, skipping registration.", woreda.getName());
            return null;
        }
        logger.info("Saving new woreda: {}", woreda);
        return woredaRepository.save(woreda);

    }


    public List<Woreda> addWoredas(List<Woreda> woredas) {
        List<Woreda> savedWoredas = new ArrayList<>();
        for (Woreda woreda : woredas) {
            if (woredaRepository.findByWoredaName(woreda.getName()) == null) {
                savedWoredas.add(woredaRepository.save(woreda));
                logger.info("Added new Woreda with name: {}", woreda.getName());
            } else {
                logger.info("Woreda with name '{}' already exists, skipping.", woreda.getName());
            }
        }
        return savedWoredas;
    }

    public Page<Woreda> getWoredas(PageRequest pageRequest) {
        Page<Woreda> woredasPage = woredaRepository.findAll(pageRequest);
        logger.info("Returning paginated result of Woredas - Page number: {}, Page size: {}",
                pageRequest.getPageNumber(), pageRequest.getPageSize());
        return woredasPage;
    }


    // Method to search Woredas with optional search term and pagination
    public Page<WoredaDto> searchWoredas(String searchTerm, Pageable pageable) {
        Page<Woreda> woredasPage;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            // Search by name if search term is provided
            woredasPage = woredaRepository.searchWoredas(searchTerm, pageable);
            logger.info("Searching for Woredas with searchTerm: {}", searchTerm);
        } else {
            // If no search term, return all woredas
            woredasPage = woredaRepository.findAll(pageable);
        }

        // Convert Woreda entities to WoredaDto
        Page<WoredaDto> woredasDtoPage = woredasPage.map(woreda -> new WoredaDto(
                woreda.getId(),
                woreda.getName(),
                woreda.getDescription(),
                woreda.getZone().getId(),
                woreda.getZoneName()
        ));

        logger.info("Returning paginated result of Woredas - Page number: {}, Page size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return woredasDtoPage;
    }


    public Page<WoredaDto> getWoredasByZoneId(Integer zoneId, Pageable pageable) {
        // Fetch the Woredas by zoneId
        Page<Woreda> woredas = woredaRepository.findByZoneId(zoneId, pageable);

        // Convert to List<WoredaDto>
        List<WoredaDto> woredaDtos = woredas.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Return the Page with the DTOs
        return new PageImpl<>(woredaDtos, pageable, woredas.getTotalElements());
    }

    // Manual conversion from Woreda to WoredaDto
    public WoredaDto convertToDto(Woreda woreda) {
        WoredaDto dto = new WoredaDto();
        dto.setId(woreda.getId());
        dto.setName(woreda.getName());
        dto.setDescription(woreda.getDescription());

        if (woreda.getZone() != null) {
            dto.setZoneId(woreda.getZone().getId()); // Set zoneId
            dto.setZoneName(woreda.getZone().getName()); // Set zoneName
        }
        logger.info("====== {}", dto.setZoneName(woreda.getZone().getName()));
        return dto;
    }



    public Page<WoredaDto> searchWoredaByZoneId(Integer zoneId, Pageable pageable) {
        return woredaRepository.searchWoredaByZoneId(zoneId, pageable);
    }

    public Woreda updateWoreda(Integer id, WoredaDto updatedWoredaDto) {
        logger.info("Updating Woreda with ID: {}", id);

        // Fetch the existing Woreda entity by ID
        return woredaRepository.findById(id).map(woreda -> {
            // Get the zoneId from the DTO
            Long zoneId = updatedWoredaDto.getZoneId(); // This should be the zoneId in your DTO

            // Check if zone exists by zoneId
            Zone zone = zoneRepository.findById(zoneId.longValue())  // Use zoneId to find the Zone
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Zone not found"));

            // Set the updated values in the Woreda entity
            woreda.setName(updatedWoredaDto.getName());
            woreda.setDescription(updatedWoredaDto.getDescription());
            woreda.setZone(zone);  // Update the zone based on the new zoneId

            // Save the updated Woreda
            Woreda savedWoreda = woredaRepository.save(woreda);
            logger.info("Woreda updated successfully: {}", savedWoreda);
            return savedWoreda;
        }).orElseThrow(() -> {
            logger.error("Woreda with ID {} not found", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Woreda not found");
        });
    }




    public boolean deleteWoreda(Integer id) {
        if (!woredaRepository.existsById(id)) {
            logger.info("Woreda with id '{}' does not exist.", id);
            return false;
        }

        woredaRepository.deleteById(id);
        logger.info("Deleted Woreda with id '{}'", id);
        return true;
    }

    public WoredaDto findWoredaById(Integer id) {
        Woreda woreda = woredaRepository.findById(id).orElse(null);

        if (woreda == null) {
            logger.info("Woreda with id '{}' not found.", id);
            return null;
        }

        WoredaDto woredaDto = convertToDto(woreda);
        logger.info("Fetched Woreda: {}", woredaDto);
        return woredaDto;
    }

}
