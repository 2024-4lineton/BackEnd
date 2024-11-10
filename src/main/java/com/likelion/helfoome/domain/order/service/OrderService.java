package com.likelion.helfoome.domain.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.order.dto.OrderCompleteList;
import com.likelion.helfoome.domain.order.entity.Order;
import com.likelion.helfoome.domain.order.repository.OrderRepository;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.domain.user.service.NotificationService;
import com.likelion.helfoome.domain.user.service.StampService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ShopRepository shopRepository;
  private final ProductRepository productRepository;
  private final NotificationService notificationService;
  private final StampService stampService;

  // 주문테이블 생성 로직
  // 상품 id받아서 shop찾기, user받아서 일단 주문 테이블에 저장
  public String createOrder(Long shopId, Long productId, String email) {
    Product product = productRepository.findById(productId).orElseThrow();

    Order order = new Order();
    order.setUser(userRepository.findByEmail(email).orElseThrow());
    order.setShop(shopRepository.findById(shopId).orElseThrow());
    order.setProductId(product.getId());
    order.setProductName(product.getProductName());
    order.setMainImage(product.getProductImageURL());
    order.setOrderStatus(0);
    order.setTotalQuantity(1);
    order.setTotalPrice(product.getDiscountPrice());
    String pinNumber = generateUniquePin();
    order.setPIN(pinNumber);

    orderRepository.save(order);

    // 주문 성공적으로 작성 헀으면 상품 수량 하나 --
    product.updateQuantity(product.getQuantity() - 1);
    productRepository.save(product);

    notificationService.createNotification(
        userRepository.findByEmail(email).orElseThrow(), pinNumber);

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

  // 주문 확정
  public void confirmOrder(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow();
    order.setOrderStatus(1);
    order.setPIN("-1");
    orderRepository.save(order);
    // 스탬프 고치는거 서비스
    stampService.editStamp(order.getUser());
    notificationService.createNotification(order.getUser(), 0);
  }

  // 주문 취소
  public void discardOrder(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow();
    order.setOrderStatus(2);
    orderRepository.save(order);
    // 알림도 추가해야하나 사장이 주문 취소하면....?
    Product product = productRepository.findById(order.getProductId()).orElseThrow();
    // 주문 취소했으니 작성 헀으면 상품 수량 하나 ++
    product.updateQuantity(product.getQuantity() + 1);
    // 만약 수량이 0개가 돼서 판매 중지가 됐을지도 모르니 체크하고 만약 됐었따면 다시 판매상태로 변경
    if (!product.getIsSelling()) {
      product.setIsSelling(true);
    }
    productRepository.save(product);
    notificationService.createNotification(order.getUser(), 1);
  }

  // 주문 내역
  public List<OrderCompleteList> getOrderHistory(String email) {
    List<Order> orders = orderRepository.findAllByUser_Email(email);
    List<OrderCompleteList> response = new ArrayList<>();
    for (Order order : orders) {
      OrderCompleteList orderCompleteList = new OrderCompleteList();
      orderCompleteList.setShopName(order.getShop().getShopName());
      orderCompleteList.setProductName(order.getProductName());
      orderCompleteList.setOrderState(order.getOrderStatus());
      orderCompleteList.setCreatedAt(order.getCreatedDate());
      response.add(orderCompleteList);
    }
    return response;
  }
}
