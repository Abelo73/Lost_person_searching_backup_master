package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.WoredaDto;
import com.act.Gakos.dto.address.ZoneDto;
import com.act.Gakos.entity.address.Woreda;
import com.act.Gakos.entity.address.Zone;
import com.act.Gakos.repository.address.WoredaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WoredaService {

    private final static Logger logger = LoggerFactory.getLogger(WoredaService.class);

    @Autowired
    private WoredaRepository woredaRepository;




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
    private WoredaDto convertToDto(Woreda woreda) {
        WoredaDto dto = new WoredaDto();
        dto.setId(woreda.getId());
        dto.setName(woreda.getName());
        dto.getDescription();
        // Set other fields as necessary
        return dto;
    }
}
