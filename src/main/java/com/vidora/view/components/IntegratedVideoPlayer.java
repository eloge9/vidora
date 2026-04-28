package com.vidora.view.components;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Lecteur vidéo intégré dans VIDORA - Utilise le lecteur PC mais intégré à l'interface
 */
public class IntegratedVideoPlayer extends JPanel {
    private MainController controller;
    private Video currentVideo;
    private boolean isPlaying = false;
    private int currentTime = 0;
    private int totalTime = 0;
    
    // Composants intégrés
    private JPanel videoPanel;
    private JLabel videoDisplay;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JPanel controlsPanel;
    private JButton btnPlay, btnStop, btnOpenPC, btnRefresh;
    private JProgressBar progressBar;
    private JLabel timeLabel;
    private JSlider volumeSlider;
    
    // Playlist
    private JTable playlistTable;
    private DefaultTableModel playlistModel;
    private ArrayList<Video> playlistVideos;
    private int currentVideoIndex = 0;
    
    public IntegratedVideoPlayer(MainController controller) {
        this.controller = controller;
        this.playlistVideos = new ArrayList<>();
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadPlaylist();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
    }
    
    private void layoutComponents() {
        // Titre intégré
        titleLabel = new JLabel("🎬 VIDORA Player - Lecteur PC Intégré", SwingConstants.CENTER);
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        add(titleLabel, BorderLayout.NORTH);
        
        // Zone vidéo intégrée
        videoPanel = createIntegratedVideoPanel();
        add(videoPanel, BorderLayout.CENTER);
        
        // Contrôles intégrés
        controlsPanel = createIntegratedControlsPanel();
        add(controlsPanel, BorderLayout.SOUTH);
        
        // Playlist intégrée
        JScrollPane playlistScroll = createIntegratedPlaylistPanel();
        add(playlistScroll, BorderLayout.EAST);
    }
    
