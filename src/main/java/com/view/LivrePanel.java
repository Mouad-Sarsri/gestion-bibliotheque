package com.view;

import com.controller.AuteurController;
import com.controller.LivreController;
import com.dao.LivreDAO;
import com.model.Categorie;
import com.model.Editeur;
import com.model.Livre;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * @author mouad
 **/
public class LivrePanel extends JPanel {

    private final LivreController livreController = new LivreController();

    // --- Critères de recherche / filtre ---
    private final JTextField champTitre = new JTextField(12);
    private final JTextField champAuteur = new JTextField(12);
    private final JTextField champAnneeMin = new JTextField(5);
    private final JTextField champAnneeMax = new JTextField(5);
    private final JComboBox<Object> comboCategorie = new JComboBox<>();
    private final JComboBox<Object> comboEditeur = new JComboBox<>();
    private final JComboBox<String> comboTri = new JComboBox<>(new String[]{"Titre", "Année"});
    private final JCheckBox caseTriDescendant = new JCheckBox("Ordre décroissant");

    private final LivreTableModel tableModel = new LivreTableModel();
    private final JTable table = new JTable(tableModel);

    private final JLabel labelPage = new JLabel();
    private final JButton boutonPagePrecedente = new JButton("< Précédent");
    private final JButton boutonPageSuivante = new JButton("Suivant >");
    private final JComboBox<Integer> comboTaillePage = new JComboBox<>(new Integer[]{5, 10, 20, 50});

    private int pageCourante = 1;
    private int nombreTotalDePages = 1;

