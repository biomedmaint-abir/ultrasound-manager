package com.pfe.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "codes_erreur")
public class CodeErreur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "modele_id")
    private Modele modele;

    @Column(nullable = false)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String symptomes;

    @Column(columnDefinition = "TEXT")
    private String causesProbables;

    @Column(columnDefinition = "TEXT")
    private String actionsCorrectives;

    private String piecesConcernees;

    private Float tempsResolutionMoyen;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Modele getModele() { return modele; }
    public void setModele(Modele modele) { this.modele = modele; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getSymptomes() { return symptomes; }
    public void setSymptomes(String symptomes) { this.symptomes = symptomes; }
    public String getCausesProbables() { return causesProbables; }
    public void setCausesProbables(String causesProbables) { this.causesProbables = causesProbables; }
    public String getActionsCorrectives() { return actionsCorrectives; }
    public void setActionsCorrectives(String actionsCorrectives) { this.actionsCorrectives = actionsCorrectives; }
    public String getPiecesConcernees() { return piecesConcernees; }
    public void setPiecesConcernees(String piecesConcernees) { this.piecesConcernees = piecesConcernees; }
    public Float getTempsResolutionMoyen() { return tempsResolutionMoyen; }
    public void setTempsResolutionMoyen(Float tempsResolutionMoyen) { this.tempsResolutionMoyen = tempsResolutionMoyen; }
}