package com.pfe.controllers;

import com.pfe.repositories.EquipementRepository;
import com.pfe.repositories.InterventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/optimisation")
@CrossOrigin(origins = "*")
public class OptimisationController {

    @Autowired
    private EquipementRepository equipementRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    @GetMapping("/score-fiabilite")
    public ResponseEntity<List<Map<String, Object>>> getScoreFiabilite() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate il12mois = LocalDate.now().minusMonths(12);

        equipementRepository.findAll().forEach(eq -> {
            List<com.pfe.entities.Intervention> interventions = interventionRepository.findByEquipementId(eq.getId());

            // Pannes correctives 12 derniers mois
            long pannes = interventions.stream()
                .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.CORRECTIF
                    && i.getDateIntervention() != null
                    && i.getDateIntervention().isAfter(il12mois))
                .count();

            // MTTR moyen
            double mttr = interventions.stream()
                .filter(i -> i.getDureeHeures() != null)
                .mapToDouble(i -> i.getDureeHeures())
                .average().orElse(0);

            // Age equipement
            int age = 0;
            if (eq.getDateInstallation() != null) {
                age = LocalDate.now().getYear() - eq.getDateInstallation().getYear();
            }

            // Preventives terminées
            long totalPrev = interventions.stream()
                .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.PREVENTIF).count();
            long termineePrev = interventions.stream()
                .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.PREVENTIF
                    && i.getStatut() == com.pfe.enums.StatutIntervention.TERMINEE).count();

            // Calcul score
            int score = 100;
            score -= (int) pannes * 15;
            if (mttr > 4) score -= 10;
            if (age > 8) score -= 20;
            else if (age > 5) score -= 10;
            if (totalPrev > 0 && termineePrev == totalPrev) score += 10;
            score = Math.max(0, Math.min(100, score));

            // Niveau et recommandation
            String niveau, recommandation, couleur;
            if (score >= 80) {
                niveau = "Fiable"; couleur = "vert";
                recommandation = "Maintenir le planning préventif actuel";
            } else if (score >= 50) {
                niveau = "Surveiller"; couleur = "orange";
                recommandation = "Augmenter la fréquence des visites préventives";
            } else {
                niveau = "Inspection recommandée"; couleur = "rouge";
                recommandation = "Inspection approfondie recommandée — contacter Philips";
            }

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", eq.getId());
            row.put("nom", eq.getNom());
            row.put("modele", eq.getModele());
            row.put("parc", eq.getParc());
            row.put("age", age);
            row.put("pannes12mois", pannes);
            row.put("score", score);
            row.put("niveau", niveau);
            row.put("couleur", couleur);
            row.put("recommandation", recommandation);
            result.add(row);
        });

        result.sort((a, b) -> (int) a.get("score") - (int) b.get("score"));
        return ResponseEntity.ok(result);
    }
}
