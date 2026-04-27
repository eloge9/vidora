package com.vidora.model;

import java.time.LocalDateTime;

public class Video {
    private int id;
    private String titre;
    private String chemin;
    private String categorie;
    private int duree; // en secondes
    private int vues;
    private LocalDateTime dateAjout;

    public Video() {
        this.vues = 0;
        this.dateAjout = LocalDateTime.now();
    }

    public Video(String titre, String chemin, String categorie, int duree) {
        this();
        this.titre = titre;
        this.chemin = chemin;
        this.categorie = categorie;
        this.duree = duree;
    }

    public Video(int id, String titre, String chemin, String categorie, int duree, int vues, LocalDateTime dateAjout) {
        this.id = id;
        this.titre = titre;
        this.chemin = chemin;
        this.categorie = categorie;
        this.duree = duree;
        this.vues = vues;
        this.dateAjout = dateAjout;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getVues() {
        return vues;
    }

    public void setVues(int vues) {
        this.vues = vues;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }

    // Méthodes utilitaires
    public void incrementerVues() {
        this.vues++;
    }

    public String getDureeFormatee() {
        int heures = duree / 3600;
        int minutes = (duree % 3600) / 60;
        int secondes = duree % 60;
        
        if (heures > 0) {
            return String.format("%02d:%02d:%02d", heures, minutes, secondes);
        } else {
            return String.format("%02d:%02d", minutes, secondes);
        }
    }

    @Override
    public String toString() {
        return String.format("Video{id=%d, titre='%s', categorie='%s', duree=%s, vues=%d, dateAjout=%s}",
                id, titre, categorie, getDureeFormatee(), vues, dateAjout);
    }
}
