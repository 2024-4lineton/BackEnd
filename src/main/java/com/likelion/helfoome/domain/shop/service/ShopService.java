package com.likelion.helfoome.domain.shop.service;

import com.likelion.helfoome.domain.shop.dto.ShopInList;
import com.likelion.helfoome.domain.shop.dto.ShopList;
import com.likelion.helfoome.domain.shop.dto.ShopRegisterRequest;
import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.domain.shop.repository.TradShopRepository;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.entity.UserInfo;
import com.likelion.helfoome.domain.user.repository.UserInfoRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.global.S3.service.S3Service;
import com.likelion.helfoome.global.distance.DistanceService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopService {

  private final ShopRepository shopRepository;
  private final UserRepository userRepository;
  private final S3Service s3Service;
  private final DistanceService distanceService;
  private final TradShopRepository tradShopRepository;
  private final ProductRepository productRepository;
  private final UserInfoRepository userInfoRepository;

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

    user.setShop(newShop);
    userRepository.save(user);

    return "가게가 성공적으로 등록되었습니다.";
  }

  public ShopList getSortedShopList(String email, int sort) {
    UserInfo userInfo =
        userInfoRepository
            .findByUser_Email(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    String userAddr = userInfo.getActivityLocation();
    // 10KM 내에 있는 TradShop 조회
    List<ShopInList> shopInLists =
        tradShopRepository.findAll().stream()
            .filter(
                tradShop ->
                    distanceService.getDistance(userAddr, tradShop.getTradShopAddr()) <= 10000)
            .map(
                tradShop -> {
                  ShopInList shopInList = new ShopInList();
                  shopInList.setShopId(tradShop.getId());
                  shopInList.setShopName(tradShop.getShopName());
                  shopInList.setDistance(
                      distanceService.getDistance(userAddr, tradShop.getTradShopAddr()));
                  shopInList.setProductCount(
                      productRepository.countByShopIdAndIsSellingTrue(tradShop.getId()));
                  shopInList.setImgUrl(tradShop.getTradShopImageUrl());
                  return shopInList;
                })
            .collect(Collectors.toList());

    // 정렬(sort 0: 가까운순, 1: 상품 많은 순)
    List<ShopInList> sortedList =
        shopInLists.stream()
            .sorted(
                (s1, s2) -> {
                  switch (sort) {
                    case 0: // 가까운순
                      return s1.getDistance().compareTo(s2.getDistance());
                    case 1: // 상품 많은 순
                      return s2.getProductCount().compareTo(s1.getProductCount());
                    default:
                      throw new IllegalArgumentException("Invalid sort value: " + sort);
                  }
                })
            .collect(Collectors.toList());

    ShopList shopList = new ShopList();
    shopList.setShopInList(sortedList);

    return shopList;
  }

  public Boolean isExistTradShop(String shopName) {
    return tradShopRepository.existsByShopName(shopName);
  }
}
