package com.likelion.helfoome.domain.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.cart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUser_Email(String email);
}
