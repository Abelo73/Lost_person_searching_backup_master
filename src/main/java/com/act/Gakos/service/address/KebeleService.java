package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.KebeleDto;
import com.act.Gakos.entity.address.Kebele;
import com.act.Gakos.repository.address.KebeleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KebeleService {

    private final static Logger logger = LoggerFactory.getLogger(KebeleService.class);
    private final KebeleRepository kebeleRepository;

    public KebeleService(KebeleRepository kebeleRepository) {
        this.kebeleRepository = kebeleRepository;
    }

    public Kebele addKebele(Kebele kebele) {
        Optional<Kebele> existingKebele = kebeleRepository.findByName(kebele.getName());
        if (existingKebele.isPresent()) {
            logger.info("Kebele with name '{}' already exists, skipping registration.", kebele.getName());
            return null;
        }
        logger.info("Saving new kebele: {}", kebele);
        return kebeleRepository.save(kebele);
    }

    public List<Kebele> addKebeles(List<Kebele> kebeles) {
        List<Kebele> uniqueKebeles = kebeles.stream()
                .filter(k -> !kebeleRepository.existsByName(k.getName()))
                .collect(Collectors.toList());

        logger.info("Saving {} new unique kebeles.", uniqueKebeles.size());
        return kebeleRepository.saveAll(uniqueKebeles);
    }

    public Page<Kebele> getKebeles(PageRequest pageRequest) {
        return kebeleRepository.findAll(pageRequest);
    }

    // Method to search Woredas with optional search term and pagination
    public Page<KebeleDto> searchKebele(String searchTerm, Pageable pageable) {
        Page<Kebele> kebelePages;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            // Search by name if search term is provided
            kebelePages = kebeleRepository.searchKebele(searchTerm, pageable);
            logger.info("Searching for kebele with searchTerm: {}", searchTerm);
        } else {
            // If no search term, return all kebeles
            kebelePages = kebeleRepository.findAll(pageable);
        }

        // Convert Woreda entities to WoredaDto
        Page<KebeleDto> kebeleDtos = kebelePages.map(kebele -> new KebeleDto(
                kebele.getId(),
                kebele.getName()
        ));

        logger.info("Returning paginated result of Woredas - Page number: {}, Page size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return kebeleDtos;
    }



    // Method to search Kebeles by Woreda name
    public Page<Kebele> searchKebeleByWoredaName(String searchTerm, Pageable pageable) {
        logger.info("Searching for Kebeles by Woreda name with searchTerm: {}", searchTerm);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            return kebeleRepository.searchKebeleByWoredaName(searchTerm, pageable);
        } else {
            // If no search term, return an empty page or all Kebeles as per your requirements
            return kebeleRepository.findAll(pageable);
        }
    }
}
