package com.vidora.view.videos;

import com.vidora.controller.MainController;
import com.vidora.model.Video;
import com.vidora.view.videos.dialogs.VideoDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Panneau de gestion des vidéos avec interface moderne et boutons noir/orange
 */
public class VideosPanel extends JPanel {
    private MainController controller;
    
    // Composants
    private JTable videosTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    
    // Boutons
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnPlay;
    private JButton btnRefresh;
    
    public VideosPanel(MainController controller) {
        this.controller = controller;
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadData();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }
    
    private void layoutComponents() {
        // Panel de recherche et filtres
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        // Panel du tableau
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        // Champ de recherche
        searchField = new JTextField(20);
        searchField.setBackground(com.vidora.view.MainView.CARD_BG);
        searchField.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        searchField.setFont(new Font("Roboto", Font.PLAIN, 13));
        searchField.setToolTipText("Rechercher par titre...");
        
        // Filtre par catégorie
        categoryFilter = new JComboBox<>();
        categoryFilter.setBackground(com.vidora.view.MainView.CARD_BG);
        categoryFilter.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        categoryFilter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        categoryFilter.setFont(new Font("Roboto", Font.PLAIN, 13));
        categoryFilter.setPreferredSize(new Dimension(150, searchField.getPreferredSize().height));
        
        // Boutons d'action avec fond noir/orange
        btnAdd = createActionButton("➕ Ajouter", com.vidora.view.MainView.SUCCESS_COLOR);
        btnEdit = createActionButton("✏️ Modifier", new Color(255, 152, 0));
        btnDelete = createActionButton("🗑️ Supprimer", com.vidora.view.MainView.ERROR_COLOR);
        btnPlay = createActionButton("▶️ Lire", com.vidora.view.MainView.SUCCESS_COLOR);
        btnRefresh = createActionButton("🔄 Actualiser", com.vidora.view.MainView.PURPLE_COLOR);
        
        JPanel searchPanelLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanelLeft.setBackground(com.vidora.view.MainView.PANEL_BG);
        searchPanelLeft.add(new JLabel("🔍"));
        searchPanelLeft.add(searchField);
        searchPanelLeft.add(new JLabel("🏷️"));
        searchPanelLeft.add(categoryFilter);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnEdit);
        buttonsPanel.add(btnDelete);
        buttonsPanel.add(btnPlay);
        buttonsPanel.add(btnRefresh);
        
        panel.add(searchPanelLeft, BorderLayout.WEST);
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        // Créer le modèle de tableau
        String[] columns = {"ID", "Titre", "Catégorie", "Durée", "Vues", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };
        
        // Créer le tableau avec fond noir
        videosTable = new JTable(tableModel);
        videosTable.setBackground(Color.BLACK); // Fond noir comme demandé
        videosTable.setForeground(Color.WHITE); // Texte blanc pour contraste maximal
        videosTable.setSelectionBackground(new Color(255, 140, 0)); // Orange pour sélection
        videosTable.setSelectionForeground(Color.BLACK); // Texte noir sur sélection
        videosTable.setGridColor(new Color(80, 80, 80)); // Gris pour lignes de grille
        videosTable.setRowHeight(32);
        videosTable.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Police moderne
        videosTable.getTableHeader().setReorderingAllowed(false);
        
        // Configurer le sorter
        sorter = new TableRowSorter<>(tableModel);
        videosTable.setRowSorter(sorter);
        
