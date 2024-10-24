package com.act.Gakos.repository;

import com.act.Gakos.entity.ImageMetadataNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetadataRepository extends JpaRepository<ImageMetadataNew, Long> {

}
