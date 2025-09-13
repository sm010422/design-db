package com.example.designdb.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.designdb.domain.card.Card;

public interface CardRepository extends JpaRepository<Card, String> {
}
