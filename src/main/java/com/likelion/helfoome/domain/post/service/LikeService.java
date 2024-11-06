package com.likelion.helfoome.domain.post.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.post.entity.Article;
import com.likelion.helfoome.domain.post.entity.ArticleLike;
import com.likelion.helfoome.domain.post.entity.Community;
import com.likelion.helfoome.domain.post.entity.CommunityLike;
import com.likelion.helfoome.domain.post.entity.Demand;
import com.likelion.helfoome.domain.post.entity.DemandLike;
import com.likelion.helfoome.domain.post.entity.Supply;
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
        Article article =
            articleRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        articleLike.setUser(user);
        articleLike.setArticle(article);
        article.setTotalLikes(article.getTotalLikes() + 1);

        articleRepository.save(article);
        articleLikeRepository.save(articleLike);
        break;
      case "community":
        CommunityLike communityLike = new CommunityLike();
        Community community =
            communityRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        communityLike.setUser(user);
        communityLike.setCommunity(community);
        community.setTotalLikes(community.getTotalLikes() + 1);

        communityRepository.save(community);
        communityLikeRepository.save(communityLike);
        break;
      case "demand":
        DemandLike demandLike = new DemandLike();
        Demand demand =
            demandRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Demand not found"));
        demandLike.setUser(user);
        demandLike.setDemand(demand);
        demand.setTotalLikes(demand.getTotalLikes() + 1);

        demandRepository.save(demand);
        demandLikeRepository.save(demandLike);
        break;
      case "supply":
        SupplyLike supplyLike = new SupplyLike();
        Supply supply =
            supplyRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Supply not found"));
        supplyLike.setUser(user);
        supplyLike.setSupply(supply);
        supply.setTotalLikes(supply.getTotalLikes() + 1);

        supplyRepository.save(supply);
        supplyLikeRepository.save(supplyLike);
        break;
      default:
        return "좋아요 중 오류가 발생했습니다.";
    }
    return "좋아요가 정상적으로 실행되었습니다.";
  }
}
