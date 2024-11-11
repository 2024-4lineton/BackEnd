package com.likelion.helfoome.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.Supply;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {
  List<Supply> findByUser_Email(String email);
}
