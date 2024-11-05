package com.likelion.helfoome.domain.Img.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.Img.entity.ProductImg;
import com.likelion.helfoome.domain.shop.entity.Product;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

  List<ProductImg> findByProductId(Product product);
}
