package com.pfe.repositories;

import com.pfe.entities.Modele;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeleRepository extends JpaRepository<Modele, Long> {
}