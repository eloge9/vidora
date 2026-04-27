package com.vidora.utils;

import com.vidora.model.Video;
import com.vidora.model.Playlist;
import com.vidora.service.VideoService;
import com.vidora.service.PlaylistService;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe utilitaire pour gérer la sélection interactive des vidéos et playlists
 * Améliore l'UX en évitant la saisie directe d'IDs
 */
public class SelectionHelper {
    
    private static VideoService videoService = new VideoService();
    private static PlaylistService playlistService = new PlaylistService();
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Permet à l'utilisateur de sélectionner une vidéo de manière interactive
     * @return La vidéo sélectionnée ou null si annulation
     */
    public static Video selectionnerVideo(String titreMenu) {
        System.out.println("\n" + titreMenu);
        System.out.println("========================================");
        System.out.println("1. Voir toutes les vidéos");
        System.out.println("2. Rechercher une vidéo par titre");
        System.out.println("3. Rechercher une vidéo par catégorie");
        System.out.println("4. Retour");
        System.out.println("----------------------------------------");
        
        int choix = lireEntier("Votre choix : ");
        
        ArrayList<Video> videos = new ArrayList<>();
        
        switch (choix) {
            case 1:
                videos = videoService.getAllVideos();
                break;
            case 2:
                String titre = lireChaine("Entrez un mot-clé dans le titre : ");
                videos = videoService.rechercherVideosParTitre(titre);
                break;
            case 3:
                String categorie = lireChaine("Entrez la catégorie : ");
                videos = videoService.rechercherVideosParCategorie(categorie);
                break;
            case 4:
                return null;
            default:
                System.out.println("Choix invalide.");
                return null;
        }
        
        if (videos.isEmpty()) {
            System.out.println("Aucune vidéo trouvée.");
            return null;
        }
        
        return selectionnerVideoDansListe(videos);
    }
    
    /**
     * Affiche une liste numérotée de vidéos et permet la sélection
     */
    public static Video selectionnerVideoDansListe(ArrayList<Video> videos) {
        System.out.println("\n--- SÉLECTIONNER UNE VIDÉO ---");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-4s %-35s %-15s %-10s %-8s%n", 
                         "N°", "TITRE", "CATÉGORIE", "DURÉE", "VUES");
        System.out.println("------------------------------------------------------------");
        
        for (int i = 0; i < videos.size(); i++) {
            Video video = videos.get(i);
            System.out.printf("%-4d %-35s %-15s %-10s %-8d%n",
                            i + 1,
                            tronquer(video.getTitre(), 35),
                            tronquer(video.getCategorie(), 15),
                            video.getDureeFormatee(),
                            video.getVues());
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.println("0. Annuler");
        
        int selection = lireEntier("Entrez le numéro de la vidéo : ");
        
        if (selection == 0) {
            return null;
        }
        
        if (selection < 1 || selection > videos.size()) {
            System.out.println("Numéro invalide.");
            return null;
        }
        
        Video videoSelectionnee = videos.get(selection - 1);
        System.out.println("\nVidéo sélectionnée : " + videoSelectionnee.getTitre());
        return videoSelectionnee;
    }
    
    /**
     * Permet à l'utilisateur de sélectionner une playlist de manière interactive
     */
    public static Playlist selectionnerPlaylist(String titreMenu) {
        System.out.println("\n" + titreMenu);
        System.out.println("========================================");
        System.out.println("1. Voir toutes les playlists");
        System.out.println("2. Rechercher une playlist par nom");
        System.out.println("3. Retour");
        System.out.println("----------------------------------------");
        
        int choix = lireEntier("Votre choix : ");
        
        ArrayList<Playlist> playlists = new ArrayList<>();
        
        switch (choix) {
            case 1:
                playlists = playlistService.getAllPlaylists();
                break;
            case 2:
                String nom = lireChaine("Entrez un mot-clé dans le nom : ");
                playlists = rechercherPlaylistsParNom(nom);
                break;
            case 3:
                return null;
            default:
                System.out.println("Choix invalide.");
                return null;
        }
        
        if (playlists.isEmpty()) {
            System.out.println("Aucune playlist trouvée.");
            return null;
        }
        
        return selectionnerPlaylistDansListe(playlists);
    }
    
    /**
     * Affiche une liste numérotée de playlists et permet la sélection
     */
    public static Playlist selectionnerPlaylistDansListe(ArrayList<Playlist> playlists) {
        System.out.println("\n--- SÉLECTIONNER UNE PLAYLIST ---");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-4s %-30s %-15s %-15s %-20s%n", 
                         "N°", "NOM", "NBR VIDÉOS", "DURÉE TOTALE", "DATE CRÉATION");
        System.out.println("-----------------------------------------------------------------");
        