        // Personnaliser l'en-tête
        JTableHeader header = videosTable.getTableHeader();
        header.setBackground(new Color(40, 40, 40)); // Gris foncé pour en-tête
        header.setForeground(Color.WHITE); // Texte blanc pour contraste
        header.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Police plus grande et gras
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 140, 0))); // Bordure orange
        
        // Ajuster les largeurs de colonnes
        videosTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        videosTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Titre
        videosTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Catégorie
        videosTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Durée
        videosTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Vues
        videosTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Date
        
        // ScrollPane avec fond noir
        JScrollPane scrollPane = new JScrollPane(videosTable);
        scrollPane.setBackground(Color.BLACK); // Fond noir pour le scrollpane
        scrollPane.getViewport().setBackground(Color.BLACK); // Fond noir pour la vue
        scrollPane.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        
        // Fond noir ou orange selon le type de bouton
        if (text.contains("Ajouter") || text.contains("Lire")) {
            button.setBackground(Color.BLACK); // Fond noir
            button.setForeground(Color.ORANGE); // Texte orange
            button.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        } else if (text.contains("Supprimer")) {
            button.setBackground(Color.BLACK); // Fond noir
            button.setForeground(new Color(255, 100, 100)); // Texte rouge clair
            button.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        } else if (text.contains("Modifier") || text.contains("Actualiser")) {
            button.setBackground(Color.ORANGE); // Fond orange
            button.setForeground(Color.BLACK); // Texte noir
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        } else {
            button.setBackground(Color.BLACK); // Fond noir par défaut
            button.setForeground(Color.WHITE); // Texte blanc
            button.setBorder(BorderFactory.createLineBorder(color, 2));
        }
        
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(button.getBackground().equals(Color.BLACK) ? Color.ORANGE : Color.BLACK, 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        button.setFont(new Font("Roboto", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getBackground().equals(Color.BLACK)) {
                    button.setBackground(new Color(40, 40, 40)); // Gris foncé
                } else {
                    button.setBackground(new Color(255, 180, 0)); // Orange clair
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getBackground().equals(new Color(40, 40, 40))) {
                    button.setBackground(Color.BLACK);
                } else if (button.getBackground().equals(new Color(255, 180, 0))) {
                    button.setBackground(Color.ORANGE);
                }
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Recherche
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterVideos();
            }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterVideos();
            }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterVideos();
            }
        });
        
        // Filtre par catégorie
        categoryFilter.addActionListener(e -> filterVideos());
        
        // Sélection dans le tableau
        videosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // Double clic pour lire la vidéo
        videosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    playSelectedVideo();
                }
            }
        });
        
        // Actions des boutons
        btnAdd.addActionListener(e -> addVideo());
        btnEdit.addActionListener(e -> editSelectedVideo());
        btnDelete.addActionListener(e -> deleteSelectedVideo());
        btnPlay.addActionListener(e -> playSelectedVideo());
        btnRefresh.addActionListener(e -> refreshData());
    }
    
    private void loadData() {
        try {
            ArrayList<Video> videos = controller.getVideoService().getAllVideos();
            populateTable(videos);
            populateCategoryFilter(videos);
        } catch (Exception e) {
            controller.showError("Erreur", "Impossible de charger les vidéos: " + e.getMessage());
        }
    }
    
    private void populateTable(ArrayList<Video> videos) {
        tableModel.setRowCount(0);
        for (Video video : videos) {
            Object[] row = {
                video.getId(),
                video.getTitre(),
                video.getCategorie(),
                formatDuration(video.getDuree()),
                video.getVues(),
                video.getDateAjout()
            };
            tableModel.addRow(row);
        }
    }
    
    private void populateCategoryFilter(ArrayList<Video> videos) {
        categoryFilter.removeAllItems();
        categoryFilter.addItem("Toutes catégories");
        
        java.util.Set<String> categories = new java.util.HashSet<>();
        for (Video video : videos) {
            categories.add(video.getCategorie());
        }
        
        for (String category : categories) {
            categoryFilter.addItem(category);
        }
    }
    
    private void filterVideos() {
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        
        if (searchText.isEmpty() && selectedCategory.equals("Toutes catégories")) {
            sorter.setRowFilter(null);
        } else {
            // Filtre simple
            if (!searchText.isEmpty()) {
                sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + searchText, 1));
            } else if (!selectedCategory.equals("Toutes catégories")) {
                sorter.setRowFilter(javax.swing.RowFilter.regexFilter(selectedCategory, 2));
            } else {
                sorter.setRowFilter(null);
            }
        }
    }
    
    private void updateButtonStates() {
        boolean hasSelection = videosTable.getSelectedRow() != -1;
        btnEdit.setEnabled(hasSelection);
        btnDelete.setEnabled(hasSelection);
        btnPlay.setEnabled(hasSelection);
    }
    
    private void addVideo() {
        VideoDialog dialog = new VideoDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                "Ajouter une vidéo", null, controller);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Video newVideo = dialog.getVideo();
            if (controller.ajouterVideo(newVideo)) {
                controller.showSuccess("Succès", "Vidéo ajoutée avec succès !");
                // Rafraîchissement immédiat et dynamique
                refreshDataImmediate();
            }
        }
    }
    
    private void editSelectedVideo() {
        int selectedRow = videosTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = videosTable.convertRowIndexToModel(selectedRow);
        int videoId = (Integer) tableModel.getValueAt(modelRow, 0);
        
        Video video = controller.getVideoService().trouverVideoParId(videoId);
        if (video != null) {
            VideoDialog dialog = new VideoDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                    "Modifier une vidéo", video, controller);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Video updatedVideo = dialog.getVideo();
                if (controller.modifierVideo(updatedVideo)) {
                    controller.showSuccess("Succès", "Vidéo modifiée avec succès !");
                    // Rafraîchissement immédiat et dynamique
                    refreshDataImmediate();
                }
            }
        }
    }
    
    private void deleteSelectedVideo() {
        int selectedRow = videosTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = videosTable.convertRowIndexToModel(selectedRow);
        int videoId = (Integer) tableModel.getValueAt(modelRow, 0);
        String videoTitle = (String) tableModel.getValueAt(modelRow, 1);
        
        if (controller.confirm("Confirmation de suppression", 
                "Êtes-vous sûr de vouloir supprimer la vidéo \"" + videoTitle + "\" ?")) {
            
            if (controller.supprimerVideo(videoId)) {
                controller.showSuccess("Succès", "Vidéo supprimée avec succès !");
                // Rafraîchissement immédiat et dynamique
                refreshDataImmediate();
            }
        }
    }
    
    private void playSelectedVideo() {
        int selectedRow = videosTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = videosTable.convertRowIndexToModel(selectedRow);
        int videoId = (Integer) tableModel.getValueAt(modelRow, 0);
        
        Video video = controller.getVideoService().trouverVideoParId(videoId);
        if (video != null) {
            // Incrémenter le compteur de vues
            video.incrementerVues();
            controller.modifierVideo(video);
            
            // Ouvrir la vidéo avec le lecteur par défaut du système
            try {
                // Vérifier que le fichier existe
                java.io.File videoFile = new java.io.File(video.getChemin());
                if (!videoFile.exists()) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Fichier vidéo introuvable:\n" + video.getChemin(), 
                        "VIDORA - Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Vérifier que Desktop est supporté
                if (!java.awt.Desktop.isDesktopSupported()) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Lecteur système non disponible", 
                        "VIDORA - Erreur système", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Ouvrir avec le lecteur par défaut du système
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                desktop.open(videoFile);
                
                // Message de succès
                JOptionPane.showMessageDialog(this, 
                    "✅ Vidéo ouverte avec le lecteur par défaut\n\n" +
                    "🎬 Titre: " + video.getTitre() + "\n" +
                    "📂 Fichier: " + videoFile.getName(), 
                    "VIDORA", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Erreur lors de l'ouverture de la vidéo:\n" + e.getMessage() + "\n\n" +
                    "📂 Fichier: " + video.getChemin(), 
                    "VIDORA - Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        loadData();
        updateButtonStates();
    }
    
    /**
     * Rafraîchissement immédiat et dynamique des données
     * Force la mise à jour de l'interface en temps réel
     */
    public void refreshDataImmediate() {
        // Exécuter dans le thread de l'interface graphique
        SwingUtilities.invokeLater(() -> {
            try {
                // Recharger les données depuis la base
                ArrayList<Video> videos = controller.getVideoService().getAllVideos();
                
                // Vider et repeupler le tableau
                tableModel.setRowCount(0);
                for (Video video : videos) {
                    Object[] row = {
                        video.getId(),
                        video.getTitre(),
                        video.getCategorie(),
                        formatDuration(video.getDuree()),
                        video.getVues(),
                        video.getDateAjout()
                    };
                    tableModel.addRow(row);
                }
                
                // Rafraîchir le filtre de catégories
                populateCategoryFilter(videos);
                
                // Forcer le rafraîchissement de l'affichage
                tableModel.fireTableDataChanged();
                videosTable.repaint();
                
                // Mettre à jour l'état des boutons
                updateButtonStates();
                
                // Forcer le réaffichage du panneau
                revalidate();
                repaint();
                
                System.out.println("Rafraîchissement dynamique effectué : " + videos.size() + " vidéos chargées");
                
            } catch (Exception e) {
                System.err.println("Erreur lors du rafraîchissement dynamique : " + e.getMessage());
                controller.showError("Erreur", "Impossible de rafraîchir les données: " + e.getMessage());
            }
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
