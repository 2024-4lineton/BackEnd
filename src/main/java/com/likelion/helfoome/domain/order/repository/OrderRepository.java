package com.likelion.helfoome.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  boolean existsByPIN(String pin);
}
