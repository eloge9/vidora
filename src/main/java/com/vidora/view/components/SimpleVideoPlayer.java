package com.vidora.view.components;

import com.vidora.model.Video;

import javax.swing.*;
import java.awt.*;
import java.awt.Desktop;
import java.io.File;

/**
 * Lecteur vidéo simple qui ouvre directement avec le lecteur système
 */
public class SimpleVideoPlayer {
    
    public static void playVideo(Video video) {
        if (video == null || video.getChemin() == null || video.getChemin().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Aucune vidéo sélectionnée", 
                "Erreur", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            File videoFile = new File(video.getChemin());
            
            if (!videoFile.exists()) {
                JOptionPane.showMessageDialog(null, 
                    "Le fichier vidéo n'existe pas:\n" + video.getChemin(), 
                    "Fichier introuvable", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ouvrir avec le lecteur système par défaut
            Desktop desktop = Desktop.getDesktop();
            desktop.open(videoFile);
            
            System.out.println("✅ Vidéo ouverte avec le lecteur système: " + video.getTitre());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Impossible d'ouvrir la vidéo:\n" + e.getMessage(), 
                "Erreur de lecture", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
