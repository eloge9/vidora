package com.vidora.view.components;

import com.vidora.model.Video;
import com.vidora.controller.MainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Carte vidéo style YouTube pour afficher les vidéos dans une grille
 */
public class VideoCard extends JPanel {
    private Video video;
    private boolean isHovered = false;
    private JFrame parentFrame;
    private MainController controller;
    
    // Composants
    private JLabel thumbnailLabel;
    private JLabel titleLabel;
    private JLabel categoryLabel;
    private JLabel viewsLabel;
    private JLabel durationLabel;
    
    public VideoCard(Video video) {
        this.video = video;
        initComponent();
        layoutComponents();
        setupEventHandlers();
    }
    
    public VideoCard(Video video, JFrame parentFrame, MainController controller) {
        this.video = video;
        this.parentFrame = parentFrame;
        this.controller = controller;
        initComponent();
        layoutComponents();
        setupEventHandlers();
    }
    
    private void initComponent() {
        // Configuration du fond style YouTube
        setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(com.vidora.view.MainView.CARD_BG, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        setLayout(new BorderLayout(0, 8));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(350, 240));
    }
    
    private void layoutComponents() {
        // Thumbnail (placeholder pour l'image)
        thumbnailLabel = new JLabel();
        thumbnailLabel.setBackground(com.vidora.view.MainView.DARK_BG);
        thumbnailLabel.setOpaque(true);
        thumbnailLabel.setPreferredSize(new Dimension(0, 200));
        thumbnailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thumbnailLabel.setVerticalAlignment(SwingConstants.CENTER);
        thumbnailLabel.setIcon(createThumbnailIcon());
        
        // Durée (superposée sur le thumbnail)
        durationLabel = new JLabel(formatDuration(video.getDuree()));
        durationLabel.setBackground(new Color(0, 0, 0, 180));
        durationLabel.setOpaque(true);
        durationLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        durationLabel.setFont(new Font("Roboto", Font.BOLD, 12));
        durationLabel.setBorder(new EmptyBorder(2, 6, 2, 6));
        
        // Panel pour le thumbnail avec durée superposée
        JPanel thumbnailPanel = new JPanel(new BorderLayout());
        thumbnailPanel.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        thumbnailPanel.add(thumbnailLabel, BorderLayout.CENTER);
        
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        durationPanel.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        durationPanel.setOpaque(false);
        durationPanel.add(durationLabel);
        
        thumbnailPanel.add(durationPanel, BorderLayout.SOUTH);
        
        // Informations vidéo
        JPanel infoPanel = new JPanel(new BorderLayout(0, 4));
        infoPanel.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        infoPanel.setOpaque(true);
        
        // Titre
        titleLabel = new JLabel(video.getTitre());
        titleLabel.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        titleLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        titleLabel.setMaximumSize(new Dimension(320, 40));
        
        // Catégorie et vues
        JPanel detailsPanel = new JPanel(new BorderLayout(0, 2));
        detailsPanel.setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
        
        categoryLabel = new JLabel(video.getCategorie());
        categoryLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        categoryLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        viewsLabel = new JLabel(formatViews(video.getVues()) + " vues");
        viewsLabel.setForeground(com.vidora.view.MainView.SECONDARY_TEXT);
        viewsLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        detailsPanel.add(categoryLabel, BorderLayout.NORTH);
        detailsPanel.add(viewsLabel, BorderLayout.CENTER);
        
        infoPanel.add(titleLabel, BorderLayout.NORTH);
        infoPanel.add(detailsPanel, BorderLayout.CENTER);
        
        add(thumbnailPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setBackground(com.vidora.view.MainView.HOVER_BG);
                thumbnailLabel.setBackground(com.vidora.view.MainView.DARK_BG);
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setBackground(com.vidora.view.MainView.VIDEO_CARD_BG);
                thumbnailLabel.setBackground(com.vidora.view.MainView.DARK_BG);
                repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Action de lecture vidéo
                playVideo();
            }
        });
    }
    
    private Icon createThumbnailIcon() {
        // Créer une icône placeholder style YouTube
        int width = 320;
        int height = 200;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fond noir
        g2d.setColor(com.vidora.view.MainView.DARK_BG);
        g2d.fillRect(0, 0, width, height);
        
        // Icône play YouTube
        g2d.setColor(com.vidora.view.MainView.ACCENT_COLOR);
        int[] xPoints = {width/2 - 20, width/2 - 20, width/2 + 20};
        int[] yPoints = {height/2 - 25, height/2 + 25, height/2};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        g2d.dispose();
        return new ImageIcon(image);
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
    
    private String formatViews(int views) {
        if (views >= 1000000) {
            return String.format("%.1fM vues", views / 1000000.0);
        } else if (views >= 1000) {
            return String.format("%.1fK vues", views / 1000.0);
        } else {
            return views + " vues";
        }
    }
    
    private void playVideo() {
        // Ouvrir la vidéo avec le lecteur par défaut du système
        try {
            // Vérifier que le fichier existe
            File videoFile = new File(video.getChemin());
            if (!videoFile.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Fichier vidéo introuvable:\n" + video.getChemin(), 
                    "VIDORA - Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier que Desktop est supporté
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Lecteur système non disponible", 
                    "VIDORA - Erreur système", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ouvrir avec le lecteur par défaut du système
            Desktop desktop = Desktop.getDesktop();
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
    
    public Video getVideo() {
        return video;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (isHovered) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Légère bordure rouge au hover style YouTube
            g2d.setColor(com.vidora.view.MainView.ACCENT_COLOR);
            g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
            
            g2d.dispose();
        }
    }
}