    private JPanel createIntegratedVideoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        panel.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 2));
        
        // Zone d'affichage vidéo
        videoDisplay = new JLabel("🎬 Aucune vidéo sélectionnée", SwingConstants.CENTER);
        videoDisplay.setBackground(com.vidora.view.MainView.DARK_BG);
        videoDisplay.setOpaque(true);
        videoDisplay.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        videoDisplay.setFont(new Font("Roboto", Font.BOLD, 14));
        videoDisplay.setPreferredSize(new Dimension(0, 350));
        
        // Informations vidéo
        infoLabel = new JLabel("📂 Sélectionnez une vidéo pour commencer", SwingConstants.CENTER);
        infoLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        infoLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        infoLabel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        panel.add(videoDisplay, BorderLayout.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createIntegratedControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Barre de progression
        JPanel progressPanel = new JPanel(new BorderLayout(0, 5));
        progressPanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground(com.vidora.view.MainView.DARK_BG);
        progressBar.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        progressBar.setStringPainted(true);
        progressBar.setString("00:00 / 00:00");
        progressBar.setPreferredSize(new Dimension(0, 20));
        
        timeLabel = new JLabel("00:00");
        timeLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        timeLabel.setFont(new Font("Roboto", Font.BOLD, 12));
        
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(timeLabel, BorderLayout.EAST);
        
        // Boutons de contrôle intégrés
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonsPanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        btnPlay = createIntegratedButton("▶", Color.GREEN);
        btnStop = createIntegratedButton("⏹", Color.RED);
        btnOpenPC = createIntegratedButton("🖥️ PC", new Color(0, 191, 255)); // Bleu
        btnRefresh = createIntegratedButton("🔄", com.vidora.view.MainView.PURPLE_COLOR);
        
        buttonsPanel.add(btnPlay);
        buttonsPanel.add(btnStop);
        buttonsPanel.add(btnOpenPC);
        buttonsPanel.add(btnRefresh);
        
        // Volume intégré
        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        volumePanel.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        
        JLabel volumeIcon = new JLabel("🔊");
        volumeIcon.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        
        volumeSlider = new JSlider(0, 100, 75);
        volumeSlider.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        volumeSlider.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        
        volumePanel.add(volumeIcon);
        volumePanel.add(volumeSlider);
        
        // Assemblage
        panel.add(progressPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(volumePanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JScrollPane createIntegratedPlaylistPanel() {
        // Créer le modèle de table
        String[] columns = {"📹 Titre", "⏱️ Durée", "👁️ Vues"};
        playlistModel = new DefaultTableModel(columns, 0);
        
        // Créer le tableau
        playlistTable = new JTable(playlistModel);
        playlistTable.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        playlistTable.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        playlistTable.setSelectionBackground(com.vidora.view.MainView.ACCENT_COLOR);
        playlistTable.setSelectionForeground(Color.BLACK);
        playlistTable.setGridColor(com.vidora.view.MainView.SIDEBAR_BG);
        playlistTable.setRowHeight(25);
        playlistTable.setFont(new Font("Roboto", Font.PLAIN, 12));
        playlistTable.getTableHeader().setReorderingAllowed(false);
        
        // Style en-tête
        playlistTable.getTableHeader().setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        playlistTable.getTableHeader().setForeground(com.vidora.view.MainView.TEXT_COLOR);
        playlistTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12));
        
        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(playlistTable);
        scrollPane.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        scrollPane.getViewport().setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        scrollPane.setPreferredSize(new Dimension(280, 0));
        
        return scrollPane;
    }
    
    private JButton createIntegratedButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(60, 35));
        
        // Effet hover intégré
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Contrôles vidéo intégrés
        btnPlay.addActionListener(e -> togglePlayPause());
        btnStop.addActionListener(e -> stopVideo());
        btnOpenPC.addActionListener(e -> openWithPCPlayer());
        btnRefresh.addActionListener(e -> refreshPlaylist());
        
        // Double-clic sur la playlist
        playlistTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    playSelectedVideo();
                }
            }
        });
        
        // Timer pour la progression
        Timer progressTimer = new Timer(1000, e -> updateProgress());
        progressTimer.start();
    }
    
    private void loadPlaylist() {
        try {
            playlistVideos = controller.getVideoService().getAllVideos();
            populatePlaylistTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void populatePlaylistTable() {
        playlistModel.setRowCount(0);
        for (Video video : playlistVideos) {
            Object[] row = {
                video.getTitre(),
                formatDuration(video.getDuree()),
                video.getVues() + " vues"
            };
            playlistModel.addRow(row);
        }
    }
    
    private void togglePlayPause() {
        if (currentVideo == null && !playlistVideos.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Aucune vidéo sélectionnée\n\n📋 Double-cliquez sur une vidéo dans la playlist", 
                "VIDORA Player", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (currentVideo == null && !playlistVideos.isEmpty()) {
            currentVideo = playlistVideos.get(0);
            currentVideoIndex = 0;
            loadCurrentVideo();
        }
        
        if (isPlaying) {
            pauseVideo();
        } else {
            playVideo();
        }
    }
    private void playVideo() {
        if (currentVideo == null) return;
        
        try {
            File videoFile = new File(currentVideo.getChemin());
            // Vérifier si Desktop est supporté
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Lecteur système non disponible", 
                    "VIDORA Player - Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Obtenir le lecteur par défaut du PC
            Desktop desktop = Desktop.getDesktop();
            
            // Afficher les informations
            long fileSize = videoFile.length();
            double fileSizeMB = fileSize / (1024.0 * 1024.0);
            
            System.out.println("🎬 VIDORA Player - Lecture avec lecteur PC intégré:");
            System.out.println("📂 Fichier: " + videoFile.getAbsolutePath());
            System.out.println("📏 Taille: " + String.format("%.1f MB", fileSizeMB));
            System.out.println("🎬 Titre: " + currentVideo.getTitre());
            
            desktop.open(videoFile);
            
            isPlaying = true;
            updatePlayButton();
            totalTime = currentVideo.getDuree();
            currentTime = 0;
            
            // Mettre à jour l'affichage intégré
            videoDisplay.setText("🎬 Lecture en cours: " + currentVideo.getTitre());
            videoDisplay.setForeground(new Color(0, 255, 0)); // Vert pour lecture
            
            infoLabel.setText(String.format("📂 %s | 📏 %.1f MB | 🎬 %s", 
                videoFile.getName(), fileSizeMB, currentVideo.getTitre()));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Erreur de lecture avec lecteur PC intégré:\n" + e.getMessage() + "\n\n" +
                "📂 Fichier: " + currentVideo.getChemin(), 
                "VIDORA Player - Erreur", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void pauseVideo() {
        isPlaying = false;
        updatePlayButton();
        System.out.println("⏸️ VIDORA Player - Pause: " + (currentVideo != null ? currentVideo.getTitre() : ""));
    }
    
    private void stopVideo() {
        isPlaying = false;
        currentTime = 0;
        updatePlayButton();
        updateProgressBar();
        
        if (currentVideo != null) {
            videoDisplay.setText("🎬 Arrêté: " + currentVideo.getTitre());
            videoDisplay.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        }
        
        System.out.println("⏹️ VIDORA Player - Arrêt: " + (currentVideo != null ? currentVideo.getTitre() : ""));
    }
    
    private void openWithPCPlayer() {
        if (currentVideo == null) {
            JOptionPane.showMessageDialog(this, 
                "Aucune vidéo sélectionnée\n\n📋 Double-cliquez sur une vidéo dans la playlist", 
                "VIDORA Player", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        playVideo(); // Utilise la même méthode de lecture
    }
    
    private void refreshPlaylist() {
        loadPlaylist();
        JOptionPane.showMessageDialog(this, 
            "🔄 Playlist rafraîchie", 
            "VIDORA Player", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void playSelectedVideo() {
        int selectedRow = playlistTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = playlistTable.convertRowIndexToModel(selectedRow);
        currentVideoIndex = modelRow;
        currentVideo = playlistVideos.get(currentVideoIndex);
        loadCurrentVideo();
        playVideo();
    }
    
    private void loadCurrentVideo() {
        if (currentVideo != null) {
            videoDisplay.setText("🎬 Prêt: " + currentVideo.getTitre());
            videoDisplay.setForeground(com.vidora.view.MainView.TEXT_COLOR);
            titleLabel.setText("🎬 VIDORA Player - " + currentVideo.getTitre());
            totalTime = currentVideo.getDuree();
            currentTime = 0;
            updateProgressBar();
            
            // Sélectionner dans la table
            playlistTable.setRowSelectionInterval(currentVideoIndex, currentVideoIndex);
        }
    }
    
    private void updatePlayButton() {
        if (isPlaying) {
            btnPlay.setText("⏸");
            btnPlay.setBackground(new Color(255, 165, 0)); // Orange pour pause
        } else {
            btnPlay.setText("▶");
            btnPlay.setBackground(Color.GREEN); // Vert pour play
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
            timeLabel.setText(formatTime(currentTime));
        }
    }
    
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%d:%02d", minutes, secs);
        }
    }
    
    private String formatDuration(int seconds) {
        return formatTime(seconds);
    }
}
