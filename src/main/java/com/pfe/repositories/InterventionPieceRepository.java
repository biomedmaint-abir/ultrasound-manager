package com.pfe.repositories;

import com.pfe.entities.InterventionPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterventionPieceRepository extends JpaRepository<InterventionPiece, Long> {
}