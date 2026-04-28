package com.vidora.view.components;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Lecteur vidéo ultra-simple - Utilise directement le lecteur du PC
 */
public class SimplePCVideoPlayer extends JPanel {
    private MainController controller;
    private ArrayList<Video> videos;
    
    public SimplePCVideoPlayer(MainController controller) {
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
        // Titre
        JLabel titleLabel = new JLabel("🎬 Lecteur Vidéo PC - Ultra Simple", SwingConstants.CENTER);
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);
        
        // Liste des vidéos
        JScrollPane scrollPane = createVideoList();
        add(scrollPane, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JScrollPane createVideoList() {
        JPanel listPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        listPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        
        // Créer les cartes vidéo
        for (Video video : videos) {
            JPanel videoCard = createVideoCard(video);
            listPanel.add(videoCard);
        }
        
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBackground(com.vidora.view.MainView.DARK_BG);
        scrollPane.getViewport().setBackground(com.vidora.view.MainView.DARK_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        return scrollPane;
    }
    
    private JPanel createVideoCard(Video video) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        card.setPreferredSize(new Dimension(0, 80));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Titre
        JLabel titleLabel = new JLabel("🎬 " + video.getTitre());
        titleLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        
        // Informations
        String info = String.format("📂 %s | ⏱️ %s | 👁️ %d vues", 
            video.getChemin(), 
            formatDuration(video.getDuree()), 
            video.getVues());
        JLabel infoLabel = new JLabel(info);
        infoLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        infoLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        // Bouton de lecture
        JButton playButton = new JButton("▶️ Lire avec Lecteur PC");
        playButton.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        playButton.setForeground(Color.WHITE);
        playButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Roboto", Font.BOLD, 12));
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Action de lecture
        playButton.addActionListener(e -> playVideoWithPC(video));
        
        // Au clic sur la carte aussi
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    playVideoWithPC(video);
                }
            }
        });
        
        // Effet hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(com.vidora.view.MainView.HOVER_BG);
                playButton.setBackground(new Color(255, 100, 0)); // Orange plus clair
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
                playButton.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
            }
        });
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(infoLabel, BorderLayout.CENTER);
        card.add(playButton, BorderLayout.EAST);
        
        return card;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(com.vidora.view.MainView.DARK_BG);
        
        JButton refreshButton = new JButton("🔄 Rafraîchir");
        refreshButton.setBackground(com.vidora.view.MainView.SUCCESS_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Roboto", Font.BOLD, 12));
        refreshButton.addActionListener(e -> loadVideos());
        
        JButton playAllButton = new JButton("▶️ Lire toutes les vidéos");
        playAllButton.setBackground(com.vidora.view.MainView.PURPLE_COLOR);
        playAllButton.setForeground(Color.WHITE);
        playAllButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        playAllButton.setFocusPainted(false);
        playAllButton.setFont(new Font("Roboto", Font.BOLD, 12));
        playAllButton.addActionListener(e -> playAllVideos());
        
        panel.add(refreshButton);
        panel.add(playAllButton);
        
        return panel;
    }
    
    private void loadVideos() {
        try {
            videos = controller.getVideoService().getAllVideos();
            
            // Rafraîchir l'affichage
            removeAll();
            layoutComponents();
            revalidate();
            repaint();
            
            JOptionPane.showMessageDialog(this, 
                "✅ " + videos.size() + " vidéos chargées", 
                "Lecteur PC", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Erreur de chargement: " + e.getMessage(), 
                "Lecteur PC", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void playVideoWithPC(Video video) {
        try {
            File videoFile = new File(video.getChemin());
            
            if (!videoFile.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Fichier introuvable:\n" + video.getChemin(), 
                    "Lecteur PC", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier si Desktop est supporté
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Lecteur système non disponible", 
                    "Lecteur PC", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Afficher les informations
            long fileSize = videoFile.length();
            double fileSizeMB = fileSize / (1024.0 * 1024.0);
            
            System.out.println("🎬 Lecteur PC - Lecture:");
            System.out.println("📂 Fichier: " + videoFile.getAbsolutePath());
            System.out.println("📏 Taille: " + String.format("%.1f MB", fileSizeMB));
            System.out.println("🎬 Titre: " + video.getTitre());
            
            // Ouvrir avec le lecteur par défaut du PC
            Desktop desktop = Desktop.getDesktop();
            desktop.open(videoFile);
            
            // Message de succès
            JOptionPane.showMessageDialog(this, 
                "✅ Vidéo lancée avec le lecteur PC\n\n" +
                "🎬 Titre: " + video.getTitre() + "\n" +
                "📂 Fichier: " + videoFile.getName() + "\n" +
                "📏 Taille: " + String.format("%.1f MB", fileSizeMB), 
                "Lecteur PC", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Erreur de lecture:\n" + e.getMessage() + "\n\n" +
                "📂 Fichier: " + video.getChemin(), 
                "Lecteur PC", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void playAllVideos() {
        if (videos.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "❌ Aucune vidéo disponible", 
                "Lecteur PC", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Version simple - ouvrir une par une
        new Thread(() -> {
            for (int i = 0; i < videos.size(); i++) {
                final int index = i + 1;
                final Video video = videos.get(i);
                
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, 
                        "🎬 Lecture " + index + "/" + videos.size() + "\n" +
                        video.getTitre(), 
                        "Lecteur PC - Lecture multiple", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
                
                try {
                    Thread.sleep(1000); // Attendre 1 seconde
                    
                    File videoFile = new File(video.getChemin());
                    if (videoFile.exists() && Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(videoFile);
                        System.out.println("🎬 Ouvert: " + video.getTitre());
                    }
                } catch (Exception e) {
                    System.err.println("❌ Erreur avec " + video.getTitre() + ": " + e.getMessage());
                }
            }
            
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(SimplePCVideoPlayer.this, 
                    "✅ Toutes les vidéos ont été lancées", 
                    "Lecteur PC", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        }).start();
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
