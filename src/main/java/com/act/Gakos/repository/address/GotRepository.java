package com.act.Gakos.repository.address;

import com.act.Gakos.dto.address.GotDto;
import com.act.Gakos.dto.address.KebeleDto;
import com.act.Gakos.entity.address.Got;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GotRepository extends JpaRepository<Got, Integer> {

    Page<Got> findByKebeleName(String kebeleName, Pageable pageable);

    Page<Got> findByKebeleWoredaName(String woredaName, Pageable pageable);

    Page<Got> findByKebeleWoredaZoneName(String zoneName, Pageable pageable);

    Page<Got> findByKebeleWoredaZoneRegionName(String regionName, Pageable pageable);

@Query(value = "SELECT g.id AS id, g.name AS name, g.nickname AS nickname, g.description AS description, " +
        "g.kebele_id AS kebeleId, k.name AS kebeleName " +
        "FROM got g " +
        "JOIN kebele k ON g.kebele_id = k.id " +
        "WHERE (:kebeleId IS NULL OR g.kebele_id = :kebeleId) " +
        "ORDER BY g.name ASC",
        countQuery = "SELECT COUNT(*) FROM got g WHERE (:kebeleId IS NULL OR g.kebele_id = :kebeleId)",
        nativeQuery = true)
    Page<Got> findByKebeleId(@Param("kebeleId") Long kebeleId, Pageable pageable);



//    @Query("SELECT new com.act.Gakos.dto.address.GotDto(g.id, g.name, g.nickname, g.description, k.name) " +
//            "FROM got g " +
//            "LEFT JOIN g.kebele k " +
//            "WHERE (:kebeleId IS NULL OR k.id = :kebeleId)")
//    Page<GotDto> searchGotByKebeleId(@Param("kebeleId") Integer kebeleId, Pageable pageable);

    @Query("SELECT new com.act.Gakos.dto.address.GotDto(g.id, g.name, g.nickname, g.description, k.name) " +
            "FROM got g " +
            "LEFT JOIN g.kebele k " +
            "LEFT JOIN k.woreda w " +
            "LEFT JOIN w.zone z " +
            "LEFT JOIN z.region r " +
            "LEFT JOIN r.country c " +
            "WHERE (:kebeleId IS NULL OR k.id = :kebeleId)" +
            "AND (:woredaId IS NULL OR w.id = :woredaId)" +
            "AND (:zoneId IS NULL OR z.id = :zoneId)" +
            "AND (:regionId IS NULL OR r.id = :regionId)" +
            "AND (:countryId IS NULL OR c.id = :countryId)"
    )
    Page<GotDto> searchGotByKebeleId(
            @Param("kebeleId") Integer kebeleId,
            @Param("woredaId") Integer woredaId,
            @Param("zoneId") Integer zoneId,
            @Param("regionId") Integer regionId,
            @Param("countryId") Integer countryId,
            Pageable pageable
    );



    @Query("SELECT new com.act.Gakos.dto.address.GotDto(g.id, g.name, g.description, g.nickname, k.name) " +
            "FROM got g " +
            "LEFT JOIN g.kebele k " +
            "WHERE (:kebeleId IS NULL OR k.id = :kebeleId)")
    Page<GotDto> searchGotByCriteria(@Param("kebeleId") Long kebeleId, Pageable pageable);




}