    public LivrePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirePanneauFiltres(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(construirePanneauBas(), BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(false); // tri géré côté SQL via comboTri

        chargerListesFiltres();
        rechargerDonnees();
    }

    private JPanel construirePanneauFiltres() {
        JPanel conteneur = new JPanel();
        conteneur.setLayout(new BoxLayout(conteneur, BoxLayout.Y_AXIS));

        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligne1.add(new JLabel("Titre :"));
        ligne1.add(champTitre);
        ligne1.add(new JLabel("Auteur :"));
        ligne1.add(champAuteur);
        ligne1.add(new JLabel("Année entre"));
        ligne1.add(champAnneeMin);
        ligne1.add(new JLabel("et"));
        ligne1.add(champAnneeMax);

        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligne2.add(new JLabel("Catégorie :"));
        ligne2.add(comboCategorie);
        ligne2.add(new JLabel("Éditeur :"));
        ligne2.add(comboEditeur);
        ligne2.add(new JLabel("Trier par :"));
        ligne2.add(comboTri);
        ligne2.add(caseTriDescendant);

        JButton boutonRechercher = new JButton("Rechercher / Filtrer");
        JButton boutonReinitialiser = new JButton("Réinitialiser");
        boutonRechercher.addActionListener(e -> { pageCourante = 1; rechargerDonnees(); });
        boutonReinitialiser.addActionListener(e -> reinitialiserFiltres());
        ligne2.add(boutonRechercher);
        ligne2.add(boutonReinitialiser);

        conteneur.add(ligne1);
        conteneur.add(ligne2);
        return conteneur;
    }

    private JPanel construirePanneauBas() {
        JPanel panneau = new JPanel(new BorderLayout());

        JPanel boutonsCrud = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");
        boutonAjouter.addActionListener(e -> ajouterLivre());
        boutonModifier.addActionListener(e -> modifierLivre());
        boutonSupprimer.addActionListener(e -> supprimerLivre());
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

    /** Charge les listes déroulantes de filtre (catégories, éditeurs), avec une option "Toutes/Tous". */
    private void chargerListesFiltres() {
        try {
            comboCategorie.removeAllItems();
            comboCategorie.addItem("Toutes les catégories");
            for (Categorie c : livreController.listerCategories()) {
                comboCategorie.addItem(c);
            }

            comboEditeur.removeAllItems();
            comboEditeur.addItem("Tous les éditeurs");
            for (Editeur e : livreController.listerEditeurs()) {
                comboEditeur.addItem(e);
            }
        } catch (SQLException ex) {
            afficherErreur("Erreur lors du chargement des filtres : " + ex.getMessage());
        }
    }

    private void reinitialiserFiltres() {
        champTitre.setText("");
        champAuteur.setText("");
        champAnneeMin.setText("");
        champAnneeMax.setText("");
        comboCategorie.setSelectedIndex(0);
        comboEditeur.setSelectedIndex(0);
        comboTri.setSelectedIndex(0);
        caseTriDescendant.setSelected(false);
        pageCourante = 1;
        rechargerDonnees();
    }

    /** Construit l'objet critère à partir de l'état actuel des champs de filtre. */
    private LivreDAO.CritereRecherche construireCritere() {
        LivreDAO.CritereRecherche critere = new LivreDAO.CritereRecherche();
        critere.titre = champTitre.getText();
        critere.auteur = champAuteur.getText();

        critere.anneeMin = parseEntierOuNull(champAnneeMin.getText());
        critere.anneeMax = parseEntierOuNull(champAnneeMax.getText());

        Object categorieSelectionnee = comboCategorie.getSelectedItem();
        if (categorieSelectionnee instanceof Categorie) {
            critere.idCategorie = ((Categorie) categorieSelectionnee).getId();
        }
        Object editeurSelectionne = comboEditeur.getSelectedItem();
        if (editeurSelectionne instanceof Editeur) {
            critere.idEditeur = ((Editeur) editeurSelectionne).getId();
        }

        critere.colonneTri = "Année".equals(comboTri.getSelectedItem()) ? "annee_publication" : "titre";
        critere.triDescendant = caseTriDescendant.isSelected();

        return critere;
    }

    private Integer parseEntierOuNull(String texte) {
        if (texte == null || texte.isBlank()) return null;
        try {
            return Integer.parseInt(texte.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /** Recharge la liste des livres selon les filtres et la page courante. */
    public void rechargerDonnees() {
        try {
            LivreDAO.CritereRecherche critere = construireCritere();
            int taillePage = (int) comboTaillePage.getSelectedItem();

            nombreTotalDePages = livreController.nombreDePages(critere, taillePage);
            if (pageCourante > nombreTotalDePages) pageCourante = nombreTotalDePages;
            if (pageCourante < 1) pageCourante = 1;

            List<Livre> livres = livreController.rechercherPagine(critere, pageCourante, taillePage);
            tableModel.setLivres(livres);

            labelPage.setText(" Page " + pageCourante + " / " + nombreTotalDePages + " ");
            boutonPagePrecedente.setEnabled(pageCourante > 1);
            boutonPageSuivante.setEnabled(pageCourante < nombreTotalDePages);
        } catch (SQLException ex) {
            afficherErreur("Erreur lors du chargement des livres : " + ex.getMessage());
        }
    }

    private void ajouterLivre() {
        LivreFormDialog dialog = new LivreFormDialog(SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isValide()) {
            try {
                livreController.ajouter(dialog.getLivre());
                JOptionPane.showMessageDialog(this, "Livre ajouté avec succès.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerListesFiltres();
                rechargerDonnees();
            } catch (IllegalArgumentException | SQLException ex) {
                afficherErreur("Impossible d'ajouter le livre : " + ex.getMessage());
            }
        }
    }

    private void modifierLivre() {
        Livre selection = recupererLivreSelectionne();
        if (selection == null) return;

        try {
            // On recharge le livre complet (avec ses associations) avant modification
            Livre livreComplet = livreController.trouverParId(selection.getId());
            LivreFormDialog dialog = new LivreFormDialog(SwingUtilities.getWindowAncestor(this), livreComplet);
            dialog.setVisible(true);
            if (dialog.isValide()) {
                livreController.modifier(dialog.getLivre());
                JOptionPane.showMessageDialog(this, "Livre modifié avec succès.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerListesFiltres();
                rechargerDonnees();
            }
        } catch (IllegalArgumentException | SQLException ex) {
            afficherErreur("Impossible de modifier le livre : " + ex.getMessage());
        }
    }

    private void supprimerLivre() {
        Livre selection = recupererLivreSelectionne();
        if (selection == null) return;

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer le livre \"" + selection.getTitre() + "\" ?\nCette action est irréversible.",
                "Confirmation de suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                livreController.supprimer(selection.getId());
                JOptionPane.showMessageDialog(this, "Livre supprimé.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                rechargerDonnees();
            } catch (SQLException ex) {
                afficherErreur("Impossible de supprimer le livre : " + ex.getMessage());
            }
        }
    }

    private Livre recupererLivreSelectionne() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre dans la liste.",
                    "Aucune sélection", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return tableModel.getLivreALaLigne(ligne);
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
