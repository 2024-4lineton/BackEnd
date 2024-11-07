package com.likelion.helfoome.domain.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  boolean existsByPIN(String pin);

  List<Order> findByProductId(Long productId);
}
