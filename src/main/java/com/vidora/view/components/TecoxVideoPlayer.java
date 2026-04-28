package com.vidora.view.components;

import com.vidora.model.Video;
import com.vidora.controller.MainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Desktop;

/**
 * TECOX - Lecteur vidéo complet intégré
 * Interface professionnelle avec tous les contrôles
 */
public class TecoxVideoPlayer extends JPanel {
    private Video currentVideo;
    private MainController controller;
    private boolean isPlaying = false;
    private int currentTime = 0;
    private int totalTime = 0;
    
    // Composants UI
    private JPanel videoPanel;
    private JLabel videoDisplay;
    private JPanel controlsPanel;
    private JButton playPauseButton;
    private JButton stopButton;
    private JButton rewindButton;
    private JButton forwardButton;
    private JProgressBar progressBar;
    private JLabel timeLabel;
    private JSlider volumeSlider;
    private JButton fullscreenButton;
    private JLabel titleLabel;
    private JLabel infoLabel;
    
    public TecoxVideoPlayer(MainController controller) {
        this.controller = controller;
        initComponent();
        layoutComponents();
        setupEventHandlers();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }
    
    private void layoutComponents() {
        // Panel vidéo principal
        videoPanel = createVideoPanel();
        add(videoPanel, BorderLayout.CENTER);
        
        // Panel d'informations
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.NORTH);
        
