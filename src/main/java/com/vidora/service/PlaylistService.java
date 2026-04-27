package com.vidora.service;

import com.vidora.model.Playlist;
import com.vidora.model.Video;
import com.vidora.dao.PlaylistDAO;
import com.vidora.dao.VideoDAO;

import java.util.ArrayList;

public class PlaylistService {
    private PlaylistDAO playlistDAO;
    private VideoDAO videoDAO;
    
    public PlaylistService() {
        this.playlistDAO = new PlaylistDAO();
        this.videoDAO = new VideoDAO();
    }
    
    public boolean creerPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("La playlist ne peut pas être null");
        }
        if (playlist.getNom() == null || playlist.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la playlist est obligatoire");
        }
        
        return playlistDAO.ajouter(playlist);
    }
    
    public boolean modifierPlaylist(Playlist playlist) {
        if (playlist == null || playlist.getId() <= 0) {
            throw new IllegalArgumentException("Playlist invalide");
        }
        if (playlist.getNom() == null || playlist.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la playlist est obligatoire");
        }
        
        return playlistDAO.modifier(playlist);
    }
    
    public boolean supprimerPlaylist(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de playlist invalide");
        }
        
        return playlistDAO.supprimer(id);
    }
    
    public Playlist trouverPlaylistParId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de playlist invalide");
        }
        
        return playlistDAO.trouverParId(id);
    }
    
    public ArrayList<Playlist> getAllPlaylists() {
        return playlistDAO.trouverToutes();
    }
    
    public boolean ajouterVideoAPlaylist(int idPlaylist, int idVideo) {
        if (idPlaylist <= 0 || idVideo <= 0) {
            throw new IllegalArgumentException("ID de playlist ou de vidéo invalide");
        }
        
        // Vérifier que la playlist et la vidéo existent
        Playlist playlist = playlistDAO.trouverParId(idPlaylist);
        Video video = videoDAO.trouverParId(idVideo);
        
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist non trouvée");
        }
        if (video == null) {
            throw new IllegalArgumentException("Vidéo non trouvée");
        }
        
        // Vérifier que la vidéo n'est pas déjà dans la playlist
        if (playlistDAO.videoEstDansPlaylist(idPlaylist, idVideo)) {
            throw new IllegalArgumentException("La vidéo est déjà dans cette playlist");
        }
        
        return playlistDAO.ajouterVideoAPlaylist(idPlaylist, idVideo);
    }
    
    public boolean supprimerVideoDePlaylist(int idPlaylist, int idVideo) {
        if (idPlaylist <= 0 || idVideo <= 0) {
            throw new IllegalArgumentException("ID de playlist ou de vidéo invalide");
        }
        
        return playlistDAO.supprimerVideoDePlaylist(idPlaylist, idVideo);
    }
    
    public int getNombreTotalPlaylists() {
        return playlistDAO.getNombreTotalPlaylists();
    }
    
    public void afficherPlaylists(ArrayList<Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) {
            System.out.println("Aucune playlist à afficher.");
            return;
        }
        
        System.out.println("\n=== LISTE DES PLAYLISTS ===");
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s %-20s%n", 
                         "ID", "NOM", "NBR VIDÉOS", "DURÉE TOTALE", "DATE CRÉATION");
        System.out.println("------------------------------------------------------------------------");
        
        for (Playlist playlist : playlists) {
            System.out.printf("%-5d %-25s %-15d %-15s %-20s%n",
                            playlist.getId(),
                            tronquer(playlist.getNom(), 25),
                            playlist.getNombreVideos(),
                            playlist.getDureeTotaleFormatee(),
                            playlist.getDateCreation().toLocalDate());
        }
        
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Total : " + playlists.size() + " playlist(s)");
    }
    
    public void afficherPlaylist(Playlist playlist) {
        if (playlist == null) {
            System.out.println("Playlist non trouvée.");
            return;
        }
        
        System.out.println("\n=== DÉTAILS DE LA PLAYLIST ===");
        System.out.println("ID : " + playlist.getId());
        System.out.println("Nom : " + playlist.getNom());
        System.out.println("Nombre de vidéos : " + playlist.getNombreVideos());
        System.out.println("Durée totale : " + playlist.getDureeTotaleFormatee());
        System.out.println("Date de création : " + playlist.getDateCreation());
        System.out.println("--------------------------------");
        
        if (!playlist.getVideos().isEmpty()) {
            System.out.println("\nVIDÉOS DANS LA PLAYLIST :");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-5s %-30s %-15s %-10s%n", 
                             "ID", "TITRE", "CATÉGORIE", "DURÉE");
            System.out.println("--------------------------------------------------");
            
            for (Video video : playlist.getVideos()) {
                System.out.printf("%-5d %-30s %-15s %-10s%n",
                                video.getId(),
                                tronquer(video.getTitre(), 30),
                                tronquer(video.getCategorie(), 15),
                                video.getDureeFormatee());
            }
            
            System.out.println("--------------------------------------------------");
        } else {
            System.out.println("Cette playlist ne contient aucune vidéo.");
        }
    }
    
    public Playlist getPlaylistAvecVideos(int id) {
        Playlist playlist = playlistDAO.trouverParId(id);
        if (playlist != null) {
            playlist.setVideos(playlistDAO.getVideosDePlaylist(id));
        }
        return playlist;
    }
    
    private String tronquer(String texte, int longueurMax) {
        if (texte == null) {
            return "";
        }
        if (texte.length() <= longueurMax) {
            return texte;
        }
        return texte.substring(0, longueurMax - 3) + "...";
    }
}
