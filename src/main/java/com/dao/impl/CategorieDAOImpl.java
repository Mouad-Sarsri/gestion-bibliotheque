package com.dao.impl;

import com.dao.CategorieDAO;
import com.dao.DBConnection;
import com.model.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mouad
 **/
public class CategorieDAOImpl implements CategorieDAO {
    @Override
    public int ajouter(Categorie categorie) throws SQLException {
        String sql = "INSERT INTO categorie (nom) VALUES (?)";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, categorie.getNom());
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public void modifier(Categorie categorie) throws SQLException{
        String sql = "UPDATE categorie SET nom = ? WHERE id = ?";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setString(1, categorie.getNom());
            ps.setInt(2, categorie.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(Integer idCategorie) throws SQLException {
        String sql = "DELETE from categorie WHERE id = ?";
        try (Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setInt(1, idCategorie);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Categorie> listerTous() throws SQLException {
        String sql = "SELECT * FROM categorie ORDER BY nom";
        List<Categorie> categories = new ArrayList<>();
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                categories.add(new Categorie(rs.getInt("id"), rs.getString("nom")));
            }
        }
        return categories;
    }
}
