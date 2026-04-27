package com.vidora.dao;

import com.vidora.model.Playlist;
import com.vidora.model.Video;
import com.vidora.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlaylistDAO {
    
    public boolean ajouter(Playlist playlist) {
        String sql = "INSERT INTO playlist (nom, date_creation) VALUES (?, ?)";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, playlist.getNom());
            pstmt.setTimestamp(2, Timestamp.valueOf(playlist.getDateCreation()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    playlist.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la playlist : " + e.getMessage());
        }
        return false;
    }
    
    public boolean modifier(Playlist playlist) {
        String sql = "UPDATE playlist SET nom = ? WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, playlist.getNom());
            pstmt.setInt(2, playlist.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la playlist : " + e.getMessage());
        }
        return false;
    }
    
    public boolean supprimer(int id) {
        try (Connection conn = Database.connect()) {
            // Supprimer d'abord les associations dans playlist_video
            String sqlAssociations = "DELETE FROM playlist_video WHERE id_playlist = ?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlAssociations)) {
                pstmt1.setInt(1, id);
                pstmt1.executeUpdate();
            }
            
            // Supprimer la playlist
            String sqlPlaylist = "DELETE FROM playlist WHERE id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlPlaylist)) {
                pstmt2.setInt(1, id);
                return pstmt2.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la playlist : " + e.getMessage());
        }
        return false;
    }
    
    public Playlist trouverParId(int id) {
        String sql = "SELECT * FROM playlist WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Playlist playlist = mapResultSetToPlaylist(rs);
                playlist.setVideos(getVideosDePlaylist(id));
                return playlist;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la playlist : " + e.getMessage());
        }
        return null;
    }
    
    public ArrayList<Playlist> trouverToutes() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlist ORDER BY date_creation DESC";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Playlist playlist = mapResultSetToPlaylist(rs);
                playlist.setVideos(getVideosDePlaylist(playlist.getId()));
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des playlists : " + e.getMessage());
        }
        return playlists;
    }
    
    public boolean ajouterVideoAPlaylist(int idPlaylist, int idVideo) {
        String sql = "INSERT INTO playlist_video (id_playlist, id_video) VALUES (?, ?)";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPlaylist);
            pstmt.setInt(2, idVideo);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la vidéo à la playlist : " + e.getMessage());
        }
        return false;
    }
    
    public boolean supprimerVideoDePlaylist(int idPlaylist, int idVideo) {
        String sql = "DELETE FROM playlist_video WHERE id_playlist = ? AND id_video = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPlaylist);
            pstmt.setInt(2, idVideo);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la vidéo de la playlist : " + e.getMessage());
        }
        return false;
    }
    
    public ArrayList<Video> getVideosDePlaylist(int idPlaylist) {
        ArrayList<Video> videos = new ArrayList<>();
        String sql = "SELECT v.* FROM video v " +
                    "JOIN playlist_video pv ON v.id = pv.id_video " +
                    "WHERE pv.id_playlist = ? " +
                    "ORDER BY v.titre";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPlaylist);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                videos.add(mapResultSetToVideo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des vidéos de la playlist : " + e.getMessage());
        }
        return videos;
    }
    
    public boolean videoEstDansPlaylist(int idPlaylist, int idVideo) {
        String sql = "SELECT COUNT(*) FROM playlist_video WHERE id_playlist = ? AND id_video = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPlaylist);
            pstmt.setInt(2, idVideo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la présence de la vidéo dans la playlist : " + e.getMessage());
        }
        return false;
    }
    
    public int getNombreTotalPlaylists() {
        String sql = "SELECT COUNT(*) FROM playlist";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des playlists : " + e.getMessage());
        }
        return 0;
    }
    
    private Playlist mapResultSetToPlaylist(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nom = rs.getString("nom");
        LocalDateTime dateCreation = rs.getTimestamp("date_creation").toLocalDateTime();
        
        return new Playlist(id, nom, dateCreation);
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
