package com.act.Gakos.repository.address;

import com.act.Gakos.entity.CurrentAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAddressRepository extends JpaRepository<CurrentAddress, Integer> {
    Page<CurrentAddress> findAllByUserId(Integer userId, PageRequest pageRequest);
}
