package com.view;

import com.controller.AuteurController;
import com.model.Auteur;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public class AuteurPanel extends JPanel {
    private final AuteurController auteurController = new AuteurController();

    private final JTextField champRecherche = new JTextField(20);
    private final AuteurTableModel tableModel = new AuteurTableModel();
    private final JTable table = new JTable(tableModel);

    private final JLabel labelPage = new JLabel();
    private final JButton boutonPagePrecedente = new JButton("< Précédent");
    private final JButton boutonPageSuivante = new JButton("Suivant >");
    private final JComboBox<Integer> comboTaillePage = new JComboBox<>(new Integer[]{5, 10, 20, 50});

    private int pageCourante = 1;
    private int nombreTotalDePages = 1;

    public AuteurPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirePanneauRecherche(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(construirePanneauBas(), BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        rechargerDonnees();
    }

    private JPanel construirePanneauRecherche() {
        JPanel panneau = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panneau.add(new JLabel("Recherche (nom, prénom, nationalité) :"));
        panneau.add(champRecherche);

        JButton boutonRechercher = new JButton("Rechercher");
        JButton boutonReinitialiser = new JButton("Réinitialiser");
        boutonRechercher.addActionListener(e -> { pageCourante = 1; rechargerDonnees(); });
        boutonReinitialiser.addActionListener(e -> {
            champRecherche.setText("");
            pageCourante = 1;
            rechargerDonnees();
        });
        // Recherche déclenchée aussi par la touche Entrée
        champRecherche.addActionListener(e -> { pageCourante = 1; rechargerDonnees(); });

        panneau.add(boutonRechercher);
        panneau.add(boutonReinitialiser);
        return panneau;
    }

    private JPanel construirePanneauBas() {
        JPanel panneau = new JPanel(new BorderLayout());

        JPanel boutonsCrud = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");
        boutonAjouter.addActionListener(e -> ajouterAuteur());
        boutonModifier.addActionListener(e -> modifierAuteur());
        boutonSupprimer.addActionListener(e -> supprimerAuteur());
        boutonsCrud.add(boutonAjouter);
        boutonsCrud.add(boutonModifier);
        boutonsCrud.add(boutonSupprimer);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        comboTaillePage.setSelectedItem(10);
        comboTaillePage.addActionListener(e -> { pageCourante = 1; rechargerDonnees(); });
        boutonPagePrecedente.addActionListener(e -> { pageCourante--; rechargerDonnees(); });
        boutonPageSuivante.addActionListener(e -> { pageCourante++; rechargerDonnees(); });

        pagination.add(new JLabel("Lignes par page :"));
        pagination.add(comboTaillePage);
        pagination.add(boutonPagePrecedente);
        pagination.add(labelPage);
        pagination.add(boutonPageSuivante);

        panneau.add(boutonsCrud, BorderLayout.WEST);
        panneau.add(pagination, BorderLayout.EAST);
        return panneau;
    }

    /** Recharge la liste des auteurs depuis la base, selon la recherche et la page courante. */
    public void rechargerDonnees() {
        try {
            String motCle = champRecherche.getText();
            int taillePage = (int) comboTaillePage.getSelectedItem();

            nombreTotalDePages = auteurController.nombreDePages(motCle, taillePage);
            if (pageCourante > nombreTotalDePages) pageCourante = nombreTotalDePages;
            if (pageCourante < 1) pageCourante = 1;

            List<Auteur> auteurs = auteurController.rechercherPagine(motCle, pageCourante, taillePage);
            tableModel.setAuteurs(auteurs);

            labelPage.setText(" Page " + pageCourante + " / " + nombreTotalDePages + " ");
            boutonPagePrecedente.setEnabled(pageCourante > 1);
            boutonPageSuivante.setEnabled(pageCourante < nombreTotalDePages);
        } catch (SQLException ex) {
            afficherErreur("Erreur lors du chargement des auteurs : " + ex.getMessage());
        }
    }

    private void ajouterAuteur() {
        AuteurFormDialog dialog = new AuteurFormDialog(SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isValide()) {
            try {
                auteurController.ajouter(dialog.getAuteur());
                JOptionPane.showMessageDialog(this, "Auteur ajouté avec succès.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                rechargerDonnees();
            } catch (IllegalArgumentException | SQLException ex) {
                afficherErreur("Impossible d'ajouter l'auteur : " + ex.getMessage());
            }
        }
    }

    private void modifierAuteur() {
        Auteur selection = recupererAuteurSelectionne();
        if (selection == null) return;

        AuteurFormDialog dialog = new AuteurFormDialog(SwingUtilities.getWindowAncestor(this), selection);
        dialog.setVisible(true);
        if (dialog.isValide()) {
            try {
                auteurController.modifier(dialog.getAuteur());
                JOptionPane.showMessageDialog(this, "Auteur modifié avec succès.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                rechargerDonnees();
            } catch (IllegalArgumentException | SQLException ex) {
                afficherErreur("Impossible de modifier l'auteur : " + ex.getMessage());
            }
        }
    }

    private void supprimerAuteur() {
        Auteur selection = recupererAuteurSelectionne();
        if (selection == null) return;

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer l'auteur \"" + selection + "\" ?\n"
                        + "Cette action supprimera aussi ses associations avec les livres.",
                "Confirmation de suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                auteurController.supprimer(selection.getId());
                JOptionPane.showMessageDialog(this, "Auteur supprimé.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                rechargerDonnees();
            } catch (SQLException ex) {
                afficherErreur("Impossible de supprimer l'auteur : " + ex.getMessage());
            }
        }
    }

    private Auteur recupererAuteurSelectionne() {
        int ligneVue = table.getSelectedRow();
        if (ligneVue == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un auteur dans la liste.",
                    "Aucune sélection", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        int ligneModele = table.convertRowIndexToModel(ligneVue);
        return tableModel.getAuteurALaLigne(ligneModele);
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
