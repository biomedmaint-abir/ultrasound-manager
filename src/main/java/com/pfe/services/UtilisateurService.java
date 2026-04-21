package com.pfe.services;

import com.pfe.entities.Utilisateur;
import com.pfe.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Utilisateur> findAll() { return utilisateurRepository.findAll(); }
    public Optional<Utilisateur> findById(Long id) { return utilisateurRepository.findById(id); }
    public Optional<Utilisateur> findByEmail(String email) { return utilisateurRepository.findByEmail(email); }
    public Utilisateur save(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return utilisateurRepository.save(utilisateur);
    }
    public void deleteById(Long id) { utilisateurRepository.deleteById(id); }
    public boolean existsByEmail(String email) { return utilisateurRepository.existsByEmail(email); }
}