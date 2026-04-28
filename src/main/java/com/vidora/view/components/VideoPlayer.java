package com.vidora.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Lecteur vidéo intégré pour VIDORA
 * Contrôles de lecture complets avec interface moderne
 */
public class VideoPlayer extends JPanel {
    // Composants du lecteur
    private JPanel videoPanel;
    private JLabel videoPlaceholder;
    private JPanel controlsPanel;
    private JButton playPauseButton;
    private JButton stopButton;
    private JButton rewindButton;
    private JButton forwardButton;
    private JProgressBar progressBar;
    private JLabel timeLabel;
    private JSlider volumeSlider;
    private JButton fullscreenButton;
    private JButton muteButton;
    
    // État du lecteur
    private boolean isPlaying = false;
    private boolean isMuted = false;
    private boolean isFullscreen = false;
    private float volume = 1.0f;
    private int currentTime = 0;
    private int totalTime = 0;
    private String currentVideoPath = "";
    private String currentVideoTitle = "";
    
    // Timer pour la mise à jour
    private Timer updateTimer;
    
    public VideoPlayer() {
        initComponent();
        layoutComponents();
        setupEventHandlers();
        setupUpdateTimer();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.PANEL_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    private void layoutComponents() {
        // Panneau vidéo principal
        videoPanel = createVideoPanel();
        add(videoPanel, BorderLayout.CENTER);
        
        // Panneau de contrôles
        controlsPanel = createControlsPanel();
        add(controlsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createVideoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.DARK_BG);
        panel.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        
        // Placeholder pour la vidéo (remplacé par le lecteur réel)
        videoPlaceholder = new JLabel("🎬", SwingConstants.CENTER);
        videoPlaceholder.setFont(new Font("Segoe UI", Font.BOLD, 48));
        videoPlaceholder.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        videoPlaceholder.setBackground(com.vidora.view.MainView.DARK_BG);
        videoPlaceholder.setOpaque(true);
        
        // Info de la vidéo
        JLabel infoLabel = new JLabel("Aucune vidéo chargée", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(videoPlaceholder, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        // Taille minimale pour la vidéo
        panel.setPreferredSize(new Dimension(640, 360));
        panel.setMinimumSize(new Dimension(400, 225));
        
        return panel;
    }
    
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Barre de progression et temps
        JPanel progressPanel = createProgressPanel();
        panel.add(progressPanel, BorderLayout.NORTH);
        
        // Contrôles principaux
        JPanel mainControlsPanel = createMainControlsPanel();
        panel.add(mainControlsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProgressPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Barre de progression
        progressBar = new JProgressBar();
        progressBar.setBackground(com.vidora.view.MainView.PANEL_BG);
        progressBar.setForeground(com.vidora.view.MainView.SUCCESS_COLOR);
        progressBar.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        progressBar.setStringPainted(true);
        progressBar.setString("00:00 / 00:00");
        progressBar.setPreferredSize(new Dimension(0, 8));
        
        panel.add(progressBar, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMainControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        // Contrôles de lecture
        JPanel playbackPanel = createPlaybackControls();
        panel.add(playbackPanel, BorderLayout.WEST);
        
        // Contrôles de volume et plein écran
        JPanel volumePanel = createVolumeControls();
        panel.add(volumePanel, BorderLayout.EAST);
        
        // Espace flexible
        panel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPlaybackControls() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        // Bouton play/pause
        playPauseButton = createControlButton("▶️", com.vidora.view.MainView.SUCCESS_COLOR);
        playPauseButton.setPreferredSize(new Dimension(40, 40));
        
        // Bouton stop
        stopButton = createControlButton("⏹️", com.vidora.view.MainView.ERROR_COLOR);
        stopButton.setPreferredSize(new Dimension(40, 40));
        
        // Boutons rewind/forward
        rewindButton = createControlButton("⏪", com.vidora.view.MainView.ACCENT_COLOR);
        rewindButton.setPreferredSize(new Dimension(35, 35));
        
        forwardButton = createControlButton("⏩", com.vidora.view.MainView.ACCENT_COLOR);
        forwardButton.setPreferredSize(new Dimension(35, 35));
        
        panel.add(playPauseButton);
        panel.add(stopButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(rewindButton);
        panel.add(forwardButton);
        
        return panel;
    }
    
    private JPanel createVolumeControls() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        // Bouton mute
        muteButton = createControlButton("🔊", com.vidora.view.MainView.PURPLE_COLOR);
        muteButton.setPreferredSize(new Dimension(35, 35));
        
        // Slider de volume
        volumeSlider = new JSlider(0, 100, 100);
        volumeSlider.setPreferredSize(new Dimension(100, 25));
        volumeSlider.setBackground(com.vidora.view.MainView.PANEL_BG);
        volumeSlider.setForeground(com.vidora.view.MainView.PURPLE_COLOR);
        volumeSlider.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        
        // Bouton plein écran
        fullscreenButton = createControlButton("⛶", com.vidora.view.MainView.BUTTON_HOVER);
        fullscreenButton.setPreferredSize(new Dimension(35, 35));
        
        panel.add(muteButton);
        panel.add(volumeSlider);
        panel.add(fullscreenButton);
        
        return panel;
    }
    
    private JButton createControlButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Play/Pause
        playPauseButton.addActionListener(e -> togglePlayPause());
        
        // Stop
        stopButton.addActionListener(e -> stopVideo());
        
        // Rewind/Forward
        rewindButton.addActionListener(e -> rewind());
        forwardButton.addActionListener(e -> forward());
        
        // Mute
        muteButton.addActionListener(e -> toggleMute());
        
        // Volume
        volumeSlider.addChangeListener(e -> updateVolume());
        
        // Plein écran
        fullscreenButton.addActionListener(e -> toggleFullscreen());
        
        // Progress bar click
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (totalTime > 0) {
                    int x = e.getX();
                    int width = progressBar.getWidth();
                    int newTime = (x * totalTime) / width;
                    seekTo(newTime);
                }
            }
        });
    }
    
