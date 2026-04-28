package com.vidora.view.player;

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
 * Lecteur vidéo simple et visible - Style Dark Mode
 */
public class SimpleVideoPlayer extends JFrame {
    private MainController controller;
    private Video currentVideo;
    private boolean isPlaying = false;
    private int currentTime = 0;
    private int totalTime = 0;
    
    // Composants visibles
    private JPanel videoPanel;
    private JLabel videoDisplay;
    private JLabel titleLabel;
    private JPanel controlsPanel;
    private JButton btnPlay, btnStop, btnPrevious, btnNext;
    private JProgressBar progressBar;
    private JLabel timeLabel;
    private JSlider volumeSlider;
    
    // Playlist
    private JTable playlistTable;
    private DefaultTableModel playlistModel;
    private ArrayList<Video> playlistVideos;
    private int currentVideoIndex = 0;
    
    public SimpleVideoPlayer(MainController controller) {
        this.controller = controller;
        this.playlistVideos = new ArrayList<>();
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadPlaylist();
        
        setTitle("🎬 Simple Video Player - Dark Mode");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Rendre la fenêtre visible
        setVisible(true);
    }
    
    private void initComponent() {
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 5));
    }
    
    private void layoutComponents() {
        // Titre
        titleLabel = new JLabel("🎬 Simple Video Player", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        add(titleLabel, BorderLayout.NORTH);
        
        // Zone vidéo visible
        videoPanel = new JPanel(new BorderLayout());
        videoPanel.setBackground(new Color(20, 20, 20));
        videoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        videoDisplay = new JLabel("🎬 Aucune vidéo sélectionnée", SwingConstants.CENTER);
        videoDisplay.setForeground(Color.WHITE);
        videoDisplay.setFont(new Font("Arial", Font.BOLD, 14));
        videoDisplay.setPreferredSize(new Dimension(0, 300));
        
        videoPanel.add(videoDisplay, BorderLayout.CENTER);
        add(videoPanel, BorderLayout.CENTER);
        
        // Barre de contrôle visible
        controlsPanel = createControlsPanel();
        add(controlsPanel, BorderLayout.SOUTH);
        
        // Playlist visible
        JScrollPane playlistScroll = createPlaylistPanel();
        add(playlistScroll, BorderLayout.EAST);
    }
    
    private JScrollPane createPlaylistPanel() {
        // Créer le modèle de table
        String[] columns = {"Titre", "Durée", "Vues"};
        playlistModel = new DefaultTableModel(columns, 0);
        
        // Créer le tableau
        playlistTable = new JTable(playlistModel);
        playlistTable.setBackground(new Color(26, 26, 26));
        playlistTable.setForeground(new Color(0, 191, 255)); // Bleu
        playlistTable.setSelectionBackground(new Color(255, 165, 0)); // Orange
        playlistTable.setSelectionForeground(Color.BLACK);
        playlistTable.setGridColor(new Color(40, 40, 40));
        playlistTable.setRowHeight(25);
        playlistTable.setFont(new Font("Arial", Font.PLAIN, 12));
        playlistTable.getTableHeader().setReorderingAllowed(false);
        
        // Style en-tête
        playlistTable.getTableHeader().setBackground(new Color(40, 40, 40));
        playlistTable.getTableHeader().setForeground(Color.WHITE);
        playlistTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(playlistTable);
        scrollPane.setBackground(new Color(26, 26, 26));
        scrollPane.getViewport().setBackground(new Color(26, 26, 26));
        scrollPane.setPreferredSize(new Dimension(250, 0));
        
        return scrollPane;
    }
    
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.BLACK);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Barre de progression
        JPanel progressPanel = new JPanel(new BorderLayout(0, 5));
        progressPanel.setBackground(Color.BLACK);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground(Color.BLACK);
        progressBar.setForeground(new Color(255, 165, 0)); // Orange
        progressBar.setStringPainted(true);
        progressBar.setString("00:00 / 00:00");
        progressBar.setPreferredSize(new Dimension(0, 20));
        
        timeLabel = new JLabel("00:00");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(timeLabel, BorderLayout.EAST);
        
        // Boutons de contrôle visibles
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(Color.BLACK);
        
        btnPrevious = createControlButton("⏮", Color.ORANGE);
        btnPlay = createControlButton("▶", Color.GREEN);
        btnStop = createControlButton("⏹", Color.RED);
        btnNext = createControlButton("⏭", Color.ORANGE);
        
        buttonsPanel.add(btnPrevious);
        buttonsPanel.add(btnPlay);
        buttonsPanel.add(btnStop);
        buttonsPanel.add(btnNext);
        
        // Volume
        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        volumePanel.setBackground(Color.BLACK);
        
        JLabel volumeIcon = new JLabel("🔊");
        volumeIcon.setForeground(Color.WHITE);
        
        volumeSlider = new JSlider(0, 100, 75);
        volumeSlider.setBackground(Color.BLACK);
        volumeSlider.setForeground(new Color(255, 165, 0));
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        
        volumePanel.add(volumeIcon);
        volumePanel.add(volumeSlider);
        
        // Assemblage
        panel.add(progressPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(volumePanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton createControlButton(String text, Color borderColor) {
        JButton button = new JButton(text);
        
        // Changer les couleurs du contenu selon le type de bouton
        if (text.equals("▶") || text.equals("⏸")) {
            // Bouton Play/Pause - fond vert, texte noir
            button.setBackground(new Color(0, 255, 0)); // Vert vif
            button.setForeground(Color.BLACK); // Texte noir
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Bordure blanche
        } else if (text.equals("⏹")) {
            // Bouton Stop - fond rouge, texte blanc
            button.setBackground(new Color(255, 0, 0)); // Rouge vif
            button.setForeground(Color.WHITE); // Texte blanc
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Bordure blanche
        } else if (text.equals("⏮") || text.equals("⏭")) {
            // Boutons Previous/Next - fond orange, texte noir
            button.setBackground(new Color(255, 165, 0)); // Orange vif
            button.setForeground(Color.BLACK); // Texte noir
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Bordure blanche
        } else {
            // Bouton par défaut - fond bleu, texte blanc
            button.setBackground(new Color(0, 191, 255)); // Bleu vif
            button.setForeground(Color.WHITE); // Texte blanc
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Bordure blanche
        }
        
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(50, 35));
        
        // Effet hover plus visible
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Rendre la couleur plus claire au survol
                Color currentBg = button.getBackground();
                button.setBackground(currentBg.brighter());
                button.setForeground(Color.BLACK); // Texte noir au survol
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Restaurer les couleurs originales
                if (text.equals("▶") || text.equals("⏸")) {
                    button.setBackground(new Color(0, 255, 0));
                    button.setForeground(Color.BLACK);
                } else if (text.equals("⏹")) {
                    button.setBackground(new Color(255, 0, 0));
                    button.setForeground(Color.WHITE);
                } else if (text.equals("⏮") || text.equals("⏭")) {
                    button.setBackground(new Color(255, 165, 0));
                    button.setForeground(Color.BLACK);
                } else {
                    button.setBackground(new Color(0, 191, 255));
                    button.setForeground(Color.WHITE);
                }
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Contrôles vidéo
        btnPlay.addActionListener(e -> togglePlayPause());
        btnStop.addActionListener(e -> stopVideo());
        btnNext.addActionListener(e -> nextVideo());
        btnPrevious.addActionListener(e -> previousVideo());
        
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
            JOptionPane.showMessageDialog(this, "Aucune vidéo disponible", "Information", JOptionPane.INFORMATION_MESSAGE);
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
            if (!videoFile.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Fichier vidéo introuvable:\n" + currentVideo.getChemin(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier si Desktop est supporté
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Lecteur système non disponible", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Obtenir le lecteur par défaut du PC
            Desktop desktop = Desktop.getDesktop();
            
            // Afficher les informations du fichier
            long fileSize = videoFile.length();
            double fileSizeMB = fileSize / (1024.0 * 1024.0);
            
            System.out.println("🎬 Lancement avec lecteur PC:");
            System.out.println("📂 Fichier: " + videoFile.getAbsolutePath());
            System.out.println("📏 Taille: " + String.format("%.1f MB", fileSizeMB));
            System.out.println("🎬 Titre: " + currentVideo.getTitre());
            
            // Ouvrir avec le lecteur par défaut du PC (VLC, Windows Media Player, etc.)
            desktop.open(videoFile);
            
            isPlaying = true;
            updatePlayButton();
            totalTime = currentVideo.getDuree();
            currentTime = 0;
            
            // Mettre à jour l'affichage
            videoDisplay.setText("🎬 Lecture en cours: " + currentVideo.getTitre());
            videoDisplay.setForeground(new Color(0, 255, 0)); // Vert pour indiquer la lecture
            
            // Message de confirmation
            JOptionPane.showMessageDialog(this, 
                "✅ Vidéo lancée avec le lecteur par défaut du PC\n\n" +
                "📂 Fichier: " + videoFile.getName() + "\n" +
                "📏 Taille: " + String.format("%.1f MB", fileSizeMB) + "\n" +
                "🎬 Titre: " + currentVideo.getTitre(), 
                "Lecteur PC", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Erreur de lecture avec lecteur PC:\n" + e.getMessage() + "\n\n" +
                "📂 Fichier: " + currentVideo.getChemin(), 
                "Erreur Lecteur PC", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void pauseVideo() {
        isPlaying = false;
        updatePlayButton();
        System.out.println("⏸️ Pause: " + (currentVideo != null ? currentVideo.getTitre() : ""));
    }
    
    private void stopVideo() {
        isPlaying = false;
        currentTime = 0;
        updatePlayButton();
        updateProgressBar();
        System.out.println("⏹️ Arrêt: " + (currentVideo != null ? currentVideo.getTitre() : ""));
    }
    
    private void nextVideo() {
        if (!playlistVideos.isEmpty()) {
            currentVideoIndex = (currentVideoIndex + 1) % playlistVideos.size();
            currentVideo = playlistVideos.get(currentVideoIndex);
            loadCurrentVideo();
            if (isPlaying) {
                playVideo();
            }
        }
    }
    
    private void previousVideo() {
        if (!playlistVideos.isEmpty()) {
            currentVideoIndex = (currentVideoIndex - 1 + playlistVideos.size()) % playlistVideos.size();
            currentVideo = playlistVideos.get(currentVideoIndex);
            loadCurrentVideo();
            if (isPlaying) {
                playVideo();
            }
        }
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
            videoDisplay.setText("🎬 " + currentVideo.getTitre());
            videoDisplay.setForeground(Color.WHITE);
            titleLabel.setText("🎬 " + currentVideo.getTitre());
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
            btnPlay.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        } else {
            btnPlay.setText("▶");
            btnPlay.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
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
