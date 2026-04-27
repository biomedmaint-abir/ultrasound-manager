package com.pfe.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pfe.enums.TypeContrat;
import com.pfe.enums.StatutContrat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contrats")
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String reference;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Column(precision = 10, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    private TypeContrat type;

    @Enumerated(EnumType.STRING)
    private StatutContrat statut;

    private String parc;

    @JsonIgnore
    @OneToMany(mappedBy = "contrat")
    private List<Equipement> equipements;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public TypeContrat getType() { return type; }
    public void setType(TypeContrat type) { this.type = type; }
    public StatutContrat getStatut() { return statut; }
    public void setStatut(StatutContrat statut) { this.statut = statut; }
    public String getParc() { return parc; }
    public void setParc(String parc) { this.parc = parc; }
}