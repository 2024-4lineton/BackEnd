package com.likelion.helfoome.domain.cart.dto;

import com.likelion.helfoome.domain.cart.entity.CartProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
  CartProduct cartProduct;
}
