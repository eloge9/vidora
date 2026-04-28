package com.vidora.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Lecteur vidéo réel utilisant le lecteur système
 */
public class RealVideoPlayer extends JPanel {
    private String currentVideoPath;
    private String currentVideoTitle;
    private boolean isPlaying = false;
    
    // Composants UI
    private JPanel videoPanel;
    private JLabel videoPlaceholder;
    private JPanel controlsPanel;
    private JButton playPauseButton;
    private JButton stopButton;
    private JButton openInSystemButton;
    private JLabel titleLabel;
    private JLabel infoLabel;
    
    public RealVideoPlayer() {
        initComponent();
        layoutComponents();
        setupEventHandlers();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        setLayout(new BorderLayout(0, 10));
    }
    
    private void layoutComponents() {
        // Panel vidéo
        videoPanel = new JPanel(new BorderLayout());
        videoPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        videoPanel.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.CARD_BG, 1));
        
        videoPlaceholder = new JLabel("🎬 Aucune vidéo chargée", SwingConstants.CENTER);
        videoPlaceholder.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        videoPlaceholder.setFont(new Font("Roboto", Font.BOLD, 18));
        videoPlaceholder.setPreferredSize(new Dimension(0, 300));
        
        videoPanel.add(videoPlaceholder, BorderLayout.CENTER);
        
        // Panel d'informations
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        infoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        titleLabel = new JLabel("Titre de la vidéo");
        titleLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        
        infoLabel = new JLabel("Informations");
        infoLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        infoLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        infoPanel.add(titleLabel, BorderLayout.NORTH);
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        
        // Panel de contrôles
        controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlsPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        controlsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        playPauseButton = new JButton("▶️ Lire");
        playPauseButton.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        playPauseButton.setForeground(Color.WHITE);
        playPauseButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        playPauseButton.setFocusPainted(false);
        playPauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        stopButton = new JButton("⏹️ Arrêter");
        stopButton.setBackground(com.vidora.view.MainView.SECONDARY_TEXT);
        stopButton.setForeground(Color.WHITE);
        stopButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        stopButton.setFocusPainted(false);
        stopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        openInSystemButton = new JButton("🖥️ Ouvrir avec lecteur système");
        openInSystemButton.setBackground(com.vidora.view.MainView.SUCCESS_COLOR);
        openInSystemButton.setForeground(Color.WHITE);
        openInSystemButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        openInSystemButton.setFocusPainted(false);
        openInSystemButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        controlsPanel.add(playPauseButton);
        controlsPanel.add(stopButton);
        controlsPanel.add(openInSystemButton);
        
        // Assemblage
        add(videoPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        add(controlsPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        playPauseButton.addActionListener(e -> togglePlayPause());
        stopButton.addActionListener(e -> stopVideo());
        openInSystemButton.addActionListener(e -> openInSystemPlayer());
    }
    
    public void loadVideo(String videoPath, String title) {
        this.currentVideoPath = videoPath;
        this.currentVideoTitle = title;
        
        // Mettre à jour l'affichage
        videoPlaceholder.setText("🎬 " + title);
        videoPlaceholder.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        titleLabel.setText(title);
        
        // Vérifier si le fichier existe
        File videoFile = new File(videoPath);
        if (videoFile.exists()) {
            long fileSize = videoFile.length();
            double fileSizeMB = fileSize / (1024.0 * 1024.0);
            
            infoLabel.setText(String.format("📁 %s | 📏 %.1f MB", 
                videoFile.getName(), fileSizeMB));
            
            // Afficher thumbnail si disponible
            videoPlaceholder.setIcon(createVideoThumbnail(videoFile));
        } else {
            infoLabel.setText("❌ Fichier introuvable: " + videoPath);
            videoPlaceholder.setText("❌ Fichier introuvable");
        }
        
        // Réinitialiser l'état
        isPlaying = false;
        updatePlayPauseButton();
        
        System.out.println("Vidéo chargée: " + videoPath);
    }
    
    public void togglePlayPause() {
        if (currentVideoPath == null || currentVideoPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Aucune vidéo chargée", 
                "Erreur", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (isPlaying) {
            pauseVideo();
        } else {
            playVideo();
        }
    }
    
    public void playVideo() {
        if (currentVideoPath == null || currentVideoPath.isEmpty()) {
            return;
        }
        
        try {
            // Ouvrir avec le lecteur système
            openInSystemPlayer();
            isPlaying = true;
            updatePlayPauseButton();
            System.out.println("Lecture de: " + currentVideoTitle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la lecture: " + e.getMessage(), 
                "Erreur de lecture", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void pauseVideo() {
        isPlaying = false;
        updatePlayPauseButton();
        System.out.println("Pause: " + currentVideoTitle);
    }
    
    public void stopVideo() {
        isPlaying = false;
        updatePlayPauseButton();
        System.out.println("Arrêt: " + currentVideoTitle);
    }
    
    private void openInSystemPlayer() {
        if (currentVideoPath == null || currentVideoPath.isEmpty()) {
            return;
        }
        
        try {
            File videoFile = new File(currentVideoPath);
            if (videoFile.exists()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(videoFile);
                isPlaying = true;
                updatePlayPauseButton();
                System.out.println("Ouverture avec lecteur système: " + currentVideoPath);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Le fichier vidéo n'existe pas:\n" + currentVideoPath, 
                    "Fichier introuvable", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Impossible d'ouvrir le lecteur système:\n" + e.getMessage(), 
                "Erreur système", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePlayPauseButton() {
        if (isPlaying) {
            playPauseButton.setText("⏸️ Pause");
            playPauseButton.setBackground(com.vidora.view.MainView.HOVER_BG);
        } else {
            playPauseButton.setText("▶️ Lire");
            playPauseButton.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        }
    }
    
    private Icon createVideoThumbnail(File videoFile) {
        // Créer une icône simple pour représenter la vidéo
        int width = 120;
        int height = 80;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fond
        g2d.setColor(com.vidora.view.MainView.CARD_BG);
        g2d.fillRect(0, 0, width, height);
        
        // Icône play
        g2d.setColor(com.vidora.view.MainView.ACCENT_COLOR);
        int[] xPoints = {width/2 - 15, width/2 - 15, width/2 + 15};
        int[] yPoints = {height/2 - 20, height/2 + 20, height/2};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
}
