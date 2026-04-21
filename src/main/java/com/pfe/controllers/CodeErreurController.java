package com.pfe.controllers;

import com.pfe.entities.CodeErreur;
import com.pfe.services.CodeErreurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/codes-erreur")
@CrossOrigin(origins = "*")
public class CodeErreurController {

    @Autowired
    private CodeErreurService codeErreurService;

    @GetMapping
    public List<CodeErreur> getAll() { return codeErreurService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<CodeErreur> getById(@PathVariable Long id) {
        return codeErreurService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/modele/{modeleId}")
    public List<CodeErreur> getByModele(@PathVariable Long modeleId) { return codeErreurService.findByModeleId(modeleId); }

    @GetMapping("/recherche/{code}")
    public List<CodeErreur> rechercher(@PathVariable String code) { return codeErreurService.rechercherParCode(code); }

    @PostMapping
    public CodeErreur create(@RequestBody CodeErreur codeErreur) { return codeErreurService.save(codeErreur); }

    @PutMapping("/{id}")
    public ResponseEntity<CodeErreur> update(@PathVariable Long id, @RequestBody CodeErreur codeErreur) {
        return codeErreurService.findById(id).map(c -> { codeErreur.setId(id); return ResponseEntity.ok(codeErreurService.save(codeErreur)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        codeErreurService.deleteById(id); return ResponseEntity.noContent().build();
    }
}