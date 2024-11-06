package com.likelion.helfoome.domain.post.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.post.entity.ArticleComment;
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

  public String createComment(String postType, String email, Long postId) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    switch (postType) {
      case "article":
        ArticleComment articleComment = new ArticleComment();
        articleComment.setUser(user);
        articleComment.setArticle(
            articleRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Article not found")));

        articleCommentRepository.save(articleComment);
        break;
      case "community":
        CommunityComment communityComment = new CommunityComment();
        communityComment.setUser(user);
        communityComment.setCommunity(
            communityRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Community not found")));

        communityCommentRepository.save(communityComment);
        break;
      default:
        return "댓글 등록 중 오류가 발생했습니다.";
    }
    return "댓글이 정상적으로 등록되었습니다.";
  }
}
