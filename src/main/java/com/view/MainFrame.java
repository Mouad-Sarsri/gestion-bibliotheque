package com.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author mouad
 **/
public class MainFrame extends JFrame {
    public MainFrame() {
        super("Gestion de bibliothèque");

        JTabbedPane onglets = new JTabbedPane();
        onglets.addTab("Livres", new LivrePanel());
        onglets.addTab("Auteurs", new AuteurPanel());

        setContentPane(onglets);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 600));
        setSize(1100, 650);
        setLocationRelativeTo(null);
    }
}
