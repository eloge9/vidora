package com.vidora.dao;

import com.vidora.model.Video;
import com.vidora.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class VideoDAO {
    
    public boolean ajouter(Video video) {
        String sql = "INSERT INTO video (titre, chemin, categorie, duree, vues, date_ajout) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, video.getTitre());
            pstmt.setString(2, video.getChemin());
            pstmt.setString(3, video.getCategorie());
            pstmt.setInt(4, video.getDuree());
            pstmt.setInt(5, video.getVues());
            pstmt.setTimestamp(6, Timestamp.valueOf(video.getDateAjout()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    video.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la vidéo : " + e.getMessage());
        }
        return false;
    }
    
    public boolean modifier(Video video) {
        String sql = "UPDATE video SET titre = ?, chemin = ?, categorie = ?, duree = ?, vues = ? WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, video.getTitre());
            pstmt.setString(2, video.getChemin());
            pstmt.setString(3, video.getCategorie());
            pstmt.setInt(4, video.getDuree());
            pstmt.setInt(5, video.getVues());
            pstmt.setInt(6, video.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la vidéo : " + e.getMessage());
        }
        return false;
    }
    
    public boolean supprimer(int id) {
        String sql = "DELETE FROM video WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la vidéo : " + e.getMessage());
        }
        return false;
    }
    
    public Video trouverParId(int id) {
        String sql = "SELECT * FROM video WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVideo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la vidéo : " + e.getMessage());
        }
        return null;
    }
    
    public ArrayList<Video> trouverToutes() {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM video ORDER BY date_ajout DESC";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des vidéos : " + e.getMessage());
        }
        return videos;
    }
    
    public ArrayList<Video> rechercherParTitre(String titre) {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM video WHERE titre LIKE ? ORDER BY titre";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + titre + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par titre : " + e.getMessage());
        }
        return videos;
    }
    
    public ArrayList<Video> rechercherParCategorie(String categorie) {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM video WHERE categorie = ? ORDER BY titre";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, categorie);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par catégorie : " + e.getMessage());
        }
        return videos;
    }
    
    public ArrayList<Video> trierParDuree(boolean croissant) {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM video ORDER BY duree " + (croissant ? "ASC" : "DESC");
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du tri par durée : " + e.getMessage());
        }
        return videos;
    }
    
    public ArrayList<Video> trierParDateAjout(boolean croissant) {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM video ORDER BY date_ajout " + (croissant ? "ASC" : "DESC");
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du tri par date d'ajout : " + e.getMessage());
        }
        return videos;
    }
    
    public ArrayList<Video> trierParVues(boolean croissant) {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM video ORDER BY vues " + (croissant ? "ASC" : "DESC");
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du tri par nombre de vues : " + e.getMessage());
        }
        return videos;
    }
    
    public boolean incrementerVues(int id) {
        String sql = "UPDATE video SET vues = vues + 1 WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'incrémentation des vues : " + e.getMessage());
        }
        return false;
    }
    
    public ArrayList<Video> getVideosPlusVues(int limite) {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM video ORDER BY vues DESC LIMIT ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des vidéos les plus vues : " + e.getMessage());
        }
        return videos;
    }
    
    public int getNombreTotalVideos() {
        String sql = "SELECT COUNT(*) FROM video";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des vidéos : " + e.getMessage());
        }
        return 0;
    }
    
    private Video mapResultSetToVideo(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titre = rs.getString("titre");
        String chemin = rs.getString("chemin");
        String categorie = rs.getString("categorie");
        int duree = rs.getInt("duree");
        int vues = rs.getInt("vues");
        LocalDateTime dateAjout = rs.getTimestamp("date_ajout").toLocalDateTime();
        
        return new Video(id, titre, chemin, categorie, duree, vues, dateAjout);
    }
}
