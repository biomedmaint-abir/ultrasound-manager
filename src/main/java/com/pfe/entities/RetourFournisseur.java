package com.pfe.entities;

import jakarta.persistence.*;
import com.pfe.enums.StatutRetour;
import java.time.LocalDate;

@Entity
@Table(name = "retours_fournisseur")
public class RetourFournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "piece_id")
    private PieceRechange piece;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "intervention_id")
    private Intervention intervention;

    private LocalDate dateRetour;
    private String motif;

    @Enumerated(EnumType.STRING)
    private StatutRetour statut;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PieceRechange getPiece() { return piece; }
    public void setPiece(PieceRechange piece) { this.piece = piece; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    public Intervention getIntervention() { return intervention; }
    public void setIntervention(Intervention intervention) { this.intervention = intervention; }
    public LocalDate getDateRetour() { return dateRetour; }
    public void setDateRetour(LocalDate dateRetour) { this.dateRetour = dateRetour; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public StatutRetour getStatut() { return statut; }
    public void setStatut(StatutRetour statut) { this.statut = statut; }
}