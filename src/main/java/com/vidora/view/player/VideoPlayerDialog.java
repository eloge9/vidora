package com.vidora.view.player;

import com.vidora.controller.MainController;
import com.vidora.model.Video;
import com.vidora.view.components.RealVideoPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenêtre de lecture vidéo dédiée
 * Interface de lecture complète avec contrôles avancés
 */
public class VideoPlayerDialog extends JDialog {
    private MainController controller;
    private Video video;
    private RealVideoPlayer videoPlayer;
    
    // Composants UI
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JButton closeButton;
    
    public VideoPlayerDialog(JFrame parent, Video video, MainController controller) {
        super(parent, "VIDORA Player - " + video.getTitre(), true);
        this.video = video;
        this.controller = controller;
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        setupKeyboardShortcuts();
        
        // Charger la vidéo
        videoPlayer = new RealVideoPlayer();
        videoPlayer.loadVideo(video.getChemin(), video.getTitre());
        
        // Taille et position
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(600, 400));
        
        // Icône et titre
        setTitle("🎬 VIDORA Player - " + video.getTitre());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        getContentPane().setBackground(com.vidora.view.MainView.DARK_BG);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(0, 10));
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Ajouter le lecteur vidéo réel
        mainPanel.add(videoPlayer, BorderLayout.CENTER);
        
        // Panel de contrôles supérieurs
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(15, 20, 10, 20));
        
        // Titre de la vidéo
        titleLabel = new JLabel("🎬 " + video.getTitre());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        
        // Informations sur la vidéo
        String info = video.getCategorie() + " • " + video.getDureeFormatee() + " • " + video.getVues() + " vues";
        infoLabel = new JLabel(info);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        infoPanel.add(titleLabel, BorderLayout.NORTH);
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        
        panel.add(infoPanel, BorderLayout.WEST);
        
        // Bouton fermer
        closeButton = new JButton("✕");
        closeButton.setBackground(com.vidora.view.MainView.ERROR_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        closeButton.setFocusPainted(false);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
        panel.add(closeButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        // Informations de lecture
        JLabel shortcutsLabel = new JLabel("Raccourcis : Espace (Play/Pause) • F (Plein écran) • M (Mute) • Esc (Fermer)");
        shortcutsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        shortcutsLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        
        panel.add(shortcutsLabel, BorderLayout.WEST);
        
        // Boutons supplémentaires
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        JButton playlistButton = new JButton("📋 Ajouter à playlist");
        playlistButton.setBackground(com.vidora.view.MainView.SUCCESS_COLOR);
        playlistButton.setForeground(Color.WHITE);
        playlistButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        playlistButton.setFocusPainted(false);
        playlistButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        playlistButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton shareButton = new JButton("🔗 Partager");
        shareButton.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        shareButton.setForeground(Color.WHITE);
        shareButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        shareButton.setFocusPainted(false);
        shareButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        shareButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonsPanel.add(playlistButton);
        buttonsPanel.add(shareButton);
        
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Gestion de la fermeture
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // videoPlayer.cleanup(); // Pas nécessaire avec RealVideoPlayer
                super.windowClosing(e);
            }
        });
        
        // Incrémenter le compteur de vues
        video.incrementerVues();
        controller.modifierVideo(video);
    }
    
    private void setupKeyboardShortcuts() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        videoPlayer.togglePlayPause();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        dispose();
                        break;
                }
        }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        setFocusable(true);
        requestFocusInWindow();
    }
    
    public RealVideoPlayer getVideoPlayer() {
        return videoPlayer;
    }
}
