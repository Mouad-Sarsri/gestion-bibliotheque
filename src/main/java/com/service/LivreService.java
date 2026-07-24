package com.service;

import com.dao.impl.LivreDAOImpl;
import com.model.Categorie;
import com.model.Editeur;
import com.model.Livre;

import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public interface LivreService {

    public int ajouterLivre(Livre livre) throws SQLException;

    public void modifierLivre(Livre livre) throws SQLException;

    public void supprimeraLivre(int idLivre) throws SQLException;

    public Livre trouverParId(int idLivre) throws SQLException;

    public List<Livre> rechercherPagine(LivreDAOImpl.CritereRecherche critere,  int page, int taillePage) throws SQLException;

    public int compterResultats(LivreDAOImpl.CritereRecherche critere) throws SQLException;

    public int nombreDePages(LivreDAOImpl.CritereRecherche critere, int taillePage) throws SQLException;

    public List<Editeur> listerEditeurs() throws SQLException;

    public List<Categorie> listerCategories() throws SQLException;

    public int ajouterEditeur(String nom) throws SQLException;

    public int ajouterCategorie(String nom) throws SQLException;

}
