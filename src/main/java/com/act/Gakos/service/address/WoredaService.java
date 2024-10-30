package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.WoredaDto;
import com.act.Gakos.entity.address.Woreda;
import com.act.Gakos.repository.address.WoredaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Page<WoredaDto> woredasDtoPage = woredasPage.map(woreda -> new WoredaDto(woreda.getId(), woreda.getName(), woreda.getDescription()));

        logger.info("Returning paginated result of Woredas - Page number: {}, Page size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return woredasDtoPage;
    }
}
