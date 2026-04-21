package com.pfe.controllers;

import com.pfe.entities.PieceRechange;
import com.pfe.services.PieceRechangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pieces")
@CrossOrigin(origins = "*")
public class PieceRechangeController {

    @Autowired
    private PieceRechangeService pieceRechangeService;

    @GetMapping
    public List<PieceRechange> getAll() { return pieceRechangeService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<PieceRechange> getById(@PathVariable Long id) {
        return pieceRechangeService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock-faible/{seuil}")
    public List<PieceRechange> getStockFaible(@PathVariable int seuil) {
        return pieceRechangeService.findStockFaible(seuil);
    }

    @PostMapping
    public PieceRechange create(@RequestBody PieceRechange piece) { return pieceRechangeService.save(piece); }

    @PutMapping("/{id}")
    public ResponseEntity<PieceRechange> update(@PathVariable Long id, @RequestBody PieceRechange piece) {
        return pieceRechangeService.findById(id).map(p -> { piece.setId(id); return ResponseEntity.ok(pieceRechangeService.save(piece)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pieceRechangeService.deleteById(id); return ResponseEntity.noContent().build();
    }
}