package com.pfe.controllers;

import com.pfe.entities.Modele;
import com.pfe.services.ModeleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/modeles")
@CrossOrigin(origins = "*")
public class ModeleController {

    @Autowired
    private ModeleService modeleService;

    @GetMapping
    public List<Modele> getAll() { return modeleService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Modele> getById(@PathVariable Long id) {
        return modeleService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Modele create(@RequestBody Modele modele) { return modeleService.save(modele); }

    @PutMapping("/{id}")
    public ResponseEntity<Modele> update(@PathVariable Long id, @RequestBody Modele modele) {
        return modeleService.findById(id).map(m -> { modele.setId(id); return ResponseEntity.ok(modeleService.save(modele)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        modeleService.deleteById(id); return ResponseEntity.noContent().build();
    }
}