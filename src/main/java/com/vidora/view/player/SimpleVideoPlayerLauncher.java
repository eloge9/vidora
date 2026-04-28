package com.vidora.view.player;

import com.vidora.controller.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Lanceur du lecteur vidéo simple depuis l'interface principale
 */
public class SimpleVideoPlayerLauncher {
    
    public static void launchSimpleVideoPlayer(MainController controller) {
        SwingUtilities.invokeLater(() -> {
            SimpleVideoPlayer player = new SimpleVideoPlayer(controller);
        });
    }
    
    // Pour créer un bouton d'intégration dans l'interface principale
    public static JButton createSimpleVideoPlayerButton(MainController controller) {
        JButton button = new JButton("🎬 Lecteur Simple");
        button.setBackground(Color.BLACK);
        button.setForeground(Color.ORANGE);
        button.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(140, 35));
        
        button.addActionListener(e -> launchSimpleVideoPlayer(controller));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(40, 40, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
            }
        });
        
        return button;
    }
}
