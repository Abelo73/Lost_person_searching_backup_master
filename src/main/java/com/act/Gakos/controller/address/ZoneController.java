package com.act.Gakos.controller.address;

import com.act.Gakos.dto.address.RegionDto;
import com.act.Gakos.dto.address.ZoneDto;
import com.act.Gakos.entity.address.Zone;
import com.act.Gakos.service.address.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;


    @GetMapping("/all")
    public ResponseEntity<Page<Zone>> getZones(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Zone> zones = zoneService.getZones(searchTerm, page, size, Sort.by("name").ascending());
        return new ResponseEntity<>(zones, HttpStatus.OK);
    }

    // Endpoint to add a new zone

    @GetMapping("/search/zone")
    public ResponseEntity<Page<ZoneDto>> searchZones(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<ZoneDto> regions = zoneService.searchZones(searchTerm, page, size, Sort.by("name").ascending());
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Zone> addZone(@RequestBody Zone zone) {
        if (zoneService.existsByNameAndRegionId(zone.getName(), zone.getRegion().getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict for duplicates
        }
        Zone createdZone = zoneService.addZone(zone);
        return new ResponseEntity<>(createdZone, HttpStatus.CREATED);
    }

    // Endpoint to add zones in bulk
    @PostMapping("/bulk")
    public ResponseEntity<List<String>> addZonesBulk(@RequestBody List<Zone> zones) {
        List<String> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (Zone zone : zones) {
            if (zoneService.existsByNameAndRegionId(zone.getName(), zone.getRegion().getId())) {
                errors.add("Duplicate zone: " + zone.getName() + " in region ID: " + zone.getRegion().getId());
            } else {
                zoneService.addZone(zone);
                results.add(zone.getName());
            }
        }

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT); // 409 Conflict for duplicates
        }

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }
}
