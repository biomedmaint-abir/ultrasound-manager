package com.pfe.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "fournisseurs")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String contact;
    private String email;
    private String telephone;

    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur")
    private List<Equipement> equipements;

    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur")
    private List<PieceRechange> pieces;

    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur")
    private List<Contrat> contrats;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}