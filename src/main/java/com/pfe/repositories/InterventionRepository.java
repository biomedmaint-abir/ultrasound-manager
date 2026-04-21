package com.pfe.repositories;

import com.pfe.entities.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    List<Intervention> findByEquipementId(Long equipementId);
    List<Intervention> findByTechnicienId(Long technicienId);
}