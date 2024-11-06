package com.likelion.helfoome.domain.post.dto;

import java.time.LocalDateTime;

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
  private String imageName;
  private String imageUrl;
  private LocalDateTime createdAt;
}
