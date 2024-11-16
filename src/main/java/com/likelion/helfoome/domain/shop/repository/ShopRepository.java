package com.likelion.helfoome.domain.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.shop.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

  Optional<Shop> findById(Long shopId);

  Optional<Shop> findByTaxId(String taxId);

  Optional<Shop> findByUser_Email(String email);

  List<Shop> findByShopType(Integer shopType);

  List<Shop> findByMarketName(String marketName);
}
