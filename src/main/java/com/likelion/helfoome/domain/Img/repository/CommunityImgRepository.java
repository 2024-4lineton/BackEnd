package com.likelion.helfoome.domain.Img.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.Img.entity.CommunityImg;

@Repository
public interface CommunityImgRepository extends JpaRepository<CommunityImg, Long> {
  List<CommunityImg> findByCommunityId(Long communityId);
}
