package com.vidora.controller;

import com.vidora.service.VideoService;
import com.vidora.service.PlaylistService;
import com.vidora.utils.VideoScanner;
import com.vidora.model.Video;
import com.vidora.model.Playlist;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Contrôleur principal de l'application VIDORA
 * Gère la logique métier et la communication entre les vues et les services
 */
public class MainController {
    private VideoService videoService;
    private PlaylistService playlistService;
    private VideoScanner videoScanner;
    
    public MainController() {
        // Initialiser les services
        this.videoService = new VideoService();
        this.playlistService = new PlaylistService();
        this.videoScanner = new VideoScanner();
    }
    
    // Services getters
    public VideoService getVideoService() {
        return videoService;
    }
    
    public PlaylistService getPlaylistService() {
        return playlistService;
    }
    
    public VideoScanner getVideoScanner() {
        return videoScanner;
    }
    
    // ==================== GESTION DES VIDÉOS ====================
    
    public ArrayList<Video> getAllVideos() {
        return videoService.getAllVideos();
    }
    
    public boolean ajouterVideo(Video video) {
        try {
            return videoService.ajouterVideo(video);
        } catch (Exception e) {
            showError("Erreur lors de l'ajout de la vidéo", e.getMessage());
            return false;
        }
    }
    
    public boolean modifierVideo(Video video) {
        try {
            return videoService.modifierVideo(video);
        } catch (Exception e) {
            showError("Erreur lors de la modification de la vidéo", e.getMessage());
            return false;
        }
    }
    
    public boolean supprimerVideo(int videoId) {
        try {
            return videoService.supprimerVideo(videoId);
        } catch (Exception e) {
            showError("Erreur lors de la suppression de la vidéo", e.getMessage());
            return false;
        }
    }
    
    public boolean jouerVideo(int videoId) {
        try {
            return videoService.jouerVideo(videoId);
        } catch (Exception e) {
            showError("Erreur lors de la lecture de la vidéo", e.getMessage());
            return false;
        }
    }
    
    public ArrayList<Video> rechercherVideosParTitre(String titre) {
        return videoService.rechercherVideosParTitre(titre);
    }
    
    public ArrayList<Video> rechercherVideosParCategorie(String categorie) {
        return videoService.rechercherVideosParCategorie(categorie);
    }
    
    public ArrayList<Video> trierVideosParDuree(boolean croissant) {
        return videoService.trierVideosParDuree(croissant);
    }
    
    public ArrayList<Video> trierVideosParDateAjout(boolean croissant) {
        return videoService.trierVideosParDateAjout(croissant);
    }
    
    public ArrayList<Video> trierVideosParVues(boolean croissant) {
        return videoService.trierVideosParVues(croissant);
    }
    
    public ArrayList<String> getCategoriesDisponibles() {
        return videoService.getCategoriesDisponibles();
    }
    
    public int getNombreTotalVideos() {
        return videoService.getNombreTotalVideos();
    }
    
    // ==================== GESTION DES PLAYLISTS ====================
    
    public ArrayList<Playlist> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }
    
    public boolean creerPlaylist(Playlist playlist) {
        try {
            return playlistService.creerPlaylist(playlist);
        } catch (Exception e) {
            showError("Erreur lors de la création de la playlist", e.getMessage());
            return false;
        }
    }
    
    public boolean modifierPlaylist(Playlist playlist) {
        try {
            return playlistService.modifierPlaylist(playlist);
        } catch (Exception e) {
            showError("Erreur lors de la modification de la playlist", e.getMessage());
            return false;
        }
    }
    
    public boolean supprimerPlaylist(int playlistId) {
        try {
            return playlistService.supprimerPlaylist(playlistId);
        } catch (Exception e) {
            showError("Erreur lors de la suppression de la playlist", e.getMessage());
            return false;
        }
    }
    
    public boolean ajouterVideoAPlaylist(int playlistId, int videoId) {
        try {
            return playlistService.ajouterVideoAPlaylist(playlistId, videoId);
        } catch (Exception e) {
            showError("Erreur lors de l'ajout de la vidéo à la playlist", e.getMessage());
            return false;
        }
    }
    
    public boolean supprimerVideoDePlaylist(int playlistId, int videoId) {
        try {
            return playlistService.supprimerVideoDePlaylist(playlistId, videoId);
        } catch (Exception e) {
            showError("Erreur lors de la suppression de la vidéo de la playlist", e.getMessage());
            return false;
        }
    }
    
    public Playlist getPlaylistAvecVideos(int playlistId) {
        return playlistService.getPlaylistAvecVideos(playlistId);
    }
    
    public int getNombreTotalPlaylists() {
        return playlistService.getNombreTotalPlaylists();
    }
    
    // ==================== SCANNER ====================
    
    public ArrayList<Video> scannerDossier(String cheminDossier) {
        return videoScanner.scannerDossier(cheminDossier);
    }
    
    public int ajouterVideosTrouvees(ArrayList<Video> videos) {
        return videoScanner.ajouterVideosTrouvees(videos);
    }
    
    // ==================== STATISTIQUES ====================
    
    public ArrayList<Video> getVideosPlusVues(int limite) {
        return videoService.getVideosPlusVues(limite);
    }
    
    // ==================== UTILITAIRES ====================
    
    public void showSuccess(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean confirm(String title, String message) {
        return JOptionPane.showConfirmDialog(null, message, title, 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
    
    public File selectDirectory(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Sélectionner un dossier à scanner");
        
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    public File selectVideoFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Sélectionner un fichier vidéo");
        
        // Filtrer les extensions vidéo
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mkv") ||
                       name.endsWith(".mov") || name.endsWith(".wmv") || name.endsWith(".flv") ||
                       name.endsWith(".webm") || name.endsWith(".m4v") || name.endsWith(".3gp");
            }
            
            @Override
            public String getDescription() {
                return "Fichiers vidéo (*.mp4, *.avi, *.mkv, *.mov, *.wmv, *.flv, *.webm, *.m4v, *.3gp)";
            }
        });
        
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    /**
     * Point d'entrée principal pour l'interface graphique
     */
    public static void main(String[] args) {
        // Test de connexion à la base de données
        if (com.vidora.Database.connect() == null) {
            JOptionPane.showMessageDialog(null, 
                    "Impossible de se connecter à la base de données. Arrêt de l'application.", 
                    "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Lancer l'interface graphique dans l'EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Appliquer le look and feel système
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            MainController controller = new MainController();
            com.vidora.view.MainView mainView = new com.vidora.view.MainView(controller);
            mainView.setVisible(true);
        });
    }
}
