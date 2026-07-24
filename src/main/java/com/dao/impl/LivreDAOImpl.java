package com.dao.impl;

import com.dao.DBConnection;
import com.dao.LivreDAO;
import com.model.Auteur;
import com.model.Categorie;
import com.model.Editeur;
import com.model.Livre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * @author mouad
 **/
public class LivreDAOImpl implements LivreDAO {

    // Ajoute un livre et ses associations et renvoie son id génère
    @Override
    public int ajouter(Livre livre) throws SQLException {
        String sql = "INSERT INTO livre (titre, annee_publication, nbre_exemplaire) VALUES (?, ?, ?)";
        Connection cnx = DBConnection.getConnection();
        try{
            cnx.setAutoCommit(false);

            int idLivre;
            try(PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, livre.getTitre());
                ps.setDate(2, java.sql.Date.valueOf(livre.getAnneePublication() + "-01-01"));
//                ps.setInt(2,livre.getAnneePublication() );
                ps.setInt(3, livre.getNbrExemplaire());
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    rs.next();
                    idLivre = rs.getInt(1);
                }
            }

            enregistrerAssociations(cnx, idLivre, livre);
            cnx.commit();
            livre.setId(idLivre);
            return idLivre;
        } catch (SQLException e){
            cnx.rollback();
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }


    // Modifie un livre existant et resynchronise ses associations
    @Override
    public void modifier(Livre livre) throws SQLException {
        String sql = "UPDATE livre SET titre = ?, annee_publication = ?, nbre_exemplaire = ? WHERE id = ?";
        Connection cnx = DBConnection.getConnection();
        try{
            cnx.setAutoCommit(false);

            try(PreparedStatement ps = cnx.prepareStatement(sql)){
                ps.setString(1, livre.getTitre());
                ps.setDate(2, java.sql.Date.valueOf(livre.getAnneePublication() + "-01-01"));
//                ps.setInt(2, livre.getAnneePublication());
                ps.setInt(3, livre.getNbrExemplaire());
                ps.setInt(4, livre.getId());
                ps.executeUpdate();
            }

            // On Supprime toutes les anciennes associations
            // puis on réinsère les nouvelles
            supprimerAssociations(cnx, livre.getId());
            enregistrerAssociations(cnx, livre.getId(), livre);

            cnx.commit();
        } catch (SQLException e){
            cnx.rollback();
            throw e;
        } finally {
            cnx.setAutoCommit(true);
        }
    }

    // Supprime un livre, les tables de jointure nettoyées via ON DELETE CASCADE
    @Override
    public void supprimer(int idLivre) throws SQLException {
        String sql = "DELETE FROM livre WHERE id = ?";
        try (Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setInt(1, idLivre);
            ps.executeUpdate();
        }
    }

    @Override
    public Livre trouverParId(int idLivre) throws SQLException {
        String sql = "SELECT * FROM livre WHERE id = ?";
        try(Connection cnx = DBConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setInt(1, idLivre);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    Livre livre = mapperLivreSimple(rs);
                    chargerAssociations(cnx, livre);
                }
            }
        }
        return null;
    }


    // Recherche paginee des livres selon les criteres fournis
    public List<Livre> rechercherPagine(CritereRecherche critere, int page, int tailleP) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT l.* FROM livre l ");
        List<Object> parametres = new ArrayList<>();
        construireRequete(sql, parametres, critere);

        String colonne = "annee_publication".equals(critere.colonneTri) ? "l.annee_publication" : "l.titre ";
        sql.append(" ORDER BY ").append(colonne).append(critere.triDescendant ? "DESC" : "ASC");
        sql.append(" LIMIT ? OFFSET ?");
        parametres.add(tailleP);
        parametres.add((page - 1) * tailleP);

        List<Livre> livres = new ArrayList<>();
        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametres.size(); i++) {
                ps.setObject(i + 1, parametres.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Livre livre = mapperLivreSimple(rs);
                    chargerAssociations(cnx, livre);
                    livres.add(livre);
                }
            }
        }
        return livres;

    }

    // Compte le nombre total de résultats pour les memes critères
    public int compterResultats(CritereRecherche critere) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT l.id) FROM livre l ");
        List<Object> parametres = new ArrayList<>();
        construireRequete(sql, parametres, critere);

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametres.size(); i++) {
                ps.setObject(i + 1, parametres.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // Construire dynamiquement les JOIN et clause WHERE en fonction des critères non null
    private void construireRequete(StringBuilder sql, List<Object> parametres, CritereRecherche critere){
        boolean besoinJoinAuteur = critere.auteur != null && ! critere.auteur.isBlank();

        if(besoinJoinAuteur){
            sql.append("JOIN livre_auteur la ON l.id = la.id_livre ")
                    .append("JOIN auteur ON la.id_auteur = a.id ");
        }
        if (critere.idCategorie != null) {
            sql.append("JOIN livre_categorie lc ON l.id = lc.id_livre ");
        }
        if (critere.idEditeur != null) {
            sql.append("JOIN livre_editeur le ON l.id = le.id_livre ");
        }

        sql.append("WHERE 1=1 ");

        if (critere.titre != null && !critere.titre.isBlank()) {
            sql.append("AND l.titre LIKE ? ");
            parametres.add("%" + critere.titre.trim() + "%");
        }
        if (besoinJoinAuteur) {
            sql.append("AND (a.nom LIKE ? OR a.prenom LIKE ?) ");
            String like = "%" + critere.auteur.trim() + "%";
            parametres.add(like);
            parametres.add(like);
        }
        if (critere.anneeMin != null) {
            sql.append("AND l.annee_publication >= ? ");
            parametres.add(critere.anneeMin);
        }
        if (critere.anneeMax != null) {
            sql.append("AND l.annee_publication <= ? ");
            parametres.add(critere.anneeMax);
        }
        if (critere.idCategorie != null) {
            sql.append("AND lc.id_categorie = ? ");
            parametres.add(critere.idCategorie);
        }
        if (critere.idEditeur != null) {
            sql.append("AND le.id_editeur = ? ");
            parametres.add(critere.idEditeur);
        }


    }
    // GESTION DES ASSOCIATIONS
    private void enregistrerAssociations(Connection cnx, int idLivre, Livre livre) throws SQLException {
        insererAssociations(cnx, "INSERT INTO livre_auteur (id_livre, id_auteur) VALUES (?, ?)", idLivre, livre.getAuteurs(), Auteur::getId);
        insererAssociations(cnx, "INSERT INTO livre_editeur (id_livre, id_editeur) VALUES (?, ?)", idLivre, livre.getEditeurs(), Editeur::getId);
        insererAssociations(cnx, "INSERT INTO livre_categorie (id_livre, id_categorie) VALUES (?, ?)", idLivre, livre.getCategories(), Categorie::getId);
    }

    private <T> void insererAssociations(Connection cnx, String sql, int idLivre,
                                         List<T> elements, ToIntFunction<T> idExtractor) throws SQLException {
        if(elements == null || elements.isEmpty()) return;
        try(PreparedStatement ps = cnx.prepareStatement(sql)){
            for (T element : elements){
                ps.setInt(1, idLivre);
                ps.setInt(2, idExtractor.applyAsInt(element));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void supprimerAssociations(Connection cnx, int idLivre) throws SQLException {
        for(String table : new String[]{"livre_auteur", "livre_editeur", "livre_categorie"}){
            try(PreparedStatement ps = cnx.prepareStatement("DELETE FROM " + table + " WHERE id_livre = ?")){
                ps.setInt(1, idLivre);
                ps.executeUpdate();
            }
        }
    }

    // Charge les auteurs, editeurs et categories associes à un livre deja instancie
    private void chargerAssociations(Connection cnx, Livre livre) throws SQLException {
        livre.setAuteurs(chargerAuteursDuLivre(cnx, livre.getId()));
        livre.setEditeurs(chargerEditeursDuLivre(cnx, livre.getId()));
        livre.setCategories(chargerCategoriesDuLivre(cnx, livre.getId()));
    }

    private List<Auteur> chargerAuteursDuLivre(Connection cnx, int idLivre) throws SQLException {
        String sql = "SELECT a.* FROM auteur a "
                + "JOIN livre_auteur la ON a.id = la.id_auteur "
                + "WHERE la.id_livre = ? ORDER BY a.nom";
        List<Auteur> auteurs = new ArrayList<>();
        try(PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, idLivre);
            try(ResultSet rs =  ps.executeQuery()){
                while (rs.next()){
                    Auteur auteur = new Auteur();
                    auteur.setId(rs.getInt("id"));
                    auteur.setNom(rs.getString("nom"));
                    auteur.setPrenom(rs.getString("prenom"));
                    auteur.setNationalite(rs.getString("nationalite"));
                    Date date = rs.getDate("date_naissance");
                    auteur.setDateNaissance(date != null ? date.toLocalDate() : null );
                    auteurs.add(auteur);
                }
            }
        }
        return auteurs;
    }

    private List<Editeur> chargerEditeursDuLivre(Connection cnx, int idLvre) throws SQLException{
        String sql = "SELECT e.* FROM editeur e "
                + "JOIN livre_editeur le ON e.id = le.id_editeur "
                + "WHERE le.id_livre = ? ORDER BY e.nom";
        List<Editeur> editeurs = new ArrayList<>();
        try(PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setInt(1, idLvre);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    editeurs.add(new Editeur(rs.getInt("id"), rs.getString("nom")));
                }
            }
        }
        return editeurs;
    }

    private List<Categorie> chargerCategoriesDuLivre(Connection cnx, int idLivre) throws SQLException{
        String sql = "SELECT c.* FROM categorie c "
                + "JOIN livre_categorie lc ON c.id = lc.id_categorie "
                + "WHERE lc.id_livre = ? ORDER BY c.nom";
        List<Categorie> categories = new ArrayList<>();
        try(PreparedStatement ps = cnx.prepareStatement(sql)){
            ps.setInt(1, idLivre);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    categories.add(new Categorie(rs.getInt("id"), rs.getString("nom")));
                }
            }
        }
        return categories;
    }

    private Livre mapperLivreSimple(ResultSet rs) throws SQLException{
        Livre livre = new Livre();
        livre.setId(rs.getInt("id"));
        livre.setTitre(rs.getString("titre"));
        Date dateAnnee = rs.getDate("annee_publication");
        livre.setAnneePublication(dateAnnee != null ? dateAnnee.toLocalDate().getYear() : 0);
//        livre.setAnneePublication(rs.getInt("annee_publication"));
        livre.setNbrExemplaire(rs.getInt("nbre_exemplaire"));
        return livre;
    }


}
