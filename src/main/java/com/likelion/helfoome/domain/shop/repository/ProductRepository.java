package com.likelion.helfoome.domain.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.shop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  // List<Product> findAllByShopId(long shop_id);
  List<Product> findByShopIdAndIsSellingTrue(Long shopId);
}
