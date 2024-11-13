package com.act.Gakos.service.address;

import com.act.Gakos.repository.address.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AddressDataService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ZoneRepository zoneRepository;
    @Autowired
    private WoredaRepository woredaRepository;
    @Autowired
    private KebeleRepository kebeleRepository;
    @Autowired
    private GotRepository gotRepository;


    public Map<String , Long> getAddressDataCount(){
        Map<String , Long> counts = new HashMap<>();
        long countryCount = countryRepository.count();
        long regionCount = regionRepository.count();
        long zoneCount = zoneRepository.count();
        long woredaCount = woredaRepository.count();
        long kebeleCount = kebeleRepository.count();
        long gotCount = gotRepository.count();

        counts.put("Country", countryCount);
        counts.put("Region", regionCount);
        counts.put("Zone", zoneCount);
        counts.put("Woreda", woredaCount);
        counts.put("Kebele", kebeleCount);
        counts.put("Got", gotCount);

        return counts;
    }
}
