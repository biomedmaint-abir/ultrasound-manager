package com.pfe.repositories;

import com.pfe.entities.CodeErreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CodeErreurRepository extends JpaRepository<CodeErreur, Long> {
    List<CodeErreur> findByModeleId(Long modeleId);
    List<CodeErreur> findByCodeContaining(String code);
}