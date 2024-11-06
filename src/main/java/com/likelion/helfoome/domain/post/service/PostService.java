package com.likelion.helfoome.domain.post.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.Img.entity.ArticleImg;
import com.likelion.helfoome.domain.Img.entity.CommunityImg;
import com.likelion.helfoome.domain.Img.entity.DemandImg;
import com.likelion.helfoome.domain.Img.entity.SupplyImg;
import com.likelion.helfoome.domain.Img.repository.ArticleImgRepository;
import com.likelion.helfoome.domain.Img.repository.CommunityImgRepository;
import com.likelion.helfoome.domain.Img.repository.DemandImgRepository;
import com.likelion.helfoome.domain.Img.repository.SupplyImgRepository;
import com.likelion.helfoome.domain.post.dto.PostRequest;
import com.likelion.helfoome.domain.post.dto.PostResponse;
import com.likelion.helfoome.domain.post.entity.Article;
import com.likelion.helfoome.domain.post.entity.Community;
import com.likelion.helfoome.domain.post.entity.Demand;
import com.likelion.helfoome.domain.post.entity.Supply;
import com.likelion.helfoome.domain.post.repository.ArticleRepository;
import com.likelion.helfoome.domain.post.repository.CommunityRepository;
import com.likelion.helfoome.domain.post.repository.DemandRepository;
import com.likelion.helfoome.domain.post.repository.SupplyRepository;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
  private final ArticleRepository articleRepository;
  private final CommunityRepository communityRepository;
  private final DemandRepository demandRepository;
  private final SupplyRepository supplyRepository;
  private final UserRepository userRepository;
  private final ArticleImgRepository articleImgRepository;
  private final CommunityImgRepository communityImgRepository;
  private final DemandImgRepository demandImgRepository;
  private final SupplyImgRepository supplyImgRepository;

  // 게시물 등록
  public String createPost(String postType, String email, PostRequest request) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    switch (postType) {
      case "article":
        Article article = new Article();
        ArticleImg articleImg = new ArticleImg();
        article.setUser(user);
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setTotalLikes(0);
        article.setTotalComments(0);
        articleImg.setArticle(article);
        articleImg.setArticleImageName(request.getImageName());
        articleImg.setArticleImageUrl(request.getImageUrl());

        articleRepository.save(article);
        articleImgRepository.save(articleImg);
        break;
      case "community":
        Community community = new Community();
        CommunityImg communityImg = new CommunityImg();
        community.setUser(user);
        community.setTitle(request.getTitle());
        community.setContent(request.getContent());
        community.setTotalLikes(0);
        community.setTotalComments(0);
        communityImg.setCommunity(community);
        communityImg.setCommunityImageName(request.getImageName());
        communityImg.setCommunityImageUrl(request.getImageUrl());

        communityRepository.save(community);
        communityImgRepository.save(communityImg);
        break;
      case "demand":
        Demand demand = new Demand();
        DemandImg demandImg = new DemandImg();
        demand.setUser(user);
        demand.setTitle(request.getTitle());
        demand.setContent(request.getContent());
        demand.setTotalLikes(0);
        demandImg.setDemand(demand);
        demandImg.setDemandImageName(request.getImageName());
        demandImg.setDemandImageUrl(request.getImageUrl());

        demandRepository.save(demand);
        demandImgRepository.save(demandImg);
        break;
      case "supply":
        Supply supply = new Supply();
        SupplyImg supplyImg = new SupplyImg();
        supply.setUser(user);
        supply.setTitle(request.getTitle());
        supply.setContent(request.getContent());
        supply.setTotalLikes(0);
        supplyImg.setSupply(supply);
        supplyImg.setSupplyImageName(request.getImageName());
        supplyImg.setSupplyImageUrl(request.getImageUrl());

        supplyRepository.save(supply);
        supplyImgRepository.save(supplyImg);
        break;
      default:
        return "글 등록 중 오류가 발생했습니다.";
    }
    return "글이 정상적으로 등록되었습니다.";
  }

  // 특정 게시물 전체 조회
  public List<PostResponse> getAllPost(String postType) {
    log.info("Post List for postType: {}", postType);

    // PostListResponse를 담을 리스트 생성하고
    List<PostResponse> postResponses = new ArrayList<>();
    PostResponse response = new PostResponse();

    // postType 사용하여 해당하는 게시글 전체 조회
    switch (postType) {
      case "article":
        List<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
          response =
              new PostResponse(
                  article.getUser().getNickname(),
                  article.getTitle(),
                  article.getContent(),
                  article.getTotalLikes(),
                  article.getTotalComments(),
                  articleImgRepository.findByArticleId(article.getId()).get().getArticleImageName(),
                  articleImgRepository.findByArticleId(article.getId()).get().getArticleImageUrl(),
                  article.getCreatedDate());
        }
        break;
      case "community":
        List<Community> communities = communityRepository.findAll();
        for (Community community : communities) {
          response =
              new PostResponse(
                  community.getUser().getNickname(),
                  community.getTitle(),
                  community.getContent(),
                  community.getTotalLikes(),
                  community.getTotalComments(),
                  communityImgRepository
                      .findByCommunityId(community.getId())
                      .get()
                      .getCommunityImageName(),
                  communityImgRepository
                      .findByCommunityId(community.getId())
                      .get()
                      .getCommunityImageUrl(),
                  community.getCreatedDate());
        }
        break;
      case "demand":
        List<Demand> demands = demandRepository.findAll();
        for (Demand demand : demands) {
          response =
              new PostResponse(
                  demand.getUser().getNickname(),
                  demand.getTitle(),
                  demand.getContent(),
                  demand.getTotalLikes(),
                  null,
                  demandImgRepository.findByDemandId(demand.getId()).get().getDemandImageName(),
                  demandImgRepository.findByDemandId(demand.getId()).get().getDemandImageUrl(),
                  demand.getCreatedDate());
        }
        break;
      case "supply":
        List<Supply> supplies = supplyRepository.findAll();
        for (Supply supply : supplies) {
          response =
              new PostResponse(
                  supply.getUser().getNickname(),
                  supply.getTitle(),
                  supply.getContent(),
                  supply.getTotalLikes(),
                  null,
                  supplyImgRepository.findBySupplyId(supply.getId()).get().getSupplyImageName(),
                  supplyImgRepository.findBySupplyId(supply.getId()).get().getSupplyImageUrl(),
                  supply.getCreatedDate());
        }
        break;
      default:
        log.warn("No post type found: {}", postType);
    }
    postResponses.add(response);

    return postResponses;
  }

  // 특정 게시물 단일 조회
  public PostResponse getPostById(String postType, Long postId) {
    log.info("Post for postType: {}", postType);

    PostResponse response = new PostResponse();

    // post id에 해당하는 게시글 단일 조회
    switch (postType) {
      case "article":
        Optional<Article> article = articleRepository.findById(postId);
        if (article.isPresent()) {
          response =
              new PostResponse(
                  article.get().getUser().getNickname(),
                  article.get().getTitle(),
                  article.get().getContent(),
                  article.get().getTotalLikes(),
                  article.get().getTotalComments(),
                  articleImgRepository
                      .findByArticleId(article.get().getId())
                      .get()
                      .getArticleImageName(),
                  articleImgRepository
                      .findByArticleId(article.get().getId())
                      .get()
                      .getArticleImageUrl(),
                  article.get().getCreatedDate());
        }
        break;
      case "community":
        if (community.isPresent()) {
          response =
              new PostResponse(
                  community.get().getUser().getNickname(),
                  community.get().getTitle(),
                  community.get().getContent(),
                  community.get().getTotalLikes(),
                  community.get().getTotalComments(),
                  communityImgRepository
                      .findByCommunityId(community.get().getId())
                      .get()
                      .getCommunityImageName(),
                  communityImgRepository
                      .findByCommunityId(community.get().getId())
                      .get()
                      .getCommunityImageUrl(),
                  community.get().getCreatedDate());
        }
        break;
      case "demand":
        if (demand.isPresent()) {
          response =
              new PostResponse(
                  demand.get().getUser().getNickname(),
                  demand.get().getTitle(),
                  demand.get().getContent(),
                  demand.get().getTotalLikes(),
                  null,
                  demandImgRepository
                      .findByDemandId(demand.get().getId())
                      .get()
                      .getDemandImageName(),
                  demandImgRepository
                      .findByDemandId(demand.get().getId())
                      .get()
                      .getDemandImageUrl(),
                  demand.get().getCreatedDate());
        }
        break;
      case "supply":
        if (supply.isPresent()) {
          response =
              new PostResponse(
                  supply.get().getUser().getNickname(),
                  supply.get().getTitle(),
                  supply.get().getContent(),
                  supply.get().getTotalLikes(),
                  null,
                  supplyImgRepository
                      .findBySupplyId(supply.get().getId())
                      .get()
                      .getSupplyImageName(),
                  supplyImgRepository
                      .findBySupplyId(supply.get().getId())
                      .get()
                      .getSupplyImageUrl(),
                  supply.get().getCreatedDate());
        }
        break;
      default:
    }

    return response;
  }
}
