package com.vidora.view.playlists.dialogs;

import com.vidora.controller.MainController;
import com.vidora.model.Video;
import com.vidora.model.Playlist;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Dialogue pour gérer les vidéos d'une playlist
 */
public class PlaylistVideosDialog extends JDialog {
    private MainController controller;
    private int playlistId;
    private String playlistName;
    private boolean hasChanges = false;
    
    // Composants UI
    private JTable videosTable;
    private DefaultTableModel tableModel;
    private JButton btnAddVideo;
    private JButton btnRemoveVideo;
    private JButton btnClose;
    private JLabel infoLabel;
    
    // Données
    private Playlist playlist;
    private ArrayList<Video> availableVideos;
    
    public PlaylistVideosDialog(JFrame parent, String playlistName, int playlistId, MainController controller) {
        super(parent, "Gestion des vidéos - " + playlistName, true);
        this.playlistName = playlistName;
        this.playlistId = playlistId;
        this.controller = controller;
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadData();
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(600, 400));
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.PANEL_BG);
        getContentPane().setBackground(com.vidora.view.MainView.PANEL_BG);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Header avec informations
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("📋 " + playlistName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        
        infoLabel = new JLabel("Chargement...");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(180, 180, 180));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(infoLabel, BorderLayout.CENTER);
        
        // Panneau central avec tableau
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        centerPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // Barre d'outils
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbarPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        toolbarPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        btnAddVideo = createActionButton("➕ Ajouter une vidéo", new Color(76, 175, 80));
        btnRemoveVideo = createActionButton("➖ Retirer la vidéo", new Color(244, 67, 54));
        btnRemoveVideo.setEnabled(false);
        
        toolbarPanel.add(btnAddVideo);
        toolbarPanel.add(btnRemoveVideo);
        toolbarPanel.add(Box.createHorizontalGlue());
        
        // Tableau des vidéos
        JPanel tablePanel = createTablePanel();
        
        centerPanel.add(toolbarPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Panneau de boutons en bas
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        bottomPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        bottomPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        btnClose = new JButton("Fermer");
        btnClose.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        btnClose.setForeground(Color.WHITE);
        btnClose.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        
        bottomPanel.add(btnClose);
        
        // Assembler la fenêtre
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        // Modèle de tableau
        String[] columns = {"ID", "Titre", "Catégorie", "Durée", "Vues"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 4) return Integer.class;
                return String.class;
            }
        };
        
        // Tableau
        videosTable = new JTable(tableModel);
        videosTable.setBackground(new Color(45, 45, 55));
        videosTable.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        videosTable.setSelectionBackground(com.vidora.view.MainView.ACCENT_COLOR);
        videosTable.setSelectionForeground(Color.WHITE);
        videosTable.setGridColor(new Color(60, 60, 70));
        videosTable.setRowHeight(30);
        videosTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        videosTable.getTableHeader().setReorderingAllowed(false);
        
        // En-tête personnalisé
        javax.swing.table.JTableHeader header = videosTable.getTableHeader();
        header.setBackground(new Color(50, 50, 60));
        header.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 80)));
        
        // Largeurs de colonnes
        videosTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        videosTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Titre
        videosTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Catégorie
        videosTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Durée
        videosTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Vues
        
        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(videosTable);
        scrollPane.setBackground(com.vidora.view.MainView.PANEL_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Sélection dans le tableau
        videosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // Actions des boutons
        btnAddVideo.addActionListener(e -> addVideoToPlaylist());
        btnRemoveVideo.addActionListener(e -> removeVideoFromPlaylist());
    }
    
    private void updateButtonStates() {
        int selectedRow = videosTable.getSelectedRow();
        boolean hasSelection = selectedRow >= 0;
        
        btnRemoveVideo.setEnabled(hasSelection);
    }
    
    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Charger la playlist avec ses vidéos
                playlist = controller.getPlaylistAvecVideos(playlistId);
                
                // Charger toutes les vidéos disponibles
                availableVideos = controller.getAllVideos();
                
                SwingUtilities.invokeLater(() -> {
                    updateInfoLabel();
                    updateTable();
                });
                
                return null;
            }
        };
        
        worker.execute();
    }
    
    private void updateInfoLabel() {
        if (playlist != null) {
            int videoCount = playlist.getNombreVideos();
            String totalDuration = playlist.getDureeTotaleFormatee();
            infoLabel.setText(videoCount + " vidéo(s) • Durée totale : " + totalDuration);
        }
    }
    
    private void updateTable() {
        tableModel.setRowCount(0);
        
        if (playlist != null && playlist.getVideos() != null) {
            for (Video video : playlist.getVideos()) {
                Object[] row = {
                        video.getId(),
                        video.getTitre(),
                        video.getCategorie(),
                        video.getDureeFormatee(),
                        video.getVues()
                };
                tableModel.addRow(row);
            }
        }
        
        updateButtonStates();
    }
    
    private void addVideoToPlaylist() {
        // Créer une boîte de dialogue pour sélectionner une vidéo
        JDialog selectDialog = new JDialog(this, "Ajouter une vidéo à la playlist", true);
        selectDialog.setSize(600, 500);
        selectDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Liste des vidéos disponibles
        DefaultTableModel listModel = new DefaultTableModel(new String[]{"ID", "Titre", "Catégorie", "Durée"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };
        
        // Filtrer les vidéos qui ne sont pas déjà dans la playlist
        for (Video video : availableVideos) {
            boolean alreadyInPlaylist = false;
            for (Video playlistVideo : playlist.getVideos()) {
                if (playlistVideo.getId() == video.getId()) {
                    alreadyInPlaylist = true;
                    break;
                }
            }
            
            if (!alreadyInPlaylist) {
                Object[] row = {
                        video.getId(),
                        video.getTitre(),
                        video.getCategorie(),
                        video.getDureeFormatee()
                };
                listModel.addRow(row);
            }
        }
        
        JTable listTable = new JTable(listModel);
        listTable.setBackground(new Color(45, 45, 55));
        listTable.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        listTable.setSelectionBackground(com.vidora.view.MainView.ACCENT_COLOR);
        listTable.setSelectionForeground(Color.WHITE);
        listTable.setGridColor(new Color(60, 60, 70));
        listTable.setRowHeight(25);
        listTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JScrollPane listScrollPane = new JScrollPane(listTable);
        listScrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70), 1));
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(new Color(158, 158, 158));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> selectDialog.dispose());
        
        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            int selectedRow = listTable.getSelectedRow();
            if (selectedRow >= 0) {
                int videoId = (Integer) listModel.getValueAt(selectedRow, 0);
                if (controller.ajouterVideoAPlaylist(playlistId, videoId)) {
                    controller.showSuccess("Succès", "Vidéo ajoutée à la playlist !");
                    hasChanges = true;
                    loadData(); // Recharger les données
                    selectDialog.dispose();
                }
            } else {
                controller.showError("Erreur", "Veuillez sélectionner une vidéo.");
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);
        
        mainPanel.add(new JLabel("Sélectionnez une vidéo à ajouter :"), BorderLayout.NORTH);
        mainPanel.add(listScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        selectDialog.setContentPane(mainPanel);
        selectDialog.setVisible(true);
    }
    
    private void removeVideoFromPlaylist() {
        int selectedRow = videosTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        int modelRow = videosTable.convertRowIndexToModel(selectedRow);
        int videoId = (Integer) tableModel.getValueAt(modelRow, 0);
        String videoTitle = (String) tableModel.getValueAt(modelRow, 1);
        
        if (controller.confirm("Confirmation de retrait",
                "Êtes-vous sûr de vouloir retirer la vidéo \"" + videoTitle + "\" de la playlist ?")) {
            
            if (controller.supprimerVideoDePlaylist(playlistId, videoId)) {
                controller.showSuccess("Succès", "Vidéo retirée de la playlist !");
                hasChanges = true;
                loadData(); // Recharger les données
            }
        }
    }
    
    public boolean hasChanges() {
        return hasChanges;
    }
}
