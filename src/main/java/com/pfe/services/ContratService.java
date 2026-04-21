package com.pfe.services;

import com.pfe.entities.Contrat;
import com.pfe.repositories.ContratRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContratService {

    @Autowired
    private ContratRepository contratRepository;

    public List<Contrat> findAll() { return contratRepository.findAll(); }
    public Optional<Contrat> findById(Long id) { return contratRepository.findById(id); }
    public Contrat save(Contrat contrat) { return contratRepository.save(contrat); }
    public void deleteById(Long id) { contratRepository.deleteById(id); }
}