package com.vidora.view.components;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Grille de cartes vidéo modernes avec miniatures et informations détaillées
 */
public class VideoCardGrid extends JPanel {
    private MainController controller;
    private ArrayList<Video> videos;
    private Video selectedVideo;
    private JScrollPane scrollPane;
    private JPanel cardsPanel;
    
    // Couleurs du design
    private static final Color CARD_BG = new Color(30, 41, 59); // #1E293B
    private static final Color CARD_HOVER = new Color(51, 65, 85); // Hover
    private static final Color CARD_SELECTED = new Color(59, 130, 246); // Sélection
    private static final Color TEXT_PRIMARY = new Color(226, 232, 240); // #E2E8F0
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184); // #94A3B8
    private static final Color ACCENT = new Color(251, 146, 60); // Orange
    
    public VideoCardGrid(MainController controller) {
        this.controller = controller;
        this.videos = new ArrayList<>();
        
        initComponent();
        layoutComponents();
        loadVideos();
    }
    
    private void initComponent() {
        setBackground(new Color(15, 23, 42)); // Fond plus sombre
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }
    
    private void layoutComponents() {
        // Titre
        JLabel titleLabel = new JLabel("🎬 Vidéos", SwingConstants.LEFT);
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panneau des cartes
        cardsPanel = new JPanel(new GridLayout(0, 2, 15, 15)); // 2 colonnes
        cardsPanel.setBackground(new Color(15, 23, 42));
        
        // ScrollPane
        scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBackground(new Color(15, 23, 42));
        scrollPane.getViewport().setBackground(new Color(15, 23, 42));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        // Style de la scrollbar
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadVideos() {
        try {
            videos = controller.getVideoService().getAllVideos();
            refreshCards();
        } catch (Exception e) {
            System.err.println("❌ Erreur de chargement des vidéos: " + e.getMessage());
        }
    }
    
    private void refreshCards() {
        cardsPanel.removeAll();
        
        for (Video video : videos) {
            VideoCard card = new VideoCard(video);
            cardsPanel.add(card);
        }
        
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
    
    /**
     * Carte vidéo individuelle avec miniature et informations
     */
    private class VideoCard extends JPanel {
        private Video video;
        private boolean isSelected = false;
        
        public VideoCard(Video video) {
            this.video = video;
            
            setLayout(new BorderLayout(15, 10));
            setBackground(CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(15, 15, 15, 15)
            ));
            setPreferredSize(new Dimension(0, 120));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            initComponents();
            setupEventHandlers();
        }
        
        private void initComponents() {
            // Panneau de gauche - Miniature
            JPanel thumbnailPanel = createThumbnailPanel();
            add(thumbnailPanel, BorderLayout.WEST);
            
            // Panneau de droite - Informations
            JPanel infoPanel = createInfoPanel();
            add(infoPanel, BorderLayout.CENTER);
            
            // Panneau de droite - Actions
            JPanel actionsPanel = createActionsPanel();
            add(actionsPanel, BorderLayout.EAST);
        }
        
        private JPanel createThumbnailPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(CARD_BG);
            panel.setPreferredSize(new Dimension(120, 70));
            
            // Miniature de la vidéo
            JLabel thumbnailLabel = new JLabel();
            thumbnailLabel.setIcon(createVideoThumbnail());
            thumbnailLabel.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
            
            // Badge de durée
            JLabel durationBadge = new JLabel(formatDuration(video.getDuree()));
            durationBadge.setBackground(new Color(0, 0, 0, 180)); // Semi-transparent
            durationBadge.setForeground(Color.WHITE);
            durationBadge.setOpaque(true);
            durationBadge.setFont(new Font("Roboto", Font.BOLD, 10));
            durationBadge.setBorder(new EmptyBorder(2, 4, 2, 4));
            
            // Positionner le badge en bas à droite de la miniature
            JPanel overlayPanel = new JPanel(new BorderLayout());
            overlayPanel.setOpaque(false);
            overlayPanel.add(thumbnailLabel, BorderLayout.CENTER);
            
            JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            badgePanel.setOpaque(false);
            badgePanel.add(durationBadge);
            overlayPanel.add(badgePanel, BorderLayout.SOUTH);
            
            panel.add(overlayPanel, BorderLayout.CENTER);
            return panel;
        }
        
        private JPanel createInfoPanel() {
            JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
            panel.setBackground(CARD_BG);
            
            // Titre
            JLabel titleLabel = new JLabel(video.getTitre());
            titleLabel.setForeground(TEXT_PRIMARY);
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
            titleLabel.setToolTipText(video.getTitre()); // Tooltip si titre trop long
            
            // Catégorie
            JLabel categoryLabel = new JLabel("📁 " + video.getCategorie());
            categoryLabel.setForeground(TEXT_SECONDARY);
            categoryLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
            
            // Vues et informations
            String infoText = String.format("👁️ %d vues • 📂 %s", 
                video.getVues(), 
                new File(video.getChemin()).getName());
            JLabel infoLabel = new JLabel(infoText);
            infoLabel.setForeground(TEXT_SECONDARY);
            infoLabel.setFont(new Font("Roboto", Font.PLAIN, 11));
            
            panel.add(titleLabel);
            panel.add(categoryLabel);
            panel.add(infoLabel);
            
            return panel;
        }
        
        private JPanel createActionsPanel() {
            JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
            panel.setBackground(CARD_BG);
            
            // Bouton Lire
            JButton playButton = createActionButton("▶️", "Lire");
            playButton.addActionListener(e -> playVideo());
            
            // Bouton Supprimer
            JButton deleteButton = createActionButton("🗑️", "Supprimer");
            deleteButton.addActionListener(e -> deleteVideo());
            
            // Bouton Playlist
            JButton playlistButton = createActionButton("📋", "Playlist");
            playlistButton.addActionListener(e -> addToPlaylist());
            
            panel.add(playButton);
            panel.add(deleteButton);
            panel.add(playlistButton);
            
            return panel;
        }
        
        private JButton createActionButton(String text, String tooltip) {
            JButton button = new JButton(text);
            button.setBackground(new Color(51, 65, 85));
            button.setForeground(TEXT_PRIMARY);
            button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            button.setFocusPainted(false);
            button.setFont(new Font("Roboto", Font.PLAIN, 12));
            button.setToolTipText(tooltip);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(ACCENT);
                    button.setForeground(Color.WHITE);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(51, 65, 85));
                    button.setForeground(TEXT_PRIMARY);
                }
            });
            
            return button;
        }
        
        private void setupEventHandlers() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        selectVideo();
                    } else if (e.getClickCount() == 2) {
                        playVideo();
                    }
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isSelected) {
                        setBackground(CARD_HOVER);
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isSelected) {
                        setBackground(CARD_BG);
                    }
                }
            });
        }
        
        private void selectVideo() {
            // Désélectionner la carte précédente
            for (Component comp : cardsPanel.getComponents()) {
                if (comp instanceof VideoCard) {
                    ((VideoCard) comp).setSelected(false);
                }
            }
            
            // Sélectionner cette carte
            setSelected(true);
            selectedVideo = video;
        }
        
        private void setSelected(boolean selected) {
            this.isSelected = selected;
            if (selected) {
                setBackground(CARD_SELECTED);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT, 2),
                    new EmptyBorder(14, 14, 14, 14)
                ));
            } else {
                setBackground(CARD_BG);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
            repaint();
        }
        
        private void playVideo() {
            selectVideo();
            
            try {
                // Vérifier que le fichier existe
                File videoFile = new File(video.getChemin());
                if (!videoFile.exists()) {
                    JOptionPane.showMessageDialog(VideoCardGrid.this, 
                        "❌ Fichier vidéo introuvable:\n" + video.getChemin(), 
                        "VIDORA - Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Vérifier que Desktop est supporté
                if (!Desktop.isDesktopSupported()) {
                    JOptionPane.showMessageDialog(VideoCardGrid.this, 
                        "❌ Lecteur système non disponible", 
                        "VIDORA - Erreur système", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Incrémenter les vues
                video.incrementerVues();
                controller.modifierVideo(video);
                
                // Ouvrir avec le lecteur par défaut
                Desktop desktop = Desktop.getDesktop();
                desktop.open(videoFile);
                
                // Message de succès
                JOptionPane.showMessageDialog(VideoCardGrid.this, 
                    "✅ Vidéo ouverte avec le lecteur par défaut\n\n" +
                    "🎬 Titre: " + video.getTitre() + "\n" +
                    "📂 Fichier: " + videoFile.getName(), 
                    "VIDORA", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(VideoCardGrid.this, 
                    "❌ Erreur lors de l'ouverture de la vidéo:\n" + e.getMessage(), 
                    "VIDORA - Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void deleteVideo() {
            int result = JOptionPane.showConfirmDialog(
                VideoCardGrid.this,
                "Voulez-vous vraiment supprimer cette vidéo ?\n\n" +
                "🎬 " + video.getTitre(),
                "VIDORA - Supprimer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                try {
                    controller.supprimerVideo(video.getId());
                    loadVideos(); // Recharger la liste
                    JOptionPane.showMessageDialog(VideoCardGrid.this, 
                        "✅ Vidéo supprimée avec succès", 
                        "VIDORA", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VideoCardGrid.this, 
                        "❌ Erreur lors de la suppression: " + e.getMessage(), 
                        "VIDORA - Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        private void addToPlaylist() {
            JOptionPane.showMessageDialog(VideoCardGrid.this, 
                "📋 Fonctionnalité playlist à implémenter", 
                "VIDORA", 
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        private ImageIcon createVideoThumbnail() {
            // Créer une miniature par défaut
            BufferedImage thumbnail = new BufferedImage(120, 70, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = thumbnail.createGraphics();
            
            // Fond dégradé
            GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 41, 59), 
                                                      120, 70, new Color(51, 65, 85));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 120, 70);
            
            // Icône vidéo
            g2d.setColor(ACCENT);
            g2d.setFont(new Font("Roboto", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            String icon = "🎬";
            int x = (120 - fm.stringWidth(icon)) / 2;
            int y = (70 + fm.getAscent()) / 2;
            g2d.drawString(icon, x, y);
            
            g2d.dispose();
            return new ImageIcon(thumbnail);
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
    
    /**
     * ScrollBar UI moderne
     */
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            trackColor = new Color(15, 23, 42);
            thumbColor = new Color(51, 65, 85);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }
    
    public void refreshData() {
        loadVideos();
    }
    
    public Video getSelectedVideo() {
        return selectedVideo;
    }
}
