package com.likelion.helfoome.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.user.entity.Stamp;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {}
