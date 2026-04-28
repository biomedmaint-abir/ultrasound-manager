package com.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/documents-philips")
@CrossOrigin(origins = "*")
public class DocumentPhilipsController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT * FROM documents_philips ORDER BY date_import DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("id", rs.getLong("id"));
                row.put("titre", rs.getString("titre"));
                row.put("contenu", rs.getString("contenu"));
                row.put("modeleEquipement", rs.getString("modele_equipement"));
                row.put("codeErreur", rs.getString("code_erreur"));
                row.put("dateImport", rs.getString("date_import"));
                result.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, String> body) {
        String sql = "INSERT INTO documents_philips (titre, contenu, modele_equipement, code_erreur) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, body.get("titre"));
            ps.setString(2, body.get("contenu"));
            ps.setString(3, body.get("modeleEquipement"));
            ps.setString(4, body.get("codeErreur"));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            Map<String, Object> result = new LinkedHashMap<>();
            if (keys.next()) result.put("id", keys.getLong(1));
            result.put("message", "Document sauvegardé avec succès");
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recherche")
    public List<Map<String, Object>> recherche(@RequestParam String query) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT * FROM documents_philips WHERE contenu LIKE ? OR code_erreur LIKE ? OR modele_equipement LIKE ? ORDER BY date_import DESC LIMIT 5";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + query + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("id", rs.getLong("id"));
                row.put("titre", rs.getString("titre"));
                row.put("contenu", rs.getString("contenu"));
                row.put("modeleEquipement", rs.getString("modele_equipement"));
                row.put("codeErreur", rs.getString("code_erreur"));
                result.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String sql = "DELETE FROM documents_philips WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return ResponseEntity.noContent().build();
    }
}