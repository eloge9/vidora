package com.vidora.view.videos;

import com.vidora.controller.MainController;
import com.vidora.model.Video;
import com.vidora.view.components.VideoCard;
import com.vidora.view.components.YouTubeButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Panneau vidéos style YouTube avec grille de cartes
 */
public class YouTubeVideosPanel extends JPanel {
    private MainController controller;
    
    // Composants
    private JPanel videosGrid;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private YouTubeButton btnAdd;
    private YouTubeButton btnRefresh;
    
    // Données
    private ArrayList<Video> videos;
    
    public YouTubeVideosPanel(MainController controller) {
        this.controller = controller;
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadVideos();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.DARK_BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }
    
    private void layoutComponents() {
        // Header YouTube
        add(createHeader(), BorderLayout.NORTH);
        
        // Grille de vidéos YouTube - 3 colonnes
        videosGrid = new JPanel(new GridLayout(0, 3, 16, 16)); // 0 lignes = auto, 3 colonnes, espacement 16px
        videosGrid.setBackground(com.vidora.view.MainView.DARK_BG);
        
        scrollPane = new JScrollPane(videosGrid);
        scrollPane.setBackground(com.vidora.view.MainView.DARK_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));
        
        // Personnaliser les scrollbars style YouTube
        customizeScrollbars(scrollPane);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 12));
        headerPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        // Titre section
        JLabel titleLabel = new JLabel("VIDORA - Bibliothèque de Vidéos");
        titleLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        
        // Barre de recherche et filtres
        JPanel searchPanel = createSearchPanel();
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(0, 8));
        searchPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        
        // Panel recherche et filtres
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filtersPanel.setBackground(com.vidora.view.MainView.DARK_BG);
        
        // Champ de recherche style YouTube
        searchField = new JTextField(25);
        searchField.setBackground(com.vidora.view.MainView.CARD_BG);
        searchField.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(com.vidora.view.MainView.CARD_BG, 1),
                new EmptyBorder(10, 16, 10, 16)
        ));
        searchField.setFont(new Font("Roboto", Font.PLAIN, 14));
        searchField.setToolTipText("Rechercher des vidéos...");
        
        // Filtre catégorie
        categoryFilter = new JComboBox<>();
        categoryFilter.setBackground(com.vidora.view.MainView.CARD_BG);
        categoryFilter.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        categoryFilter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(com.vidora.view.MainView.CARD_BG, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        categoryFilter.setFont(new Font("Roboto", Font.PLAIN, 14));
        categoryFilter.setPreferredSize(new Dimension(150, searchField.getPreferredSize().height));
        
        // Boutons d'action
        btnAdd = new YouTubeButton("➕ Ajouter");
        btnRefresh = new YouTubeButton("🔄 Actualiser");
        
        filtersPanel.add(searchField);
        filtersPanel.add(categoryFilter);
        filtersPanel.add(Box.createHorizontalStrut(20));
        filtersPanel.add(btnAdd);
        filtersPanel.add(btnRefresh);
        
        searchPanel.add(filtersPanel, BorderLayout.CENTER);
        
        return searchPanel;
    }
    
    private void setupEventHandlers() {
        // Recherche en temps réel
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterVideos(); }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterVideos(); }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterVideos(); }
        });
        
        // Filtre par catégorie
        categoryFilter.addActionListener(e -> filterVideos());
        
        // Boutons d'action
        btnAdd.addActionListener(e -> addVideo());
        btnRefresh.addActionListener(e -> loadVideos());
    }
    
    private void loadVideos() {
        try {
            videos = controller.getVideoService().getAllVideos();
            populateVideosGrid();
            populateCategoryFilter();
        } catch (Exception e) {
            controller.showError("Erreur", "Impossible de charger les vidéos: " + e.getMessage());
        }
    }
    
    private void populateVideosGrid() {
        videosGrid.removeAll();
        
        for (Video video : videos) {
            VideoCard card = new VideoCard(video, (JFrame) SwingUtilities.getWindowAncestor(this), controller);
            videosGrid.add(card);
        }
        
        videosGrid.revalidate();
        videosGrid.repaint();
    }
    
    private void populateCategoryFilter() {
        categoryFilter.removeAllItems();
        categoryFilter.addItem("Toutes catégories");
        
        // Ajouter les catégories uniques
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
        
        videosGrid.removeAll();
        
        for (Video video : videos) {
            boolean matchesSearch = searchText.isEmpty() || 
                                   video.getTitre().toLowerCase().contains(searchText);
            boolean matchesCategory = selectedCategory.equals("Toutes catégories") || 
                                     video.getCategorie().equals(selectedCategory);
            
            if (matchesSearch && matchesCategory) {
                VideoCard card = new VideoCard(video, (JFrame) SwingUtilities.getWindowAncestor(this), controller);
                videosGrid.add(card);
            }
        }
        
        videosGrid.revalidate();
        videosGrid.repaint();
    }
    
    private void addVideo() {
        // Ouvrir le dialogue d'ajout
        // TODO: Implémenter l'ajout de vidéo
        JOptionPane.showMessageDialog(this, "Ajout de vidéo à implémenter", 
                                   "Fonctionnalité", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void customizeScrollbars(JScrollPane scrollPane) {
        // Personnaliser l'apparence des scrollbars style YouTube
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(com.vidora.view.MainView.DARK_BG);
        verticalScrollBar.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
        
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setBackground(com.vidora.view.MainView.DARK_BG);
        horizontalScrollBar.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        horizontalScrollBar.setBorder(BorderFactory.createEmptyBorder());
    }
    
    /**
     * Layout personnalisé pour les cartes style YouTube
     */
    private static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }
        
        @Override
        public Dimension preferredLayoutSize(Container target) {
            // FlowLayout standard mais avec retour à la ligne automatique
            return super.preferredLayoutSize(target);
        }
    }
}
