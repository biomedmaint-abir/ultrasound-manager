package com.pfe.controllers;

import com.pfe.entities.Contrat;
import com.pfe.services.ContratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@CrossOrigin(origins = "*")
public class ContratController {

    @Autowired
    private ContratService contratService;

    @GetMapping
    public List<Contrat> getAll() { return contratService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Contrat> getById(@PathVariable Long id) {
        return contratService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Contrat create(@RequestBody Contrat contrat) { return contratService.save(contrat); }

    @PutMapping("/{id}")
    public ResponseEntity<Contrat> update(@PathVariable Long id, @RequestBody Contrat contrat) {
        return contratService.findById(id).map(c -> { contrat.setId(id); return ResponseEntity.ok(contratService.save(contrat)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contratService.deleteById(id); return ResponseEntity.noContent().build();
    }
}