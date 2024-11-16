package com.likelion.helfoome.domain.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.SupplyLike;

@Repository
public interface SupplyLikeRepository extends JpaRepository<SupplyLike, Long> {
  Optional<SupplyLike> findBySupplyIdAndUser_Email(Long supplyId, String email);
}
