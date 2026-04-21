package com.pfe.controllers;

import com.pfe.entities.RetourFournisseur;
import com.pfe.services.RetourFournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/retours")
@CrossOrigin(origins = "*")
public class RetourFournisseurController {

    @Autowired
    private RetourFournisseurService retourFournisseurService;

    @GetMapping
    public List<RetourFournisseur> getAll() { return retourFournisseurService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<RetourFournisseur> getById(@PathVariable Long id) {
        return retourFournisseurService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RetourFournisseur create(@RequestBody RetourFournisseur retour) { return retourFournisseurService.save(retour); }

    @PutMapping("/{id}")
    public ResponseEntity<RetourFournisseur> update(@PathVariable Long id, @RequestBody RetourFournisseur retour) {
        return retourFournisseurService.findById(id).map(r -> { retour.setId(id); return ResponseEntity.ok(retourFournisseurService.save(retour)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        retourFournisseurService.deleteById(id); return ResponseEntity.noContent().build();
    }
}