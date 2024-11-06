package com.likelion.helfoome.domain.Img.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.likelion.helfoome.domain.post.entity.Supply;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "supplyImg")
public class SupplyImg extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplyId", referencedColumnName = "id", nullable = false)
  private Supply supply;

  @Column(name = "supplyImageName", nullable = false)
  private String supplyImageName;

  @Column(name = "supplyImageUrl", nullable = false)
  private String supplyImageUrl;
}
