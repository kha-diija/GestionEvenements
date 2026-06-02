package application.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InscriptionInterface {
    
    public static void main(String[] args) {
        // Créer le JFrame principal
        JFrame frame = new JFrame("Gestion des Événements");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Définir un thème Aqua et Noir pour le fond
        frame.getContentPane().setBackground(Color.BLACK);

        // Créer un panneau avec une disposition de type BorderLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Créer un Label avec un texte en couleur Aqua
        JLabel label = new JLabel("Bienvenue dans l'application de gestion d'événements", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(new Color(0, 255, 255)); // Aqua

        // Ajouter le label au centre du panneau
        panel.add(label, BorderLayout.CENTER);

        // Créer un menu en haut
        JMenuBar menuBar = new JMenuBar();

        // Menu "Fichier"
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem newItem = new JMenuItem("Nouveau");
        newItem.setIcon(new ImageIcon("icons/new_icon.png"));  // Icône "Nouveau"
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Création d'un nouvel événement");
            }
        });
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(newItem);
        fileMenu.add(exitItem);

        // Menu "Aide"
        JMenu helpMenu = new JMenu("Aide");
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Version 1.0 - Application de gestion des événements.");
            }
        });
        helpMenu.add(aboutItem);

        // Ajouter les menus à la barre de menu
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        // Ajouter la barre de menu à la fenêtre
        frame.setJMenuBar(menuBar);

        // Ajouter le panneau au JFrame
        frame.add(panel);

        // Afficher la fenêtre
        frame.setLocationRelativeTo(null);  // Centrer la fenêtre
        frame.setVisible(true);
    }
}
