package com.vidora.view;

import com.vidora.controller.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée principal pour l'interface graphique VIDORA
 * Remplace la version console par une interface Swing moderne
 */
public class MainGUI {
    
    public static void main(String[] args) {
        // Test de connexion à la base de données
        if (com.vidora.Database.connect() == null) {
            JOptionPane.showMessageDialog(null, 
                    "Impossible de se connecter à la base de données. Arrêt de l'application.", 
                    "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Configurer le Look and Feel
        try {
            // Essayer d'appliquer un look moderne
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Personnaliser les couleurs par défaut pour le thème sombre
            UIManager.put("Panel.background", MainView.DARK_BG);
            UIManager.put("OptionPane.background", MainView.DARK_BG);
            UIManager.put("OptionPane.messageForeground", MainView.TEXT_COLOR);
            UIManager.put("OptionPane.buttonBackground", MainView.ACCENT_COLOR);
            UIManager.put("OptionPane.buttonForeground", Color.WHITE);
            UIManager.put("TextField.background", new Color(50, 50, 60));
            UIManager.put("TextField.foreground", MainView.TEXT_COLOR);
            UIManager.put("TextField.caretForeground", MainView.TEXT_COLOR);
            UIManager.put("ComboBox.background", new Color(50, 50, 60));
            UIManager.put("ComboBox.foreground", MainView.TEXT_COLOR);
            UIManager.put("ScrollBar.thumb", new Color(70, 70, 80));
            UIManager.put("ScrollBar.thumbDarkShadow", new Color(60, 60, 70));
            UIManager.put("ScrollBar.thumbHighlight", new Color(80, 80, 90));
            
        } catch (Exception e) {
            System.err.println("Impossible d'appliquer le look and feel: " + e.getMessage());
        }
        
        // Lancer l'interface graphique dans l'EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Afficher un écran de chargement
                showLoadingScreen();
                
                // Créer le contrôleur principal
                MainController controller = new MainController();
                
                // Créer et afficher la fenêtre principale
                MainView mainView = new MainView(controller);
                
                // Fermer l'écran de chargement et afficher la fenêtre principale
                closeLoadingScreen();
                mainView.setVisible(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                        "Erreur lors du démarrage de l'interface : " + e.getMessage(), 
                        "Erreur de démarrage", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    private static JDialog loadingDialog;
    
    private static void showLoadingScreen() {
        loadingDialog = new JDialog();
        loadingDialog.setTitle("VIDORA - Chargement");
        loadingDialog.setSize(400, 200);
        loadingDialog.setLocationRelativeTo(null);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setUndecorated(true);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MainView.DARK_BG);
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(MainView.ACCENT_COLOR, 2));
        
        // Logo et titre
        JLabel titleLabel = new JLabel("🎬 VIDORA", SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(MainView.ACCENT_COLOR);
        
        JLabel subtitleLabel = new JLabel("Gestionnaire Intelligent de Vidéos", SwingConstants.CENTER);
        subtitleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        subtitleLabel.setForeground(MainView.TEXT_COLOR);
        
        // Barre de progression
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setBackground(new Color(50, 50, 60));
        progressBar.setForeground(MainView.ACCENT_COLOR);
        progressBar.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(70, 70, 80), 1));
        progressBar.setStringPainted(true);
        progressBar.setString("Chargement...");
        
        JPanel titlePanel = new JPanel(new java.awt.GridLayout(2, 1));
        titlePanel.setBackground(MainView.DARK_BG);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        JPanel progressPanel = new JPanel(new java.awt.FlowLayout());
        progressPanel.setBackground(MainView.DARK_BG);
        progressPanel.add(progressBar);
        
        panel.add(titlePanel, java.awt.BorderLayout.CENTER);
        panel.add(progressPanel, java.awt.BorderLayout.SOUTH);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        loadingDialog.setContentPane(panel);
        loadingDialog.setVisible(true);
    }
    
    private static void closeLoadingScreen() {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            loadingDialog.dispose();
        }
    }
}
