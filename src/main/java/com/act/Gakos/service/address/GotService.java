package com.act.Gakos.service.address;

import com.act.Gakos.dto.address.GotDto;
import com.act.Gakos.dto.address.KebeleDto;
import com.act.Gakos.entity.address.Got;
import com.act.Gakos.entity.address.Kebele;
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
                got.getDescription(),
                got.getKebele().getName()
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

    public Page<GotDto> findGotByKebeleId(Long kebeleId, Pageable pageable) {
        Page<Got> gotPage;
        if (kebeleId != null) {
            gotPage = gotRepository.findByKebeleId(kebeleId, pageable);
        } else {
            gotPage = gotRepository.findAll(pageable);
        }
        return gotPage.map(this::convertToDto);
    }

    private GotDto convertToDto(Got got) {
        GotDto gotDto = new GotDto();
        gotDto.setId(got.getId());
        gotDto.setName(got.getName());
        gotDto.setNickname(got.getNickname());
        gotDto.setDescription(got.getDescription());
        gotDto.setKebeleName(got.getKebele() != null ? got.getKebele().getName() : null);
        return gotDto;
    }




    private KebeleDto convertToDto(Kebele kebele) {
        return new KebeleDto(
                kebele.getId(),
                kebele.getName(),
                kebele.getWoredaName(),
                kebele.getWoreda().getZoneName(),
                kebele.getWoreda().getZone().getRegionName(),
                kebele.getWoreda().getZone().getRegion().getCountry().getCountryName()
        );
    }

//    public Page<KebeleDto> searchKebeleNew(String country, String region,String zone, String woreda, String kebele, Pageable pageable) {
//        Page<Kebele> kebeles = kebeleRepository.searchKebeleNew(country, region, zone, woreda, kebele, pageable);
//        return kebeles.map(this::convertToDto);
//    }


    public Page<GotDto> searchKebeleByCriteria(Long kebeleId, Pageable pageable) {
        return gotRepository.searchGotByCriteria(kebeleId, pageable);
    }

//    public Page<GotDto> findGotByKebeleId(Long kebeleId, Pageable pageable) {
//        Page<Got> gotPage;
//        if (kebeleId != null) {
//            gotPage = gotRepository.findByKebeleId(kebeleId, pageable);
//        } else {
//            gotPage = gotRepository.findAll(pageable);
//        }
//
//        // Convert the Got entities to GotDto objects
//        return gotPage.map(this::convertToDto);
//    }
//
//    // Conversion method from Got entity to GotDto
//    private GotDto convertToDto(Got got) {
//        GotDto gotDto = new GotDto();
//        gotDto.setId(got.getId());
//        gotDto.setName(got.getName());
//        gotDto.setNickname(got.getNickname());
//        gotDto.setDescription(got.getDescription());
//        gotDto.setKebeleName(got.getKebele() != null ? got.getKebele().getName() : null); // Assuming Kebele has a name property
//        return gotDto;
//    }

}
