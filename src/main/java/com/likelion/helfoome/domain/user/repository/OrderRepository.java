package com.likelion.helfoome.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.user.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}
