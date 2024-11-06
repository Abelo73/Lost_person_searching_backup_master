package com.act.Gakos.controller.address;

import com.act.Gakos.dto.address.GotDto;
import com.act.Gakos.dto.address.KebeleDto;
import com.act.Gakos.entity.address.Got;
import com.act.Gakos.service.address.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gots")
public class GotController {

    private final GotService gotService;

    @Autowired
    public GotController(GotService gotService) {
        this.gotService = gotService;
    }

    // Bulk add Gots
    @PostMapping("/bulk")
    public ResponseEntity<List<Got>> addMultipleGots(@RequestBody List<Got> gots) {
        List<Got> addedGots = gotService.addMultipleGots(gots);
        return new ResponseEntity<>(addedGots, HttpStatus.CREATED);
    }

    // Get all Gots with pagination and filtering
    @GetMapping
    public ResponseEntity<Page<GotDto>> getAllGots(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String kebeleName,
            @RequestParam(required = false) String woredaName,
            @RequestParam(required = false) String zoneName,
            @RequestParam(required = false) String regionName
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<GotDto> gotPage = gotService.getAllGots(pageRequest, kebeleName, woredaName, zoneName, regionName);
        return new ResponseEntity<>(gotPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Got> getGotById(@PathVariable Integer id) {
        return gotService.getGotById(id)
                .map(got -> new ResponseEntity<>(got, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


//    @GetMapping("/search")
//    public ResponseEntity<Page<GotDto>> getGotByKebeleId(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) Long kebeleId
//    ) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
//        Page<GotDto> gotDtoPage = gotService.findGotByKebeleId(kebeleId, pageRequest);
//
//        return new ResponseEntity<>(gotDtoPage, HttpStatus.OK);
//    }

    @GetMapping("/search/kebeleId")
    public ResponseEntity<Page<GotDto>> getGotByKebeleId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long kebeleId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<GotDto> gotDtoPage = gotService.findGotByKebeleId(kebeleId, pageRequest);

        return new ResponseEntity<>(gotDtoPage, HttpStatus.OK);
    }

    @GetMapping("/search")
    public Page<GotDto> searchGotByKebeleId(
            @RequestParam(value = "kebele", required = false) Integer kebele,
            @RequestParam(value = "woreda", required = false) Integer woreda,
            @RequestParam(value = "zone", required = false) Integer zone,
            @RequestParam(value = "region", required = false) Integer region,
            @RequestParam(value = "country", required = false) Integer country,

            Pageable pageable) {
        return gotService.searchGotByKebeleId(kebele,woreda,zone,region, country, pageable);
    }


    @GetMapping("/search/new")
    public Page<GotDto> searchKebele(
            @RequestParam(value = "got", required = false) Long got,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return gotService.searchKebeleByCriteria(got, pageable);
    }

    @PostMapping("/search")
    public ResponseEntity<Got> createGot(@RequestBody Got got) {
        Got createdGot = gotService.createGot(got);
        return new ResponseEntity<>(createdGot, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Got> updateGot(@PathVariable Integer id, @RequestBody Got updatedGot) {
        try {
            Got got = gotService.updateGot(id, updatedGot);
            return new ResponseEntity<>(got, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGot(@PathVariable Integer id) {
        gotService.deleteGot(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
