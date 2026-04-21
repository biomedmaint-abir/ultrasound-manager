package com.pfe.services;

import com.pfe.entities.Intervention;
import com.pfe.repositories.InterventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InterventionService {

    @Autowired
    private InterventionRepository interventionRepository;

    public List<Intervention> findAll() { return interventionRepository.findAll(); }
    public Optional<Intervention> findById(Long id) { return interventionRepository.findById(id); }
    public List<Intervention> findByEquipementId(Long equipementId) { return interventionRepository.findByEquipementId(equipementId); }
    public List<Intervention> findByTechnicienId(Long technicienId) { return interventionRepository.findByTechnicienId(technicienId); }
    public Intervention save(Intervention intervention) { return interventionRepository.save(intervention); }
    public void deleteById(Long id) { interventionRepository.deleteById(id); }
    public Double calculerMTTR() {
        return interventionRepository.findAll().stream()
            .filter(i -> i.getDureeHeures() != null)
            .mapToDouble(Intervention::getDureeHeures)
            .average().orElse(0.0);
    }
}