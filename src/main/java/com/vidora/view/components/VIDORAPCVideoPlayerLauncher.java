package com.vidora.view.components;

import com.vidora.controller.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Lanceur du lecteur vidéo VIDORA qui utilise le lecteur PC
 */
public class VIDORAPCVideoPlayerLauncher {
    
    public static void launchVIDORAPCPlayer(MainController controller) {
        SwingUtilities.invokeLater(() -> {
            // Créer une fenêtre VIDORA
            JFrame frame = new JFrame("🎬 VIDORA Player - Lecteur Vidéo");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 650);
            frame.setLocationRelativeTo(null);
            frame.setIconImage(createVIDORAIcon());
            
            // Ajouter le lecteur VIDORA
            VIDORAPCVideoPlayer player = new VIDORAPCVideoPlayer(controller);
            frame.add(player);
            
            // Rendre visible
            frame.setVisible(true);
            
            System.out.println("🎬 VIDORA Player - Lancé");
        });
    }
    
    // Pour créer un bouton VIDORA
    public static JButton createVIDORAPCPlayerButton(MainController controller) {
        JButton button = new JButton("🎬 VIDORA Player");
        button.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 40));
        button.setToolTipText("Lancer VIDORA Player - Lecteur Vidéo");
        
        button.addActionListener(e -> launchVIDORAPCPlayer(controller));
        
        // Effet hover VIDORA
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
    
    // Pour créer un menu item VIDORA
    public static JMenuItem createVIDORAPCPlayerMenuItem(MainController controller) {
        JMenuItem menuItem = new JMenuItem("🎬 VIDORA Player");
        menuItem.setBackground(com.vidora.view.MainView.DARK_BG);
        menuItem.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        menuItem.setFont(new Font("Roboto", Font.PLAIN, 12));
        menuItem.addActionListener(e -> launchVIDORAPCPlayer(controller));
        return menuItem;
    }
    
    // Créer une icône simple pour VIDORA
    private static Image createVIDORAIcon() {
        // Créer une icône simple
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        
        // Fond
        g2d.setColor(com.vidora.view.MainView.ACCENT_COLOR);
        g2d.fillRoundRect(0, 0, 32, 32, 8, 8);
        
        // Texte "V"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("V", 8, 24);
        
        g2d.dispose();
        return icon;
    }
}
