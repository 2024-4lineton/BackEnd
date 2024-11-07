package com.likelion.helfoome.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
  private String nickname;
  private String title;
  private String content;
  private Integer totalLikes;
  private Integer totalComments;
  private LocalDateTime createdAt;
  private String imageUrl;
  private List<String> imageUrls;
}
