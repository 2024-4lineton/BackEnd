package com.likelion.helfoome.domain.Img.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.Img.entity.ArticleImg;

@Repository
public interface ArticleImgRepository extends JpaRepository<ArticleImg, Long> {}