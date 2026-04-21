package com.pfe.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "modeles")
public class Modele {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String marque;
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "modele")
    private List<Equipement> equipements;

    @JsonIgnore
    @OneToMany(mappedBy = "modele")
    private List<CodeErreur> codesErreur;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}