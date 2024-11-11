package com.likelion.helfoome.domain.shop.repository;

import com.likelion.helfoome.domain.shop.entity.TradShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradShopRepository extends JpaRepository<TradShop, Long> {

  Boolean existsByShopName(String name);
}
