package com.pfe.services;

import com.pfe.entities.Fournisseur;
import com.pfe.repositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    public List<Fournisseur> findAll() { return fournisseurRepository.findAll(); }
    public Optional<Fournisseur> findById(Long id) { return fournisseurRepository.findById(id); }
    public Fournisseur save(Fournisseur fournisseur) { return fournisseurRepository.save(fournisseur); }
    public void deleteById(Long id) { fournisseurRepository.deleteById(id); }
}