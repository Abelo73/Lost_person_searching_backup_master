package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.GotDto;
import com.act.Gakos.entity.address.Got;
import com.act.Gakos.repository.address.GotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GotService {

    private final GotRepository gotRepository;

    @Autowired
    public GotService(GotRepository gotRepository) {
        this.gotRepository = gotRepository;
    }

    public List<Got> addMultipleGots(List<Got> gots) {
        return gotRepository.saveAll(gots);
    }

    public Page<GotDto> getAllGots(Pageable pageable, String kebeleName, String woredaName, String zoneName, String regionName) {
        Page<Got> gotPage;

        if (kebeleName != null) {
            gotPage = gotRepository.findByKebeleName(kebeleName, pageable);
        } else if (woredaName != null) {
            gotPage = gotRepository.findByKebeleWoredaName(woredaName, pageable);
        } else if (zoneName != null) {
            gotPage = gotRepository.findByKebeleWoredaZoneName(zoneName, pageable);
        } else if (regionName != null) {
            gotPage = gotRepository.findByKebeleWoredaZoneRegionName(regionName, pageable);
        } else {
            gotPage = gotRepository.findAll(pageable);
        }

        // Map each Got entity to a GotDto and return as a Page of GotDto
        return gotPage.map(this::mapToDto);
    }

    // Method to map Got entity to GotDto
    private GotDto mapToDto(Got got) {
        return new GotDto(
                got.getId(),
                got.getName(),
                got.getNickname(),
                got.getDescription()
        );
    }

    public Optional<Got> getGotById(Integer id) {
        return gotRepository.findById(id);
    }

    public Got createGot(Got got) {
        return gotRepository.save(got);
    }

    public Got updateGot(Integer id, Got updatedGot) {
        return gotRepository.findById(id)
                .map(existingGot -> {
                    existingGot.setName(updatedGot.getName());
                    existingGot.setNickname(updatedGot.getNickname());
                    existingGot.setDescription(updatedGot.getDescription());
                    existingGot.setKebele(updatedGot.getKebele());
                    return gotRepository.save(existingGot);
                })
                .orElseThrow(() -> new RuntimeException("Got with ID " + id + " not found"));
    }

    public void deleteGot(Integer id) {
        gotRepository.deleteById(id);
    }
}
