package com.likelion.helfoome.domain.post.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.post.entity.ArticleLike;
import com.likelion.helfoome.domain.post.entity.CommunityLike;
import com.likelion.helfoome.domain.post.entity.DemandLike;
import com.likelion.helfoome.domain.post.entity.SupplyLike;
import com.likelion.helfoome.domain.post.repository.ArticleLikeRepository;
import com.likelion.helfoome.domain.post.repository.ArticleRepository;
import com.likelion.helfoome.domain.post.repository.CommunityLikeRepository;
import com.likelion.helfoome.domain.post.repository.CommunityRepository;
import com.likelion.helfoome.domain.post.repository.DemandLikeRepository;
import com.likelion.helfoome.domain.post.repository.DemandRepository;
import com.likelion.helfoome.domain.post.repository.SupplyLikeRepository;
import com.likelion.helfoome.domain.post.repository.SupplyRepository;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
  private final ArticleLikeRepository articleLikeRepository;
  private final CommunityLikeRepository communityLikeRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final CommunityRepository communityRepository;
  private final DemandRepository demandRepository;
  private final SupplyRepository supplyRepository;
  private final DemandLikeRepository demandLikeRepository;
  private final SupplyLikeRepository supplyLikeRepository;

  public String createLike(String postType, String email, Long postId) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    switch (postType) {
      case "article":
        ArticleLike articleLike = new ArticleLike();
        articleLike.setUser(user);
        articleLike.setArticle(
            articleRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Article not found")));

        articleLikeRepository.save(articleLike);
        break;
      case "community":
        CommunityLike communityLike = new CommunityLike();
        communityLike.setUser(user);
        communityLike.setCommunity(
            communityRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Community not found")));

        communityLikeRepository.save(communityLike);
        break;
      case "demand":
        DemandLike demandLike = new DemandLike();
        demandLike.setUser(user);
        demandLike.setDemand(
            demandRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Demand not found")));

        demandLikeRepository.save(demandLike);
        break;
      case "supply":
        SupplyLike supplyLike = new SupplyLike();
        supplyLike.setUser(user);
        supplyLike.setSupply(
            supplyRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Supply not found")));

        supplyLikeRepository.save(supplyLike);
        break;
      default:
        return "좋아요 중 오류가 발생했습니다.";
    }
    return "좋아요가 정상적으로 실행되었습니다.";
  }
}
