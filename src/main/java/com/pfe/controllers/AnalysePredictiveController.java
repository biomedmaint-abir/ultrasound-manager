package com.pfe.controllers;

import com.pfe.repositories.EquipementRepository;
import com.pfe.repositories.InterventionRepository;
import com.pfe.repositories.ContratRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/analyse-predictive")
@CrossOrigin(origins = "*")
public class AnalysePredictiveController {

    @Autowired private EquipementRepository equipementRepository;
    @Autowired private InterventionRepository interventionRepository;
    @Autowired private ContratRepository contratRepository;

    private Map<String, Object> calculerScore(com.pfe.entities.Equipement eq) {
        LocalDate il12mois = LocalDate.now().minusMonths(12);
        LocalDate il6mois = LocalDate.now().minusMonths(6);
        List<com.pfe.entities.Intervention> interventions = interventionRepository.findByEquipementId(eq.getId());

        long pannes12 = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.CORRECTIF
                && i.getDateIntervention() != null && i.getDateIntervention().isAfter(il12mois))
            .count();

        long pannes6 = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.CORRECTIF
                && i.getDateIntervention() != null && i.getDateIntervention().isAfter(il6mois))
            .count();

        double mttr = interventions.stream()
            .filter(i -> i.getDureeHeures() != null).mapToDouble(i -> i.getDureeHeures())
            .average().orElse(0);

        int age = eq.getDateInstallation() != null ?
            LocalDate.now().getYear() - eq.getDateInstallation().getYear() : 0;

        long totalPrev = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.PREVENTIF).count();
        long termPrev = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.PREVENTIF
                && i.getStatut() == com.pfe.enums.StatutIntervention.TERMINEE).count();

        int score = 100;
        score -= (int) pannes12 * 15;
        if (mttr > 4) score -= 10;
        if (age > 8) score -= 20;
        else if (age > 5) score -= 10;
        if (totalPrev > 0 && termPrev == totalPrev) score += 10;
        if (pannes6 == 0) score += 10;
        score = Math.max(0, Math.min(100, score));

        int probabilite = 100 - score;
        String niveau, couleur;
        if (probabilite < 50) { niveau = "Normal"; couleur = "vert"; }
        else if (probabilite < 80) { niveau = "Modéré"; couleur = "orange"; }
        else { niveau = "Critique"; couleur = "rouge"; }

        boolean sousContrat = contratRepository.findAll().stream()
            .anyMatch(c -> c.getParc() != null && eq.getParc() != null
                && c.getParc().equals(eq.getParc()) && "ACTIF".equals(c.getStatut() != null ? c.getStatut().toString() : ""));

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("id", eq.getId()); r.put("nom", eq.getNom()); r.put("modele", eq.getModele());
        r.put("parc", eq.getParc()); r.put("service", eq.getService());
        r.put("statut", eq.getStatut()); r.put("age", age);
        r.put("pannes12mois", pannes12); r.put("score", score);
        r.put("probabilite", probabilite); r.put("niveau", niveau); r.put("couleur", couleur);
        r.put("sousContrat", sousContrat); r.put("mttr", Math.round(mttr * 10.0) / 10.0);
        r.put("dateInstallation", eq.getDateInstallation());
        r.put("numInventaire", eq.getNumInventaire());
        return r;
    }

    @GetMapping("/scores")
    public ResponseEntity<List<Map<String, Object>>> getAllScores(@RequestParam(defaultValue = "3") int periode) {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate depuis = LocalDate.now().minusMonths(periode);
        equipementRepository.findAll().forEach(eq -> {
            Map<String, Object> score = calculerScorePeriode(eq, depuis);
            result.add(score);
        });
        result.sort((a, b) -> (int) b.get("probabilite") - (int) a.get("probabilite"));
        return ResponseEntity.ok(result);
    }

    private Map<String, Object> calculerScorePeriode(com.pfe.entities.Equipement eq, LocalDate depuis) {
        LocalDate il12mois = LocalDate.now().minusMonths(12);
        LocalDate il6mois = LocalDate.now().minusMonths(6);
        List<com.pfe.entities.Intervention> interventions = interventionRepository.findByEquipementId(eq.getId());

        long pannes12 = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.CORRECTIF
                && i.getDateIntervention() != null && i.getDateIntervention().isAfter(il12mois))
            .count();

        long pannes6 = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.CORRECTIF
                && i.getDateIntervention() != null && i.getDateIntervention().isAfter(il6mois))
            .count();

        // Pannes par mois pour sparkline
        java.util.Map<String, Long> pannesParMois = new java.util.LinkedHashMap<>();
        for (int m = (int)(java.time.temporal.ChronoUnit.MONTHS.between(depuis, LocalDate.now())); m >= 0; m--) {
            LocalDate moisDate = LocalDate.now().minusMonths(m);
            String moisKey = moisDate.getYear() + "-" + String.format("%02d", moisDate.getMonthValue());
            final int mFinal = m;
            long cnt = interventions.stream()
                .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.CORRECTIF
                    && i.getDateIntervention() != null
                    && i.getDateIntervention().isAfter(LocalDate.now().minusMonths(mFinal + 1))
                    && !i.getDateIntervention().isAfter(LocalDate.now().minusMonths(mFinal)))
                .count();
            pannesParMois.put(moisKey, cnt);
        }

        double mttr = interventions.stream()
            .filter(i -> i.getDureeHeures() != null).mapToDouble(i -> i.getDureeHeures())
            .average().orElse(0);
        int age = eq.getDateInstallation() != null ?
            LocalDate.now().getYear() - eq.getDateInstallation().getYear() : 0;
        long totalPrev = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.PREVENTIF).count();
        long termPrev = interventions.stream()
            .filter(i -> i.getType() == com.pfe.enums.TypeIntervention.PREVENTIF
                && i.getStatut() == com.pfe.enums.StatutIntervention.TERMINEE).count();

        int score = 100;
        score -= (int) pannes12 * 15;
        if (mttr > 4) score -= 10;
        if (age > 8) score -= 20;
        else if (age > 5) score -= 10;
        if (totalPrev > 0 && termPrev == totalPrev) score += 10;
        if (pannes6 == 0) score += 10;
        score = Math.max(0, Math.min(100, score));
        int probabilite = 100 - score;

        String niveau, couleur;
        if (probabilite < 50) { niveau = "Normal"; couleur = "vert"; }
        else if (probabilite < 80) { niveau = "Avertissement"; couleur = "orange"; }
        else { niveau = "Critique"; couleur = "rouge"; }

        boolean sousContrat = contratRepository.findAll().stream()
            .anyMatch(c -> c.getParc() != null && eq.getParc() != null
                && c.getParc().equals(eq.getParc()) && "ACTIF".equals(c.getStatut() != null ? c.getStatut().toString() : ""));

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("id", eq.getId()); r.put("nom", eq.getNom()); r.put("modele", eq.getModele());
        r.put("parc", eq.getParc()); r.put("service", eq.getService());
        r.put("statut", eq.getStatut() != null ? eq.getStatut().toString() : "");
        r.put("age", age); r.put("pannes12mois", pannes12);
        r.put("score", score); r.put("probabilite", probabilite);
        r.put("niveau", niveau); r.put("couleur", couleur);
        r.put("sousContrat", sousContrat); r.put("pannesParMois", pannesParMois);
        return r;
    }

    @GetMapping("/fse/{fseId}")
    public ResponseEntity<List<Map<String, Object>>> getByFse(@PathVariable Long fseId) {
        List<com.pfe.entities.Intervention> myInterv = interventionRepository.findByTechnicienId(fseId);
        Set<Long> equipIds = new HashSet<>();
        myInterv.forEach(i -> { if (i.getEquipement() != null) equipIds.add(i.getEquipement().getId()); });
        List<Map<String, Object>> result = new ArrayList<>();
        equipIds.forEach(id -> equipementRepository.findById(id).ifPresent(eq -> result.add(calculerScore(eq))));
        result.sort((a, b) -> (int) b.get("probabilite") - (int) a.get("probabilite"));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/equipement/{id}")
    public ResponseEntity<Map<String, Object>> getByEquipement(@PathVariable Long id) {
        return equipementRepository.findById(id)
            .map(eq -> ResponseEntity.ok(calculerScore(eq)))
            .orElse(ResponseEntity.notFound().build());
    }
}
