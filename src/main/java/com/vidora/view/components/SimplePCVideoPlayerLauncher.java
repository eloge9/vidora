package com.vidora.view.components;

import com.vidora.controller.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Lanceur ultra-simple du lecteur vidéo PC
 */
public class SimplePCVideoPlayerLauncher {
    
    public static void launchSimplePCPlayer(MainController controller) {
        SwingUtilities.invokeLater(() -> {
            // Créer une fenêtre simple
            JFrame frame = new JFrame("🎬 Lecteur Vidéo PC - Ultra Simple");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            
            // Ajouter le lecteur PC ultra-simple
            SimplePCVideoPlayer player = new SimplePCVideoPlayer(controller);
            frame.add(player);
            
            // Rendre visible
            frame.setVisible(true);
        });
    }
    
    // Pour créer un bouton ultra-simple
    public static JButton createSimplePCPlayerButton(MainController controller) {
        JButton button = new JButton("🎬 Lecteur PC Simple");
        button.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 35));
        
        button.addActionListener(e -> launchSimplePCPlayer(controller));
        
        // Effet hover simple
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 100, 0)); // Orange plus clair
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return button;
    }
}
