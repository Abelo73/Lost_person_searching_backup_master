package com.act.Gakos.repository;

import com.act.Gakos.entity.ProfilePicture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Integer> {
//    List<ProfilePicture> findByUserId(Integer userId);
//    Page<ProfilePicture> findByUserId(Integer userId, Pageable pageable);

//    @Query(value = "SELECT * FROM profile_picture WHERE user_id = :userId", nativeQuery = true)
    Page<ProfilePicture> findByUserId(Integer userId, Pageable pageable);
}
