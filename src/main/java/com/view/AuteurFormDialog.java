package com.view;

import com.model.Auteur;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author mouad
 **/
public class AuteurFormDialog extends JDialog {

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField champNom = new JTextField(20);
    private final JTextField champPrenom = new JTextField(20);
    private final JTextField champNationalite = new JTextField(20);
    private final JTextField champDateNaissance = new JTextField(20);

    private boolean valide = false;
    private Auteur auteur;

    public AuteurFormDialog(Window parent, Auteur auteurExistant){
        super(parent, auteurExistant == null ? "Ajouter un auteur" : "Modifier un auteur",
                ModalityType.APPLICATION_MODAL);
        this.auteur = auteurExistant;

        construireainterface();
        if(auteurExistant != null)
            preRemplirFormulaire(auteurExistant);

        pack();
        setLocationRelativeTo(parent);
    }

    private void construireainterface(){
        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int ligne = 0;
        ajouterChamp(formulaire, gbc, ligne++, "Nom *", champNom);
        ajouterChamp(formulaire, gbc, ligne++, "Prenom *", champPrenom);
        ajouterChamp(formulaire, gbc, ligne++, "Nationalité *", champNationalite);
        ajouterChamp(formulaire, gbc, ligne++, "Date de naissance (jj/mm/aaaa)", champDateNaissance);

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

    private void ajouterChamp(JPanel panel, GridBagConstraints gbc, int ligne, String libelle, JComponent champ){
        gbc.gridx = 0;
        gbc.gridy = ligne;
        gbc.weightx = 0;
        panel.add(new JLabel(libelle), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(champ, gbc);
    }

    private void preRemplirFormulaire(Auteur auteur){
        champNom.setText(auteur.getNom());
        champPrenom.setText(auteur.getPrenom());
        champNationalite.setText(auteur.getNationalite());
        if (auteur.getDateNaissance() != null){
            champDateNaissance.setText(auteur.getDateNaissance().format(FORMAT_DATE));
        }
    }

    private void validerFormulaire(){
        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String nationalite = champNationalite.getText().trim();
        String texteDate = champDateNaissance.getText().trim();

        if(nom.isEmpty() || prenom.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Le nom est le prenom sont obligatoires",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dateNaissance = null;
        if(!texteDate.isEmpty()){
            try{
                dateNaissance = LocalDate.parse(texteDate, FORMAT_DATE);
            } catch(DateTimeParseException ex){
                JOptionPane.showMessageDialog(this,
                        "Format de date invalide. Utilisez jj/mm/aaaa",
                        "Date invalide",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (auteur == null){
            auteur = new Auteur();
        }
        auteur.setNom(nom);
        auteur.setPrenom(prenom);
        auteur.setNationalite(nationalite);
        auteur.setDateNaissance(dateNaissance);

        valide = true;
        dispose();
    }

    public boolean isValide(){
        return valide;
    }

    public Auteur getAuteur(){
        return auteur;
    }
}
