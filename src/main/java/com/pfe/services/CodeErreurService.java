package com.pfe.services;

import com.pfe.entities.CodeErreur;
import com.pfe.repositories.CodeErreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CodeErreurService {

    @Autowired
    private CodeErreurRepository codeErreurRepository;

    public List<CodeErreur> findAll() { return codeErreurRepository.findAll(); }
    public Optional<CodeErreur> findById(Long id) { return codeErreurRepository.findById(id); }
    public List<CodeErreur> findByModeleId(Long modeleId) { return codeErreurRepository.findByModeleId(modeleId); }
    public List<CodeErreur> rechercherParCode(String code) { return codeErreurRepository.findByCodeContaining(code); }
    public CodeErreur save(CodeErreur codeErreur) { return codeErreurRepository.save(codeErreur); }
    public void deleteById(Long id) { codeErreurRepository.deleteById(id); }
}