package com.likelion.helfoome.domain.post.service;

import java.io.IOException;
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
import com.likelion.helfoome.domain.Img.service.ImgService;
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
  private final ImgService imgService;
  private final ArticleImgRepository articleImgRepository;
  private final CommunityImgRepository communityImgRepository;
  private final DemandImgRepository demandImgRepository;
  private final SupplyImgRepository supplyImgRepository;

  // 게시물 등록
  public String createPost(String postType, String email, PostRequest request) throws IOException {
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

        articleRepository.save(article);
        imgService.uploadArticleImg(request.getImages(), article);
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

        communityRepository.save(community);
        imgService.uploadCommunityImg(request.getImages(), community);
        break;
      case "demand":
        Demand demand = new Demand();
        DemandImg demandImg = new DemandImg();
        demand.setUser(user);
        demand.setTitle(request.getTitle());
        demand.setContent(request.getContent());
        demand.setTotalLikes(0);
        demandImg.setDemand(demand);

        demandRepository.save(demand);
        imgService.uploadDemandImg(request.getImages(), demand);
        break;
      case "supply":
        Supply supply = new Supply();
        SupplyImg supplyImg = new SupplyImg();
        supply.setUser(user);
        supply.setTitle(request.getTitle());
        supply.setContent(request.getContent());
        supply.setTotalLikes(0);
        supplyImg.setSupply(supply);

        supplyRepository.save(supply);
        imgService.uploadSupplyImg(request.getImages(), supply);
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
                  article.getCreatedDate(),
                  articleImgRepository
                      .findByArticleId(article.getId())
                      .getFirst()
                      .getArticleImgUrl(),
                  null);
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
                  community.getCreatedDate(),
                  communityImgRepository
                      .findByCommunityId(community.getId())
                      .getFirst()
                      .getCommunityImgUrl(),
                  null);
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
                  demand.getCreatedDate(),
                  demandImgRepository.findByDemandId(demand.getId()).getFirst().getDemandImgUrl(),
                  null);
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
                  supply.getCreatedDate(),
                  supplyImgRepository.findBySupplyId(supply.getId()).getFirst().getSupplyImgUrl(),
                  null);
        }
        break;
      default:
        log.warn("No post type found: {}", postType);
    }
    postResponses.add(response);

    return postResponses;
  }

  // 특정 게시물 단일 조회
  public PostResponse getPostById(String postType, Long id) {
    log.info("Post for postType: {}", postType);

    PostResponse response = new PostResponse();

    // post id에 해당하는 게시글 단일 조회
    switch (postType) {
      case "article":
        Optional<Article> article = articleRepository.findById(id);

        if (article.isPresent()) {
          List<ArticleImg> articleImgs =
              articleImgRepository.findByArticleId(article.get().getId());
          List<String> articleImgUrls = new ArrayList<>();
          for (ArticleImg articleImg : articleImgs) {
            articleImgUrls.add(articleImg.getArticleImgUrl());
          }
          response =
              new PostResponse(
                  article.get().getUser().getNickname(),
                  article.get().getTitle(),
                  article.get().getContent(),
                  article.get().getTotalLikes(),
                  article.get().getTotalComments(),
                  article.get().getCreatedDate(),
                  null,
                  articleImgUrls);
        }
        break;
      case "community":
        Optional<Community> community = communityRepository.findById(id);
        if (community.isPresent()) {
          List<CommunityImg> communityImgs =
              communityImgRepository.findByCommunityId(community.get().getId());
          List<String> communityImgUrls = new ArrayList<>();
          for (CommunityImg communityImg : communityImgs) {
            communityImgUrls.add(communityImg.getCommunityImgUrl());
          }
          response =
              new PostResponse(
                  community.get().getUser().getNickname(),
                  community.get().getTitle(),
                  community.get().getContent(),
                  community.get().getTotalLikes(),
                  community.get().getTotalComments(),
                  community.get().getCreatedDate(),
                  null,
                  communityImgUrls);
        }
        break;
      case "demand":
        Optional<Demand> demand = demandRepository.findById(id);
        if (demand.isPresent()) {
          List<DemandImg> demandImgs = demandImgRepository.findByDemandId(demand.get().getId());
          List<String> demandImgUrls = new ArrayList<>();
          for (DemandImg demandImg : demandImgs) {
            demandImgUrls.add(demandImg.getDemandImgUrl());
          }
          response =
              new PostResponse(
                  demand.get().getUser().getNickname(),
                  demand.get().getTitle(),
                  demand.get().getContent(),
                  demand.get().getTotalLikes(),
                  null,
                  demand.get().getCreatedDate(),
                  null,
                  demandImgUrls);
        }
        break;
      case "supply":
        Optional<Supply> supply = supplyRepository.findById(id);
        if (supply.isPresent()) {
          List<SupplyImg> supplyImgs = supplyImgRepository.findBySupplyId(supply.get().getId());
          List<String> supplyImgUrls = new ArrayList<>();
          for (SupplyImg supplyImg : supplyImgs) {
            supplyImgUrls.add(supplyImg.getSupplyImgUrl());
          }
          response =
              new PostResponse(
                  supply.get().getUser().getNickname(),
                  supply.get().getTitle(),
                  supply.get().getContent(),
                  supply.get().getTotalLikes(),
                  null,
                  supply.get().getCreatedDate(),
                  null,
                  supplyImgUrls);
        }
        break;
      default:
        log.warn("No post id found: {}", id);
    }

    return response;
  }
}
