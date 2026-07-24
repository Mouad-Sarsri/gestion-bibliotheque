package com.view;

import com.model.Auteur;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mouad
 **/
public class AuteurTableModel extends AbstractTableModel {
    private static final String[] COLONNES = {"ID", "Nom", "Prenom", "Nationalité", "Date de naissance"};
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private List<Auteur> auteurs = new ArrayList<>();

    public void setAuteurs(List<Auteur> auteurs) {
        this.auteurs = auteurs;
        fireTableDataChanged();
    }

    public Auteur getAuteurALaLigne(int ligne){
        return auteurs.get(ligne);
    }

    @Override
    public int getRowCount() {
        return auteurs.size();
    }

    @Override
    public int getColumnCount() {
        return COLONNES.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLONNES[col];
    }

    @Override
    public Object getValueAt(int ligne, int colonne) {
        Auteur auteur = auteurs.get(ligne);
        return switch (colonne) {
            case 0 -> auteur.getId();
            case 1 -> auteur.getNom();
            case 2 -> auteur.getPrenom();
            case 3 -> auteur.getNationalite();
            case 4 -> auteur.getDateNaissance() != null ? auteur.getDateNaissance().format(FORMAT_DATE) : "";
            default -> null;
        };
    }

}
