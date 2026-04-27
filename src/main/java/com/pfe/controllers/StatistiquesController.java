package com.pfe.controllers;
 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
 
@RestController
@RequestMapping("/api/statistiques")
@CrossOrigin(origins = "*")
public class StatistiquesController {
 
    private final DataSource dataSource;
 
    public StatistiquesController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/statistiques/pieces-par-parc?annee=2025
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/pieces-par-parc")
    public ResponseEntity<List<Map<String, Object>>> getPiecesParParc(
            @RequestParam(name = "annee", required = false) Integer annee) {
 
        int anneeEffective = (annee != null) ? annee : LocalDate.now().getYear();
 
        String sql = "SELECT e.parc, " +
                     "SUM(ip.quantite) AS totalQuantite, " +
                     "COUNT(DISTINCT ip.id) AS nombreLignes, " +
                     "SUM(ip.quantite * ip.prix_unitaire) AS coutTotal " +
                     "FROM intervention_pieces ip " +
                     "JOIN interventions i ON ip.intervention_id = i.id " +
                     "JOIN equipements e ON i.equipement_id = e.id " +
                     "WHERE YEAR(i.date_intervention) = " + anneeEffective + " " +
                     "GROUP BY e.parc " +
                     "ORDER BY coutTotal DESC";
 
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("parc",          rs.getString("parc"));
                row.put("totalQuantite", rs.getInt("totalQuantite"));
                row.put("nombreLignes",  rs.getInt("nombreLignes"));
                row.put("coutTotal",     rs.getDouble("coutTotal"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(result);
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/statistiques/pieces-par-annee
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/pieces-par-annee")
    public ResponseEntity<List<Map<String, Object>>> getPiecesParAnnee() {
 
        String sql = "SELECT YEAR(i.date_intervention) AS annee, " +
                     "SUM(ip.quantite) AS totalQuantite, " +
                     "COUNT(DISTINCT ip.id) AS nombreLignes, " +
                     "SUM(ip.quantite * ip.prix_unitaire) AS coutTotal, " +
                     "COUNT(DISTINCT e.parc) AS nombreParcs " +
                     "FROM intervention_pieces ip " +
                     "JOIN interventions i ON ip.intervention_id = i.id " +
                     "JOIN equipements e ON i.equipement_id = e.id " +
                     "GROUP BY YEAR(i.date_intervention) " +
                     "ORDER BY annee DESC";
 
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("annee",         rs.getInt("annee"));
                row.put("totalQuantite", rs.getInt("totalQuantite"));
                row.put("nombreLignes",  rs.getInt("nombreLignes"));
                row.put("coutTotal",     rs.getDouble("coutTotal"));
                row.put("nombreParcs",   rs.getInt("nombreParcs"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(result);
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/statistiques/pourcentage-pieces-par-parc?annee=2025
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/pourcentage-pieces-par-parc")
    public ResponseEntity<List<Map<String, Object>>> getPourcentagePiecesParParc(
            @RequestParam(name = "annee", required = false) Integer annee) {
 
        int anneeEffective = (annee != null) ? annee : LocalDate.now().getYear();
 
        String sql = "SELECT e.parc, " +
                     "SUM(ip.quantite) AS quantiteTotale, " +
                     "SUM(ip.quantite * ip.prix_unitaire) AS coutTotal, " +
                     "ROUND(SUM(ip.quantite) * 100.0 / " +
                     "(SELECT SUM(ip2.quantite) FROM intervention_pieces ip2 " +
                     "JOIN interventions i2 ON ip2.intervention_id = i2.id " +
                     "WHERE YEAR(i2.date_intervention) = " + anneeEffective + "), 2) AS pourcentage " +
                     "FROM intervention_pieces ip " +
                     "JOIN interventions i ON ip.intervention_id = i.id " +
                     "JOIN equipements e ON i.equipement_id = e.id " +
                     "WHERE YEAR(i.date_intervention) = " + anneeEffective + " " +
                     "GROUP BY e.parc " +
                     "ORDER BY pourcentage DESC";
 
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("parc",           rs.getString("parc"));
                row.put("quantiteTotale", rs.getInt("quantiteTotale"));
                row.put("coutTotal",      rs.getDouble("coutTotal"));
                row.put("pourcentage",    rs.getDouble("pourcentage"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(result);
    }
}
 