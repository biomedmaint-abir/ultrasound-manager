package com.pfe.controllers;

import com.pfe.entities.Utilisateur;
import com.pfe.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "*")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public List<Utilisateur> getAll() { return utilisateurService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
        return utilisateurService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Utilisateur utilisateur) {
        if (utilisateurService.existsByEmail(utilisateur.getEmail())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        return ResponseEntity.ok(utilisateurService.save(utilisateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        return utilisateurService.findById(id).map(u -> { utilisateur.setId(id); return ResponseEntity.ok(utilisateurService.save(utilisateur)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.deleteById(id); return ResponseEntity.noContent().build();
    }
}