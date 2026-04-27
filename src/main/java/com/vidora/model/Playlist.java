package com.vidora.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Playlist {
    private int id;
    private String nom;
    private LocalDateTime dateCreation;
    private ArrayList<Video> videos;

    public Playlist() {
        this.dateCreation = LocalDateTime.now();
        this.videos = new ArrayList<>();
    }

    public Playlist(String nom) {
        this();
        this.nom = nom;
    }

    public Playlist(int id, String nom, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.videos = new ArrayList<>();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    // Méthodes de gestion des vidéos
    public void ajouterVideo(Video video) {
        if (!videos.contains(video)) {
            videos.add(video);
        }
    }

    public void supprimerVideo(Video video) {
        videos.remove(video);
    }

    public boolean contientVideo(Video video) {
        return videos.contains(video);
    }

    public int getNombreVideos() {
        return videos.size();
    }

    public int getDureeTotale() {
        int dureeTotale = 0;
        for (Video video : videos) {
            dureeTotale += video.getDuree();
        }
        return dureeTotale;
    }

    public String getDureeTotaleFormatee() {
        int dureeTotale = getDureeTotale();
        int heures = dureeTotale / 3600;
        int minutes = (dureeTotale % 3600) / 60;
        int secondes = dureeTotale % 60;
        
        if (heures > 0) {
            return String.format("%02d:%02d:%02d", heures, minutes, secondes);
        } else {
            return String.format("%02d:%02d", minutes, secondes);
        }
    }

    @Override
    public String toString() {
        return String.format("Playlist{id=%d, nom='%s', nombreVideos=%d, dureeTotale=%s, dateCreation=%s}",
                id, nom, getNombreVideos(), getDureeTotaleFormatee(), dateCreation);
    }
}
