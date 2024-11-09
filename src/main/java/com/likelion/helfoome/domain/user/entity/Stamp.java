package com.likelion.helfoome.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stamp")
public class Stamp extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;

  @Column(name = "mon")
  private Boolean mon = false;

  @Column(name = "total")
  private Boolean tue = false;

  @Column(name = "wed")
  private Boolean wed = false;

  @Column(name = "thu")
  private Boolean thu = false;

  @Column(name = "fri")
  private Boolean fri = false;

  @Column(name = "sat")
  private Boolean sat = false;

  @Column(name = "sun")
  private Boolean sun = false;
}
