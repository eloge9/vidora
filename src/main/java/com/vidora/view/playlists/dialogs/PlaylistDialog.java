package com.vidora.view.playlists.dialogs;

import com.vidora.controller.MainController;
import com.vidora.model.Playlist;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dialogue pour créer ou modifier une playlist
 */
public class PlaylistDialog extends JDialog {
    private MainController controller;
    private Playlist playlist;
    private boolean confirmed = false;
    
    // Champs du formulaire
    private JTextField nameField;
    
    public PlaylistDialog(JFrame parent, String title, Playlist playlist, MainController controller) {
        super(parent, title, true);
        this.controller = controller;
        this.playlist = playlist;
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadPlaylistData();
        
        pack();
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(400, 0));
    }
    
    private void initComponent() {
        setBackground(com.vidora.view.MainView.PANEL_BG);
        getContentPane().setBackground(com.vidora.view.MainView.PANEL_BG);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Panneau principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nom de la playlist
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(createLabel("Nom de la playlist *:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        nameField = createTextField();
        mainPanel.add(nameField, gbc);
        
        // Informations supplémentaires
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 0;
        JLabel infoLabel = new JLabel("<html><i style='color: #888;'>Le nom sera utilisé pour identifier votre playlist dans l'application.</i></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        mainPanel.add(infoLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panneau de boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(new Color(158, 158, 158));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());
        
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> savePlaylist());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(50, 50, 60));
        field.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setCaretColor(com.vidora.view.MainView.TEXT_COLOR);
        return field;
    }
    
    private void setupEventHandlers() {
        // Validation en temps réel
        nameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateRealTime(); }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateRealTime(); }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateRealTime(); }
        });
        
        // Enter pour sauvegarder
        getRootPane().setDefaultButton(createSaveButton());
    }
    
    private JButton createSaveButton() {
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> savePlaylist());
        return saveButton;
    }
    
    private void loadPlaylistData() {
        if (playlist != null) {
            nameField.setText(playlist.getNom());
        }
    }
    
    private void savePlaylist() {
        if (!validateForm()) {
            return;
        }
        
        try {
            String nom = nameField.getText().trim();
            
            if (playlist == null) {
                // Nouvelle playlist
                playlist = new Playlist(nom);
            } else {
                // Modification
                playlist.setNom(nom);
            }
            
            confirmed = true;
            dispose();
            
        } catch (Exception e) {
            controller.showError("Erreur", "Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            controller.showError("Erreur de validation", "Le nom de la playlist est obligatoire.");
            nameField.requestFocus();
            return false;
        }
        
        // Vérifier que le nom n'est pas trop long
        if (nameField.getText().trim().length() > 255) {
            controller.showError("Erreur de validation", "Le nom de la playlist ne peut pas dépasser 255 caractères.");
            nameField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void validateRealTime() {
        // Changer la couleur de la bordure en fonction de la validation
        if (nameField.getText().trim().isEmpty()) {
            nameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(244, 67, 54), 1),
                    new EmptyBorder(10, 12, 10, 12)
            ));
        } else if (nameField.getText().trim().length() > 255) {
            nameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(com.vidora.view.MainView.ERROR_COLOR, 1),
                    new EmptyBorder(10, 12, 10, 12)
            ));
        } else {
            nameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(com.vidora.view.MainView.SUCCESS_COLOR, 1),
                    new EmptyBorder(10, 12, 10, 12)
            ));
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Playlist getPlaylist() {
        return playlist;
    }
}
