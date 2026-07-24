package com.view;

import com.controller.AuteurController;
import com.controller.LivreController;
import com.model.Auteur;
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
public class LivreFormDialog extends JDialog {
    private LivreController livreController = new LivreController();
    private AuteurController auteurController = new AuteurController();

    private final JTextField champTitre = new JTextField(25);
    private final JTextField champAnnee = new JTextField(25);
    private final JTextField champExemplaires = new JTextField(25);

    private final DefaultListModel<Auteur> modeleListeAuteurs = new DefaultListModel<>();
    private final JList<Auteur> listeAuteurs = new JList<>(modeleListeAuteurs);

    private final DefaultListModel<Editeur> modeleListeEditeurs = new DefaultListModel<>();
    private final JList<Editeur> listeEditeurs = new JList<>(modeleListeEditeurs);

    private final DefaultListModel<Categorie> modeleListeCategories = new DefaultListModel<>();
    private final JList<Categorie> listeCategories = new JList<>(modeleListeCategories);

    private boolean valide = false;
    private Livre livre;

    public LivreFormDialog(Window parent, Livre livreExistant) {
        super(parent, livreExistant == null ? "Ajouter un livre" : "Modifier un livre",
                ModalityType.APPLICATION_MODAL);
        this.livre = livreExistant;
        construireInterface();
        chargerListesDeSelection();
        if (livreExistant != null) {
            preRemplirFormulaire(livreExistant);
        }

        setSize(600, 550);
        setLocationRelativeTo(parent);
    }



