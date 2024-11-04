package com.act.Gakos.controller.address;

import com.act.Gakos.dto.address.KebeleDto;
import com.act.Gakos.dto.address.WoredaDto;
import com.act.Gakos.entity.address.Kebele;
import com.act.Gakos.service.address.KebeleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kebele")
public class KebeleController {

    private final static Logger logger = LoggerFactory.getLogger(KebeleController.class);
    private final KebeleService kebeleService;

    public KebeleController(KebeleService kebeleService) {
        this.kebeleService = kebeleService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Kebele>> getKebeles(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<Kebele> kebeles = kebeleService.getKebeles(PageRequest.of(page, size, Sort.by("name").ascending()));
        logger.info("Fetched page {} of Kebeles with size {}", page, size);
        return ResponseEntity.ok(kebeles);
    }


    @GetMapping
    public ResponseEntity<Page<KebeleDto>> getKebelesByWoredaId(
            @RequestParam(value = "woredaId") Integer woredaId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<KebeleDto> kebeleDtos = kebeleService.getKebelesByWoredaId(woredaId, pageable);
        logger.info("Fetched Kebeles for Woreda ID {} on page {}", woredaId, page);

        return ResponseEntity.ok(kebeleDtos);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> addKebeles(@RequestBody List<Kebele> kebeles) {
        List<Kebele> savedKebeles = kebeleService.addKebeles(kebeles);

        if (savedKebeles.isEmpty()) {
            logger.info("All Kebeles in the list already exist.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("All Kebeles in the list already exist.");
        }
        logger.info("Successfully added {} new Kebeles.", savedKebeles.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedKebeles);
    }

    @PostMapping("/addKebele")
    public ResponseEntity<?> addKebele(@RequestBody Kebele kebele) {
        Kebele savedKebele = kebeleService.addKebele(kebele);

        if (savedKebele == null) {
            logger.info("Kebele with name '{}' already exists.", kebele.getName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Kebele with this name already exists.");
        }
        return ResponseEntity.ok(savedKebele);
    }




    // Endpoint for searching Kebeles by Woreda name
    @GetMapping("/search/kebeleByWoredaName")
    public ResponseEntity<Page<KebeleDto>> searchKebeleByWoredaName(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Kebele> kebeles = kebeleService.searchKebeleByWoredaName(searchTerm, pageable);

        // Convert the Page<Kebele> to Page<KebeleDto> if necessary, assuming you have a conversion method
        Page<KebeleDto> kebeleDtos = kebeles.map(this::convertToDto); // Create this conversion method

        return new ResponseEntity<>(kebeleDtos, HttpStatus.OK);
    }

    private KebeleDto convertToDto(Kebele kebele) {
        return new KebeleDto(
                kebele.getId(),
                kebele.getName()
        );
    }




}
