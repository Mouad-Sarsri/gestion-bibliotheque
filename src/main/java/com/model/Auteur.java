package com.model;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author mouad
 **/
public class Auteur {
    private Integer id;
    private String nom;
    private String prenom;
    private String nationalite;
    private LocalDate dateNaissance;

    public Auteur() {
    }

    public Auteur(String nom, String prenom, String nationalite, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.nationalite = nationalite;
        this.dateNaissance = dateNaissance;
    }

    public Auteur(Integer id, String nom, String prenom, String nationalite, LocalDate dateNaissance) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.nationalite = nationalite;
        this.dateNaissance = dateNaissance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String toString(){
        return prenom + " " + nom;
    }
}
