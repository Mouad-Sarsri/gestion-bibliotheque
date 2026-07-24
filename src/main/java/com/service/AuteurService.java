package com.service;

import com.model.Auteur;

import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public interface AuteurService {
    public int ajouterAuteur(Auteur auteur) throws SQLException;

    public void modifierAuteur(Auteur auteur) throws SQLException;

    public void supprimerAuteur(int idAuteur) throws SQLException;

    public Auteur trouverParId(int idAuteur) throws SQLException;

    public List<Auteur> listerTous() throws SQLException;

    public List<Auteur> rechercherPagine(String motCle, int page, int taillePage) throws SQLException;

    public int compterResultats(String motCle) throws SQLException;

    public int nombreDePages(String motCle, int taillePage) throws SQLException;

}
