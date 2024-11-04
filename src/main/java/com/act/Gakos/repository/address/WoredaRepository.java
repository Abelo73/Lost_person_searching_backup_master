package com.act.Gakos.repository.address;

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

//    Page<Woreda> findByWoredaNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    @Query(value = "SELECT * FROM woreda WHERE LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
            nativeQuery = true)
    Page<Woreda> searchWoredas(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Woreda> findByZoneId(Integer zoneId, Pageable pageable);
}
