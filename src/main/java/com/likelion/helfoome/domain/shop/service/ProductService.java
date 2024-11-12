package com.likelion.helfoome.domain.shop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.helfoome.domain.order.entity.Order;
import com.likelion.helfoome.domain.order.repository.OrderRepository;
import com.likelion.helfoome.domain.shop.dto.product.MainProductResponse;
import com.likelion.helfoome.domain.shop.dto.product.OrderInList;
import com.likelion.helfoome.domain.shop.dto.product.ProductEditRequest;
import com.likelion.helfoome.domain.shop.dto.product.ProductInList;
import com.likelion.helfoome.domain.shop.dto.product.ProductList;
import com.likelion.helfoome.domain.shop.dto.product.ProductManagingResponse;
import com.likelion.helfoome.domain.shop.dto.product.ProductRequest;
import com.likelion.helfoome.domain.shop.dto.product.ProductResponse;
import com.likelion.helfoome.domain.shop.dto.product.SellingProductInList;
import com.likelion.helfoome.domain.shop.dto.product.SellingProductList;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserInfoRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.global.S3.service.S3Service;
import com.likelion.helfoome.global.distance.DistanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ShopRepository shopRepository;
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final S3Service s3Service;
  private final DistanceService distanceService;

  @Transactional
  public String createProduct(ProductRequest productRequest) throws IOException {

    System.out.println("Shop ID type: " + productRequest.getShopId().getClass().getName());
    System.out.println("Shop ID from request: " + productRequest.getShopId());
    // Shop 엔티티 조회
    Optional<Shop> shopOptional = shopRepository.findById(productRequest.getShopId());
    if (shopOptional.isEmpty()) {
      throw new IllegalArgumentException("Invalid shopID");
    }
    Shop shop = shopOptional.get();
    // Product 엔티티 생성 및 저장

    Product product = new Product();
    product.setShop(shop);
    product.setProductName(productRequest.getProductName());
    product.setPrice(productRequest.getPrice());
    product.setDiscountPrice(productRequest.getDiscountPrice());
    product.setQuantity(productRequest.getQuantity());
    product.setDiscountPercent(productRequest.getDiscountPercent());
    product.setIsSelling(true);
    if (productRequest.getRealAddr() == null) {
      product.setRealAddr(shop.getShopAddr());
    } else {
      product.setRealAddr(productRequest.getRealAddr());
    }
    product.setProductImageName(productRequest.getProductImg().getOriginalFilename());
    String imgUrl = s3Service.upload(productRequest.getProductImg(), "productImages");
    product.setProductImageURL(imgUrl);

    productRepository.save(product);

    return "상품이 성공적으로 등록되었습니다.";
  }

  // 상품상세
  public ProductResponse getProductDetail(Long productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid productID"));

    ProductResponse productResponse = new ProductResponse();
    productResponse.setShopId(product.getShop().getId());
    productResponse.setShopName(product.getShop().getShopName());
    productResponse.setProductId(product.getId());
    productResponse.setProductName(product.getProductName());
    productResponse.setPrice(product.getPrice());
    product.setDiscountPrice(product.getDiscountPrice());
    productResponse.setQuantity(product.getQuantity());
    productResponse.setDiscountPercent(product.getDiscountPercent());
    productResponse.setIsSelling(false);
    productResponse.setProductImgUrl(product.getProductImageURL());

    return productResponse;
  }

  // 상품 수정
  public String updateProduct(ProductEditRequest request) {
    Product product = productRepository.findById(request.getProductId()).orElseThrow();

    if (request.getQuantity() != null) {
      if (request.getQuantity() < 0) {
        throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
      }
      if (request.getQuantity() == 0) {
        product.setIsSelling(false);
      }
    }

    if (request.getProductName() != null) {
      product.setProductName(request.getProductName());
    }
    if (request.getPrice() != null) {
      product.setPrice(request.getPrice());
    }
    if (request.getDiscountPercent() != null) {
      product.setDiscountPercent(request.getDiscountPercent());
    }
    if (request.getDiscountPrice() != null) {
      product.setDiscountPrice(request.getDiscountPrice());
    }
    if (request.getRealAddr() != null) {
      product.setRealAddr(request.getRealAddr());
    }
    productRepository.save(product);
    return "상품 수정 완료";
  }

  @Transactional
  public String deleteProduct(Long productId) {
    Optional<Product> productOptional = productRepository.findById(productId);
    if (productOptional.isEmpty()) {
      return "해당 상품을 찾을 수 없습니다.";
    }
    // Order에서 해당 productId가 존재하는지 확인
    boolean orderExists = orderRepository.existsByProductId(productId);
    if (orderExists) {
      return "이 상품은 주문 내역에 사용되고 있어 삭제할 수 없습니다.";
    }

    productRepository.deleteById(productId);
    return "상품이 성공적으로 삭제되었습니다.";
  }

  public ProductList getSortedProductList(
      String email, Integer shopType, int sort, String marketName) {
    String userAddr =
        userInfoRepository.findByUser_Email(email).orElseThrow().getActivityLocation();
    ProductList productList = getProductList(userAddr, shopType, marketName);

    // 정렬(sort 0, 1, 2면 각각 단거리, 최저가, 최고할인순)
    List<ProductInList> sortedList =
        productList.getProductInList().stream()
            .sorted(
                (p1, p2) -> {
                  switch (sort) {
                    case 0: // 단거리순
                      return p1.getDistance().compareTo(p2.getDistance());
                    case 1: // 최저가순 (discountPrice 기준)
                      return Integer.parseInt(p1.getDiscountPrice())
                          - Integer.parseInt(p2.getDiscountPrice());
                    case 2: // 최고할인순 (discountPercent 기준)
                      return Integer.parseInt(p2.getDiscountPercent())
                          - Integer.parseInt(p1.getDiscountPercent());
                    default:
                      throw new IllegalArgumentException("Invalid sort value: " + sort);
                  }
                })
            .collect(Collectors.toList());

    ProductList sortedProductList = new ProductList();
    sortedProductList.setProductInList(sortedList);

    return sortedProductList;
  }

  public ProductList getProductList(String userAddr, Integer shopType, String marketName) {
    List<Shop> shops = new ArrayList<>();
    switch (shopType) {
      case 0:
        shops = shopRepository.findByMarketName(marketName);
        break;
      case 1:
      case 2:
        shops = shopRepository.findByShopType(shopType);
        break;
    }
    List<ProductInList> returnProducts = new ArrayList<>();
    for (Shop shop : shops) {
      List<Product> products;
      products = productRepository.findByShopIdAndIsSellingTrue(shop.getId());

      for (Product product : products) {
        if (distanceService.getDistance(userAddr, product.getRealAddr()) <= 5000) {
          ProductInList productInList = new ProductInList();
          productInList.setProductId(product.getId());
          productInList.setShop(shop.getShopName());
          productInList.setProductName(product.getProductName());
          productInList.setPrice(product.getPrice());
          productInList.setDiscountPercent(product.getDiscountPercent());
          productInList.setQuantity(product.getQuantity());
          productInList.setDiscountPrice(product.getDiscountPrice());
          Long distance = distanceService.getDistance(userAddr, product.getRealAddr());
          productInList.setDistance(distance);
          productInList.setImgUrl(product.getProductImageURL());

          returnProducts.add(productInList);
        }
      }
    }
    ProductList productList = new ProductList();
    productList.setProductInList(returnProducts);
    return productList;
  }

  public ProductManagingResponse getProductManaging(Long productId) {

    ProductManagingResponse response = new ProductManagingResponse();
    Product product = productRepository.findById(productId).orElseThrow();
    List<OrderInList> returnOrders = new ArrayList<>();
    List<Order> orders = orderRepository.findByProductId(productId);
    response.setProduct(product);
    for (Order order : orders) {
      OrderInList orderInList = new OrderInList();
      orderInList.setOrderId(order.getId());
      orderInList.setNickname(order.getUser().getNickname());
      orderInList.setPIN(order.getPIN());
      orderInList.setOrderTime(order.getCreatedDate());
      returnOrders.add(orderInList);
    }
    response.setOrders(returnOrders);
    return response;
  }

  public SellingProductList getSellingProduct(Long shopId) {
    Shop shop = shopRepository.findById(shopId).orElseThrow();
    List<SellingProductInList> returnProducts = new ArrayList<>();
    List<Product> products;
    products = productRepository.findByShopIdAndIsSellingTrue(shopId);
    for (Product product : products) {
      SellingProductInList productInList = new SellingProductInList();
      productInList.setProductId(product.getId());
      productInList.setShop(shop.getShopName());
      productInList.setProductName(product.getProductName());
      productInList.setQuantity(product.getQuantity());
      productInList.setOrders(orderRepository.countByProductId(product.getId()));
      productInList.setImgUrl(product.getProductImageURL());

      returnProducts.add(productInList);
    }
    SellingProductList productList = new SellingProductList();
    productList.setProductInList(returnProducts);
    return productList;
  }

  public SellingProductList getUnSellingProduct(Long shopId) {
    Shop shop = shopRepository.findById(shopId).orElseThrow();
    List<SellingProductInList> returnProducts = new ArrayList<>();
    List<Product> products;
    products = productRepository.findByShopIdAndIsSellingFalse(shopId);
    for (Product product : products) {
      SellingProductInList productInList = new SellingProductInList();
      productInList.setProductId(product.getId());
      productInList.setShop(shop.getShopName());
      productInList.setProductName(product.getProductName());
      productInList.setQuantity(product.getQuantity());
      productInList.setOrders(orderRepository.countByProductId(product.getId()));
      productInList.setImgUrl(product.getProductImageURL());
      returnProducts.add(productInList);
    }
    SellingProductList productList = new SellingProductList();
    productList.setProductInList(returnProducts);
    return productList;
  }

  public List<MainProductResponse> getLastProductList(String currentTime) {
    List<Shop> shops = shopRepository.findAll();

    List<Shop> sortedShops =
        shops.stream()
            .sorted(
                Comparator.comparing(
                    shop -> {
                      String endTime = shop.getBusinessHours().split(", ")[1].replaceAll(":", "");
                      return Integer.parseInt(endTime)
                          - Integer.parseInt(currentTime.replaceAll(":", ""));
                    }))
            .toList();

    List<MainProductResponse> mainProductResponses = new ArrayList<>();
    MainProductResponse response;
    for (Shop shop : sortedShops) {
      response =
          new MainProductResponse(
              shop.getShopName(),
              shop.getProductList().getFirst().getId(),
              shop.getProductList().getFirst().getProductName(),
              shop.getProductList().getFirst().getDiscountPrice(),
              shop.getProductList().getFirst().getDiscountPercent(),
              shop.getProductList().getFirst().getProductImageURL());

      mainProductResponses.add(response);
    }

    return mainProductResponses;
  }

  public List<MainProductResponse> getRandomProductList(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

    List<Product> productList = productRepository.findByRealAddrStartingWith(
        user.getUserInfo().getActivityLocation().substring(0, 3));

    List<MainProductResponse> mainProductResponses = new ArrayList<>();
    MainProductResponse response;
    Set<Integer> set = new HashSet<>();
    Random random = new Random();

    int selectionSize = Math.min(5, productList.size()); // 최대 5개, productList 크기만큼만 선택

    while (set.size() < selectionSize) {
      int number = random.nextInt(productList.size());
      set.add(number);
    }

    for (Integer index : set) {
      Product product = productList.get(index);
      response = new MainProductResponse(
          product.getShop().getShopName(),
          product.getId(),
          product.getProductName(),
          product.getDiscountPrice(),
          product.getDiscountPercent(),
          product.getProductImageURL()
      );
      mainProductResponses.add(response);
    }

    return mainProductResponses;
  }

}
