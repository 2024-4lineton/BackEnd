package com.likelion.helfoome.domain.Img.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.Img.entity.ProductImg;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

  Optional<ProductImg> findByProductId(Long product_id);
}
