package com.dao.impl;

import com.dao.AuteurDAO;
import com.dao.DBConnection;
import com.model.Auteur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mouad
 **/
public class AuteurDAOImpl implements AuteurDAO {
    @Override
    public int ajouter(Auteur auteur) throws SQLException {
        String sql = "INSERT INTO auteur (nom, prenom, nationalite, date_naissance) VALUES(?, ?, ?, ?)";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, auteur.getNom());
            ps.setString(2, auteur.getPrenom());
            ps.setString(3, auteur.getNationalite());
            ps.setDate(4, auteur.getDateNaissance() != null ? Date.valueOf(auteur.getDateNaissance()) : null);
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public void modifier(Auteur auteur) throws SQLException {
        String sql = "UPDATE auteur SEt nom =?, prenom = ?, nationalite = ?, date_naissance = ? WHERE id = ?";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)){

            ps.setString(1, auteur.getNom());
            ps.setString(2, auteur.getPrenom());
            ps.setString(3, auteur.getNationalite());
            ps.setDate(4, auteur.getDateNaissance() != null ? Date.valueOf(auteur.getDateNaissance()) : null);
            ps.setInt(5, auteur.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int idAuteur) throws SQLException {
        String sql = "DELETE FROM auteur WHERE id = ?";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, idAuteur);
            ps.executeUpdate();
        }
    }

    @Override
    public Auteur trouverParId(int idAuteur) throws SQLException {
        String sql = "SELECT * FROM auteur WHERE id = ?";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps= cnx.prepareStatement(sql)){
            ps.setInt(1, idAuteur);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapper(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Auteur> listerTous() throws SQLException {
        String sql = "SELECT * FROM auteur ORDER BY nom, prenom";
        List<Auteur> auteurs = new ArrayList<>();
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                auteurs.add(mapper(rs));
            }
        }
        return auteurs;
    }

    @Override
    public List<Auteur> rechercherPagine(String motCle, int page, int tailleP) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM auteur WHERE 1=1 ");
        List<Object> parametres = new ArrayList<>();

        if(motCle != null && !motCle.isBlank()){
            sql.append("AND (nom LIKE ? OR prenom LIKE ? oR nationalite LIKE ?) ");
            String like = "%" + motCle.trim() + "%";
            parametres.add(like);
            parametres.add(like);
            parametres.add(like);
        }

        sql.append("ORDER BY nom, prenom LIMIT ? OFFSET ?");
        parametres.add(tailleP);
        parametres.add((page - 1) * tailleP);

        List<Auteur> auteurs = new ArrayList<>();
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql.toString())){
            for(int i = 0; i < parametres.size(); i++){
                ps.setObject(i + 1, parametres.get(i));
            }
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    auteurs.add(mapper(rs));
                }
            }
        }
        return auteurs;
    }

    @Override
    public int compterResultats(String motCle) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM auteur WHERE 1=1 ");
        List<Object> parametres = new ArrayList<>();

        if(motCle != null && !motCle.isBlank()){
            sql.append("AND (nom LIKE ? OR prenom LIKE ? OR nationalite LIKE ?) ");
            String like = "%" + motCle.trim() + "%";
            parametres.add(like);
            parametres.add(like);
            parametres.add(like);
        }

        try (Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql.toString())){
            for(int i = 0; i < parametres.size(); i++){
                ps.setObject(i + 1, parametres.get(i));
            }
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private Auteur mapper(ResultSet rs) throws SQLException {
        Auteur auteur = new Auteur();
        auteur.setId(rs.getInt("id"));
        auteur.setNom(rs.getString("nom"));
        auteur.setPrenom(rs.getString("prenom"));
        auteur.setNationalite(rs.getString("nationalite"));
        Date date = rs.getDate("date_naissance");
        auteur.setDateNaissance(date != null ? date.toLocalDate() : null);
        return auteur;
    }
}
