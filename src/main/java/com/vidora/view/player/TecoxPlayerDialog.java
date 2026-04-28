package com.vidora.view.player;

import com.vidora.controller.MainController;
import com.vidora.model.Video;
import com.vidora.view.components.TecoxVideoPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * TECOX - Fenêtre de lecture vidéo professionnelle
 */
public class TecoxPlayerDialog extends JDialog {
    private MainController controller;
    private Video video;
    private TecoxVideoPlayer tecoxPlayer;
    
    public TecoxPlayerDialog(JFrame parent, Video video, MainController controller) {
        super(parent, "🎬 TECOX Player - " + video.getTitre(), true);
        this.video = video;
        this.controller = controller;
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        setupKeyboardShortcuts();
        
        // Charger la vidéo
        tecoxPlayer.loadVideo(video);
        
        // Taille et position
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(800, 600));
        
        // Icône et titre
        setTitle("🎬 TECOX Player - " + video.getTitre());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        getContentPane().setBackground(com.vidora.view.MainView.DARK_BG);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Ajouter le lecteur TECOX
        tecoxPlayer = new TecoxVideoPlayer(controller);
        mainPanel.add(tecoxPlayer, BorderLayout.CENTER);
        
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
        JLabel titleLabel = new JLabel("🎬 TECOX - " + video.getTitre());
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        
        // Informations sur la vidéo
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        infoPanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        JLabel categoryLabel = new JLabel("🏷️ " + video.getCategorie());
        categoryLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        categoryLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        JLabel viewsLabel = new JLabel("👁️ " + video.getVues() + " vues");
        viewsLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        viewsLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        JLabel durationLabel = new JLabel("⏱️ " + formatDuration(video.getDuree()));
        durationLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        durationLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        infoPanel.add(categoryLabel);
        infoPanel.add(viewsLabel);
        infoPanel.add(durationLabel);
        
        // Boutons
        JButton closeButton = new JButton("✖️ Fermer TECOX");
        closeButton.setBackground(com.vidora.view.MainView.ERROR_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Gestion de la fermeture
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Incrémenter le compteur de vues
                video.incrementerVues();
                controller.modifierVideo(video);
                super.windowClosing(e);
            }
        });
    }
    
    private void setupKeyboardShortcuts() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        tecoxPlayer.togglePlayPause();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        dispose();
                        break;
                    case KeyEvent.VK_LEFT:
                        tecoxPlayer.rewindVideo();
                        break;
                    case KeyEvent.VK_RIGHT:
                        tecoxPlayer.forwardVideo();
                        break;
                    case KeyEvent.VK_UP:
                        // Augmenter volume
                        break;
                    case KeyEvent.VK_DOWN:
                        // Diminuer volume
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
    
    private String formatDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%d:%02d", minutes, secs);
        }
    }
    
    public TecoxVideoPlayer getTecoxPlayer() {
        return tecoxPlayer;
    }
}
