package com.vidora.view.components;

import com.vidora.controller.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Lanceur du lecteur vidéo intégré dans VIDORA
 */
public class IntegratedVideoPlayerLauncher {
    
    public static void launchIntegratedPlayer(MainController controller) {
        SwingUtilities.invokeLater(() -> {
            // Créer une fenêtre pour le lecteur intégré
            JFrame frame = new JFrame("🎬 VIDORA Player - Lecteur PC Intégré");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 650);
            frame.setLocationRelativeTo(null);
            
            // Ajouter le lecteur intégré
            IntegratedVideoPlayer player = new IntegratedVideoPlayer(controller);
            frame.add(player);
            
            // Rendre visible
            frame.setVisible(true);
        });
    }
    
    // Pour créer un bouton d'intégration dans l'interface principale VIDORA
    public static JButton createIntegratedPlayerButton(MainController controller) {
        JButton button = new JButton("🎬 Lecteur Intégré PC");
        button.setBackground(com.vidora.view.MainView.DARK_BG);
        button.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        button.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.ACCENT_COLOR, 2));
        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(160, 35));
        
        button.addActionListener(e -> launchIntegratedPlayer(controller));
        
        // Effet hover intégré
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(com.vidora.view.MainView.HOVER_BG);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(com.vidora.view.MainView.DARK_BG);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return button;
    }
    
    // Pour créer un menu item
    public static JMenuItem createIntegratedPlayerMenuItem(MainController controller) {
        JMenuItem menuItem = new JMenuItem("🎬 Lecteur PC Intégré");
        menuItem.setBackground(com.vidora.view.MainView.DARK_BG);
        menuItem.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        menuItem.setFont(new Font("Roboto", Font.PLAIN, 12));
        menuItem.addActionListener(e -> launchIntegratedPlayer(controller));
        return menuItem;
    }
}
