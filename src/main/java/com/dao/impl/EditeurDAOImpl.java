package com.dao.impl;

import com.dao.DBConnection;
import com.dao.EditeurDAO;
import com.model.Editeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mouad
 **/
public class EditeurDAOImpl implements EditeurDAO {
    @Override
    public int ajouter(Editeur editeur) throws SQLException {
        String sql = "INSERT INTO editeur (nom) VALUES (?)";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, editeur.getNom());
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public void modifier(Editeur editeur) throws SQLException {
        String sql = "UPDATE editeur SET nom = ? WHERE id = ?";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setString(1, editeur.getNom());
            ps.setInt(2, editeur.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int idEditeur) throws SQLException {
        String sql = "DELETE FROM editeur WHERE id = ?";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setInt(1, idEditeur);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Editeur> listerTous() throws SQLException {
        String sql = "SELECT * FROM editeur ORDER BY nom";
        List<Editeur> editeurs = new ArrayList<>();
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                editeurs.add(new Editeur(rs.getInt("id"), rs.getString("nom")));
            }
        }
        return editeurs;
    }
}
