package com.vidora.view;

import com.vidora.controller.MainController;
import com.vidora.view.dashboard.DashboardPanel;
import com.vidora.view.videos.VideosPanel;
import com.vidora.view.components.VideoCardGrid;
import com.vidora.view.playlists.PlaylistsPanel;
import com.vidora.view.scanner.ScannerPanel;
import com.vidora.view.statistics.StatisticsPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'application VIDORA
 * Architecture MVC avec sidebar et CardLayout
 */
public class MainView extends JFrame {
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panneaux de contenu
    private DashboardPanel dashboardPanel;
    private VideoCardGrid videosPanel;
    private PlaylistsPanel playlistsPanel;
    private ScannerPanel scannerPanel;
    private StatisticsPanel statisticsPanel;
    
    // Contrôleur
    private MainController controller;
    
    // Boutons de navigation
    private JButton btnDashboard;
    private JButton btnVideos;
    private JButton btnPlaylists;
    private JButton btnScanner;
    private JButton btnStatistics;
    
    // Couleurs du thème YouTube 2024
    public static final Color DARK_BG = new Color(18, 18, 18);          // Fond principal YouTube #121212
    public static final Color SIDEBAR_BG = new Color(24, 24, 24);       // Sidebar YouTube #181818
    public static final Color CARD_BG = new Color(28, 28, 28);          // Cartes vidéos #1C1C1C
    public static final Color ACCENT_COLOR = new Color(255, 0, 0);      // Rouge YouTube #FF0000
    public static final Color TEXT_COLOR = new Color(241, 241, 241);    // Texte principal blanc #F1F1F1
    public static final Color SECONDARY_TEXT = new Color(170, 170, 170); // Texte secondaire gris #AAAAAA
    public static final Color BUTTON_HOVER = new Color(204, 0, 0);      // Hover rouge foncé #CC0000
    public static final Color SUCCESS_COLOR = new Color(0, 204, 0);     // Succès vert #00CC00
    public static final Color ERROR_COLOR = new Color(255, 0, 0);        // Erreur rouge YouTube
    public static final Color PURPLE_COLOR = new Color(128, 0, 128);    // Violet YouTube
    public static final Color PANEL_BG = CARD_BG; // Alias pour compatibilité
    
    // Couleurs spécifiques YouTube
    public static final Color VIDEO_CARD_BG = new Color(32, 32, 32);    // Fond cartes vidéos
    public static final Color HOVER_BG = new Color(48, 48, 48);          // Hover cartes
    public static final Color SUBSCRIBE_BUTTON = new Color(255, 0, 0);    // Bouton s'abonner
    public static final Color NOTIFICATION_BG = new Color(64, 64, 64);    // Notifications
    
