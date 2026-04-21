package com.pfe.services;

import com.pfe.entities.Modele;
import com.pfe.repositories.ModeleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ModeleService {

    @Autowired
    private ModeleRepository modeleRepository;

    public List<Modele> findAll() { return modeleRepository.findAll(); }
    public Optional<Modele> findById(Long id) { return modeleRepository.findById(id); }
    public Modele save(Modele modele) { return modeleRepository.save(modele); }
    public void deleteById(Long id) { modeleRepository.deleteById(id); }
}