package com.vidora.view.statistics;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Panneau des statistiques avec graphiques et analyses
 */
public class StatisticsPanel extends JPanel {
    private MainController controller;
    
    // Composants UI
    private StatCard totalVideosLabel;
    private StatCard totalPlaylistsLabel;
    private StatCard totalCategoriesLabel;
    private StatCard totalViewsLabel;
    
    private JPanel topVideosPanel;
    private JPanel categoriesPanel;
    private JPanel trendsPanel;
    
    // Données
    private ArrayList<Video> topVideos;
    private Map<String, Integer> categoryStats;
    
    public StatisticsPanel(MainController controller) {
        this.controller = controller;
        this.topVideos = new ArrayList<>();
        this.categoryStats = new HashMap<>();
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        refreshData();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.PANEL_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }
    
    private void layoutComponents() {
        // Panneau principal avec scroll
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(com.vidora.view.MainView.PANEL_BG);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Titre
        JLabel titleLabel = createTitle("📈 Statistiques et Analyses");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Cartes statistiques principales
        JPanel mainStatsPanel = createMainStatsPanel();
        mainStatsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(mainStatsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Panneau des vidéos populaires
        topVideosPanel = createTopVideosPanel();
        topVideosPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(topVideosPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Panneau des catégories
        categoriesPanel = createCategoriesPanel();
        categoriesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(categoriesPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Panneau des tendances
        trendsPanel = createTrendsPanel();
        trendsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(trendsPanel);
        
        scrollPane.setViewportView(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JLabel createTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        return title;
    }
    
    private JPanel createMainStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Cartes statistiques
        totalVideosLabel = createStatCard("🎬 Vidéos totales", "0", new Color(33, 150, 243));
        totalPlaylistsLabel = createStatCard("📋 Playlists", "0", new Color(76, 175, 80));
        totalCategoriesLabel = createStatCard("🏷️ Catégories", "0", new Color(255, 152, 0));
        totalViewsLabel = createStatCard("👁️ Vues totales", "0", new Color(156, 39, 176));
        
        panel.add(totalVideosLabel);
        panel.add(totalPlaylistsLabel);
        panel.add(totalCategoriesLabel);
        panel.add(totalViewsLabel);
        
        return panel;
    }
    
    private StatCard createStatCard(String title, String value, Color color) {
        return new StatCard(title, value, color);
    }
    
    /**
     * Carte statistique personnalisée
     */
    private static class StatCard extends JPanel {
        private JLabel valueLabel;
        
        public StatCard(String title, String value, Color color) {
            setBackground(com.vidora.view.MainView.CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1),
                    new EmptyBorder(20, 20, 20, 20)
            ));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setLayout(new BorderLayout());
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            titleLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
            
            valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            valueLabel.setForeground(color);
            
            add(titleLabel, BorderLayout.NORTH);
            add(valueLabel, BorderLayout.CENTER);
            
            // Effet hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(com.vidora.view.MainView.PANEL_BG);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(com.vidora.view.MainView.CARD_BG);
                }
            });
        }
        
        public void setText(String text) {
            valueLabel.setText(text);
        }
    }
    
    private JPanel createTopVideosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel("🔥 Top 10 des vidéos les plus vues");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Liste des vidéos
        JPanel videosListPanel = new JPanel();
        videosListPanel.setLayout(new BoxLayout(videosListPanel, BoxLayout.Y_AXIS));
        videosListPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        JScrollPane scrollPane = new JScrollPane(videosListPanel);
        scrollPane.setBackground(com.vidora.view.MainView.PANEL_BG);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel("📊 Répartition par catégorie");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Graphique des catégories
        JPanel chartPanel = createCategoryChart();
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCategoryChart() {
        JPanel panel = new JPanel();
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        return panel;
    }
    
    private JPanel createTrendsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel("📈 Informations supplémentaires");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Informations détaillées
        JPanel infoPanel = createInfoPanel();
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        // Cartes d'information
        panel.add(createInfoCard("📅 Date d'ajout", "Plus récente", "En cours..."));
        panel.add(createInfoCard("⏱️ Durée moyenne", "Calcul en cours...", "En cours..."));
        panel.add(createInfoCard("🎯 Catégorie populaire", "En cours...", "En cours..."));
        panel.add(createInfoCard("📊 Taux de lecture", "Calcul en cours...", "En cours..."));
        
        return panel;
    }
    
    private JPanel createInfoCard(String title, String value, String subtitle) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(com.vidora.view.MainView.CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        subtitleLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(com.vidora.view.MainView.CARD_BG);
        textPanel.add(valueLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        cardPanel.add(textPanel, BorderLayout.CENTER);
        
        return cardPanel;
    }
    
    private void setupEventHandlers() {
        // Les clics sur les cartes pourront afficher des détails
        totalVideosLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(StatisticsPanel.this, 
                        "Navigation vers la section vidéos", 
                        "Navigation", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        totalPlaylistsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(StatisticsPanel.this, 
                        "Navigation vers la section playlists", 
                        "Navigation", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    public void refreshData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Charger les statistiques principales
                int totalVideos = controller.getNombreTotalVideos();
                int totalPlaylists = controller.getNombreTotalPlaylists();
                int totalCategories = controller.getCategoriesDisponibles().size();
                
                // Calculer le total des vues
                ArrayList<Video> allVideos = controller.getAllVideos();
                int totalViews = allVideos.stream().mapToInt(Video::getVues).sum();
                
                // Charger les vidéos populaires
                topVideos = controller.getVideosPlusVues(10);
                
                // Calculer les statistiques par catégorie
                categoryStats.clear();
                for (Video video : allVideos) {
                    String category = video.getCategorie();
                    categoryStats.put(category, categoryStats.getOrDefault(category, 0) + 1);
                }
                
                // Mettre à jour l'UI sur l'EDT
                SwingUtilities.invokeLater(() -> {
                    updateMainStats(totalVideos, totalPlaylists, totalCategories, totalViews);
                    updateTopVideos();
                    updateCategoriesChart();
                    updateAdditionalInfo(allVideos);
                });
                
                return null;
            }
        };
        
        worker.execute();
    }
    
    private void updateMainStats(int videos, int playlists, int categories, int views) {
        totalVideosLabel.setText(String.valueOf(videos));
        totalPlaylistsLabel.setText(String.valueOf(playlists));
        totalCategoriesLabel.setText(String.valueOf(categories));
        totalViewsLabel.setText(formatNumber(views));
    }
    
    private void updateTopVideos() {
        JPanel videosListPanel = (JPanel) ((JScrollPane) topVideosPanel.getComponent(1)).getViewport().getView();
        videosListPanel.removeAll();
        
        if (topVideos.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucune vidéo disponible");
            emptyLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            videosListPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < topVideos.size(); i++) {
                Video video = topVideos.get(i);
                VideoRankItem item = new VideoRankItem(i + 1, video);
                item.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.getPreferredSize().height));
                videosListPanel.add(item);
                
                if (i < topVideos.size() - 1) {
                    videosListPanel.add(Box.createVerticalStrut(8));
                }
            }
        }
        
        videosListPanel.revalidate();
        videosListPanel.repaint();
    }
    
    private void updateCategoriesChart() {
        JPanel chartPanel = (JPanel) categoriesPanel.getComponent(1);
        chartPanel.removeAll();
        
        if (categoryStats.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucune catégorie disponible");
            emptyLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chartPanel.add(emptyLabel);
        } else {
            // Trouver la valeur maximale pour le calcul des pourcentages
            int maxCount = categoryStats.values().stream().mapToInt(Integer::intValue).max().orElse(1);
            
            for (Map.Entry<String, Integer> entry : categoryStats.entrySet()) {
                String category = entry.getKey();
                int count = entry.getValue();
                double percentage = (double) count / maxCount * 100;
                
                CategoryBar bar = new CategoryBar(category, count, percentage);
                bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, bar.getPreferredSize().height));
                chartPanel.add(bar);
                chartPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    
    private void updateAdditionalInfo(ArrayList<Video> allVideos) {
        JPanel infoPanel = (JPanel) ((JPanel) trendsPanel.getComponent(1)).getComponent(0);
        
        // Calculer les informations supplémentaires
        if (!allVideos.isEmpty()) {
            // Vidéo la plus récente
            Video mostRecent = allVideos.stream()
                    .max((v1, v2) -> v1.getDateAjout().compareTo(v2.getDateAjout()))
                    .orElse(null);
            
            // Durée moyenne
            double avgDuration = allVideos.stream()
                    .mapToInt(Video::getDuree)
                    .average()
                    .orElse(0.0);
            
            // Catégorie la plus populaire
            String popularCategory = categoryStats.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
            
            // Mettre à jour les cartes d'information
            // (Implementation simplifiée - pourrait être améliorée avec des composants dynamiques)
        }
    }
    
    private String formatNumber(int number) {
        if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000.0);
        } else if (number >= 1000) {
            return String.format("%.1fK", number / 1000.0);
        }
        return String.valueOf(number);
    }
    
    /**
     * Item vidéo pour le classement
     */
    private static class VideoRankItem extends JPanel {
        public VideoRankItem(int rank, Video video) {
            setBackground(com.vidora.view.MainView.CARD_BG);
            setBorder(new EmptyBorder(12, 15, 12, 15));
            setLayout(new BorderLayout());
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Rang
            JLabel rankLabel = new JLabel("#" + rank);
            rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            rankLabel.setForeground(rank <= 3 ? new Color(255, 215, 0) : com.vidora.view.MainView.ACCENT_COLOR);
            rankLabel.setPreferredSize(new Dimension(50, 0));
            
            // Informations vidéo
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(com.vidora.view.MainView.CARD_BG);
            
            JLabel titleLabel = new JLabel(video.getTitre());
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            titleLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
            
            JLabel detailsLabel = new JLabel(video.getCategorie() + " • " + video.getVues() + " vues");
            detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            detailsLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
            
            infoPanel.add(titleLabel, BorderLayout.NORTH);
            infoPanel.add(detailsLabel, BorderLayout.CENTER);
            
            add(rankLabel, BorderLayout.WEST);
            add(infoPanel, BorderLayout.CENTER);
            
            // Effet hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(com.vidora.view.MainView.PANEL_BG);
                    infoPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(com.vidora.view.MainView.CARD_BG);
                    infoPanel.setBackground(com.vidora.view.MainView.CARD_BG);
                }
            });
        }
    }
    
    /**
     * Barre de catégorie pour le graphique
     */
    private static class CategoryBar extends JPanel {
        public CategoryBar(String category, int count, double percentage) {
            setBackground(com.vidora.view.MainView.CARD_BG);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(8, 15, 8, 15));
            
            // Nom de la catégorie et nombre
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            labelPanel.setBackground(com.vidora.view.MainView.CARD_BG);
            
            JLabel categoryLabel = new JLabel(category);
            categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            categoryLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
            
            JLabel countLabel = new JLabel("(" + count + ")");
            countLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            countLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
            
            labelPanel.add(categoryLabel);
            labelPanel.add(Box.createHorizontalStrut(8));
            labelPanel.add(countLabel);
            
            // Barre de progression
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue((int) percentage);
            progressBar.setStringPainted(false);
            progressBar.setBackground(com.vidora.view.MainView.PANEL_BG);
            progressBar.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
            progressBar.setBorder(BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1));
            progressBar.setPreferredSize(new Dimension(0, 8));
            
            add(labelPanel, BorderLayout.WEST);
            add(progressBar, BorderLayout.CENTER);
        }
    }
}
