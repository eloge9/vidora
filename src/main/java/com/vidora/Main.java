package com.vidora;

import com.vidora.model.Video;
import com.vidora.model.Playlist;
import com.vidora.service.VideoService;
import com.vidora.service.PlaylistService;
import com.vidora.utils.VideoScanner;
import com.vidora.utils.SelectionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static VideoService videoService = new VideoService();
    private static PlaylistService playlistService = new PlaylistService();
    private static VideoScanner scanner = new VideoScanner();
    private static Scanner scannerInput = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Test de connexion à la base de données
        if (Database.connect() == null) {
            System.err.println("Impossible de se connecter à la base de données. Arrêt de l'application.");
            return;
        }
        
        System.out.println("========================================");
        System.out.println("      VIDORA - Gestionnaire Vidéo      ");
        System.out.println("========================================");
        System.out.println("Bienvenue dans votre gestionnaire de vidéos intelligent !");
        
        boolean continuer = true;
        while (continuer) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix : ");
            
            try {
                switch (choix) {
                    case 1:
                        menuGestionVideos();
                        break;
                    case 2:
                        menuRechercheVideos();
                        break;
                    case 3:
                        menuTriVideos();
                        break;
                    case 4:
                        menuLectureVideo();
                        break;
                    case 5:
                        menuGestionPlaylists();
                        break;
                    case 6:
                        menuScannerDossiers();
                        break;
                    case 7:
                        menuStatistiques();
                        break;
                    case 8:
                        continuer = false;
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
            }
            
            if (continuer) {
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scannerInput.nextLine();
            }
        }
        
        scannerInput.close();
    }
    
    private static void afficherMenuPrincipal() {
        System.out.println("\n========================================");
        System.out.println("              MENU PRINCIPAL           ");
        System.out.println("========================================");
        System.out.println("1. Gestion des vidéos");
        System.out.println("2. Recherche de vidéos");
        System.out.println("3. Tri des vidéos");
        System.out.println("4. Lecture de vidéo");
        System.out.println("5. Gestion des playlists");
        System.out.println("6. Scanner des dossiers");
        System.out.println("7. Statistiques");
        System.out.println("8. Quitter");
        System.out.println("----------------------------------------");
    }
    
    private static void menuGestionVideos() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- GESTION DES VIDÉOS ---");
            System.out.println("1. Ajouter une vidéo");
            System.out.println("2. Modifier une vidéo");
            System.out.println("3. Supprimer une vidéo");
            System.out.println("4. Afficher toutes les vidéos");
            System.out.println("5. Retour au menu principal");
            
            int choix = lireEntier("Votre choix : ");
            
            switch (choix) {
                case 1:
                    ajouterVideo();
                    break;
                case 2:
                    modifierVideo();
                    break;
                case 3:
                    supprimerVideo();
                    break;
                case 4:
                    afficherToutesVideos();
                    break;
                case 5:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    
    private static void ajouterVideo() {
        System.out.println("\n--- AJOUTER UNE VIDÉO ---");
        
        String titre = lireChaine("Titre de la vidéo : ");
        String chemin = lireChaine("Chemin complet du fichier : ");
        String categorie = lireChaine("Catégorie (laisser vide pour 'Non classée') : ");
        int duree = lireEntier("Durée en secondes : ");
        
        if (categorie.trim().isEmpty()) {
            categorie = "Non classée";
        }
        
        Video video = new Video(titre, chemin, categorie, duree);
        
        try {
            if (videoService.ajouterVideo(video)) {
                System.out.println("Vidéo ajoutée avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout de la vidéo.");
            }
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
    
    private static void modifierVideo() {
        Video video = SelectionHelper.selectionnerVideo("MODIFIER UNE VIDÉO");
        
        if (video == null) {
            return;
        }
        
        System.out.println("\n--- MODIFICATION DE LA VIDÉO ---");
        System.out.println("Vidéo actuelle :");
        videoService.afficherVideo(video);
        
        String nouveauTitre = lireChaine("Nouveau titre (laisser vide pour conserver) : ");
        String nouveauChemin = lireChaine("Nouveau chemin (laisser vide pour conserver) : ");
        String nouvelleCategorie = lireChaine("Nouvelle catégorie (laisser vide pour conserver) : ");
        int nouvelleDuree = lireEntier("Nouvelle durée en secondes (-1 pour conserver) : ");
        
        if (!nouveauTitre.trim().isEmpty()) {
            video.setTitre(nouveauTitre);
        }
        if (!nouveauChemin.trim().isEmpty()) {
            video.setChemin(nouveauChemin);
        }
        if (!nouvelleCategorie.trim().isEmpty()) {
            video.setCategorie(nouvelleCategorie);
        }
        if (nouvelleDuree >= 0) {
            video.setDuree(nouvelleDuree);
        }
        
        try {
            if (videoService.modifierVideo(video)) {
                System.out.println("✅ Vidéo modifiée avec succès !");
            } else {
                System.out.println("❌ Erreur lors de la modification de la vidéo.");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }
    }
    
    private static void supprimerVideo() {
        Video video = SelectionHelper.selectionnerVideo("SUPPRIMER UNE VIDÉO");
        
        if (video == null) {
            return;
        }
        
        System.out.println("\n--- SUPPRESSION DE LA VIDÉO ---");
        System.out.println("Vidéo à supprimer :");
        videoService.afficherVideo(video);
        
        if (SelectionHelper.confirmerSuppression("Vidéo", video.getTitre())) {
            try {
                if (videoService.supprimerVideo(video.getId())) {
                    System.out.println("✅ Vidéo supprimée avec succès !");
                } else {
                    System.out.println("❌ Erreur lors de la suppression de la vidéo.");
                }
            } catch (Exception e) {
                System.err.println("❌ Erreur : " + e.getMessage());
            }
        } else {
            System.out.println("❌ Suppression annulée.");
        }
    }
    
    private static void afficherToutesVideos() {
        System.out.println("\n--- TOUTES LES VIDÉOS ---");
        ArrayList<Video> videos = videoService.getAllVideos();
        videoService.afficherVideos(videos);
    }
    
    private static void menuRechercheVideos() {
        System.out.println("\n--- RECHERCHE DE VIDÉOS ---");
        System.out.println("1. Rechercher par titre");
        System.out.println("2. Rechercher par catégorie");
        System.out.println("3. Afficher les catégories disponibles");
        
        int choix = lireEntier("Votre choix : ");
        
        switch (choix) {
            case 1:
                rechercherParTitre();
                break;
            case 2:
                rechercherParCategorie();
                break;
            case 3:
                afficherCategories();
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }
    
    private static void rechercherParTitre() {
        String titre = lireChaine("Entrez le titre à rechercher : ");
        ArrayList<Video> videos = videoService.rechercherVideosParTitre(titre);
        
        if (videos.isEmpty()) {
            System.out.println("Aucune vidéo trouvée pour ce titre.");
        } else {
            System.out.println("Résultats de la recherche :");
            videoService.afficherVideos(videos);
        }
    }
    
    private static void rechercherParCategorie() {
        String categorie = lireChaine("Entrez la catégorie à rechercher : ");
        ArrayList<Video> videos = videoService.rechercherVideosParCategorie(categorie);
        
        if (videos.isEmpty()) {
            System.out.println("Aucune vidéo trouvée pour cette catégorie.");
        } else {
            System.out.println("Vidéos de la catégorie '" + categorie + "' :");
            videoService.afficherVideos(videos);
        }
    }
    
    private static void afficherCategories() {
        ArrayList<String> categories = videoService.getCategoriesDisponibles();
        
        if (categories.isEmpty()) {
            System.out.println("Aucune catégorie disponible.");
        } else {
            System.out.println("\n--- CATÉGORIES DISPONIBLES ---");
            for (String categorie : categories) {
                System.out.println("- " + categorie);
            }
        }
    }
    
    private static void menuTriVideos() {
        System.out.println("\n--- TRI DES VIDÉOS ---");
        System.out.println("1. Trier par durée (croissant)");
        System.out.println("2. Trier par durée (décroissant)");
        System.out.println("3. Trier par date d'ajout (croissant)");
        System.out.println("4. Trier par date d'ajout (décroissant)");
        System.out.println("5. Trier par nombre de vues (croissant)");
        System.out.println("6. Trier par nombre de vues (décroissant)");
        
        int choix = lireEntier("Votre choix : ");
        
        switch (choix) {
            case 1:
                videoService.afficherVideos(videoService.trierVideosParDuree(true));
                break;
            case 2:
                videoService.afficherVideos(videoService.trierVideosParDuree(false));
                break;
            case 3:
                videoService.afficherVideos(videoService.trierVideosParDateAjout(true));
                break;
            case 4:
                videoService.afficherVideos(videoService.trierVideosParDateAjout(false));
                break;
            case 5:
                videoService.afficherVideos(videoService.trierVideosParVues(true));
                break;
            case 6:
                videoService.afficherVideos(videoService.trierVideosParVues(false));
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }
    
    private static void menuLectureVideo() {
        Video video = SelectionHelper.selectionnerVideo("LECTURE DE VIDÉO");
        
        if (video == null) {
            return;
        }
        
        System.out.println("\n--- LECTURE DE LA VIDÉO ---");
        System.out.println("Lecture de : " + video.getTitre());
        
        try {
            if (videoService.jouerVideo(video.getId())) {
                System.out.println("✅ Lecture de la vidéo lancée avec succès !");
                System.out.println("📺 La vidéo s'ouvrira avec votre lecteur par défaut.");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la lecture : " + e.getMessage());
        }
    }
    
    private static void menuGestionPlaylists() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- GESTION DES PLAYLISTS ---");
            System.out.println("1. Créer une playlist");
            System.out.println("2. Modifier une playlist");
            System.out.println("3. Supprimer une playlist");
            System.out.println("4. Afficher toutes les playlists");
            System.out.println("5. Ajouter une vidéo à une playlist");
            System.out.println("6. Supprimer une vidéo d'une playlist");
            System.out.println("7. Afficher le contenu d'une playlist");
            System.out.println("8. Retour au menu principal");
            
            int choix = lireEntier("Votre choix : ");
            
            switch (choix) {
                case 1:
                    creerPlaylist();
                    break;
                case 2:
                    modifierPlaylist();
                    break;
                case 3:
                    supprimerPlaylist();
                    break;
                case 4:
                    afficherToutesPlaylists();
                    break;
                case 5:
                    ajouterVideoAPlaylist();
                    break;
                case 6:
                    supprimerVideoDePlaylist();
                    break;
                case 7:
                    afficherContenuPlaylist();
                    break;
                case 8:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    
    private static void creerPlaylist() {
        String nom = lireChaine("Nom de la playlist : ");
        
        if (nom.trim().isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return;
        }
        
        Playlist playlist = new Playlist(nom);
        
        try {
            if (playlistService.creerPlaylist(playlist)) {
                System.out.println("Playlist créée avec succès !");
            } else {
                System.out.println("Erreur lors de la création de la playlist.");
            }
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
    
    private static void modifierPlaylist() {
        Playlist playlist = SelectionHelper.selectionnerPlaylist("MODIFIER UNE PLAYLIST");
        
        if (playlist == null) {
            return;
        }
        
        System.out.println("\n--- MODIFICATION DE LA PLAYLIST ---");
        System.out.println("Playlist actuelle :");
        playlistService.afficherPlaylist(playlist);
        
        String nouveauNom = lireChaine("Nouveau nom (laisser vide pour conserver) : ");
        
        if (!nouveauNom.trim().isEmpty()) {
            playlist.setNom(nouveauNom);
        }
        
        try {
            if (playlistService.modifierPlaylist(playlist)) {
                System.out.println("✅ Playlist modifiée avec succès !");
            } else {
                System.out.println("❌ Erreur lors de la modification de la playlist.");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }
    }
    
    private static void supprimerPlaylist() {
        Playlist playlist = SelectionHelper.selectionnerPlaylist("SUPPRIMER UNE PLAYLIST");
        
        if (playlist == null) {
            return;
        }
        
        System.out.println("\n--- SUPPRESSION DE LA PLAYLIST ---");
        System.out.println("Playlist à supprimer :");
        playlistService.afficherPlaylist(playlist);
        
        if (SelectionHelper.confirmerSuppression("Playlist", playlist.getNom())) {
            try {
                if (playlistService.supprimerPlaylist(playlist.getId())) {
                    System.out.println("✅ Playlist supprimée avec succès !");
                } else {
                    System.out.println("❌ Erreur lors de la suppression de la playlist.");
                }
            } catch (Exception e) {
                System.err.println("❌ Erreur : " + e.getMessage());
            }
        } else {
            System.out.println("❌ Suppression annulée.");
        }
    }
    
    private static void afficherToutesPlaylists() {
        System.out.println("\n--- TOUTES LES PLAYLISTS ---");
        ArrayList<Playlist> playlists = playlistService.getAllPlaylists();
        playlistService.afficherPlaylists(playlists);
    }
    
    private static void ajouterVideoAPlaylist() {
        System.out.println("\n--- AJOUTER UNE VIDÉO À UNE PLAYLIST ---");
        
        // Sélectionner la playlist
        Playlist playlist = SelectionHelper.selectionnerPlaylist("SÉLECTIONNER LA PLAYLIST");
        if (playlist == null) {
            return;
        }
        
        // Sélectionner la vidéo
        Video video = SelectionHelper.selectionnerVideo("SÉLECTIONNER LA VIDÉO À AJOUTER");
        if (video == null) {
            return;
        }
        
        System.out.println("\n--- AJOUT ---");
        System.out.println("Playlist : " + playlist.getNom());
        System.out.println("Vidéo : " + video.getTitre());
        
        if (SelectionHelper.confirmer("Confirmer l'ajout de cette vidéo à la playlist ?")) {
            try {
                if (playlistService.ajouterVideoAPlaylist(playlist.getId(), video.getId())) {
                    System.out.println("✅ Vidéo ajoutée à la playlist avec succès !");
                } else {
                    System.out.println("❌ Erreur lors de l'ajout de la vidéo à la playlist.");
                }
            } catch (Exception e) {
                System.err.println("❌ Erreur : " + e.getMessage());
            }
        } else {
            System.out.println("❌ Ajout annulé.");
        }
    }
    
    private static void supprimerVideoDePlaylist() {
        System.out.println("\n--- SUPPRIMER UNE VIDÉO D'UNE PLAYLIST ---");
        
        // Sélectionner la playlist
        Playlist playlist = SelectionHelper.selectionnerPlaylist("SÉLECTIONNER LA PLAYLIST");
        if (playlist == null) {
            return;
        }
        
        // Charger les vidéos de la playlist
        playlist = playlistService.getPlaylistAvecVideos(playlist.getId());
        
        if (playlist.getVideos().isEmpty()) {
            System.out.println("Cette playlist ne contient aucune vidéo.");
            return;
        }
        
        // Sélectionner la vidéo dans la playlist
        Video video = SelectionHelper.selectionnerVideoDansListe(new ArrayList<>(playlist.getVideos()));
        if (video == null) {
            return;
        }
        
        System.out.println("\n--- SUPPRESSION ---");
        System.out.println("Playlist : " + playlist.getNom());
        System.out.println("Vidéo à supprimer : " + video.getTitre());
        
        if (SelectionHelper.confirmer("Confirmer la suppression de cette vidéo de la playlist ?")) {
            try {
                if (playlistService.supprimerVideoDePlaylist(playlist.getId(), video.getId())) {
                    System.out.println("✅ Vidéo supprimée de la playlist avec succès !");
                } else {
                    System.out.println("❌ Erreur lors de la suppression de la vidéo de la playlist.");
                }
            } catch (Exception e) {
                System.err.println("❌ Erreur : " + e.getMessage());
            }
        } else {
            System.out.println("❌ Suppression annulée.");
        }
    }
    
    private static void afficherContenuPlaylist() {
        Playlist playlist = SelectionHelper.selectionnerPlaylist("AFFICHER LE CONTENU D'UNE PLAYLIST");
        
        if (playlist == null) {
            return;
        }
        
        // Charger le contenu complet de la playlist
        playlist = playlistService.getPlaylistAvecVideos(playlist.getId());
        playlistService.afficherPlaylist(playlist);
    }
    
    private static void menuScannerDossiers() {
        System.out.println("\n--- SCANNER DES DOSSIERS ---");
        System.out.println("Entrez les chemins des dossiers à scanner (séparés par des points-virgules) :");
        System.out.println("Exemple : C:\\Videos;D:\\Films;E:\\Séries");
        
        String chemins = lireChaine("Chemins : ");
        
        if (chemins.trim().isEmpty()) {
            System.out.println("Aucun chemin spécifié.");
            return;
        }
        
        List<String> listeChemins = Arrays.asList(chemins.split(";"));
        
        System.out.println("\nScan en cours...");
        ArrayList<Video> videosTrouvees = scanner.scannerDossiers(listeChemins);
        
        if (videosTrouvees.isEmpty()) {
            System.out.println("Aucune nouvelle vidéo trouvée.");
            return;
        }
        
        scanner.afficherResultatsScan(videosTrouvees);
        
        String ajouter = lireChaine("Voulez-vous ajouter ces vidéos à la base de données ? (O/N) : ");
        
        if (ajouter.equalsIgnoreCase("O")) {
            scanner.ajouterVideosTrouvees(videosTrouvees);
        } else {
            System.out.println("Aucune vidéo n'a été ajoutée.");
        }
    }
    
    private static void menuStatistiques() {
        System.out.println("\n--- STATISTIQUES ---");
        
        int totalVideos = videoService.getNombreTotalVideos();
        int totalPlaylists = playlistService.getNombreTotalPlaylists();
        
        System.out.println("Nombre total de vidéos : " + totalVideos);
        System.out.println("Nombre total de playlists : " + totalPlaylists);
        
        System.out.println("\n--- VIDÉOS LES PLUS VUES (TOP 10) ---");
        ArrayList<Video> videosPlusVues = videoService.getVideosPlusVues(10);
        videoService.afficherVideos(videosPlusVues);
        
        System.out.println("\n--- CATÉGORIES ---");
        ArrayList<String> categories = videoService.getCategoriesDisponibles();
        System.out.println("Nombre de catégories : " + categories.size());
        for (String categorie : categories) {
            ArrayList<Video> videosCategorie = videoService.rechercherVideosParCategorie(categorie);
            System.out.println("- " + categorie + " : " + videosCategorie.size() + " vidéo(s)");
        }
    }
    
    // Méthodes utilitaires pour la saisie
    private static String lireChaine(String message) {
        System.out.print(message);
        return scannerInput.nextLine();
    }
    
    private static int lireEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scannerInput.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier valide.");
            }
        }
    }
}