package com.pfe.services;

import com.pfe.entities.InterventionPiece;
import com.pfe.repositories.InterventionPieceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InterventionPieceService {

    @Autowired
    private InterventionPieceRepository interventionPieceRepository;

    public List<InterventionPiece> findAll() { return interventionPieceRepository.findAll(); }
    public Optional<InterventionPiece> findById(Long id) { return interventionPieceRepository.findById(id); }
    public InterventionPiece save(InterventionPiece interventionPiece) { return interventionPieceRepository.save(interventionPiece); }
    public void deleteById(Long id) { interventionPieceRepository.deleteById(id); }
}