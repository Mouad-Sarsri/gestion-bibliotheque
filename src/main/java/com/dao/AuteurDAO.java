package com.dao;

import com.model.Auteur;

import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public interface AuteurDAO {
    public int ajouter(Auteur auteur) throws SQLException;

    public void modifier(Auteur auteur) throws SQLException;

    public void supprimer(int idAuteur) throws SQLException;

    public Auteur trouverParId(int idAuteur) throws SQLException;

    public List<Auteur> listerTous() throws SQLException;

    public List<Auteur> rechercherPagine(String motCle, int page, int tailleP) throws SQLException;

    public int compterResultats(String motCle) throws SQLException;
}
