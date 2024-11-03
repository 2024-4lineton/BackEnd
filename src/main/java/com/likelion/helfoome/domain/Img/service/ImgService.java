package com.likelion.helfoome.domain.Img.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.likelion.helfoome.domain.Img.entity.ProductImg;
import com.likelion.helfoome.domain.Img.repository.ArticleImgRepository;
import com.likelion.helfoome.domain.Img.repository.CommunityImgRepository;
import com.likelion.helfoome.domain.Img.repository.ProductImgRepository;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.global.S3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImgService {

  private final ArticleImgRepository articleImgRepository;
  private final CommunityImgRepository communityImgRepository;
  private final ProductImgRepository productImgRepository;
  private final S3Service s3Service;

  public void uploadProductImg(List<MultipartFile> productImages, Product product)
      throws IOException {
    for (MultipartFile productImage : productImages) {
      String productImageUrl = s3Service.upload(productImage, "productImages");
      ProductImg productImg = new ProductImg();
      productImg.setProduct(product);
      productImg.setProductImageName(productImage.getOriginalFilename());
      productImg.setProductImageUrl(productImageUrl);
      productImgRepository.save(productImg);
    }
  }
}
