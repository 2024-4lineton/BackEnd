package com.likelion.helfoome.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.user.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
  Optional<UserInfo> findByUser_Email(String email);

  Optional<UserInfo> findByUser_Nickname(String nickname);
}
