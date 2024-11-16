package com.likelion.helfoome.global.auth.dto;

public interface OAuth2Response {
  String getProvider();

  String getProviderId();

  String getEmail();

  String getNickname();
}
