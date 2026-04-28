package com.vidora.utils;

import com.vidora.model.Video;
import com.vidora.service.VideoService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoScanner {
    
    private static final List<String> EXTENSIONS_VIDEO = Arrays.asList(
            ".mp4", ".avi", ".mkv", ".mov", ".wmv", ".flv", ".webm", ".m4v", ".3gp"
    );
    
    private VideoService videoService;
    
    public VideoScanner() {
        this.videoService = new VideoService();
    }
    
    public ArrayList<Video> scannerDossiers(List<String> cheminsDossiers) {
        ArrayList<Video> videosTrouvees = new ArrayList<>();
        
        if (cheminsDossiers == null || cheminsDossiers.isEmpty()) {
            System.out.println("Aucun dossier à scanner.");
            return videosTrouvees;
        }
        
        for (String cheminDossier : cheminsDossiers) {
            String cheminCorrige = corrigerEncodageChemin(cheminDossier.trim());
            
            // Si le chemin n'existe pas, essayer de trouver automatiquement
            if (!new File(cheminCorrige).exists()) {
                System.out.println("Chemin non trouvé, recherche automatique...");
                cheminCorrige = trouverCheminAutomatique(cheminDossier);
            }
            
            if (cheminCorrige != null && new File(cheminCorrige).exists()) {
                System.out.println("Scan du dossier : " + cheminCorrige);
                ArrayList<Video> videosDossier = scannerDossier(cheminCorrige);
                videosTrouvees.addAll(videosDossier);
                System.out.println("Trouvé " + videosDossier.size() + " vidéo(s) dans ce dossier.");
            } else {
                System.out.println("Aucun dossier valide trouvé pour : " + cheminDossier);
            }
        }
        
        return videosTrouvees;
    }
    
    /**
     * Trouve automatiquement le chemin correspondant même avec des problèmes d'encodage
     */
    private String trouverCheminAutomatique(String cheminOriginal) {
        try {
            String username = System.getProperty("user.name");
            System.out.println("Recherche automatique pour l'utilisateur : " + username);
            
            // Détecter le type de dossier demandé
            String dossierCible = "";
            if (cheminOriginal.contains("Video") || cheminOriginal.contains("Vidéo") || cheminOriginal.contains("Vid?o")) {
                dossierCible = "Video";
            } else if (cheminOriginal.contains("Film") || cheminOriginal.contains("Movie")) {
                dossierCible = "Films";
            } else if (cheminOriginal.contains("Serie") || cheminOriginal.contains("Série")) {
                dossierCible = "Séries";
            }
            
            // Dossiers parents à tester
            String[] dossiersParents = {
                "C:\\Users\\" + username + "\\Téléchargements",
                "C:\\Users\\" + username + "\\Downloads",
                "C:\\Users\\" + username + "\\Documents",
                "C:\\Users\\" + username + "\\Desktop",
                "C:\\Users\\" + username + "\\Videos",
                "C:\\Users\\" + username + "\\My Videos",
                "C:\\Videos",
                "D:\\Videos",
                "E:\\Videos"
            };
            
            // Si un dossier spécifique est demandé
            if (!dossierCible.isEmpty()) {
                for (String parent : dossiersParents) {
                    File dossierParent = new File(parent);
                    if (dossierParent.exists()) {
                        // Chercher le sous-dossier vidéo
                        File[] sousDossiers = dossierParent.listFiles(File::isDirectory);
                        if (sousDossiers != null) {
                            for (File sousDossier : sousDossiers) {
                                String nomSousDossier = sousDossier.getName().toLowerCase();
                                if (nomSousDossier.contains("video") || 
                                    nomSousDossier.contains("vidéo") ||
                                    nomSousDossier.contains("vid?o") ||
                                    nomSousDossier.equals(dossierCible.toLowerCase()) ||
                                    nomSousDossier.equals(dossierCible.toLowerCase() + "s")) {
                                    
                                    System.out.println("Dossier trouvé automatiquement : " + sousDossier.getAbsolutePath());
                                    return sousDossier.getAbsolutePath();
                                }
                            }
                        }
                        
                        // Essayer directement avec le nom du dossier cible
                        File dossierCibleFile = new File(parent + "\\" + dossierCible);
                        if (dossierCibleFile.exists()) {
                            System.out.println("Dossier trouvé directement : " + dossierCibleFile.getAbsolutePath());
                            return dossierCibleFile.getAbsolutePath();
                        }
                    }
                }
            }
            
            // Si aucun dossier spécifique, scanner les dossiers parents
            for (String parent : dossiersParents) {
                File dossierParent = new File(parent);
                if (dossierParent.exists()) {
                    System.out.println("Dossier parent trouvé : " + parent);
                    return parent;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche automatique : " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Corrige les problèmes d'encodage des caractères accentués dans les chemins
     */
    private String corrigerEncodageChemin(String chemin) {
        if (chemin == null || chemin.isEmpty()) {
            return chemin;
        }
        
        System.out.println("Chemin original : " + chemin);
        
        // Détecter si c'est un chemin avec Téléchargements problématique
        if (chemin.contains("T?l?chargements") || chemin.contains("T?l?chargement")) {
            String cheminCorrige = corrigerCheminTelechargements(chemin);
            if (cheminCorrige != null) {
                return cheminCorrige;
            }
        }
        
        // Liste des encodages à tester
        String[] encodages = {"UTF-8", "ISO-8859-1", "Windows-1252", "Cp1252"};
        
        // Tester différentes combinaisons d'encodage
        for (String encFrom : encodages) {
            for (String encTo : encodages) {
                if (!encFrom.equals(encTo)) {
                    try {
                        byte[] bytes = chemin.getBytes(encFrom);
                        String corrige = new String(bytes, encTo);
                        
                        File testFile = new File(corrige);
                        if (testFile.exists()) {
                            System.out.println("Chemin corrigé : " + corrige + " (" + encFrom + " -> " + encTo + ")");
                            return corrige;
                        }
                    } catch (Exception e) {
                        // Ignorer les erreurs et continuer
                    }
                }
            }
        }
        
        // Méthode alternative : remplacer manuellement les caractères problématiques
        String corrige = chemin.replace("T?l?chargements", "Téléchargements")
                              .replace("T?l?chargement", "Téléchargement")
                              .replace("T?l?charg", "Télécharg");
        
        File testFile = new File(corrige);
        if (testFile.exists()) {
            System.out.println("Chemin corrigé par remplacement : " + corrige);
            return corrige;
        }
        
        System.out.println("Impossible de corriger le chemin, utilisation de l'original");
        return chemin;
    }
    
    /**
     * Corrige spécifiquement les chemins avec Téléchargements
     */
    private String corrigerCheminTelechargements(String chemin) {
        try {
            // Récupérer le nom d'utilisateur actuel
            String username = System.getProperty("user.name");
            System.out.println("Nom d'utilisateur détecté : " + username);
            
            // Chemins possibles pour le dossier Téléchargements
            String[] cheminsPossibles = {
                "C:\\Users\\" + username + "\\Téléchargements",
                "C:\\Users\\" + username + "\\Downloads",
                "C:\\Users\\" + username + "\\Documents\\Téléchargements",
                "C:\\Users\\" + username + "\\Desktop\\Téléchargements"
            };
            
            // Extraire le sous-dossier après Téléchargements
            String sousDossier = "";
            if (chemin.contains("Video")) {
                sousDossier = "\\Video";
            } else if (chemin.contains("\\")) {
                int lastBackslash = chemin.lastIndexOf("\\");
                if (lastBackslash > 0) {
                    sousDossier = chemin.substring(lastBackslash);
                }
            }
            
            // Tester chaque chemin possible
            for (String cheminBase : cheminsPossibles) {
                String cheminComplet = cheminBase + sousDossier;
                File testFile = new File(cheminComplet);
                
                System.out.println("Test du chemin : " + cheminComplet);
                
                if (testFile.exists()) {
                    System.out.println("Chemin trouvé et corrigé : " + cheminComplet);
                    return cheminComplet;
                }
            }
            
            // Si le sous-dossier n'existe pas, essayer juste le dossier Téléchargements
            for (String cheminBase : cheminsPossibles) {
                File testFile = new File(cheminBase);
                if (testFile.exists()) {
                    System.out.println("Dossier Téléchargements trouvé : " + cheminBase);
                    System.out.println("Sous-dossier '" + sousDossier.substring(1) + "' non trouvé dans ce dossier.");
                    return cheminBase;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la correction du chemin Téléchargements : " + e.getMessage());
        }
        
        return null;
    }
    
    public ArrayList<Video> scannerDossier(String cheminDossier) {
        ArrayList<Video> videos = new ArrayList<>();
        File dossier = new File(cheminDossier);
        
        if (!dossier.exists()) {
            System.err.println("Le dossier n'existe pas : " + cheminDossier);
            return videos;
        }
        
        if (!dossier.isDirectory()) {
            System.err.println("Le chemin n'est pas un dossier : " + cheminDossier);
            return videos;
        }
        
        scannerRecursif(dossier, videos);
        
        return videos;
    }
    
    private void scannerRecursif(File dossier, ArrayList<Video> videos) {
        File[] fichiers = dossier.listFiles();
        
        if (fichiers == null) {
            System.out.println("Aucun fichier trouvé dans : " + dossier.getAbsolutePath());
            return;
        }
        
        System.out.println("Scan du dossier : " + dossier.getAbsolutePath() + " (" + fichiers.length + " éléments)");
        
        for (File fichier : fichiers) {
            if (fichier.isDirectory()) {
                System.out.println("-> Sous-dossier trouvé : " + fichier.getName());
                // Scanner récursivement les sous-dossiers
                scannerRecursif(fichier, videos);
            } else if (fichier.isFile()) {
                String nomFichier = fichier.getName().toLowerCase();
                System.out.println("-> Fichier trouvé : " + nomFichier);
                
                if (estVideo(fichier)) {
                    System.out.println("   *** VIDÉO DÉTECTÉE : " + fichier.getName());
                    Video video = creerVideoDepuisFichier(fichier);
                    if (video != null) {
                        videos.add(video);
                        System.out.println("   *** VIDÉO AJOUTÉE : " + video.getTitre());
                    } else {
                        System.out.println("   *** VIDÉO IGNOREE (existe déjà ou erreur)");
                    }
                }
            }
        }
    }
    
    private boolean estVideo(File fichier) {
        String nomFichier = fichier.getName().toLowerCase();
        System.out.println("   Test extension pour : " + nomFichier);
        
        for (String extension : EXTENSIONS_VIDEO) {
            if (nomFichier.endsWith(extension)) {
                System.out.println("   -> Extension reconnue : " + extension);
                return true;
            }
        }
        
        System.out.println("   -> Extension NON reconnue");
        return false;
    }
    
    private Video creerVideoDepuisFichier(File fichier) {
        try {
            String nomFichier = fichier.getName();
            String titre = extraireTitre(nomFichier);
            String chemin = fichier.getAbsolutePath();
            String categorie = extraireCategorie(fichier.getParentFile());
            int duree = 0; // Durée par défaut, pourrait être extraite avec des librairies externes
            
            Video video = new Video(titre, chemin, categorie, duree);
            
            // Vérifier si la vidéo existe déjà dans la base de données
            ArrayList<Video> videosExistantes = videoService.rechercherVideosParTitre(titre);
            for (Video videoExistante : videosExistantes) {
                if (videoExistante.getChemin().equals(chemin)) {
                    System.out.println("Vidéo déjà existante : " + titre);
                    return null;
                }
            }
            
            return video;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la vidéo depuis le fichier : " + fichier.getName());
            return null;
        }
    }
    
    private String extraireTitre(String nomFichier) {
        // Supprimer l'extension
        int dernierPoint = nomFichier.lastIndexOf('.');
        if (dernierPoint > 0) {
            nomFichier = nomFichier.substring(0, dernierPoint);
        }
        
        // Remplacer les caractères non désirés
        nomFichier = nomFichier.replace('_', ' ').replace('-', ' ');
        
        // Supprimer les espaces multiples
        nomFichier = nomFichier.replaceAll("\\s+", " ").trim();
        
        // Capitaliser les mots
        String[] mots = nomFichier.split(" ");
        StringBuilder titre = new StringBuilder();
        
        for (String mot : mots) {
            if (!mot.isEmpty()) {
                if (titre.length() > 0) {
                    titre.append(" ");
                }
                titre.append(Character.toUpperCase(mot.charAt(0)))
                     .append(mot.substring(1).toLowerCase());
            }
        }
        
        return titre.toString();
    }
    
    private String extraireCategorie(File dossierParent) {
        if (dossierParent == null) {
            return "Non classée";
        }
        
        String nomDossier = dossierParent.getName().toLowerCase();
        
        // Catégories communes basées sur les noms de dossiers
        if (nomDossier.contains("film") || nomDossier.contains("movie")) {
            return "Films";
        } else if (nomDossier.contains("serie") || nomDossier.contains("series") || nomDossier.contains("show")) {
            return "Séries";
        } else if (nomDossier.contains("documentaire") || nomDossier.contains("doc")) {
            return "Documentaires";
        } else if (nomDossier.contains("animation") || nomDossier.contains("anime") || nomDossier.contains("cartoon")) {
            return "Animation";
        } else if (nomDossier.contains("music") || nomDossier.contains("clip")) {
            return "Musique";
        } else if (nomDossier.contains("sport")) {
            return "Sport";
        } else if (nomDossier.contains("gameplay") || nomDossier.contains("jeu")) {
            return "Jeux vidéo";
        } else if (nomDossier.contains("tutorial") || nomDossier.contains("tuto")) {
            return "Tutoriels";
        } else if (nomDossier.contains("news") || nomDossier.contains("actualité")) {
            return "Actualités";
        }
        
        return "Non classée";
    }
    
    public int ajouterVideosTrouvees(ArrayList<Video> videos) {
        if (videos == null || videos.isEmpty()) {
            return 0;
        }
        
        int nbAjoutees = 0;
        System.out.println("\nAjout des vidéos dans la base de données...");
        
        for (Video video : videos) {
            try {
                if (videoService.ajouterVideo(video)) {
                    nbAjoutees++;
                    System.out.println("Ajoutée : " + video.getTitre());
                } else {
                    System.err.println("Erreur lors de l'ajout : " + video.getTitre());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'ajout de " + video.getTitre() + " : " + e.getMessage());
            }
        }
        
        System.out.println("\nTotal : " + nbAjoutees + "/" + videos.size() + " vidéo(s) ajoutée(s) avec succès.");
        return nbAjoutees;
    }
    
    public void afficherResultatsScan(ArrayList<Video> videos) {
        if (videos == null || videos.isEmpty()) {
            System.out.println("Aucune vidéo trouvée lors du scan.");
            return;
        }
        
        System.out.println("\n=== VIDÉOS TROUVÉES LORS DU SCAN ===");
        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-40s %-15s %-20s%n", "TITRE", "CATÉGORIE", "CHEMIN");
        System.out.println("-----------------------------------------------------------");
        
        for (Video video : videos) {
            System.out.printf("%-40s %-15s %-20s%n",
                            tronquer(video.getTitre(), 40),
                            tronquer(video.getCategorie(), 15),
                            tronquer(video.getChemin(), 60));
        }
        
        System.out.println("-----------------------------------------------------------");
        System.out.println("Total : " + videos.size() + " vidéo(s) trouvée(s)");
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
    
    public static List<String> getExtensionsSupportees() {
        return new ArrayList<>(EXTENSIONS_VIDEO);
    }
}
