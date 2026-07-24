package com.dao;

import com.model.Categorie;

import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public interface CategorieDAO {
    public int ajouter(Categorie categorie) throws SQLException;

    public void modifier(Categorie categorie) throws SQLException;

    public void supprimer(Integer idCategorie) throws SQLException;

    public List<Categorie> listerTous() throws SQLException;
}
