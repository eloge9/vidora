package com.vidora.service;

import com.vidora.model.Video;
import com.vidora.dao.VideoDAO;
import com.vidora.dao.PlaylistDAO;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;

public class VideoService {
    private VideoDAO videoDAO;
    private PlaylistDAO playlistDAO;
    
    public VideoService() {
        this.videoDAO = new VideoDAO();
        this.playlistDAO = new PlaylistDAO();
    }
    
    public boolean ajouterVideo(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("La vidéo ne peut pas être null");
        }
        if (video.getTitre() == null || video.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la vidéo est obligatoire");
        }
        if (video.getChemin() == null || video.getChemin().trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin de la vidéo est obligatoire");
        }
        if (video.getCategorie() == null || video.getCategorie().trim().isEmpty()) {
            video.setCategorie("Non classée");
        }
        if (video.getDuree() < 0) {
            throw new IllegalArgumentException("La durée ne peut pas être négative");
        }
        
        return videoDAO.ajouter(video);
    }
    
    public boolean modifierVideo(Video video) {
        if (video == null || video.getId() <= 0) {
            throw new IllegalArgumentException("Vidéo invalide");
        }
        if (video.getTitre() == null || video.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la vidéo est obligatoire");
        }
        if (video.getChemin() == null || video.getChemin().trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin de la vidéo est obligatoire");
        }
        if (video.getCategorie() == null || video.getCategorie().trim().isEmpty()) {
            video.setCategorie("Non classée");
        }
        if (video.getDuree() < 0) {
            throw new IllegalArgumentException("La durée ne peut pas être négative");
        }
        
        return videoDAO.modifier(video);
    }
    
    public boolean supprimerVideo(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de vidéo invalide");
        }
        
        return videoDAO.supprimer(id);
    }
    
    public Video trouverVideoParId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de vidéo invalide");
        }
        
        return videoDAO.trouverParId(id);
    }
    
    public ArrayList<Video> getAllVideos() {
        return videoDAO.trouverToutes();
    }
    
    public ArrayList<Video> rechercherVideosParTitre(String titre) {
        if (titre == null || titre.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return videoDAO.rechercherParTitre(titre);
    }
    
    public ArrayList<Video> rechercherVideosParCategorie(String categorie) {
        if (categorie == null || categorie.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return videoDAO.rechercherParCategorie(categorie);
    }
    
    public ArrayList<Video> trierVideosParDuree(boolean croissant) {
        return videoDAO.trierParDuree(croissant);
    }
    
    public ArrayList<Video> trierVideosParDateAjout(boolean croissant) {
        return videoDAO.trierParDateAjout(croissant);
    }
    
    public ArrayList<Video> trierVideosParVues(boolean croissant) {
        return videoDAO.trierParVues(croissant);
    }
    
    public boolean jouerVideo(int id) {
        Video video = trouverVideoParId(id);
        if (video == null) {
            throw new IllegalArgumentException("Vidéo non trouvée");
        }
        
        try {
            File videoFile = new File(video.getChemin());
            if (!videoFile.exists()) {
                throw new RuntimeException("Le fichier vidéo n'existe pas : " + video.getChemin());
            }
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(videoFile);
                
                // Incrémenter le nombre de vues
                video.incrementerVues();
                videoDAO.incrementerVues(id);
                
                return true;
            } else {
                throw new RuntimeException("L'ouverture de fichiers n'est pas supportée sur ce système");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture de la vidéo : " + e.getMessage());
        }
    }
    
    public ArrayList<Video> getVideosPlusVues(int limite) {
        if (limite <= 0) {
            limite = 10; // Valeur par défaut
        }
        
        return videoDAO.getVideosPlusVues(limite);
    }
    
    public int getNombreTotalVideos() {
        return videoDAO.getNombreTotalVideos();
    }
    
    public ArrayList<String> getCategoriesDisponibles() {
        ArrayList<Video> videos = getAllVideos();
        ArrayList<String> categories = new ArrayList<>();
        
        for (Video video : videos) {
            String categorie = video.getCategorie();
            if (categorie != null && !categorie.trim().isEmpty() && !categories.contains(categorie)) {
                categories.add(categorie);
            }
        }
        
        return categories;
    }
    
    public void afficherVideos(ArrayList<Video> videos) {
        if (videos == null || videos.isEmpty()) {
            System.out.println("Aucune vidéo à afficher.");
            return;
        }
        
        System.out.println("\n=== LISTE DES VIDÉOS ===");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-30s %-15s %-10s %-8s %-20s%n", 
                         "ID", "TITRE", "CATÉGORIE", "DURÉE", "VUES", "DATE AJOUT");
        System.out.println("----------------------------------------------------------------------------------------");
        
        for (Video video : videos) {
            System.out.printf("%-5d %-30s %-15s %-10s %-8d %-20s%n",
                            video.getId(),
                            tronquer(video.getTitre(), 30),
                            tronquer(video.getCategorie(), 15),
                            video.getDureeFormatee(),
                            video.getVues(),
                            video.getDateAjout().toLocalDate());
        }
        
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Total : " + videos.size() + " vidéo(s)");
    }
    
    public void afficherVideo(Video video) {
        if (video == null) {
            System.out.println("Vidéo non trouvée.");
            return;
        }
        
        System.out.println("\n=== DÉTAILS DE LA VIDÉO ===");
        System.out.println("ID : " + video.getId());
        System.out.println("Titre : " + video.getTitre());
        System.out.println("Catégorie : " + video.getCategorie());
        System.out.println("Durée : " + video.getDureeFormatee());
        System.out.println("Nombre de vues : " + video.getVues());
        System.out.println("Date d'ajout : " + video.getDateAjout());
        System.out.println("Chemin : " + video.getChemin());
        System.out.println("---------------------------");
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
