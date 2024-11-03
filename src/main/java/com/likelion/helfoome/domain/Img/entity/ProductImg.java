package com.likelion.helfoome.domain.Img.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "productImg")
public class ProductImg extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "productId", referencedColumnName = "id", nullable = false)
  private Product product;

  @Column(name = "productImageName", nullable = false)
  private String productImageName;

  @Column(name = "productImageUrl", nullable = false)
  private String productImageUrl;
}
