package com.pfe.controllers;

import com.pfe.entities.Intervention;
import com.pfe.enums.StatutIntervention;
import com.pfe.services.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/en-attente-validation")
    public List<Intervention> getEnAttenteValidation() {
        return interventionService.findAll().stream()
            .filter(i -> i.getStatut() == StatutIntervention.EN_ATTENTE_VALIDATION)
            .toList();
    }

    @GetMapping("/mttr")
    public ResponseEntity<Double> getMTTR() { return ResponseEntity.ok(interventionService.calculerMTTR()); }

    @PostMapping
    public Intervention create(@RequestBody Intervention intervention) { return interventionService.save(intervention); }

    @PutMapping("/{id}")
    public ResponseEntity<Intervention> update(@PathVariable Long id, @RequestBody Intervention intervention) {
        return interventionService.findById(id).map(i -> {
            intervention.setId(id);
            return ResponseEntity.ok(interventionService.save(intervention));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<Intervention> valider(@PathVariable Long id) {
        return interventionService.findById(id).map(i -> {
            i.setStatut(StatutIntervention.TERMINEE);
            i.setDateValidation(LocalDateTime.now().toString());
            i.setCommentaireRejet(null);
            return ResponseEntity.ok(interventionService.save(i));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/rejeter")
    public ResponseEntity<Intervention> rejeter(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return interventionService.findById(id).map(i -> {
            i.setStatut(StatutIntervention.EN_COURS);
            i.setCommentaireRejet(body.get("commentaire"));
            i.setDateValidation(null);
            return ResponseEntity.ok(interventionService.save(i));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interventionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
