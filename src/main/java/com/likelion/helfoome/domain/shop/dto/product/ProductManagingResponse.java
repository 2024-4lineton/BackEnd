package com.likelion.helfoome.domain.shop.dto.product;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductManagingResponse {


  private ProductResponse productResponse;
  private List<OrderInList> orders;
}
