package com.act.Gakos.controller.address;

import com.act.Gakos.dto.address.WoredaDto;
import com.act.Gakos.dto.address.ZoneDto;
import com.act.Gakos.entity.address.Region;
import com.act.Gakos.entity.address.Woreda;
import com.act.Gakos.service.address.WoredaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/woreda")
public class WoredaController {

    private final static Logger logger = LoggerFactory.getLogger(WoredaController.class);
    private final WoredaService woredaService;


    public WoredaController(WoredaService woredaService) {
        this.woredaService = woredaService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Woreda>> getWoredas(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Page<Woreda> woredas = woredaService.getWoredas(PageRequest.of(page, size, Sort.by("name").ascending()));
        logger.info("Fetched page {} of Woredas with size {}", page, size, Sort.by("countryName").ascending());
        return ResponseEntity.ok(woredas);
    }

    @GetMapping
    public ResponseEntity<Page<WoredaDto>> getWoredasByZone(
            @RequestParam(name = "zoneId", required = true) Integer zoneId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<WoredaDto> woredas = woredaService.getWoredasByZoneId(zoneId, PageRequest.of(page, size, Sort.by("name").ascending()));
        logger.info("Fetched page {} of Woredas for zoneId {} with size {}", page, zoneId, size);
        return ResponseEntity.ok(woredas);
    }




    @PostMapping("/bulk")
    public ResponseEntity<?> addWoredas(@RequestBody List<Woreda> woredas){
        List<Woreda> savedWoredas = woredaService.addWoredas(woredas);

        if (savedWoredas.isEmpty()){
            logger.info("All Woredas in the list already exist.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("All Woredas in the list already exist.");

        }
        logger.info("Successfully added {} new Woredas.", savedWoredas.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWoredas);

    }


    @PostMapping("/addWoreda")
    public ResponseEntity<?> addWoreda(@RequestBody Woreda woreda){
        Woreda savedWoreda = woredaService.addWoreda(woreda);

        if (savedWoreda == null){
            logger.info("Woreda with name '{}' is already exists.", woreda.getName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Woreda with this name already exists.");
        }
        return ResponseEntity.ok(savedWoreda);
    }

    // Endpoint for searching Woredas
    @GetMapping("/search/woreda")
    public ResponseEntity<Page<WoredaDto>> searchWoredas(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<WoredaDto> woredasDto = woredaService.searchWoredas(searchTerm, pageable);
        return new ResponseEntity<>(woredasDto, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> saveWoreda(@RequestBody Woreda woreda){
        Woreda savedWoreda = woredaService.addWoreda(woreda);

        if (savedWoreda == null){
            logger.info("Woreda with name '{}' is already exists.", woreda.getName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Woreda with this name already exists.");
        }
        return ResponseEntity.ok(savedWoreda);
    }

    @PutMapping("/search/{id}")
    public ResponseEntity<?> updateWoreda(
            @PathVariable Integer id,
            @RequestBody WoredaDto woredaDto) {

        Woreda updatedWoreda = woredaService.updateWoreda(id, woredaDto);
        if (updatedWoreda == null) {
            logger.info("Woreda with id '{}' not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Woreda not found.");
        }

        logger.info("Updated Woreda with id '{}'", id);
        return ResponseEntity.ok(updatedWoreda);
    }


    @GetMapping("/search")
    public Page<WoredaDto> searchWoredaByZoneId(
            @RequestParam(value = "zone", required = false) Integer zone,
            Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.asc("name")));

        return woredaService.searchWoredaByZoneId(zone, pageable);
    }

    @DeleteMapping("/search/{id}")
    public ResponseEntity<?> deleteWoreda(@PathVariable Integer id) {
        boolean isDeleted = woredaService.deleteWoreda(id);

        if (!isDeleted) {
            logger.info("Woreda with id '{}' not found for deletion.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Woreda not found.");
        }

        logger.info("Deleted Woreda with id '{}'", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findWoredaById(@PathVariable Integer id) {
        WoredaDto woredaDto = woredaService.findWoredaById(id);

        if (woredaDto == null) {
            logger.info("Woreda with id '{}' not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Woreda not found.");
        }

        logger.info("Fetched Woreda with id '{}'", id);
        return ResponseEntity.ok(woredaDto);
    }

}
