package com.pfe.repositories;

import com.pfe.entities.ChecklistMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChecklistMaintenanceRepository extends JpaRepository<ChecklistMaintenance, Long> {
    List<ChecklistMaintenance> findByInterventionId(Long interventionId);
    void deleteByInterventionId(Long interventionId);
}
