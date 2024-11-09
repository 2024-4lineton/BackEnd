package com.likelion.helfoome.domain.Img.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.likelion.helfoome.domain.Img.entity.ArticleImg;
import com.likelion.helfoome.domain.Img.entity.CommunityImg;
import com.likelion.helfoome.domain.Img.entity.DemandImg;
import com.likelion.helfoome.domain.Img.entity.SupplyImg;
import com.likelion.helfoome.domain.Img.repository.ArticleImgRepository;
import com.likelion.helfoome.domain.Img.repository.CommunityImgRepository;
import com.likelion.helfoome.domain.Img.repository.DemandImgRepository;
import com.likelion.helfoome.domain.Img.repository.SupplyImgRepository;
import com.likelion.helfoome.domain.post.entity.Article;
import com.likelion.helfoome.domain.post.entity.Community;
import com.likelion.helfoome.domain.post.entity.Demand;
import com.likelion.helfoome.domain.post.entity.Supply;
import com.likelion.helfoome.global.S3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImgService {

  private final ArticleImgRepository articleImgRepository;
  private final CommunityImgRepository communityImgRepository;
  private final DemandImgRepository demandImgRepository;
  private final SupplyImgRepository supplyImgRepository;
  private final S3Service s3Service;

  public void uploadArticleImg(List<MultipartFile> articleImages, Article article)
      throws IOException {
    for (MultipartFile articleImage : articleImages) {
      String articleImageUrl = s3Service.upload(articleImage, "articleImages");
      ArticleImg articleImg = new ArticleImg();
      articleImg.setArticle(article);
      articleImg.setArticleImgName(articleImage.getOriginalFilename());
      articleImg.setArticleImgUrl(articleImageUrl);
      articleImgRepository.save(articleImg);
    }
  }

  public void uploadCommunityImg(List<MultipartFile> communityImages, Community community)
      throws IOException {
    for (MultipartFile communityImage : communityImages) {
      String communityImageUrl = s3Service.upload(communityImage, "communityImages");
      CommunityImg communityImg = new CommunityImg();
      communityImg.setCommunity(community);
      communityImg.setCommunityImgName(communityImage.getOriginalFilename());
      communityImg.setCommunityImgUrl(communityImageUrl);
      communityImgRepository.save(communityImg);
    }
  }

  public void uploadDemandImg(List<MultipartFile> demandImages, Demand demand) throws IOException {
    for (MultipartFile demandImage : demandImages) {
      String demandImageUrl = s3Service.upload(demandImage, "demandImages");
      DemandImg demandImg = new DemandImg();
      demandImg.setDemand(demand);
      demandImg.setDemandImgName(demandImage.getOriginalFilename());
      demandImg.setDemandImgUrl(demandImageUrl);
      demandImgRepository.save(demandImg);
    }
  }

  public void uploadSupplyImg(List<MultipartFile> supplyImages, Supply supply) throws IOException {
    for (MultipartFile supplyImage : supplyImages) {
      String supplyImageUrl = s3Service.upload(supplyImage, "supplyImages");
      SupplyImg supplyImg = new SupplyImg();
      supplyImg.setSupply(supply);
      supplyImg.setSupplyImgName(supplyImage.getOriginalFilename());
      supplyImg.setSupplyImgUrl(supplyImageUrl);
      supplyImgRepository.save(supplyImg);
    }
  }
}
