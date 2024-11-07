package com.likelion.helfoome.domain.order.service;

import com.likelion.helfoome.domain.Img.entity.ProductImg;
import com.likelion.helfoome.domain.Img.repository.ProductImgRepository;
import com.likelion.helfoome.domain.order.dto.OrderRequest;
import com.likelion.helfoome.domain.order.entity.Order;
import com.likelion.helfoome.domain.order.repository.OrderRepository;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ShopRepository shopRepository;
  private final ProductRepository productRepository;
  private final ProductImgRepository productImgRepository;

  // 주문테이블 생성 로직
  // 상품 id받아서 shop찾기, user받아서 일단 주문 테이블에 저장
  public String createOrder(OrderRequest orderRequest) {
    Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow();
    ProductImg productImg =
        productImgRepository.findByProductId(orderRequest.getProductId()).getFirst();

    Order order = new Order();
    order.setUser(userRepository.findByEmail(orderRequest.getUserEmail()).orElseThrow());
    order.setShop(shopRepository.findById(orderRequest.getShopId()).orElseThrow());
    order.setProductId(product.getId());
    order.setMainImage(productImg.getProductImageUrl());
    order.setOrderStatus(0);
    order.setTotalQuantity(orderRequest.getQuantity());
    order.setTotalPrice(
        String.valueOf(orderRequest.getQuantity() * Integer.parseInt(product.getDiscountPrice())));

    String pinNumber = generateUniquePin();
    order.setPIN(pinNumber);

    orderRepository.save(order);

    // 주문 성공적으로 작성 헀으면 상품 수량 하나 --
    product.updateQuantity(orderRequest.getQuantity() - 1);
    productRepository.save(product);

    // 여기따가 알림 객체 생성하는 코드도 추가해야됨 하지만? 난 일단 주문 끝내고 할거야 지금 계속 깊이우선 탐색 하느라 대가리 빠개질거같음 ㅠ

    // 까먹을까봐 메오 isSelling인 애들만 리스트에 담도록 필터링 하는거 추가요
    return pinNumber;
  }

  private String generateUniquePin() {
    Random random = new Random();
    String pin;
    do {
      // 0000부터 9999까지의 숫자 중 랜덤하게 4자리 생성
      pin = String.format("%04d", random.nextInt(10000));
    } while (orderRepository.existsByPIN(pin)); // 생성된 핀 번호가 데이터베이스에 존재하는지 확인

    return pin;
  }

  //주문 확정
  public void confirmOrder(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow();
    order.setOrderStatus(1);
    orderRepository.save(order);
    //여기에 스탬프 생성하는 코드 추가해야함
  }

  //주문 취소
  public void discardOrder(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow();
    order.setOrderStatus(2);
    orderRepository.save(order);
    //알림도 추가해야하나 사장이 주문 취소하면....?
    Product product = productRepository.findById(order.getProductId()).orElseThrow();
    // 주문 취소했으니 작성 헀으면 상품 수량 하나 ++
    product.updateQuantity(product.getQuantity() + 1);
    //만약 수량이 0개가 돼서 판매 중지가 됐을지도 모르니 체크하고 만약 됐었따면 다시 판매상태로 변경
    if (!product.getIsSelling()) {
      product.setIsSelling(true);
    }
    productRepository.save(product);
  }
}