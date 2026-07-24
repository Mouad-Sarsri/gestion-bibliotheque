package com.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mouad
 **/
public class Livre {
    private Integer id;
    private String titre;
    private int anneePublication;
    private int nbrExemplaires;

    private List<Auteur> auteurs = new ArrayList<>();
    private List<Editeur> editeurs = new ArrayList<>();
    private List<Categorie> categories = new ArrayList<>();

    public Livre() {
    }

    public Livre(String titre, int anneePublication,  int nbrExemplaires) {
        this.titre = titre;
        this.anneePublication = anneePublication;
        this.nbrExemplaires = nbrExemplaires;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public int getNbrExemplaire() {
        return nbrExemplaires;
    }

    public void setNbrExemplaire(int nbrExemplaire) {
        this.nbrExemplaires = nbrExemplaire;
    }

    public List<Auteur> getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(List<Auteur> auteurs) {
        this.auteurs = auteurs;
    }

    public List<Editeur> getEditeurs() {
        return editeurs;
    }

    public void setEditeurs(List<Editeur> editeurs) {
        this.editeurs = editeurs;
    }

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    // Concatène les noms des auteurs pour affichage dans JTable
    public String getAuteursAffichage(){
        return auteurs.stream()
                .map(Auteur::toString)
                .collect(Collectors.joining(", "));
    }

    // Concatène les noms des editeurs pour affichage dans JTable
    public String getEditeursAffichage(){
        return editeurs.stream()
                .map(Editeur::getNom)
                .collect(Collectors.joining(", "));
    }

    // Concatène les noms des categories pour affichage dans JTable
    public String getCategoriesAffichage(){
        return categories.stream()
                .map(Categorie::getNom)
                .collect(Collectors.joining(", "));
    }

}
