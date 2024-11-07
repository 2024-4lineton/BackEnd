package com.likelion.helfoome.domain.Img.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.Img.entity.UserProfileImg;

@Repository
public interface UserProfileImgRepository extends JpaRepository<UserProfileImg, Long> {
  UserProfileImg findByUserInfoId(Long userInfoId);
}
