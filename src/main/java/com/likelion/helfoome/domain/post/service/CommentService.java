package com.likelion.helfoome.domain.post.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.post.dto.CommentRequest;
import com.likelion.helfoome.domain.post.dto.CommentResponse;
import com.likelion.helfoome.domain.post.entity.Article;
import com.likelion.helfoome.domain.post.entity.ArticleComment;
import com.likelion.helfoome.domain.post.entity.Community;
import com.likelion.helfoome.domain.post.entity.CommunityComment;
import com.likelion.helfoome.domain.post.repository.ArticleCommentRepository;
import com.likelion.helfoome.domain.post.repository.ArticleRepository;
import com.likelion.helfoome.domain.post.repository.CommunityCommentRepository;
import com.likelion.helfoome.domain.post.repository.CommunityRepository;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
  private final ArticleCommentRepository articleCommentRepository;
  private final CommunityCommentRepository communityCommentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final CommunityRepository communityRepository;

  public String createComment(String postType, String email, Long postId, CommentRequest request) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    switch (postType) {
      case "article":
        ArticleComment articleComment = new ArticleComment();
        Article article =
            articleRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        articleComment.setUser(user);
        articleComment.setArticle(article);
        articleComment.setContent(request.getContent());
        article.setTotalComments(article.getTotalComments() + 1);

        articleRepository.save(article);
        articleCommentRepository.save(articleComment);
        break;
      case "community":
        CommunityComment communityComment = new CommunityComment();
        Community community =
            communityRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        communityComment.setUser(user);
        communityComment.setCommunity(community);
        communityComment.setContent(request.getContent());

        community.setTotalComments(community.getTotalComments() + 1);

        communityRepository.save(community);
        communityCommentRepository.save(communityComment);
        break;
      default:
        return "댓글 등록 중 오류가 발생했습니다.";
    }
    return "댓글이 정상적으로 등록되었습니다.";
  }

  // 특정 게시물의 댓글 조회
  public List<CommentResponse> getAllComment(String postType, Long postId) {
    log.info("Post List for postId: {}", postId);

    // CommentListResponse를 담을 리스트 생성하고
    List<CommentResponse> responses = new ArrayList<>();
    CommentResponse response = new CommentResponse();

    // postType 사용하여 해당하는 게시글 전체 조회
    switch (postType) {
      case "article":
        List<ArticleComment> articleComments = articleCommentRepository.findByArticleId(postId);
        for (ArticleComment articleComment : articleComments) {
          response =
              new CommentResponse(
                  articleComment.getUser().getNickname(),
                  articleComment.getContent(),
                  articleComment.getCreatedDate());
        }
        break;
      case "community":
        List<CommunityComment> communityComments =
            communityCommentRepository.findByCommunityId(postId);
        for (CommunityComment communityComment : communityComments) {
          response =
              new CommentResponse(
                  communityComment.getUser().getNickname(),
                  communityComment.getContent(),
                  communityComment.getCreatedDate());
        }
        break;
      default:
        log.warn("No post Id found: {}", postId);
    }
    responses.add(response);

    return responses;
  }
}
