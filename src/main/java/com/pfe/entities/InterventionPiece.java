package com.pfe.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "intervention_pieces")
public class InterventionPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "intervention_id")
    private Intervention intervention;

    @ManyToOne
    @JoinColumn(name = "piece_id")
    private PieceRechange piece;

    private Integer quantite;

    @Column(precision = 10, scale = 2)
    private BigDecimal coutUnitaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Intervention getIntervention() { return intervention; }
    public void setIntervention(Intervention intervention) { this.intervention = intervention; }
    public PieceRechange getPiece() { return piece; }
    public void setPiece(PieceRechange piece) { this.piece = piece; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public BigDecimal getCoutUnitaire() { return coutUnitaire; }
    public void setCoutUnitaire(BigDecimal coutUnitaire) { this.coutUnitaire = coutUnitaire; }
}