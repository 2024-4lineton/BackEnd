package com.likelion.helfoome.domain.Img.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.Img.entity.DemandImg;

@Repository
public interface DemandImgRepository extends JpaRepository<DemandImg, Long> {
  Optional<DemandImg> findByDemandId(Long id);
}
