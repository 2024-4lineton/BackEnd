package com.likelion.helfoome.domain.post.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
  private String title;
  private String content;
  private List<MultipartFile> images;
}
