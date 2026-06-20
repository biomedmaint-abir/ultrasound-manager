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
            Intervention saved = interventionService.save(intervention);
            // Si intervention TERMINEE → remettre équipement EN_SERVICE
            if (intervention.getStatut() == StatutIntervention.TERMINEE && intervention.getEquipement() != null) {
                equipementRepository.findById(intervention.getEquipement().getId()).ifPresent(e -> {
                    e.setStatut(com.pfe.enums.StatutEquipement.EN_SERVICE);
                    equipementRepository.save(e);
                });
            }
            return ResponseEntity.ok(saved);
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
                i.setCommentaireRejet(null);
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

    @PostMapping("/generer-planning")
    public ResponseEntity<Map<String, Object>> genererPlanning(@RequestBody Map<String, Object> body) {
        try {
            int annee = Integer.parseInt(body.get("annee").toString());
            List<Map<String, Object>> equipements = (List<Map<String, Object>>) body.get("equipements");

            List<String> visitesDates = body.containsKey("visitesDates") ?
                (List<String>) body.get("visitesDates") :
                java.util.Arrays.asList(annee + "-03-15", annee + "-06-15", annee + "-09-15", annee + "-12-15");

            String[] descLabels = {"1ère visite", "2ème visite", "3ème visite", "4ème visite"};

            int count = 0;
            for (Map<String, Object> eq : equipements) {
                Long equipementId = Long.valueOf(eq.get("id").toString());
                for (int i = 0; i < visitesDates.size(); i++) {
                    final String dateStr = visitesDates.get(i);
                    final String desc = "Maintenance préventive — " + (i < descLabels.length ? descLabels[i] : (i+1) + "ème visite") + " " + annee;
                    Intervention inv = new Intervention();
                    inv.setType(com.pfe.enums.TypeIntervention.PREVENTIF);
                    inv.setStatut(StatutIntervention.EN_ATTENTE);
                    inv.setDateIntervention(java.time.LocalDate.parse(dateStr));
                    inv.setDescriptionPanne(desc);
                    equipementRepository.findById(equipementId).ifPresent(e -> {
                        inv.setEquipement(e);
                        e.setStatut(com.pfe.enums.StatutEquipement.EN_MAINTENANCE);
                        equipementRepository.save(e);
                    });
                    interventionService.save(inv);
                    count++;
                }
            }

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("interventionsCreees", count);
            result.put("message", "Planning généré — " + count + " interventions créées");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interventionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
