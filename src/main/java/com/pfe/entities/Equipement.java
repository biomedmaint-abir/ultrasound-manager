package com.pfe.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pfe.enums.StatutEquipement;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipements")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroSerie;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "modele_id")
    private Modele modele;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "contrat_id")
    private Contrat contrat;

    private LocalDate dateInstallation;
    private String service;
    private String parc;

    @Enumerated(EnumType.STRING)
    private StatutEquipement statut;

    @JsonIgnore
    @OneToMany(mappedBy = "equipement")
    private List<Intervention> interventions;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Modele getModele() { return modele; }
    public void setModele(Modele modele) { this.modele = modele; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    public Contrat getContrat() { return contrat; }
    public void setContrat(Contrat contrat) { this.contrat = contrat; }
    public LocalDate getDateInstallation() { return dateInstallation; }
    public void setDateInstallation(LocalDate dateInstallation) { this.dateInstallation = dateInstallation; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getParc() { return parc; }
    public void setParc(String parc) { this.parc = parc; }
    public StatutEquipement getStatut() { return statut; }
    public void setStatut(StatutEquipement statut) { this.statut = statut; }
}