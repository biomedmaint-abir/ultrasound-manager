package com.pfe.controllers;

import com.pfe.entities.Equipement;
import com.pfe.services.EquipementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@CrossOrigin(origins = "*")
public class EquipementController {

    @Autowired
    private EquipementService equipementService;

    @GetMapping
    public List<Equipement> getAll() { return equipementService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Equipement> getById(@PathVariable Long id) {
        return equipementService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Equipement create(@RequestBody Equipement equipement) { return equipementService.save(equipement); }

    @PutMapping("/{id}")
    public ResponseEntity<Equipement> update(@PathVariable Long id, @RequestBody Equipement equipement) {
        return equipementService.findById(id).map(e -> { equipement.setId(id); return ResponseEntity.ok(equipementService.save(equipement)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipementService.deleteById(id); return ResponseEntity.noContent().build();
    }
}