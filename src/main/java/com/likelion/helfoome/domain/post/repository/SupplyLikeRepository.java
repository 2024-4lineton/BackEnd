package com.likelion.helfoome.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.SupplyLike;

@Repository
public interface SupplyLikeRepository extends JpaRepository<SupplyLike, Long> {}