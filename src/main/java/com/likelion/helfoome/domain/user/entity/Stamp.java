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

  @Column(name = "mon", nullable = false)
  private Boolean mon;

  @Column(name = "total", nullable = false)
  private Boolean tue = false;

  @Column(name = "wed", nullable = false)
  private Boolean wed = false;

  @Column(name = "thu", nullable = false)
  private Boolean thu = false;

  @Column(name = "fri", nullable = false)
  private Boolean fri = false;

  @Column(name = "sat", nullable = false)
  private Boolean sat = false;

  @Column(name = "sun", nullable = false)
  private Boolean sun = false;
}
