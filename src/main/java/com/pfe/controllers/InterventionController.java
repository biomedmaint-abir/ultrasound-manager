package com.pfe.controllers;

import com.pfe.entities.Intervention;
import com.pfe.services.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interventions")
@CrossOrigin(origins = "*")
public class InterventionController {

    @Autowired
    private InterventionService interventionService;

    @GetMapping
    public List<Intervention> getAll() { return interventionService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Intervention> getById(@PathVariable Long id) {
        return interventionService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/equipement/{equipementId}")
    public List<Intervention> getByEquipement(@PathVariable Long equipementId) {
        return interventionService.findByEquipementId(equipementId);
    }

    @GetMapping("/technicien/{technicienId}")
    public List<Intervention> getByTechnicien(@PathVariable Long technicienId) {
        return interventionService.findByTechnicienId(technicienId);
    }

    @GetMapping("/mttr")
    public ResponseEntity<Double> getMTTR() { return ResponseEntity.ok(interventionService.calculerMTTR()); }

    @PostMapping
    public Intervention create(@RequestBody Intervention intervention) { return interventionService.save(intervention); }

    @PutMapping("/{id}")
    public ResponseEntity<Intervention> update(@PathVariable Long id, @RequestBody Intervention intervention) {
        return interventionService.findById(id).map(i -> { intervention.setId(id); return ResponseEntity.ok(interventionService.save(intervention)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interventionService.deleteById(id); return ResponseEntity.noContent().build();
    }
}