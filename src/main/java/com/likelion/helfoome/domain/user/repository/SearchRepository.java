package com.likelion.helfoome.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.user.entity.Search;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
  List<Search> findByUser_Email(String email);
}
