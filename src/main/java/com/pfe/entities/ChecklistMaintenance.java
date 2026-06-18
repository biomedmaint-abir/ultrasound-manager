package com.pfe.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_maintenance")
public class ChecklistMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "intervention_id")
    private Intervention intervention;

    @Column(name = "element")
    private String element;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "statut")
    private String statut;

    @Column(name = "remarque", length = 500)
    private String remarque;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    public ChecklistMaintenance() { this.dateCreation = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Intervention getIntervention() { return intervention; }
    public void setIntervention(Intervention intervention) { this.intervention = intervention; }
    public String getElement() { return element; }
    public void setElement(String element) { this.element = element; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
