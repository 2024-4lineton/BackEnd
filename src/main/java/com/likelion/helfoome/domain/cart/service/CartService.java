package com.likelion.helfoome.domain.cart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.cart.dto.AddProductRequest;
import com.likelion.helfoome.domain.cart.dto.CartProductResponse;
import com.likelion.helfoome.domain.cart.entity.Cart;
import com.likelion.helfoome.domain.cart.entity.CartProduct;
import com.likelion.helfoome.domain.cart.repository.CartProductRepository;
import com.likelion.helfoome.domain.cart.repository.CartRepository;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final CartProductRepository cartProductRepository;
  private final ProductRepository productRepository;

  // 현재 사용자의 장바구니 생성
  public String createCart(String email) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    if (cartRepository.findByUser_Email(email).isPresent()) {
      return "사용자의 장바구니가 이미 존재합니다.";
    }

    Cart cart = new Cart();
    cart.setUser(user);
    cart.setCartProductList(new ArrayList<>());

    cartRepository.save(cart);

    return "장바구니가 정상적으로 등록되었습니다.";
  }

  // 사용자 장바구니에 상품 등록
  public String addProduct(String email, AddProductRequest request) {
    Cart cart =
        cartRepository
            .findByUser_Email(email)
            .orElseThrow(() -> new RuntimeException("User's Cart not found"));
    List<CartProduct> cartProductList = cart.getCartProductList();
    CartProduct cartProduct = new CartProduct();

    cartProduct.setUser(cart.getUser());
    cartProduct.setCart(cart);
    cartProduct.setProduct(
        productRepository
            .findById(request.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found")));

    cartProductList.add(cartProduct);
    cart.setCartProductList(cartProductList);

    cartRepository.save(cart);
    cartProductRepository.save(cartProduct);

    return "장바구니에 상품이 정상적으로 등록되었습니다.";
  }

  // 특정 사용자 장바구니 상품 조회
  public List<CartProductResponse> getCartProducts(String email) {
    log.info("Get cart product for email: {}", email);

    Cart cart =
        cartRepository
            .findByUser_Email(email)
            .orElseThrow(() -> new RuntimeException("User's Cart not found"));
    List<CartProduct> cartProductList = cart.getCartProductList();
    List<CartProductResponse> cartProductResponses = new ArrayList<>();
    CartProductResponse cartProductResponse;

    for (CartProduct cartProduct : cartProductList) {
      cartProductResponse =
          new CartProductResponse(
              cartProduct.getProduct().getShop().getShopName(),
              cartProduct.getProduct().getProductName(),
              cartProduct.getProduct().getDiscountPrice(),
              cartProduct.getProduct().getIsSelling(),
              cartProduct.getProduct().getProductImageURL());
      cartProductResponses.add(cartProductResponse);
    }

    return cartProductResponses;
  }
}