        // Panel de contrôles
        controlsPanel = createControlsPanel();
        add(controlsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createVideoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.DARK_BG);
        panel.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.CARD_BG, 2));
        
        // Zone d'affichage vidéo
        videoDisplay = new JLabel("🎬 TECOX - Lecteur Vidéo", SwingConstants.CENTER);
        videoDisplay.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        videoDisplay.setOpaque(true);
        videoDisplay.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        videoDisplay.setFont(new Font("Roboto", Font.BOLD, 24));
        videoDisplay.setPreferredSize(new Dimension(0, 400));
        videoDisplay.setBorder(new EmptyBorder(50, 20, 50, 20));
        
        // Icône TECOX avec fond noir et bordure visible
        videoDisplay.setIcon(createTecoxIcon());
        
        panel.add(videoDisplay, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(15, 20, 10, 20));
        
        titleLabel = new JLabel("🎬 TECOX - Aucune vidéo chargée");
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        
        infoLabel = new JLabel("📁 Chargement en attente...");
        infoLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        infoLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(15, 20, 20, 20));
        
        // Barre de progression
        JPanel progressPanel = new JPanel(new BorderLayout(0, 5));
        progressPanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground(com.vidora.view.MainView.CARD_BG);
        progressBar.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        progressBar.setStringPainted(true);
        progressBar.setString("00:00 / 00:00");
        progressBar.setPreferredSize(new Dimension(0, 20));
        
        timeLabel = new JLabel("⏱️ 00:00");
        timeLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        timeLabel.setFont(new Font("Roboto", Font.BOLD, 12));
        
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(timeLabel, BorderLayout.EAST);
        
        // Boutons de contrôle
        JPanel buttonsPanel = createButtonsPanel();
        
        // Volume
        JPanel volumePanel = createVolumePanel();
        
        // Assemblage
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        bottomPanel.add(buttonsPanel, BorderLayout.CENTER);
        bottomPanel.add(volumePanel, BorderLayout.EAST);
        
        panel.add(progressPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        // Boutons principaux
        playPauseButton = createControlButton("▶️", com.vidora.view.MainView.ACCENT_COLOR);
        stopButton = createControlButton("⏹️", com.vidora.view.MainView.SECONDARY_TEXT);
        rewindButton = createControlButton("⏪", com.vidora.view.MainView.SECONDARY_TEXT);
        forwardButton = createControlButton("⏩", com.vidora.view.MainView.SECONDARY_TEXT);
        fullscreenButton = createControlButton("🖥️", com.vidora.view.MainView.SUCCESS_COLOR);
        
        panel.add(rewindButton);
        panel.add(playPauseButton);
        panel.add(stopButton);
        panel.add(forwardButton);
        panel.add(fullscreenButton);
        
        return panel;
    }
    
    private JPanel createVolumePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        JLabel volumeIcon = new JLabel("🔊");
        volumeIcon.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        volumeIcon.setFont(new Font("Roboto", Font.PLAIN, 14));
        
        volumeSlider = new JSlider(0, 100, 75);
        volumeSlider.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        volumeSlider.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        
        panel.add(volumeIcon);
        panel.add(volumeSlider);
        
        return panel;
    }
    
    private JButton createControlButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        
        // 1. Fond noir comme demandé
        button.setBackground(Color.BLACK);
        
        // 2. Texte en blanc très visible
        button.setForeground(new Color(255, 255, 255)); // Blanc pur
        
        // 3. Bordure colorée pour voir les boutons
        button.setBorder(BorderFactory.createLineBorder(bgColor, 2));
        
        // 4. Pas de focus carré blanc
        button.setFocusPainted(false);
        
        // 5. Fond bien rempli
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        
        // 6. Curseur main
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 7. Police plus grande et visible
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        
        // 8. Padding pour que ce soit bien visible
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Effet hover avec changement de bordure
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(40, 40, 40)); // Gris clair
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor.brighter(), 3),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor, 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        playPauseButton.addActionListener(e -> togglePlayPause());
        stopButton.addActionListener(e -> stopVideo());
        rewindButton.addActionListener(e -> rewindVideo());
        forwardButton.addActionListener(e -> forwardVideo());
        fullscreenButton.addActionListener(e -> toggleFullscreen());
        
        // Timer pour la progression
        Timer progressTimer = new Timer(1000, e -> updateProgress());
        progressTimer.start();
    }
    
    // Méthodes de contrôle
    public void loadVideo(Video video) {
        this.currentVideo = video;
        
        if (video == null || video.getChemin() == null) {
            resetDisplay();
            return;
        }
        
        // Mettre à jour l'affichage
        titleLabel.setText("🎬 TECOX - " + video.getTitre());
        videoDisplay.setText("🎬 " + video.getTitre());
        
        // Vérifier le fichier
        File videoFile = new File(video.getChemin());
        if (videoFile.exists()) {
            long fileSize = videoFile.length();
            double fileSizeMB = fileSize / (1024.0 * 1024.0);
            
            infoLabel.setText(String.format("📁 %s | 📏 %.1f MB | 👁️ %d vues | ⏱️ %s sec", 
                videoFile.getName(), fileSizeMB, video.getVues(), formatDuration(video.getDuree())));
            
            // Simuler le chargement
            totalTime = video.getDuree();
            currentTime = 0;
            updateProgressBar();
            
            System.out.println("✅ TECOX - Vidéo chargée: " + video.getTitre());
        } else {
            infoLabel.setText("❌ Fichier introuvable: " + video.getChemin());
            videoDisplay.setText("❌ Fichier introuvable");
        }
        
        // Réinitialiser l'état
        isPlaying = false;
        updatePlayPauseButton();
    }
    
    public void togglePlayPause() {
        if (currentVideo == null) {
            JOptionPane.showMessageDialog(this, 
                "Aucune vidéo chargée dans TECOX", 
                "TECOX Player", 
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
        if (currentVideo == null) return;
        
        try {
            // Vérifier d'abord si le fichier existe
            File videoFile = new File(currentVideo.getChemin());
            if (!videoFile.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ TECOX - Fichier vidéo introuvable:\n" + currentVideo.getChemin(), 
                    "Erreur TECOX", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ouvrir avec le lecteur système
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(videoFile);
                isPlaying = true;
                updatePlayPauseButton();
                System.out.println("🎬 TECOX - Lecture lancée: " + currentVideo.getTitre());
                System.out.println("📂 Fichier: " + videoFile.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ TECOX - Lecteur système non supporté", 
                    "Erreur TECOX", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ TECOX - Erreur de lecture: " + e.getMessage() + "\nFichier: " + currentVideo.getChemin(), 
                "Erreur TECOX", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public void pauseVideo() {
        isPlaying = false;
        updatePlayPauseButton();
        System.out.println("⏸️ TECOX - Pause: " + (currentVideo != null ? currentVideo.getTitre() : "Aucune vidéo"));
    }
    
    public void stopVideo() {
        isPlaying = false;
        currentTime = 0;
        updatePlayPauseButton();
        updateProgressBar();
        System.out.println("⏹️ TECOX - Arrêt: " + (currentVideo != null ? currentVideo.getTitre() : "Aucune vidéo"));
    }
    
    public void rewindVideo() {
        if (currentTime > 10) {
            currentTime -= 10;
        } else {
            currentTime = 0;
        }
        updateProgressBar();
        System.out.println("⏪ TECOX - Retour rapide: -10s");
    }
    
    public void forwardVideo() {
        if (currentTime < totalTime - 10) {
            currentTime += 10;
        } else {
            currentTime = totalTime;
        }
        updateProgressBar();
        System.out.println("⏩ TECOX - Avance rapide: +10s");
    }
    
    public void toggleFullscreen() {
        JOptionPane.showMessageDialog(this, 
            "🖥️ TECOX - Mode plein écran\n(Utilisez le lecteur système pour le plein écran)", 
            "TECOX Player", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updatePlayPauseButton() {
        if (isPlaying) {
            playPauseButton.setText("⏸️");
            playPauseButton.setBackground(com.vidora.view.MainView.HOVER_BG);
        } else {
            playPauseButton.setText("▶️");
            playPauseButton.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        }
    }
    
    private void updateProgress() {
        if (isPlaying && currentTime < totalTime) {
            currentTime++;
            updateProgressBar();
        }
    }
    
    private void updateProgressBar() {
        if (totalTime > 0) {
            int progress = (int) ((double) currentTime / totalTime * 100);
            progressBar.setValue(progress);
            progressBar.setString(formatTime(currentTime) + " / " + formatTime(totalTime));
            timeLabel.setText("⏱️ " + formatTime(currentTime));
        }
    }
    
    private void resetDisplay() {
        titleLabel.setText("🎬 TECOX - Aucune vidéo chargée");
        videoDisplay.setText("🎬 TECOX - Lecteur Vidéo");
        infoLabel.setText("📁 Chargement en attente...");
        progressBar.setValue(0);
        progressBar.setString("00:00 / 00:00");
        timeLabel.setText("⏱️ 00:00");
        currentTime = 0;
        totalTime = 0;
        isPlaying = false;
        updatePlayPauseButton();
    }
    
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }
    
    private String formatDuration(int seconds) {
        return formatTime(seconds);
    }
    
    private Icon createTecoxIcon() {
        int width = 120;
        int height = 120;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fond noir comme demandé
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        
        // Bordure blanche pour voir l'icône
        g2d.setColor(Color.WHITE);
        g2d.drawRect(2, 2, width-4, height-4);
        
        // Cercle TECOX en rouge bien visible
        g2d.setColor(new Color(255, 0, 0)); // Rouge pur
        g2d.fillOval(20, 20, 80, 80);
        
        // Texte TECOX en blanc très visible
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "TECOX";
        int textX = (width - fm.stringWidth(text)) / 2;
        int textY = height / 2 + fm.getAscent() / 2 + 10;
        g2d.drawString(text, textX, textY);
        
        // Bouton play en blanc très visible
        g2d.setColor(Color.WHITE);
        int[] xPoints = {width/2 - 15, width/2 - 15, width/2 + 15};
        int[] yPoints = {height/2 - 15, height/2 + 15, height/2};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Ajouter un effet de brillance
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.fillOval(25, 25, 70, 70);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
}
