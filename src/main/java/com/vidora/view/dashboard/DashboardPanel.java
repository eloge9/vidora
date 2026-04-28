package com.vidora.view.dashboard;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Panneau du dashboard avec cartes statistiques
 */
public class DashboardPanel extends JPanel {
    private MainController controller;
    
    // Cartes statistiques
    private StatCard videosCard;
    private StatCard playlistsCard;
    private StatCard categoriesCard;
    private JPanel topVideosPanel;
    
    public DashboardPanel(MainController controller) {
        this.controller = controller;
        initComponent();
        layoutComponents();
        setupEventHandlers();
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.PANEL_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));
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
        JLabel titleLabel = createTitle("📊 Tableau de Bord");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Panneau des cartes statistiques
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Créer les cartes
        videosCard = new StatCard("🎬 Vidéos", "0", "Total des vidéos");
        playlistsCard = new StatCard("📋 Playlists", "0", "Total des playlists");
        categoriesCard = new StatCard("🏷️ Catégories", "0", "Catégories uniques");
        
        statsPanel.add(videosCard);
        statsPanel.add(playlistsCard);
        statsPanel.add(categoriesCard);
        
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(40));
        
        // Panneau des vidéos populaires
        topVideosPanel = createTopVideosPanel();
        topVideosPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(topVideosPanel);
        
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
    
    private JPanel createTopVideosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(com.vidora.view.MainView.PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Titre
        JLabel titleLabel = new JLabel("🔥 Top 5 des vidéos les plus vues");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Liste des vidéos
        JPanel videosListPanel = new JPanel();
        videosListPanel.setLayout(new BoxLayout(videosListPanel, BoxLayout.Y_AXIS));
        videosListPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        // Placeholder pour les vidéos (sera rempli dans refreshData)
        for (int i = 0; i < 5; i++) {
            VideoItem item = new VideoItem(i + 1, null);
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.getPreferredSize().height));
            videosListPanel.add(item);
            if (i < 4) {
                videosListPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane videosScrollPane = new JScrollPane(videosListPanel);
        videosScrollPane.setBackground(com.vidora.view.MainView.PANEL_BG);
        videosScrollPane.setBorder(null);
        videosScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        videosScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(videosScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Les clics sur les cartes pourront naviguer vers les sections correspondantes
        videosCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Naviguer vers la section vidéos
                JOptionPane.showMessageDialog(DashboardPanel.this, 
                        "Navigation vers la section vidéos", 
                        "Navigation", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        playlistsCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Naviguer vers la section playlists
                JOptionPane.showMessageDialog(DashboardPanel.this, 
                        "Navigation vers la section playlists", 
                        "Navigation", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    public void refreshData() {
        // Mettre à jour les statistiques
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                int totalVideos = controller.getNombreTotalVideos();
                int totalPlaylists = controller.getNombreTotalPlaylists();
                int totalCategories = controller.getCategoriesDisponibles().size();
                
                // Mettre à jour les cartes sur l'EDT
                SwingUtilities.invokeLater(() -> {
                    videosCard.setValue(String.valueOf(totalVideos));
                    playlistsCard.setValue(String.valueOf(totalPlaylists));
                    categoriesCard.setValue(String.valueOf(totalCategories));
                });
                
                // Récupérer les vidéos populaires
                ArrayList<Video> topVideos = controller.getVideosPlusVues(5);
                
                SwingUtilities.invokeLater(() -> {
                    updateTopVideos(topVideos);
                });
                
                return null;
            }
        };
        
        worker.execute();
    }
    
    private void updateTopVideos(ArrayList<Video> videos) {
        JPanel videosListPanel = (JPanel) ((JScrollPane) topVideosPanel.getComponent(1)).getViewport().getView();
        videosListPanel.removeAll();
        
        if (videos.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucune vidéo disponible");
            emptyLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            videosListPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < Math.min(5, videos.size()); i++) {
                Video video = videos.get(i);
                VideoItem item = new VideoItem(i + 1, video);
                item.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.getPreferredSize().height));
                videosListPanel.add(item);
                
                if (i < Math.min(4, videos.size() - 1)) {
                    videosListPanel.add(Box.createVerticalStrut(10));
                }
            }
        }
        
        videosListPanel.revalidate();
        videosListPanel.repaint();
    }
    
    /**
     * Carte statistique personnalisée
     */
    private static class StatCard extends JPanel {
        private JLabel valueLabel;
        private JLabel titleLabel;
        
        public StatCard(String emoji, String value, String title) {
            setBackground(new Color(50, 50, 60));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 80), 1),
                    new EmptyBorder(20, 20, 20, 20)
            ));
            setLayout(new BorderLayout());
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Emoji et titre
            JLabel emojiLabel = new JLabel(emoji);
            emojiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            
            titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            titleLabel.setForeground(new Color(180, 180, 180));
            
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            titlePanel.setBackground(new Color(50, 50, 60));
            titlePanel.add(emojiLabel);
            titlePanel.add(Box.createHorizontalStrut(10));
            titlePanel.add(titleLabel);
            
            // Valeur
            valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            valueLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
            
            add(titlePanel, BorderLayout.NORTH);
            add(valueLabel, BorderLayout.CENTER);
            
            // Effet hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(60, 60, 70));
                    titlePanel.setBackground(new Color(60, 60, 70));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(50, 50, 60));
                    titlePanel.setBackground(new Color(50, 50, 60));
                }
            });
        }
        
        public void setValue(String value) {
            valueLabel.setText(value);
        }
    }
    
    /**
     * Item vidéo pour le top des vidéos
     */
    private static class VideoItem extends JPanel {
        public VideoItem(int rank, Video video) {
            setBackground(new Color(45, 45, 55));
            setBorder(new EmptyBorder(15, 15, 15, 15));
            setLayout(new BorderLayout());
            
            // Rang
            JLabel rankLabel = new JLabel("#" + rank);
            rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            rankLabel.setForeground(com.vidora.view.MainView.ACCENT_COLOR);
            rankLabel.setPreferredSize(new Dimension(40, 0));
            
            // Informations vidéo
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(new Color(45, 45, 55));
            
            if (video != null) {
                JLabel titleLabel = new JLabel(video.getTitre());
                titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                titleLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
                
                JLabel detailsLabel = new JLabel(video.getCategorie() + " • " + video.getVues() + " vues");
                detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                detailsLabel.setForeground(new Color(160, 160, 160));
                
                infoPanel.add(titleLabel, BorderLayout.NORTH);
                infoPanel.add(detailsLabel, BorderLayout.CENTER);
            } else {
                JLabel placeholderLabel = new JLabel("Chargement...");
                placeholderLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                placeholderLabel.setForeground(new Color(120, 120, 120));
                infoPanel.add(placeholderLabel, BorderLayout.CENTER);
            }
            
            add(rankLabel, BorderLayout.WEST);
            add(infoPanel, BorderLayout.CENTER);
            
            // Effet hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(55, 55, 65));
                    infoPanel.setBackground(new Color(55, 55, 65));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(45, 45, 55));
                    infoPanel.setBackground(new Color(45, 45, 55));
                }
            });
        }
    }
}
