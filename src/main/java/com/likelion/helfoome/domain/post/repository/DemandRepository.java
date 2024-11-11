package com.likelion.helfoome.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.Demand;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {
  List<Demand> findByUser_Email(String email);
}
