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

import com.likelion.helfoome.domain.post.entity.Demand;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "demandImg")
public class DemandImg extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "demandImgName", nullable = false)
  private String demandImgName;

  @Column(name = "demandImgUrl", nullable = false)
  private String demandImgUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "demandId", referencedColumnName = "id", nullable = false)
  private Demand demand;
}