        for (int i = 0; i < playlists.size(); i++) {
            Playlist playlist = playlists.get(i);
            System.out.printf("%-4d %-30s %-15d %-15s %-20s%n",
                            i + 1,
                            tronquer(playlist.getNom(), 30),
                            playlist.getNombreVideos(),
                            playlist.getDureeTotaleFormatee(),
                            playlist.getDateCreation().toLocalDate());
        }
        
        System.out.println("-----------------------------------------------------------------");
        System.out.println("0. Annuler");
        
        int selection = lireEntier("Entrez le numéro de la playlist : ");
        
        if (selection == 0) {
            return null;
        }
        
        if (selection < 1 || selection > playlists.size()) {
            System.out.println("Numéro invalide.");
            return null;
        }
        
        Playlist playlistSelectionnee = playlists.get(selection - 1);
        // Charger les vidéos de la playlist
        playlistSelectionnee = playlistService.getPlaylistAvecVideos(playlistSelectionnee.getId());
        System.out.println("\nPlaylist sélectionnée : " + playlistSelectionnee.getNom());
        return playlistSelectionnee;
    }
    
    /**
     * Permet de sélectionner plusieurs vidéos (pour les playlists)
     */
    public static ArrayList<Video> selectionnerVideosMultiples() {
        ArrayList<Video> videosSelectionnees = new ArrayList<>();
        
        while (true) {
            System.out.println("\n--- AJOUTER DES VIDÉOS ---");
            System.out.println("Vidéos sélectionnées : " + videosSelectionnees.size());
            System.out.println("1. Ajouter une vidéo");
            System.out.println("2. Voir les vidéos sélectionnées");
            System.out.println("3. Terminer");
            
            int choix = lireEntier("Votre choix : ");
            
            switch (choix) {
                case 1:
                    Video video = selectionnerVideo("AJOUTER UNE VIDÉO");
                    if (video != null) {
                        // Vérifier si la vidéo n'est pas déjà sélectionnée
                        boolean dejaSelectionnee = false;
                        for (Video v : videosSelectionnees) {
                            if (v.getId() == video.getId()) {
                                dejaSelectionnee = true;
                                break;
                            }
                        }
                        
                        if (!dejaSelectionnee) {
                            videosSelectionnees.add(video);
                            System.out.println("Vidéo ajoutée : " + video.getTitre());
                        } else {
                            System.out.println("Cette vidéo est déjà sélectionnée.");
                        }
                    }
                    break;
                    
                case 2:
                    if (videosSelectionnees.isEmpty()) {
                        System.out.println("Aucune vidéo sélectionnée.");
                    } else {
                        System.out.println("\n--- VIDÉOS SÉLECTIONNÉES ---");
                        for (int i = 0; i < videosSelectionnees.size(); i++) {
                            Video videoSelectionnee = videosSelectionnees.get(i);
                            System.out.println((i + 1) + ". " + videoSelectionnee.getTitre() + 
                                             " (" + videoSelectionnee.getDureeFormatee() + ")");
                        }
                    }
                    break;
                    
                case 3:
                    return videosSelectionnees;
                    
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    
    /**
     * Recherche des playlists par nom (recherche partielle)
     */
    private static ArrayList<Playlist> rechercherPlaylistsParNom(String nom) {
        ArrayList<Playlist> toutesPlaylists = playlistService.getAllPlaylists();
        ArrayList<Playlist> resultats = new ArrayList<>();
        
        for (Playlist playlist : toutesPlaylists) {
            if (playlist.getNom().toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(playlist);
            }
        }
        
        return resultats;
    }
    
    /**
     * Affiche un message de confirmation
     */
    public static boolean confirmer(String message) {
        String reponse = lireChaine(message + " (O/N) : ");
        return reponse.equalsIgnoreCase("O") || reponse.equalsIgnoreCase("OUI");
    }
    
    /**
     * Affiche un menu de confirmation pour les opérations destructives
     */
    public static boolean confirmerSuppression(String element, String nom) {
        System.out.println("\n⚠️  CONFIRMATION DE SUPPRESSION ⚠️");
        System.out.println("Élément : " + element);
        System.out.println("Nom : " + nom);
        System.out.println("Cette action est irréversible !");
        
        String reponse = lireChaine("Confirmer la suppression (O/N) : ");
        return reponse.equalsIgnoreCase("O") || reponse.equalsIgnoreCase("OUI");
    }
    
    // Méthodes utilitaires
    private static String lireChaine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
    
    private static int lireEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier valide.");
            }
        }
    }
    
    private static String tronquer(String texte, int longueurMax) {
        if (texte == null) {
            return "";
        }
        if (texte.length() <= longueurMax) {
            return texte;
        }
        return texte.substring(0, longueurMax - 3) + "...";
    }
}
