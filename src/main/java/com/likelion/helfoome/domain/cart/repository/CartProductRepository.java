package com.likelion.helfoome.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.cart.entity.CartProduct;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {}
