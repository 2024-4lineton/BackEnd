package com.likelion.helfoome.domain.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  boolean existsByPIN(String pin);

  List<Order> findByProductId(Long productId);

  Long countByProductId(Long productId);

  boolean existsByProductId(Long productId);

  List<Order> findAllByUser_Email(String email);

  Long countByOrderStatusAndUser_EmailAndCreatedDateBetween(
      Integer orderStatus,
      String email,
      java.time.LocalDateTime startDate,
      java.time.LocalDateTime endDate);
}
