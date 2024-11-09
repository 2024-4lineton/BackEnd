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
@Table(name = "userInfo")
public class UserInfo extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "phone", nullable = false, unique = true)
  private String phone;

  @Column(name = "birth", nullable = false)
  private String birth;

  @Column(name = "gender", nullable = false)
  private String gender;

  @Column(name = "activityLocation", nullable = false)
  private String activityLocation;

  @Column(name = "foodCategory", nullable = false)
  private String foodCategory;

  @Column(name = "ToS", nullable = false)
  private Boolean ToS;

  @Column(name = "privacyPolicy", nullable = false)
  private Boolean privacyPolicy;

  @Column(name = "LBS", nullable = false)
  private Boolean LBS;

  @Column(name = "marketingPolicy", nullable = false)
  private Boolean marketingPolicy;

  @Column(name = "profileImageName", nullable = false)
  private String profileImageName;

  @Column(name = "profileImageURL", nullable = false)
  private String profileImageURL;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;
}
