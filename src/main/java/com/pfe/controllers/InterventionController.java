package com.pfe.controllers;

import com.pfe.entities.Intervention;
import com.pfe.entities.Equipement;
import com.pfe.entities.Utilisateur;
import com.pfe.enums.StatutIntervention;
import com.pfe.services.InterventionService;
import com.pfe.repositories.EquipementRepository;
import com.pfe.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interventions")
@CrossOrigin(origins = "*")
public class InterventionController {

    @Autowired
    private InterventionService interventionService;

    @Autowired
    private EquipementRepository equipementRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

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

    @GetMapping("/non-assignees")
    public List<Intervention> getNonAssignees() {
        return interventionService.findAll().stream()
            .filter(i -> i.getTechnicien() == null && i.getNomFse() == null)
            .collect(Collectors.toList());
    }

    @GetMapping("/mttr")
    public ResponseEntity<Double> getMTTR() { return ResponseEntity.ok(interventionService.calculerMTTR()); }

    @PostMapping
    public Intervention create(@RequestBody Intervention intervention) {
        Intervention saved = interventionService.save(intervention);
        if (intervention.getEquipement() != null && intervention.getEquipement().getId() != null) {
            equipementRepository.findById(intervention.getEquipement().getId()).ifPresent(e -> {
                e.setStatut(com.pfe.enums.StatutEquipement.EN_MAINTENANCE);
                equipementRepository.save(e);
            });
        }
        return saved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Intervention> update(@PathVariable Long id, @RequestBody Intervention intervention) {
        return interventionService.findById(id).map(i -> {
            intervention.setId(id);
            return ResponseEntity.ok(interventionService.save(intervention));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/assigner-fse")
    public ResponseEntity<Intervention> assignerFse(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return interventionService.findById(id).map(i -> {
            Long fseId = Long.valueOf(body.get("fseId").toString());
            String nomFse = body.get("nomFse") != null ? body.get("nomFse").toString() : "";
            utilisateurRepository.findById(fseId).ifPresent(fse -> {
                i.setTechnicien(fse);
                i.setNomFse(nomFse);
                i.setStatut(StatutIntervention.EN_COURS);
            });
            return ResponseEntity.ok(interventionService.save(i));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/en-attente-validation")
    public List<Intervention> getEnAttenteValidation() {
        return interventionService.findAll().stream()
            .filter(i -> i.getStatut() == StatutIntervention.EN_ATTENTE_VALIDATION)
            .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interventionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
