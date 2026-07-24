package com.dao;

import com.model.Livre;

import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public interface LivreDAO {
    public int ajouter(Livre livre) throws SQLException;

    public void modifier(Livre livre) throws SQLException;

    public void supprimer(int idLivre) throws SQLException;

    public Livre trouverParId(int idLivre) throws SQLException;

    public List<Livre> rechercherPagine(CritereRecherche critere, int page, int tailleP) throws SQLException;

    public int compterResultats(CritereRecherche critere) throws SQLException;



    public static class CritereRecherche{
        public String titre;
        public String auteur;
        public Integer anneeMin;
        public Integer anneeMax;
        public Integer idCategorie;
        public Integer idEditeur;
        public String colonneTri = "titre";
        public boolean triDescendant = false;
    }
}
