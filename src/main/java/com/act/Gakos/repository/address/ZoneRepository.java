package com.act.Gakos.repository.address;

import com.act.Gakos.entity.address.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByNameAndRegionId(String name, Long regionId);

    Page<Zone> findByNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    Page<Zone> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchTerm, String searchTerm1, Pageable pageable);
}
