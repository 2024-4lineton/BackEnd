package com.likelion.helfoome.global.auth.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.likelion.helfoome.domain.user.dto.UserInfoResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
  private final UserInfoResponse userInfoResponse;

  @Override
  public Map<String, Object> getAttributes() {
    return null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();

    collection.add((GrantedAuthority) userInfoResponse::getRole);

    return collection;
  }

  @Override
  public String getName() {
    return userInfoResponse.getNickname();
  }

  public String getEmail() {
    return userInfoResponse.getEmail();
  }
}
