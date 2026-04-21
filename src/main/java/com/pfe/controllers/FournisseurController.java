package com.pfe.controllers;

import com.pfe.entities.Fournisseur;
import com.pfe.services.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@CrossOrigin(origins = "*")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    @GetMapping
    public List<Fournisseur> getAll() { return fournisseurService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Fournisseur> getById(@PathVariable Long id) {
        return fournisseurService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Fournisseur create(@RequestBody Fournisseur fournisseur) { return fournisseurService.save(fournisseur); }

    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> update(@PathVariable Long id, @RequestBody Fournisseur fournisseur) {
        return fournisseurService.findById(id).map(f -> { fournisseur.setId(id); return ResponseEntity.ok(fournisseurService.save(fournisseur)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fournisseurService.deleteById(id); return ResponseEntity.noContent().build();
    }
}