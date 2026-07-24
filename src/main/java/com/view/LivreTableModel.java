package com.view;

import com.model.Livre;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mouad
 **/
public class LivreTableModel extends AbstractTableModel {

    private static final String[] COLONNES =
            {"ID", "Titre", "Année", "Auteur(s)", "Éditeur(s)", "Catégorie(s)", "Exemplaires"};

    private List<Livre> livres = new ArrayList<>();

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
        fireTableDataChanged();
    }

    public Livre getLivreALaLigne(int ligne) {
        return livres.get(ligne);
    }

    @Override
    public int getRowCount() {
        return livres.size();
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
        Livre livre = livres.get(ligne);
        return switch (colonne) {
            case 0 -> livre.getId();
            case 1 -> livre.getTitre();
            case 2 -> livre.getAnneePublication();
            case 3 -> livre.getAuteursAffichage();
            case 4 -> livre.getEditeursAffichage();
            case 5 -> livre.getCategoriesAffichage();
            case 6 -> livre.getNbrExemplaire();
            default -> null;
        };
    }

}
