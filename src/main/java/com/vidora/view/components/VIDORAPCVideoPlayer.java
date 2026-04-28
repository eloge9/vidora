package com.vidora.view.components;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Lecteur vidéo VIDORA qui utilise le lecteur PC mais intégré à l'interface VIDORA
 */
public class VIDORAPCVideoPlayer extends JPanel {
    private MainController controller;
    private ArrayList<Video> videos;
    private Video currentVideo;
    
    // Composants VIDORA
    private JLabel titleLabel;
    private JLabel videoDisplay;
    private JLabel statusLabel;
    private JPanel videoPanel;
    private JScrollPane videoList;
    
    public VIDORAPCVideoPlayer(MainController controller) {
        this.controller = controller;
        this.videos = new ArrayList<>();
        
        initComponent();
        layoutComponents();
        loadVideos();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        setLayout(new BorderLayout(0, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }
    
    private void layoutComponents() {
        // Titre VIDORA
        titleLabel = new JLabel("🎬 VIDORA Player - Lecteur Vidéo", SwingConstants.CENTER);
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        
        // Zone vidéo VIDORA
        videoPanel = createVIDORAVideoPanel();
        mainPanel.add(videoPanel, BorderLayout.CENTER);
        
        // Liste des vidéos VIDORA
        videoList = createVIDORAVideoList();
        mainPanel.add(videoList, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createVIDORAVideoPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        panel.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 2));
        panel.setPreferredSize(new Dimension(500, 400));
        
        // Zone d'affichage vidéo
        videoDisplay = new JLabel("🎬 VIDORA Player\n\nSélectionnez une vidéo pour commencer", SwingConstants.CENTER);
        videoDisplay.setBackground(com.vidora.view.MainView.DARK_BG);
        videoDisplay.setOpaque(true);
        videoDisplay.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        videoDisplay.setFont(new Font("Roboto", Font.BOLD, 16));
        videoDisplay.setBorder(new EmptyBorder(50, 20, 50, 20));
        
        // Status VIDORA
        statusLabel = new JLabel("📹 VIDORA - Prêt", SwingConstants.CENTER);
        statusLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        statusLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        panel.add(videoDisplay, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JScrollPane createVIDORAVideoList() {
        JPanel listPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        listPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        
        // Créer les cartes vidéo VIDORA
        for (Video video : videos) {
            JPanel videoCard = createVIDORAVideoCard(video);
            listPanel.add(videoCard);
        }
        
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBackground(com.vidora.view.MainView.DARK_BG);
        scrollPane.getViewport().setBackground(com.vidora.view.MainView.DARK_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        scrollPane.setPreferredSize(new Dimension(300, 0));
        
        return scrollPane;
    }
    
    private JPanel createVIDORAVideoCard(Video video) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        card.setPreferredSize(new Dimension(0, 100));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icône vidéo
        JLabel iconLabel = new JLabel("🎬", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Roboto", Font.PLAIN, 24));
        iconLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        iconLabel.setPreferredSize(new Dimension(50, 0));
        
        // Informations vidéo
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        infoPanel.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        
        JLabel titleLabel = new JLabel(video.getTitre());
        titleLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        
        JLabel pathLabel = new JLabel("📂 " + video.getChemin());
        pathLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        pathLabel.setFont(new Font("Roboto", Font.PLAIN, 11));
        
        JLabel infoLabel = new JLabel(String.format("⏱️ %s | 👁️ %d vues", 
            formatDuration(video.getDuree()), video.getVues()));
        infoLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        infoLabel.setFont(new Font("Roboto", Font.PLAIN, 11));
        
        infoPanel.add(titleLabel);
        infoPanel.add(pathLabel);
        infoPanel.add(infoLabel);
        
        // Bouton de lecture VIDORA
        JButton playButton = new JButton("▶️");
        playButton.setBackground(com.vidora.view.MainView.SUCCESS_COLOR);
        playButton.setForeground(Color.WHITE);
        playButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Roboto", Font.BOLD, 16));
        playButton.setPreferredSize(new Dimension(50, 50));
        playButton.setToolTipText("Lire avec VIDORA Player");
        
        // Action de lecture VIDORA
        playButton.addActionListener(e -> playVideoWithVIDORA(video));
        
        // Au clic sur la carte aussi
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    playVideoWithVIDORA(video);
                }
            }
        });
        
        // Effet hover VIDORA
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(com.vidora.view.MainView.HOVER_BG);
                playButton.setBackground(new Color(0, 200, 0)); // Vert plus clair
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
                playButton.setBackground(com.vidora.view.MainView.SUCCESS_COLOR);
            }
        });
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(playButton, BorderLayout.EAST);
        
        return card;
    }
    
    private void loadVideos() {
        try {
            videos = controller.getVideoService().getAllVideos();
            
            // Rafraîchir l'affichage
            removeAll();
            layoutComponents();
            revalidate();
            repaint();
            
            System.out.println("🎬 VIDORA Player - " + videos.size() + " vidéos chargées");
                
        } catch (Exception e) {
            System.err.println("❌ VIDORA Player - Erreur de chargement: " + e.getMessage());
        }
    }
    
    private void playVideoWithVIDORA(Video video) {
        currentVideo = video;
        
        try {
            File videoFile = new File(video.getChemin());
            
            if (!videoFile.exists()) {
                updateVIDORADisplay("❌ Fichier introuvable", "📂 " + video.getChemin(), Color.RED);
                JOptionPane.showMessageDialog(this, 
                    "❌ Fichier introuvable:\n" + video.getChemin(), 
                    "VIDORA Player", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier si Desktop est supporté
            if (!Desktop.isDesktopSupported()) {
                updateVIDORADisplay("❌ Lecteur non disponible", "VIDORA Player - Erreur système", Color.RED);
                return;
            }
            
            // Afficher les informations VIDORA
            long fileSize = videoFile.length();
            double fileSizeMB = fileSize / (1024.0 * 1024.0);
            
            System.out.println("🎬 VIDORA Player - Lecture:");
            System.out.println("🎬 Titre: " + video.getTitre());
            System.out.println("📂 Fichier: " + videoFile.getAbsolutePath());
            System.out.println("📏 Taille: " + String.format("%.1f MB", fileSizeMB));
            
            // Mettre à jour l'affichage VIDORA
            updateVIDORADisplay("🎬 Lecture en cours", video.getTitre(), new Color(0, 255, 0));
            
            // Ouvrir avec le lecteur par défaut du PC (mais présenté comme VIDORA)
            Desktop desktop = Desktop.getDesktop();
            desktop.open(videoFile);
            
            // Message de succès VIDORA
            JOptionPane.showMessageDialog(this, 
                "✅ VIDORA Player - Vidéo lancée\n\n" +
                "🎬 Titre: " + video.getTitre() + "\n" +
                "📂 Fichier: " + videoFile.getName() + "\n" +
                "📏 Taille: " + String.format("%.1f MB", fileSizeMB) + "\n\n" +
                "🎬 VIDORA Player utilise votre lecteur système", 
                "VIDORA Player", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            updateVIDORADisplay("❌ Erreur de lecture", "VIDORA Player - Erreur", Color.RED);
            JOptionPane.showMessageDialog(this, 
                "❌ VIDORA Player - Erreur de lecture:\n" + e.getMessage() + "\n\n" +
                "📂 Fichier: " + video.getChemin(), 
                "VIDORA Player", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateVIDORADisplay(String status, String title, Color color) {
        SwingUtilities.invokeLater(() -> {
            videoDisplay.setText("🎬 VIDORA Player\n\n" + title);
            videoDisplay.setForeground(color);
            statusLabel.setText("📹 " + status);
        });
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
}
