package com.act.Gakos.repository.address;

import com.act.Gakos.entity.address.Got;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GotRepository extends JpaRepository<Got, Integer> {

    Page<Got> findByKebeleName(String kebeleName, Pageable pageable);

    Page<Got> findByKebeleWoredaName(String woredaName, Pageable pageable);

    Page<Got> findByKebeleWoredaZoneName(String zoneName, Pageable pageable);

    Page<Got> findByKebeleWoredaZoneRegionName(String regionName, Pageable pageable);
}
