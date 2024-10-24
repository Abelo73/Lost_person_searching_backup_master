package com.act.Gakos.repository;

import com.act.Gakos.entity.LoginAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    Page<LoginAttempt> findBySuccess(Boolean success, Pageable pageable);

    @Query("SELECT la FROM LoginAttempt la WHERE la.operatingSystem = :operatingSystem")
    Page<LoginAttempt> findByOperatingSystem(@Param("operatingSystem") String operatingSystem, Pageable pageable);

    @Query("SELECT la FROM LoginAttempt la WHERE la.success = :success AND la.operatingSystem = :operatingSystem")
    Page<LoginAttempt> findBySuccessAndOperatingSystem(@Param("success") Boolean success, @Param("operatingSystem") String operatingSystem, Pageable pageable);
//    Page<LoginAttempt> findBySuccess(boolean success, Pageable pageable);
//
//    Page<LoginAttempt> findByOperatingSystem(String operatingSystem, Pageable pageable);
//
//    Page<LoginAttempt> findBySuccessAndOperatingSystem(Boolean success, String operatingSystem, Pageable pageable);
}
