package com.service.impl;

import com.dao.CategorieDAO;
import com.dao.EditeurDAO;
import com.dao.LivreDAO;
import com.dao.impl.CategorieDAOImpl;
import com.dao.impl.EditeurDAOImpl;
import com.dao.impl.LivreDAOImpl;
import com.model.Categorie;
import com.model.Editeur;
import com.model.Livre;
import com.service.LivreService;

import java.sql.SQLException;
import java.time.Year;
import java.util.List;

/**
 * @author mouad
 **/
public class LivreServiceImpl implements LivreService {
    private final LivreDAO livreDAO = new LivreDAOImpl();
    private final EditeurDAO editeurDAO = new EditeurDAOImpl();
    private final CategorieDAO categorieDAO = new CategorieDAOImpl();

    @Override
    public int ajouterLivre(Livre livre) throws SQLException {
        valider(livre);
        return livreDAO.ajouter(livre);
    }

    @Override
    public void modifierLivre(Livre livre) throws SQLException {
        if(livre.getId() <= 0)
            throw new IllegalArgumentException("Livre invalide (id manquant)");
        valider(livre);
        livreDAO.modifier(livre);
    }

    @Override
    public void supprimeraLivre(int idLivre) throws SQLException {
        livreDAO.supprimer(idLivre);
    }

    @Override
    public Livre trouverParId(int idLivre) throws SQLException {
        return livreDAO.trouverParId(idLivre);
    }

    // Recherche paginee avec filtres ou criteres
    @Override
    public List<Livre> rechercherPagine(LivreDAO.CritereRecherche critere, int page, int taillePage) throws SQLException {
        return livreDAO.rechercherPagine(critere, page, taillePage);
    }

    @Override
    public int compterResultats(LivreDAO.CritereRecherche critere) throws SQLException {
        return livreDAO.compterResultats(critere);
    }

    @Override
    public int nombreDePages(LivreDAO.CritereRecherche critere, int taillePage) throws SQLException {
        int total = compterResultats(critere);
        return Math.max(1, (int) Math.ceil((double) total/ taillePage));
    }

    @Override
    public List<Editeur> listerEditeurs() throws SQLException {
        return editeurDAO.listerTous();
    }

    @Override
    public List<Categorie> listerCategories() throws SQLException {
        return categorieDAO.listerTous();
    }

    @Override
    public int ajouterEditeur(String nom) throws SQLException {
        if(nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom de l'éditeur est obligatoire");
        return editeurDAO.ajouter(new Editeur(nom.trim()));
    }

    @Override
    public int ajouterCategorie(String nom) throws SQLException {
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom de la catégorie est obligatoire");
        return categorieDAO.ajouter(new Categorie(nom.trim()));
    }


    private void valider(Livre livre){
        if (livre.getTitre() == null || livre.getTitre().isBlank())
            throw new IllegalArgumentException("Le titre du livre est obligatoire");
        int anneeCourante = Year.now().getValue();
        if (livre.getAnneePublication() < 1000 || livre.getAnneePublication() > anneeCourante)
            throw new IllegalArgumentException("L'annee de publication doit etre entre 1000 et " + anneeCourante);
        if(livre.getNbrExemplaire() < 0)
            throw new IllegalArgumentException("Le nombre d'exemplaires ne peut pas etre negatif");
        if(livre.getAuteurs() == null || livre.getAuteurs().isEmpty())
            throw new IllegalArgumentException("Un livre doit avoir au moins un auteur");
    }
}
