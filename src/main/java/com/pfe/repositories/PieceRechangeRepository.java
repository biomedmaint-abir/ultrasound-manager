package com.pfe.repositories;

import com.pfe.entities.PieceRechange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceRechangeRepository extends JpaRepository<PieceRechange, Long> {
}