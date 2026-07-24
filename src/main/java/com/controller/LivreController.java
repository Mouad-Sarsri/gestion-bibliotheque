package com.controller;

import com.dao.LivreDAO;
import com.model.Categorie;
import com.model.Editeur;
import com.model.Livre;
import com.service.LivreService;
import com.service.impl.LivreServiceImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public class LivreController {
    private final LivreService livreService = new LivreServiceImpl();

    public int ajouter(Livre livre) throws SQLException {
        return livreService.ajouterLivre(livre);
    }

    public void modifier(Livre livre) throws SQLException {
        livreService.modifierLivre(livre);
    }

    public void supprimer(int idLivre) throws SQLException {
        livreService.supprimeraLivre(idLivre);
    }

    public Livre trouverParId(int idLivre) throws SQLException {
        return livreService.trouverParId(idLivre);
    }

    public List<Livre> rechercherPagine(LivreDAO.CritereRecherche critere, int page, int taillePage) throws SQLException {
        return livreService.rechercherPagine(critere, page, taillePage);
    }

    public int nombreDePages(LivreDAO.CritereRecherche  critere, int taillePage) throws SQLException {
        return livreService.nombreDePages(critere, taillePage);
    }

    public List<Editeur> listerEditeurs() throws SQLException {
        return livreService.listerEditeurs();
    }

    public List<Categorie> listerCategories() throws SQLException {
        return livreService.listerCategories();
    }

    public int ajouterEditeur(String nom) throws SQLException {
        return livreService.ajouterEditeur(nom);
    }

    public int ajouterCategorie(String nom) throws SQLException {
        return livreService.ajouterCategorie(nom);
    }

}
