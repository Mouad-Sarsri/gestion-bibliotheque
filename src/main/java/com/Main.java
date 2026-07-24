package com;

import com.view.MainFrame;

import javax.swing.*;

/**
 * @author mouad
 **/
public class Main {
    public static void main() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si indisponible, on garde le look and feel par défaut
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame fenetre = new MainFrame();
            fenetre.setVisible(true);
        });
    }
}
