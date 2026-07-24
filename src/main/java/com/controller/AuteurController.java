package com.controller;

import com.model.Auteur;
import com.service.AuteurService;
import com.service.impl.AuteurServiceImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public class AuteurController {
    private final AuteurService auteurService = new AuteurServiceImpl();


    public int ajouter(Auteur auteur) throws SQLException {
        return auteurService.ajouterAuteur(auteur);
    }

    public void modifier(Auteur auteur) throws SQLException {
        auteurService.modifierAuteur(auteur);
    }

    public void supprimer(int idAuteur) throws SQLException {
        auteurService.supprimerAuteur(idAuteur);
    }

    public Auteur trouverParId(int idAuteur) throws SQLException {
        return auteurService.trouverParId(idAuteur);
    }

    public List<Auteur> listerTous() throws SQLException {
        return auteurService.listerTous();
    }

    public List<Auteur> rechercherPagine(String motCle, int page, int taillePage) throws SQLException {
        return auteurService.rechercherPagine(motCle, page, taillePage);
    }

    public int nombreDePages(String motCle, int taillePage) throws SQLException {
        return auteurService.nombreDePages(motCle, taillePage);
    }
}
