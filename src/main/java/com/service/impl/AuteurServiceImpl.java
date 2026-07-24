package com.service.impl;

import com.Main;
import com.dao.AuteurDAO;
import com.dao.impl.AuteurDAOImpl;
import com.model.Auteur;
import com.service.AuteurService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author mouad
 **/
public class AuteurServiceImpl implements AuteurService {

    private final AuteurDAO auteurDAO = new AuteurDAOImpl();

    @Override
    public int ajouterAuteur(Auteur auteur) throws SQLException {
        valider(auteur);
        return auteurDAO.ajouter(auteur);
    }

    @Override
    public void modifierAuteur(Auteur auteur) throws SQLException {
        if (auteur.getId() <= 0)
            throw new IllegalArgumentException("Auteur invalide (id manquant)");
        valider(auteur);
        auteurDAO.modifier(auteur);
    }

    @Override
    public void supprimerAuteur(int idAuteur) throws SQLException {
        auteurDAO.supprimer(idAuteur);
    }

    @Override
    public Auteur trouverParId(int idAuteur) throws SQLException {
        return auteurDAO.trouverParId(idAuteur);
    }

    @Override
    public List<Auteur> listerTous() throws SQLException {
        return auteurDAO.listerTous();
    }

    @Override
    public List<Auteur> rechercherPagine(String motCle, int page, int taillePage) throws SQLException {
        return auteurDAO.rechercherPagine(motCle, page, taillePage);
    }

    @Override
    public int compterResultats(String motCle) throws SQLException {
        return auteurDAO.compterResultats(motCle);
    }

    // Calcule le nombre total de pages pour un mot cle et une taille de page donnes
    @Override
    public int nombreDePages(String motCle, int taillePage) throws SQLException {
        int total = compterResultats(motCle);
        return Math.max(1, (int) Math.ceil((double) total / taillePage));
    }

    private void valider(Auteur auteur) {
        if(auteur.getNom() == null || auteur.getNom().isBlank())
            throw new IllegalArgumentException("Le nom de l'auteur est obligatoire");

        if(auteur.getPrenom() == null || auteur.getPrenom().isBlank())
            throw new IllegalArgumentException("Le prenom de l'auteur est obligatoire");

        if(auteur.getDateNaissance() != null && auteur.getDateNaissance().isAfter(LocalDate.now()))
            throw  new IllegalArgumentException("La date de naissance ne peut pas etre dans le futur");

    }
}
