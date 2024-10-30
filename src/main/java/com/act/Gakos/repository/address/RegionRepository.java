package com.act.Gakos.repository.address;

import com.act.Gakos.dto.address.RegionDto;
import com.act.Gakos.entity.address.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
    Page<Region> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchTerm, String searchTerm1, Pageable pageable);

    boolean existsByName(String name);

    Page<Region> findByCountryAndNameContainingIgnoreCase(String country, String searchTerm, PageRequest pageRequest);

    Page<Region> findByCountry(String country, PageRequest pageRequest);
}
