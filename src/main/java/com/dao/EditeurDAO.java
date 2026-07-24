package com.dao;

import com.model.Editeur;

import java.sql.*;
import java.util.List;

/**
 * @author mouad
 **/
public interface EditeurDAO {
    public int ajouter(Editeur editeur) throws SQLException;

    public void modifier(Editeur editeur) throws SQLException;

    public void supprimer(int idEditeur) throws SQLException;

    public List<Editeur> listerTous() throws SQLException;
}
