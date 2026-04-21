package com.pfe.services;

import com.pfe.entities.PieceRechange;
import com.pfe.repositories.PieceRechangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PieceRechangeService {

    @Autowired
    private PieceRechangeRepository pieceRechangeRepository;

    public List<PieceRechange> findAll() { return pieceRechangeRepository.findAll(); }
    public Optional<PieceRechange> findById(Long id) { return pieceRechangeRepository.findById(id); }
    public PieceRechange save(PieceRechange piece) { return pieceRechangeRepository.save(piece); }
    public void deleteById(Long id) { pieceRechangeRepository.deleteById(id); }
    public List<PieceRechange> findStockFaible(int seuil) {
        return pieceRechangeRepository.findAll().stream()
            .filter(p -> p.getQuantiteStock() != null && p.getQuantiteStock() <= seuil)
            .toList();
    }
}