    private void construireInterface() {
        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int ligne = 0;
        ajouterLigne(formulaire, gbc, ligne++, "Titre *", champTitre);
        ajouterLigne(formulaire, gbc, ligne++, "Année de publication *", champAnnee);
        ajouterLigne(formulaire, gbc, ligne++, "Nombre d'exemplaires *", champExemplaires);

        gbc.gridx = 0; gbc.gridy = ligne; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formulaire.add(new JLabel("Auteur(s) *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1;
        formulaire.add(construireBlocSelection(listeAuteurs, "Nouvel auteur...", this::creerNouvelAuteur), gbc);
        ligne++;

        gbc.gridx = 0; gbc.gridy = ligne; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.weighty = 0;
        formulaire.add(new JLabel("Éditeur(s)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1;
        formulaire.add(construireBlocSelection(listeEditeurs, "Nouvel éditeur...", this::creerNouvelEditeur), gbc);
        ligne++;

        gbc.gridx = 0; gbc.gridy = ligne; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.weighty = 0;
        formulaire.add(new JLabel("Catégorie(s)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1;
        formulaire.add(construireBlocSelection(listeCategories, "Nouvelle catégorie...", this::creerNouvelleCategorie), gbc);

        JButton boutonValider = new JButton("Enregistrer");
        JButton boutonAnnuler = new JButton("Annuler");
        boutonValider.addActionListener(e -> validerFormulaire());
        boutonAnnuler.addActionListener(e -> dispose());

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutons.add(boutonAnnuler);
        boutons.add(boutonValider);

        setLayout(new BorderLayout());
        add(formulaire, BorderLayout.CENTER);
        add(boutons, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(boutonValider);
    }

    /** Construit un panneau JScrollPane(JList) + bouton "Nouveau..." pour une liste de sélection multiple. */
    private <T> JPanel construireBlocSelection(JList<T> liste, String texteBouton, Runnable actionNouveau) {
        liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        liste.setVisibleRowCount(4);

        JPanel panneau = new JPanel(new BorderLayout(5, 5));
        panneau.add(new JScrollPane(liste), BorderLayout.CENTER);

        JButton boutonNouveau = new JButton(texteBouton);
        boutonNouveau.addActionListener(e -> actionNouveau.run());
        JPanel panneauBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panneauBouton.add(boutonNouveau);
        panneau.add(panneauBouton, BorderLayout.SOUTH);

        return panneau;
    }

    private void ajouterLigne(JPanel panel, GridBagConstraints gbc, int ligne, String libelle, JComponent champ) {
        gbc.gridx = 0; gbc.gridy = ligne; gbc.weightx = 0; gbc.weighty = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(libelle), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(champ, gbc);
    }

    private void chargerListesDeSelection() {
        try {
            modeleListeAuteurs.clear();
            for (Auteur a : auteurController.listerTous()) {
                modeleListeAuteurs.addElement(a);
            }
            modeleListeEditeurs.clear();
            for (Editeur e : livreController.listerEditeurs()) {
                modeleListeEditeurs.addElement(e);
            }
            modeleListeCategories.clear();
            for (Categorie c : livreController.listerCategories()) {
                modeleListeCategories.addElement(c);
            }
        } catch (SQLException ex) {
            afficherErreur("Erreur lors du chargement des listes : " + ex.getMessage());
        }
    }

    private void preRemplirFormulaire(Livre livre) {
        champTitre.setText(livre.getTitre());
        champAnnee.setText(String.valueOf(livre.getAnneePublication()));
        champExemplaires.setText(String.valueOf(livre.getNbrExemplaire()));

        preselectionner(listeAuteurs, modeleListeAuteurs, livre.getAuteurs());
        preselectionner(listeEditeurs, modeleListeEditeurs, livre.getEditeurs());
        preselectionner(listeCategories, modeleListeCategories, livre.getCategories());
    }

    private <T> void preselectionner(JList<T> liste, DefaultListModel<T> modele, java.util.List<T> selectionnes) {
        if (selectionnes == null || selectionnes.isEmpty()) return;
        for (T element : selectionnes) {
            int index = modele.indexOf(element);
            if (index >= 0) {
                liste.addSelectionInterval(index, index);
            }
        }
    }

    private void creerNouvelAuteur() {
        AuteurFormDialog dialog = new AuteurFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isValide()) {
            try {
                int id = auteurController.ajouter(dialog.getAuteur());
                Auteur nouvel = auteurController.trouverParId(id);
                modeleListeAuteurs.addElement(nouvel);
                int index = modeleListeAuteurs.indexOf(nouvel);
                listeAuteurs.addSelectionInterval(index, index);
            } catch (IllegalArgumentException | SQLException ex) {
                afficherErreur("Impossible de créer l'auteur : " + ex.getMessage());
            }
        }
    }

    private void creerNouvelEditeur() {
        String nom = JOptionPane.showInputDialog(this, "Nom du nouvel éditeur :", "Nouvel éditeur",
                JOptionPane.PLAIN_MESSAGE);
        if (nom != null && !nom.isBlank()) {
            try {
                int id = livreController.ajouterEditeur(nom);
                Editeur nouvel = new Editeur(id, nom.trim());
                modeleListeEditeurs.addElement(nouvel);
                int index = modeleListeEditeurs.indexOf(nouvel);
                listeEditeurs.addSelectionInterval(index, index);
            } catch (IllegalArgumentException | SQLException ex) {
                afficherErreur("Impossible de créer l'éditeur : " + ex.getMessage());
            }
        }
    }

    private void creerNouvelleCategorie() {
        String nom = JOptionPane.showInputDialog(this, "Nom de la nouvelle catégorie :", "Nouvelle catégorie",
                JOptionPane.PLAIN_MESSAGE);
        if (nom != null && !nom.isBlank()) {
            try {
                int id = livreController.ajouterCategorie(nom);
                Categorie nouvelle = new Categorie(id, nom.trim());
                modeleListeCategories.addElement(nouvelle);
                int index = modeleListeCategories.indexOf(nouvelle);
                listeCategories.addSelectionInterval(index, index);
            } catch (IllegalArgumentException | SQLException ex) {
                afficherErreur("Impossible de créer la catégorie : " + ex.getMessage());
            }
        }
    }

    private void validerFormulaire() {
        String titre = champTitre.getText().trim();
        String texteAnnee = champAnnee.getText().trim();
        String texteExemplaires = champExemplaires.getText().trim();

        if (titre.isEmpty()) {
            afficherErreur("Le titre est obligatoire.");
            return;
        }

        int annee;
        try {
            annee = Integer.parseInt(texteAnnee);
        } catch (NumberFormatException ex) {
            afficherErreur("L'année de publication doit être un nombre entier.");
            return;
        }

        int exemplaires;
        try {
            exemplaires = Integer.parseInt(texteExemplaires);
        } catch (NumberFormatException ex) {
            afficherErreur("Le nombre d'exemplaires doit être un nombre entier.");
            return;
        }

        List<Auteur> auteursChoisis = listeAuteurs.getSelectedValuesList();
        if (auteursChoisis.isEmpty()) {
            afficherErreur("Veuillez sélectionner au moins un auteur.");
            return;
        }

        if (livre == null) {
            livre = new Livre();
        }
        livre.setTitre(titre);
        livre.setAnneePublication(annee);
        livre.setNbrExemplaire(exemplaires);
        livre.setAuteurs(auteursChoisis);
        livre.setEditeurs(listeEditeurs.getSelectedValuesList());
        livre.setCategories(listeCategories.getSelectedValuesList());

        valide = true;
        dispose();
    }

    public boolean isValide() {
        return valide;
    }

    public Livre getLivre() {
        return livre;
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }


    public void setLivreController(LivreController livreController){
        this.livreController = livreController;
    }

    public void setAuteurController(AuteurController auteurController){
        this.auteurController = auteurController;
    }
}
