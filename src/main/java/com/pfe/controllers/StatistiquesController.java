package com.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/statistiques")
@CrossOrigin(origins = "*")
public class StatistiquesController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/pieces-par-parc")
    public List<Map<String, Object>> getPiecesParParc(
            @RequestParam(defaultValue = "2024") int annee) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
            SELECT 
                e.parc,
                p.nom as piece,
                SUM(ip.quantite) as quantite_totale,
                SUM(ip.quantite * ip.cout_unitaire) as cout_total,
                ROUND(SUM(ip.quantite) * 100.0 / (
                    SELECT SUM(ip2.quantite) 
                    FROM intervention_pieces ip2
                    JOIN interventions i2 ON ip2.intervention_id = i2.id
                    WHERE YEAR(i2.date_intervention) = ?
                ), 2) as pourcentage
            FROM intervention_pieces ip
            JOIN interventions i ON ip.intervention_id = i.id
            JOIN equipements e ON i.equipement_id = e.id
            JOIN pieces_rechange p ON ip.piece_id = p.id
            WHERE YEAR(i.date_intervention) = ?
            GROUP BY e.parc, p.nom
            ORDER BY e.parc, quantite_totale DESC
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, annee);
            ps.setInt(2, annee);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("parc", rs.getString("parc"));
                row.put("piece", rs.getString("piece"));
                row.put("quantiteTotale", rs.getInt("quantite_totale"));
                row.put("coutTotal", rs.getDouble("cout_total"));
                row.put("pourcentage", rs.getDouble("pourcentage"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/pieces-par-annee")
    public List<Map<String, Object>> getPiecesParAnnee() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
            SELECT 
                YEAR(i.date_intervention) as annee,
                e.parc,
                SUM(ip.quantite) as quantite_totale,
                SUM(ip.quantite * ip.cout_unitaire) as cout_total
            FROM intervention_pieces ip
            JOIN interventions i ON ip.intervention_id = i.id
            JOIN equipements e ON i.equipement_id = e.id
            GROUP BY YEAR(i.date_intervention), e.parc
            ORDER BY annee DESC, cout_total DESC
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("annee", rs.getInt("annee"));
                row.put("parc", rs.getString("parc"));
                row.put("quantiteTotale", rs.getInt("quantite_totale"));
                row.put("coutTotal", rs.getDouble("cout_total"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}