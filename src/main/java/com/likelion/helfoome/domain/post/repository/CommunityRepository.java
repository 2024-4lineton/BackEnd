package com.likelion.helfoome.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
  List<Community> findByUser_Email(String email);
}
