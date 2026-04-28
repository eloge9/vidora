package com.vidora.view.player;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Lecteur vidéo complet et fonctionnel - Style Dark Mode
 * Architecture : Zone centrale + Barre de contrôle + Playlist
 */
public class CompleteVideoPlayer extends JFrame {
    private MainController controller;
    private Video currentVideo;
    private boolean isPlaying = false;
    private int currentTime = 0;
    private int totalTime = 0;
    
    // Composants principaux
    private JPanel videoPanel;
    private JPanel controlsPanel;
    private JPanel playlistPanel;
    private JLabel videoDisplay;
    private JProgressBar progressBar;
    private JLabel timeLabel;
    private JSlider volumeSlider;
    private JTable playlistTable;
    private DefaultTableModel playlistModel;
    
    // Boutons de contrôle
    private JButton btnPlay;
    private JButton btnStop;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnRefresh;
    
    // Données
    private ArrayList<Video> playlistVideos;
    private int currentVideoIndex = 0;
    
    public CompleteVideoPlayer(MainController controller) {
        this.controller = controller;
        this.playlistVideos = new ArrayList<>();
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadPlaylist();
        
        setTitle("COMPUTECH Video Player - Mode Dark");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initComponent() {
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 10));
    }
    
    private void layoutComponents() {
        // Zone titre
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Zone centrale (vidéo + playlist)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(700);
        splitPane.setResizeWeight(0.7);
        
        // Zone vidéo
        videoPanel = createVideoPanel();
        splitPane.setLeftComponent(videoPanel);
        
        // Playlist
        playlistPanel = createPlaylistPanel();
        splitPane.setRightComponent(playlistPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Barre de contrôle
        controlsPanel = createControlsPanel();
        add(controlsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.BLACK);
        
        JLabel title = new JLabel("🎬 COMPUTECH Video Player");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        
        panel.add(title);
        return panel;
    }
    
    private JPanel createVideoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        
        // Zone d'affichage vidéo
        videoDisplay = new JLabel("🎬 Aucune vidéo sélectionnée", SwingConstants.CENTER);
        videoDisplay.setBackground(new Color(20, 20, 20)); // Noir très profond
        videoDisplay.setForeground(Color.WHITE);
        videoDisplay.setFont(new Font("Arial", Font.BOLD, 16));
        videoDisplay.setPreferredSize(new Dimension(0, 400));
        
        panel.add(videoDisplay, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createPlaylistPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        panel.setPreferredSize(new Dimension(280, 0));
        
        // Titre playlist
        JLabel playlistTitle = new JLabel("📋 Playlist");
        playlistTitle.setForeground(Color.WHITE);
        playlistTitle.setFont(new Font("Arial", Font.BOLD, 14));
        playlistTitle.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        // Tableau playlist
        String[] columns = {"Titre", "Durée", "Vues"};
        playlistModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        
        playlistTable = new JTable(playlistModel);
        playlistTable.setBackground(new Color(26, 26, 26)); // Gris foncé
        playlistTable.setForeground(new Color(0, 191, 255)); // Bleu #00BFFF
        playlistTable.setSelectionBackground(new Color(255, 165, 0)); // Orange
        playlistTable.setSelectionForeground(Color.BLACK);
        playlistTable.setGridColor(new Color(40, 40, 40));
        playlistTable.setRowHeight(25);
        playlistTable.setFont(new Font("Arial", Font.PLAIN, 12));
        playlistTable.getTableHeader().setReorderingAllowed(false);
        
        // Style en-tête
        JTableHeader header = playlistTable.getTableHeader();
        header.setBackground(new Color(40, 40, 40));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(playlistTable);
        scrollPane.setBackground(new Color(26, 26, 26));
        scrollPane.getViewport().setBackground(new Color(26, 26, 26));
        
        panel.add(playlistTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.BLACK);
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));
        
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
        
        // Boutons de contrôle
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(Color.BLACK);
        
        btnPrevious = creerBouton("⏮", Color.ORANGE);
        btnPlay = creerBouton("▶", Color.GREEN);
        btnStop = creerBouton("⏹", Color.RED);
        btnNext = creerBouton("⏭", Color.ORANGE);
        
        buttonsPanel.add(btnPrevious);
        buttonsPanel.add(btnPlay);
        buttonsPanel.add(btnStop);
        buttonsPanel.add(btnNext);
        
        // Volume et actions
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 5));
        bottomPanel.setBackground(Color.BLACK);
        
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
        
        // Boutons d'action
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.BLACK);
        
        btnAdd = creerBouton("Ajouter", Color.GREEN);
        btnEdit = creerBouton("Modifier", Color.ORANGE);
        btnRefresh = creerBouton("Actualiser", new Color(30, 144, 255)); // Bleu
        
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnRefresh);
        
        bottomPanel.add(volumePanel, BorderLayout.WEST);
        bottomPanel.add(actionPanel, BorderLayout.EAST);
        
        panel.add(progressPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton creerBouton(String texte, Color couleurBordure) {
        JButton btn = new JButton(texte);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE); // Texte en blanc
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        // On applique la bordure de couleur demandée
        btn.setBorder(BorderFactory.createLineBorder(couleurBordure, 2));
        btn.setPreferredSize(new Dimension(60, 40));
        
        // Effet hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(40, 40, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.BLACK);
            }
        });
        
        return btn;
    }
    
    private void setupEventHandlers() {
        // Contrôles vidéo
        btnPlay.addActionListener(e -> togglePlayPause());
        btnStop.addActionListener(e -> stopVideo());
        btnNext.addActionListener(e -> nextVideo());
        btnPrevious.addActionListener(e -> previousVideo());
        
        // Actions playlist
        btnAdd.addActionListener(e -> addVideo());
        btnEdit.addActionListener(e -> editVideo());
        btnRefresh.addActionListener(e -> loadPlaylist());
        
        // Sélection dans la playlist
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
                    "Fichier vidéo introuvable:\n" + currentVideo.getChemin(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Desktop.getDesktop().open(videoFile);
            isPlaying = true;
            updatePlayButton();
            totalTime = currentVideo.getDuree();
            currentTime = 0;
            
            System.out.println("Lecture: " + currentVideo.getTitre());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur de lecture: " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void pauseVideo() {
        isPlaying = false;
        updatePlayButton();
        System.out.println("Pause: " + (currentVideo != null ? currentVideo.getTitre() : ""));
    }
    
    private void stopVideo() {
        isPlaying = false;
        currentTime = 0;
        updatePlayButton();
        updateProgressBar();
        System.out.println("Arrêt: " + (currentVideo != null ? currentVideo.getTitre() : ""));
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
            totalTime = currentVideo.getDuree();
            currentTime = 0;
            updateProgressBar();
            
            // Sélectionner dans la table
            playlistTable.setRowSelectionInterval(currentVideoIndex, currentVideoIndex);
            playlistTable.scrollRectToVisible(playlistTable.getCellRect(currentVideoIndex, 0, true));
        }
    }
    
    private void addVideo() {
        // Implémenter l'ajout de vidéo
        JOptionPane.showMessageDialog(this, "Fonctionnalité d'ajout à implémenter", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editVideo() {
        // Implémenter l'édition de vidéo
        JOptionPane.showMessageDialog(this, "Fonctionnalité d'édition à implémenter", "Information", JOptionPane.INFORMATION_MESSAGE);
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
