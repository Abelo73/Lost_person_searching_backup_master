package com.act.Gakos.repository.address;

import com.act.Gakos.dto.address.WoredaDto;
import com.act.Gakos.entity.address.Woreda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WoredaRepository extends JpaRepository<Woreda, Integer> {
    @Query(value = "SELECT * FROM woreda WHERE name = :name", nativeQuery = true)
    Woreda findByWoredaName(@Param("name") String name);

    @Query(value = "SELECT * FROM woreda WHERE LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
            nativeQuery = true)
    Page<Woreda> searchWoredas(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Woreda> findByZoneId(Integer zoneId, Pageable pageable);


    @Query("SELECT new com.act.Gakos.dto.address.WoredaDto(w.id, w.name, w.description, w.zone.id, w.zoneName) " +
            "FROM woreda w " +
            "LEFT JOIN w.zone z " +
            "WHERE (:zoneId IS NULL OR z.id = :zoneId)")
    Page<WoredaDto> searchWoredaByZoneId(@Param("zoneId") Integer zoneId, Pageable pageable);

}
