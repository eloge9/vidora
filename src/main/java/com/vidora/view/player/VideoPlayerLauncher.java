package com.vidora.view.player;

import com.vidora.controller.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Lanceur du lecteur vidéo complet depuis l'interface principale
 */
public class VideoPlayerLauncher {
    
    public static void launchVideoPlayer(MainController controller) {
        SwingUtilities.invokeLater(() -> {
            CompleteVideoPlayer player = new CompleteVideoPlayer(controller);
            player.setVisible(true);
        });
    }
    
    // Pour ajouter un menu ou bouton dans l'interface principale
    public static JMenuItem createVideoPlayerMenuItem(MainController controller) {
        JMenuItem menuItem = new JMenuItem("🎬 Lecteur Vidéo Complet");
        menuItem.setBackground(Color.BLACK);
        menuItem.setForeground(Color.WHITE);
        menuItem.setFont(new Font("Arial", Font.PLAIN, 12));
        menuItem.addActionListener(e -> launchVideoPlayer(controller));
        return menuItem;
    }
    
    // Pour créer un bouton d'action
    public static JButton createVideoPlayerButton(MainController controller) {
        JButton button = new JButton("🎬 Lecteur Complet");
        button.setBackground(Color.BLACK);
        button.setForeground(Color.ORANGE);
        button.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 35));
        
        button.addActionListener(e -> launchVideoPlayer(controller));
        
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
