package com.pfe.controllers;

import com.pfe.entities.ChecklistMaintenance;
import com.pfe.entities.Intervention;
import com.pfe.repositories.ChecklistMaintenanceRepository;
import com.pfe.repositories.InterventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/checklist")
@CrossOrigin(origins = "*")
public class ChecklistMaintenanceController {

    @Autowired
    private ChecklistMaintenanceRepository checklistRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    @GetMapping("/intervention/{interventionId}")
    public List<ChecklistMaintenance> getByIntervention(@PathVariable Long interventionId) {
        return checklistRepository.findByInterventionId(interventionId);
    }

    @PostMapping("/intervention/{interventionId}/bulk")
    public ResponseEntity<List<ChecklistMaintenance>> saveBulk(
            @PathVariable Long interventionId,
            @RequestBody List<ChecklistMaintenance> items) {
        checklistRepository.deleteByInterventionId(interventionId);
        interventionRepository.findById(interventionId).ifPresent(intervention -> {
            items.forEach(item -> item.setIntervention(intervention));
        });
        return ResponseEntity.ok(checklistRepository.saveAll(items));
    }
}
