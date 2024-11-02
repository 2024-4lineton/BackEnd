package com.likelion.helfoome.domain.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.shop.entity.ShopEntity;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
  Optional<ShopEntity> findByTaxId(String taxId);

  Optional<ShopEntity> findByUser_Email(String email);

  List<ShopEntity> findByShopType(Integer shopType);
}
