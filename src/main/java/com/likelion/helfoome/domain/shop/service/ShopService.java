package com.likelion.helfoome.domain.shop.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.shop.dto.ShopRegisterRequest;
import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.global.S3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopService {

  private final ShopRepository shopRepository;
  private final UserRepository userRepository;
  private final S3Service s3Service;

  // 전송받은 사업자 번호 또는 현재 사용자의 가게가 DB에 존재하는지 확인
  public Boolean isTaxIdExist(String email, String taxId) {
    Optional<Shop> existingShop = shopRepository.findByUser_Email(email);
    Optional<Shop> existingTaxId = shopRepository.findByTaxId(taxId);

    return (existingShop.isPresent() || existingTaxId.isPresent());
  }

  // 쿠키로 현재 사용자 email 받아와서 가게 생성
  public String storeRegister(String email, ShopRegisterRequest request) throws IOException {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    Shop newShop = new Shop();
    newShop.setUser(user);
    newShop.setShopName(request.getShopName());
    newShop.setShopType(request.getShopType());
    newShop.setMarketName(request.getMarketName());
    newShop.setTaxId(request.getTaxId());
    newShop.setBusinessHours(request.getBusinessHours());
    newShop.setDayOff(request.getDayOff());
    newShop.setShopAddr(request.getShopAddr());
    newShop.setShopContact(request.getShopContact());
    newShop.setShopImageName(request.getShopImg().getOriginalFilename());
    String imgUrl = s3Service.upload(request.getShopImg(), "shopImages");
    newShop.setShopImageURL(imgUrl);

    shopRepository.save(newShop);

    return "가게가 성공적으로 등록되었습니다.";
  }
}
