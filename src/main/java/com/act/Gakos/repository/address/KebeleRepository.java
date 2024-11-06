package com.act.Gakos.repository.address;

import com.act.Gakos.dto.address.KebeleDto;
import com.act.Gakos.dto.address.KebeleSearchDto;
import com.act.Gakos.entity.address.Kebele;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KebeleRepository extends JpaRepository<Kebele, Integer> {
    Optional<Kebele> findByName(String name);

    boolean existsByName(String name);

    // Native SQL query for searching Kebeles with "like"
    @Query(value = "SELECT * FROM kebele WHERE LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
            nativeQuery = true)
    Page<Kebele> searchKebele(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT k FROM kebele k WHERE " +
                  "(:searchTerm IS NULL OR LOWER(k.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
                  "OR LOWER(k.woredaName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Kebele> searchKebeleByWoredaName(@Param("searchTerm") String searchTerm, Pageable pageable);


    Page<Kebele> findByWoredaId(Integer woredaId, Pageable pageable);



    @Query(value = "SELECT k FROM kebele k " +
            "JOIN k.woreda w " +
            "JOIN w.zone z " +
            "JOIN z.region r " +
            "WHERE (:country IS NULL OR LOWER(r.country) = LOWER(:country)) AND " +
            "(:region IS NULL OR LOWER(z.name) = LOWER(:region)) AND " +
            "(:zone IS NULL OR LOWER(z.name) = LOWER(:zone)) AND " + // Keep this as is, expecting zone to be a string
            "(:woreda IS NULL OR LOWER(w.name) = LOWER(:woreda)) AND " +
            "(:kebele IS NULL OR LOWER(k.name) LIKE LOWER(CONCAT('%', :kebele, '%')))")
    Page<Kebele> searchKebeleNew(
            @Param("country") String country,
            @Param("region") String region,
            @Param("zone") String zone, // Ensure this remains a String
            @Param("woreda") String woreda,
            @Param("kebele") String kebele,
            Pageable pageable);


    @Query("SELECT new com.act.Gakos.dto.address.KebeleDto(k.id, k.name, w.name, w.zone.name, w.zone.region.name, w.zone.region.country.countryName) " +
            "FROM kebele k " +
            "LEFT JOIN k.woreda w " +
            "LEFT JOIN w.zone z " +
            "LEFT JOIN z.region r " +
            "LEFT JOIN r.country c " +
            "WHERE (:woredaId IS NULL OR w.id = :woredaId)")
    Page<KebeleDto> searchKebeleByCriteria(@Param("woredaId") Long woredaId, Pageable pageable);

}