    private void setupUpdateTimer() {
        updateTimer = new Timer(1000, e -> updateProgress());
        updateTimer.start();
    }
    
    // Méthodes de contrôle du lecteur
    public void loadVideo(String videoPath, String title) {
        this.currentVideoPath = videoPath;
        this.currentVideoTitle = title;
        
        // Mettre à jour l'affichage
        videoPlaceholder.setText("🎬 " + title);
        videoPlaceholder.setForeground(new Color(100, 200, 255));
        
        // Réinitialiser l'état
        currentTime = 0;
        totalTime = 300; // Simulé 5 minutes pour l'exemple
        isPlaying = false;
        updatePlayPauseButton();
        
        // Ici, vous intégrerez votre lecteur vidéo réel
        // Par exemple avec JavaFX Media ou VLCJ
        System.out.println("Chargement de la vidéo: " + videoPath);
    }
    
    public void play() {
        if (!currentVideoPath.isEmpty()) {
            isPlaying = true;
            updatePlayPauseButton();
            System.out.println("Lecture de: " + currentVideoTitle);
        }
    }
    
    public void pause() {
        isPlaying = false;
        updatePlayPauseButton();
        System.out.println("Pause");
    }
    
    public void stop() {
        isPlaying = false;
        currentTime = 0;
        updatePlayPauseButton();
        updateProgress();
        System.out.println("Stop");
    }
    
    public void togglePlayPause() {
        if (isPlaying) {
            pause();
        } else {
            play();
        }
    }
    
    private void stopVideo() {
        stop();
    }
    
    public void rewind() {
        if (currentTime > 10) {
            currentTime -= 10;
        } else {
            currentTime = 0;
        }
        updateProgress();
        System.out.println("Recul 10s");
    }
    
    public void forward() {
        if (currentTime < totalTime - 10) {
            currentTime += 10;
        } else {
            currentTime = totalTime;
        }
        updateProgress();
        System.out.println("Avance 10s");
    }
    
    private void seekTo(int time) {
        currentTime = time;
        updateProgress();
        System.out.println("Seek to: " + time + "s");
    }
    
    public void toggleMute() {
        isMuted = !isMuted;
        muteButton.setText(isMuted ? "🔇" : "🔊");
        volumeSlider.setEnabled(!isMuted);
        System.out.println("Mute: " + isMuted);
    }
    
    private void updateVolume() {
        volume = volumeSlider.getValue() / 100.0f;
        System.out.println("Volume: " + (int)(volume * 100) + "%");
    }
    
    public void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        fullscreenButton.setText(isFullscreen ? "⛶" : "⛶");
        System.out.println("Fullscreen: " + isFullscreen);
        
        // Implémenter le mode plein écran ici
        if (isFullscreen) {
            // Passer en plein écran
            enterFullscreen();
        } else {
            // Sortir du plein écran
            exitFullscreen();
        }
    }
    
    private void enterFullscreen() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.dispose();
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        }
    }
    
    private void exitFullscreen() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.dispose();
            frame.setUndecorated(false);
            frame.setExtendedState(JFrame.NORMAL);
            frame.setVisible(true);
        }
    }
    
    private void updatePlayPauseButton() {
        playPauseButton.setText(isPlaying ? "⏸️" : "▶️");
    }
    
    private void updateProgress() {
        if (isPlaying && currentTime < totalTime) {
            currentTime++;
        }
        
        // Mettre à jour la barre de progression
        if (totalTime > 0) {
            int progress = (currentTime * 100) / totalTime;
            progressBar.setValue(progress);
        }
        
        // Mettre à jour le temps
        String currentTimeStr = formatTime(currentTime);
        String totalTimeStr = formatTime(totalTime);
        progressBar.setString(currentTimeStr + " / " + totalTimeStr);
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
    
    // Getters pour l'état
    public boolean isPlaying() {
        return isPlaying;
    }
    
    public String getCurrentVideoPath() {
        return currentVideoPath;
    }
    
    public String getCurrentVideoTitle() {
        return currentVideoTitle;
    }
    
    public void cleanup() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }
}
