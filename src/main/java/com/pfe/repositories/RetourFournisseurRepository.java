package com.pfe.repositories;

import com.pfe.entities.RetourFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetourFournisseurRepository extends JpaRepository<RetourFournisseur, Long> {
}