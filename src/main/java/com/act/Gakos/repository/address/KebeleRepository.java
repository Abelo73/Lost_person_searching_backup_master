package com.act.Gakos.repository.address;

import com.act.Gakos.entity.address.Kebele;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KebeleRepository extends JpaRepository<Kebele, Integer> {
    Optional<Kebele> findByName(String name);

    boolean existsByName(String name);

    // Native SQL query for searching Kebeles with "like"
    @Query(value = "SELECT * FROM kebele WHERE LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
            nativeQuery = true)
    Page<Kebele> searchKebele(@Param("searchTerm") String searchTerm, Pageable pageable);

//    @Query(value = "SELECT * FROM kebele k JOIN woreda w ON k.id = w.id WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
//            nativeQuery = true)
//    Page<Kebele> searchKebeleByWoredaName(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Native SQL query for searching Kebeles by Woreda name
//    @Query(value = "SELECT k.id AS kebele_id, k.name AS kebele_name, w.id AS woreda_id, w.name AS woreda_name " +
//            "FROM kebele k JOIN woreda w ON k.woreda_id = w.id " +
//            "WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
//            nativeQuery = true)
//    Page<Kebele> searchKebeleByWoredaName(@Param("searchTerm") String searchTerm, Pageable pageable);


//    @Query(value = "SELECT k.id AS id, k.name AS name, '' AS description " +
//            "FROM kebele k JOIN woreda w ON k.woreda_id = w.id " +
//            "WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
//            nativeQuery = true)
//    Page<Kebele> searchKebeleByWoredaName(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT k FROM kebele k JOIN k.woreda w WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Kebele> searchKebeleByWoredaName(String searchTerm, Pageable pageable);


    Page<Kebele> findByWoredaId(Integer woredaId, Pageable pageable);

}
