package com.likelion.helfoome.domain.user.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.helfoome.domain.Img.repository.ArticleImgRepository;
import com.likelion.helfoome.domain.Img.repository.CommunityImgRepository;
import com.likelion.helfoome.domain.Img.repository.DemandImgRepository;
import com.likelion.helfoome.domain.Img.repository.SupplyImgRepository;
import com.likelion.helfoome.domain.post.dto.PostResponse;
import com.likelion.helfoome.domain.post.entity.Article;
import com.likelion.helfoome.domain.post.entity.Community;
import com.likelion.helfoome.domain.post.entity.Demand;
import com.likelion.helfoome.domain.post.entity.Supply;
import com.likelion.helfoome.domain.post.repository.ArticleRepository;
import com.likelion.helfoome.domain.post.repository.CommunityRepository;
import com.likelion.helfoome.domain.post.repository.DemandRepository;
import com.likelion.helfoome.domain.post.repository.SupplyRepository;
import com.likelion.helfoome.domain.shop.dto.product.MainProductResponse;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.user.dto.SearchResponse;
import com.likelion.helfoome.domain.user.entity.Search;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.SearchRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final SearchRepository searchRepository;
  private final ArticleRepository articleRepository;
  private final ArticleImgRepository articleImgRepository;
  private final CommunityRepository communityRepository;
  private final CommunityImgRepository communityImgRepository;
  private final DemandRepository demandRepository;
  private final DemandImgRepository demandImgRepository;
  private final SupplyRepository supplyRepository;
  private final SupplyImgRepository supplyImgRepository;

  public void createSearch(String email, String keyword) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    Search search = new Search();

    search.setUser(user);
    search.setContent(keyword);

    searchRepository.save(search);
  }

  public List<SearchResponse> getSearchByEmail(String email) {
    log.info("Get Search by email: {}", email);

    List<SearchResponse> responses = new ArrayList<>();
    SearchResponse response;

    List<Search> searchList = searchRepository.findByUser_Email(email);
    if (!searchList.isEmpty()) {
      for (Search search : searchList) {
        response = new SearchResponse(search.getContent(), search.getCreatedDate());

        responses.add(response);
      }
    } else {
      log.warn("No search found: {}", email);
    }
    return responses;
  }

  public List<MainProductResponse> searchProductByKeyword(String keyword) {
    log.info("Get product by keyword: {}", keyword);

    List<MainProductResponse> responses = new ArrayList<>();
    MainProductResponse response;

    List<Product> productList = productRepository.findByProductNameContaining(keyword);
    if (!productList.isEmpty()) {
      for (Product product : productList) {
        response =
            new MainProductResponse(
                product.getShop().getShopName(),
                product.getId(),
                product.getProductName(),
                product.getDiscountPrice(),
                product.getDiscountPercent(),
                product.getProductImageURL());

        responses.add(response);
      }
    } else {
      log.warn("No product found: {}", keyword);
    }
    return responses;
  }

  public List<PostResponse> searchPostByKeyword(String keyword) {
    log.info("Get post by keyword: {}", keyword);

    List<PostResponse> responses = new ArrayList<>();
    PostResponse response;

    List<Article> articleList = articleRepository.findByContentContaining(keyword);
    if (!articleList.isEmpty()) {
      for (Article article : articleList) {
        response =
            new PostResponse(
                article.getUser().getNickname(),
                article.getTitle(),
                article.getContent(),
                article.getTotalLikes(),
                article.getTotalComments(),
                article.getCreatedDate(),
                articleImgRepository.findByArticleId(article.getId()).getFirst().getArticleImgUrl(),
                null);

        responses.add(response);
      }
    } else {
      log.warn("No article found: {}", keyword);
    }

    List<Community> communityList = communityRepository.findByContentContaining(keyword);
    if (!communityList.isEmpty()) {
      for (Community community : communityList) {
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

        responses.add(response);
      }
    } else {
      log.warn("No community found: {}", keyword);
    }

    List<Demand> demandList = demandRepository.findByContentContaining(keyword);
    if (!demandList.isEmpty()) {
      for (Demand demand : demandList) {
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

        responses.add(response);
      }
    } else {
      log.warn("No demand found: {}", keyword);
    }

    List<Supply> supplyList = supplyRepository.findByContentContaining(keyword);
    if (!supplyList.isEmpty()) {
      for (Supply supply : supplyList) {
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

        responses.add(response);
      }
    } else {
      log.warn("No supply found: {}", keyword);
    }
    responses.sort(Comparator.comparing(PostResponse::getCreatedAt).reversed());

    return responses;
  }

  @Transactional
  public String deleteSearchHistory(Long searchId) {
    Optional<Search> searchOptional = searchRepository.findById(searchId);
    if (searchOptional.isEmpty()) {
      return "해당 기록을 찾을 수 없습니다.";
    }

    productRepository.deleteById(searchId);
    return "검색 기록이 성공적으로 삭제되었습니다.";
  }
}
