package com.likelion.helfoome.domain.Img.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.Img.entity.SupplyImg;

@Repository
public interface SupplyImgRepository extends JpaRepository<SupplyImg, Long> {
  List<SupplyImg> findBySupplyId(Long supplyId);
}
