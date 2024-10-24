package com.act.Gakos.repository;

import com.act.Gakos.entity.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<UploadImage, Long> {



    Optional<UploadImage> findByName(String fileName);
}
