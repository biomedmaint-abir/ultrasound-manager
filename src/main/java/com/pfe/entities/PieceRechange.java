package com.pfe.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "pieces_rechange")
public class PieceRechange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    private Integer quantiteStock;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    private String client;

    @JsonIgnore
    @OneToMany(mappedBy = "piece")
    private List<InterventionPiece> interventions;

    @JsonIgnore
    @OneToMany(mappedBy = "piece")
    private List<RetourFournisseur> retours;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    public Integer getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(Integer quantiteStock) { this.quantiteStock = quantiteStock; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
}