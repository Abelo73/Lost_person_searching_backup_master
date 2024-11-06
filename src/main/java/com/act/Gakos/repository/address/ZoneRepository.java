package com.act.Gakos.repository.address;

import com.act.Gakos.dto.address.ZoneDto;
import com.act.Gakos.entity.address.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByNameAndRegionId(String name, Long regionId);

    Page<Zone> findByNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    Page<Zone> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchTerm, String searchTerm1, Pageable pageable);

    List<Zone> findByRegionId(Long regionId);


    @Query("SELECT new com.act.Gakos.dto.address.ZoneDto(z.id, z.name, z.description, r.name) " +
            "FROM zone z " +
            "LEFT JOIN z.region r " +
            "WHERE (:regionId IS NULL OR r.id = :regionId)")
    Page<ZoneDto> searchZoneByRegionId(@Param("regionId") Long regionId, Pageable pageable);
}
