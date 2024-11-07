package com.likelion.helfoome.domain.shop.dto.product;

import java.util.List;

import com.likelion.helfoome.domain.shop.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductManagingResponse {

  private Product product;
  private List<OrderInList> orders;
}
