package com.pfe.services;

import com.pfe.entities.RetourFournisseur;
import com.pfe.repositories.RetourFournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RetourFournisseurService {

    @Autowired
    private RetourFournisseurRepository retourFournisseurRepository;

    public List<RetourFournisseur> findAll() { return retourFournisseurRepository.findAll(); }
    public Optional<RetourFournisseur> findById(Long id) { return retourFournisseurRepository.findById(id); }
    public RetourFournisseur save(RetourFournisseur retour) { return retourFournisseurRepository.save(retour); }
    public void deleteById(Long id) { retourFournisseurRepository.deleteById(id); }
}