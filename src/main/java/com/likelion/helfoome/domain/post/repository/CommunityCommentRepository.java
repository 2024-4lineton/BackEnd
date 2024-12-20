package com.likelion.helfoome.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.CommunityComment;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
  List<CommunityComment> findByCommunityId(Long id);

  List<CommunityComment> findByUser_Email(String email);
}
