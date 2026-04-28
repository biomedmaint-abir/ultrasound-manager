package com.pfe.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pfe.enums.TypeIntervention;
import com.pfe.enums.StatutIntervention;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interventions")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipement_id")
    private Equipement equipement;

    @ManyToOne
    @JoinColumn(name = "technicien_id")
    private Utilisateur technicien;

    private LocalDate dateIntervention;

    @Enumerated(EnumType.STRING)
    private TypeIntervention type;

    @Enumerated(EnumType.STRING)
    private StatutIntervention statut;

    @Column(columnDefinition = "TEXT")
    private String descriptionPanne;

    @Column(columnDefinition = "TEXT")
    private String actionsEffectuees;

    private Float dureeHeures;
    private String nomFse;

    @Column(precision = 10, scale = 2)
    private BigDecimal coutTotal;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL)
    private List<InterventionPiece> pieces;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Equipement getEquipement() { return equipement; }
    public void setEquipement(Equipement equipement) { this.equipement = equipement; }
    public Utilisateur getTechnicien() { return technicien; }
    public void setTechnicien(Utilisateur technicien) { this.technicien = technicien; }
    public LocalDate getDateIntervention() { return dateIntervention; }
    public void setDateIntervention(LocalDate dateIntervention) { this.dateIntervention = dateIntervention; }
    public TypeIntervention getType() { return type; }
    public void setType(TypeIntervention type) { this.type = type; }
    public StatutIntervention getStatut() { return statut; }
    public void setStatut(StatutIntervention statut) { this.statut = statut; }
    public String getDescriptionPanne() { return descriptionPanne; }
    public void setDescriptionPanne(String descriptionPanne) { this.descriptionPanne = descriptionPanne; }
    public String getActionsEffectuees() { return actionsEffectuees; }
    public void setActionsEffectuees(String actionsEffectuees) { this.actionsEffectuees = actionsEffectuees; }
    public Float getDureeHeures() { return dureeHeures; }
    public void setDureeHeures(Float dureeHeures) { this.dureeHeures = dureeHeures; }
    public String getNomFse() { return nomFse; }
    public void setNomFse(String nomFse) { this.nomFse = nomFse; }
    public BigDecimal getCoutTotal() { return coutTotal; }
    public void setCoutTotal(BigDecimal coutTotal) { this.coutTotal = coutTotal; }
}