    public MainView(MainController controller) {
        this.controller = controller;
        
        setTitle("VIDORA - Gestionnaire Intelligent de Vidéos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        
        initComponents();
        layoutComponents();
        setupEventHandlers();
        applyTheme();
        
        // Afficher le dashboard par défaut
        showDashboard();
    }
    
    private void initComponents() {
        // Créer les panneaux principaux
        mainPanel = new JPanel(new BorderLayout());
        sidebarPanel = new JPanel();
        contentPanel = new JPanel();
        
        // Configuration du CardLayout pour le contenu
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Créer les panneaux de contenu
        dashboardPanel = new com.vidora.view.dashboard.DashboardPanel(controller);
        contentPanel.add(dashboardPanel, "dashboard");
        
        // Vidéos style YouTube
        videosPanel = new VideoCardGrid(controller);
        contentPanel.add(videosPanel, "videos");
        
        playlistsPanel = new PlaylistsPanel(controller);
        contentPanel.add(playlistsPanel, "playlists");
        
        scannerPanel = new ScannerPanel(controller);
        contentPanel.add(scannerPanel, "scanner");
        
        statisticsPanel = new StatisticsPanel(controller);
        contentPanel.add(statisticsPanel, "statistics");
        
        // Créer les boutons de navigation
        btnDashboard = createSidebarButton("📊 Dashboard");
        btnVideos = createSidebarButton("🎬 Vidéos");
        btnPlaylists = createSidebarButton("📋 Playlists");
        btnScanner = createSidebarButton("🔍 Scanner");
        btnStatistics = createSidebarButton("📈 Statistiques");
    }
    
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(SIDEBAR_BG);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(SIDEBAR_BG);
                }
            }
        });
        
        return button;
    }
    
    private void layoutComponents() {
        // Layout de la sidebar
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Titre de l'application dans la sidebar
        JLabel titleLabel = new JLabel("VIDORA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Ajouter les composants à la sidebar
        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(btnVideos);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(btnPlaylists);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(btnScanner);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(btnStatistics);
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Ajouter les panneaux au CardLayout
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(videosPanel, "videos");
        contentPanel.add(playlistsPanel, "playlists");
        contentPanel.add(scannerPanel, "scanner");
        contentPanel.add(statisticsPanel, "statistics");
        
        // Assembler la fenêtre principale
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private void setupEventHandlers() {
        btnDashboard.addActionListener(e -> showDashboard());
        btnVideos.addActionListener(e -> showVideos());
        btnPlaylists.addActionListener(e -> showPlaylists());
        btnScanner.addActionListener(e -> showScanner());
        btnStatistics.addActionListener(e -> showStatistics());
    }
    
    private void applyTheme() {
        mainPanel.setBackground(DARK_BG);
        contentPanel.setBackground(PANEL_BG);
        
        // Appliquer le thème à tous les panneaux
        applyThemeRecursively(mainPanel);
    }
    
    private void applyThemeRecursively(Component component) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            if (panel != sidebarPanel && panel != contentPanel) {
                panel.setBackground(PANEL_BG);
            }
        }
        
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyThemeRecursively(child);
            }
        }
    }
    
    // Méthodes de navigation
    public void showDashboard() {
        cardLayout.show(contentPanel, "dashboard");
        resetButtonStates();
        btnDashboard.setBackground(ACCENT_COLOR);
        dashboardPanel.refreshData();
    }
    
    public void showVideos() {
        cardLayout.show(contentPanel, "videos");
        resetButtonStates();
        btnVideos.setBackground(ACCENT_COLOR);
        // videosPanel.refreshData(); // YouTubeVideosPanel gère son propre rafraîchissement
    }
    
    public void showPlaylists() {
        cardLayout.show(contentPanel, "playlists");
        resetButtonStates();
        btnPlaylists.setBackground(ACCENT_COLOR);
        playlistsPanel.refresh();
    }
    
    public void showScanner() {
        cardLayout.show(contentPanel, "scanner");
        resetButtonStates();
        btnScanner.setBackground(ACCENT_COLOR);
    }
    
    public void showStatistics() {
        cardLayout.show(contentPanel, "statistics");
        resetButtonStates();
        btnStatistics.setBackground(ACCENT_COLOR);
        statisticsPanel.refreshData();
    }
    
    private void resetButtonStates() {
        btnDashboard.setFont(new Font("Roboto", Font.PLAIN, 14));
        btnVideos.setFont(new Font("Roboto", Font.PLAIN, 14));
        btnPlaylists.setFont(new Font("Roboto", Font.PLAIN, 14));
        btnScanner.setFont(new Font("Roboto", Font.PLAIN, 14));
        btnStatistics.setFont(new Font("Roboto", Font.PLAIN, 14));
    }
    
    // Getters pour les contrôleurs
    public JPanel getVideosPanel() {
        return videosPanel;
    }
    
    public PlaylistsPanel getPlaylistsPanel() {
        return playlistsPanel;
    }
    
    public ScannerPanel getScannerPanel() {
        return scannerPanel;
    }
    
    public DashboardPanel getDashboardPanel() {
        return dashboardPanel;
    }
    
    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }
}
