package com.pfe.controllers;

import com.pfe.entities.InterventionPiece;
import com.pfe.services.InterventionPieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/intervention-pieces")
@CrossOrigin(origins = "*")
public class InterventionPieceController {

    @Autowired
    private InterventionPieceService interventionPieceService;

    @GetMapping
    public List<InterventionPiece> getAll() { return interventionPieceService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<InterventionPiece> getById(@PathVariable Long id) {
        return interventionPieceService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public InterventionPiece create(@RequestBody InterventionPiece interventionPiece) {
        return interventionPieceService.save(interventionPiece);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<InterventionPiece>> createBulk(@RequestBody List<InterventionPiece> pieces) {
        List<InterventionPiece> saved = pieces.stream()
            .map(interventionPieceService::save)
            .toList();
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterventionPiece> update(@PathVariable Long id, @RequestBody InterventionPiece interventionPiece) {
        return interventionPieceService.findById(id).map(i -> {
            interventionPiece.setId(id);
            return ResponseEntity.ok(interventionPieceService.save(interventionPiece));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interventionPieceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}