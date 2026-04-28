package com.vidora.view.videos.dialogs;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * Dialogue pour ajouter ou modifier une vidéo
 */
public class VideoDialog extends JDialog {
    private MainController controller;
    private Video video;
    private boolean confirmed = false;
    
    // Champs du formulaire
    private JTextField titleField;
    private JTextField pathField;
    private JTextField categoryField;
    private JSpinner durationSpinner;
    private JButton browseButton;
    
    public VideoDialog(JFrame parent, String title, Video video, MainController controller) {
        super(parent, title, true);
        this.controller = controller;
        this.video = video;
        
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadVideoData();
        
        pack();
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(500, 0));
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titre
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(createLabel("Titre *:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        titleField = createTextField();
        mainPanel.add(titleField, gbc);
        
        // Chemin
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        mainPanel.add(createLabel("Chemin *:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        pathField = createTextField();
        browseButton = new JButton("📁 Parcourir");
        browseButton.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        browseButton.setForeground(Color.WHITE);
        browseButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        browseButton.setFocusPainted(false);
        browseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pathPanel.add(pathField, BorderLayout.CENTER);
        pathPanel.add(browseButton, BorderLayout.EAST);
        mainPanel.add(pathPanel, gbc);
        
        // Catégorie
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        mainPanel.add(createLabel("Catégorie:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        categoryField = createTextField();
        mainPanel.add(categoryField, gbc);
        
        // Durée
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        mainPanel.add(createLabel("Durée (secondes)*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        durationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        durationSpinner.setBackground(com.vidora.view.MainView.CARD_BG);
        durationSpinner.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        durationSpinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        durationSpinner.setEditor(new JSpinner.NumberEditor(durationSpinner, "#"));
        mainPanel.add(durationSpinner, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panneau de boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(com.vidora.view.MainView.PANEL_BG);
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(com.vidora.view.MainView.SECONDARY_TEXT);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());
        
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(com.vidora.view.MainView.SUCCESS_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveVideo());
        
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
        field.setBackground(com.vidora.view.MainView.CARD_BG);
        field.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setCaretColor(com.vidora.view.MainView.TEXT_COLOR);
        return field;
    }
    
    private void setupEventHandlers() {
        browseButton.addActionListener(e -> browseVideoFile());
        
        // Validation en temps réel
        titleField.getDocument().addDocumentListener(new ValidationListener());
        pathField.getDocument().addDocumentListener(new ValidationListener());
        
        // Enter pour sauvegarder
        getRootPane().setDefaultButton(createSaveButton());
    }
    
    private JButton createSaveButton() {
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(com.vidora.view.MainView.SUCCESS_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveVideo());
        return saveButton;
    }
    
    private void browseVideoFile() {
        File selectedFile = controller.selectVideoFile(this);
        if (selectedFile != null) {
            pathField.setText(selectedFile.getAbsolutePath());
            
            // Extraire le titre du nom de fichier si le champ titre est vide
            if (titleField.getText().trim().isEmpty()) {
                String fileName = selectedFile.getName();
                String title = fileName.substring(0, fileName.lastIndexOf('.'));
                title = title.replace('_', ' ').replace('-', ' ');
                title = title.replaceAll("\\s+", " ").trim();
                
                // Capitaliser les mots
                String[] words = title.split(" ");
                StringBuilder formattedTitle = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        if (formattedTitle.length() > 0) {
                            formattedTitle.append(" ");
                        }
                        formattedTitle.append(Character.toUpperCase(word.charAt(0)))
                                     .append(word.substring(1).toLowerCase());
                    }
                }
                titleField.setText(formattedTitle.toString());
            }
        }
    }
    
    private void loadVideoData() {
        if (video != null) {
            titleField.setText(video.getTitre());
            pathField.setText(video.getChemin());
            categoryField.setText(video.getCategorie());
            durationSpinner.setValue(video.getDuree());
        } else {
            // Valeurs par défaut pour une nouvelle vidéo
            categoryField.setText("Non classée");
        }
    }
    
    private void saveVideo() {
        if (!validateForm()) {
            return;
        }
        
        try {
            String titre = titleField.getText().trim();
            String chemin = pathField.getText().trim();
            String categorie = categoryField.getText().trim();
            int duree = (Integer) durationSpinner.getValue();
            
            if (categorie.isEmpty()) {
                categorie = "Non classée";
            }
            
            if (video == null) {
                // Nouvelle vidéo
                video = new Video(titre, chemin, categorie, duree);
            } else {
                // Modification
                video.setTitre(titre);
                video.setChemin(chemin);
                video.setCategorie(categorie);
                video.setDuree(duree);
            }
            
            confirmed = true;
            dispose();
            
        } catch (Exception e) {
            controller.showError("Erreur", "Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (titleField.getText().trim().isEmpty()) {
            errors.append("• Le titre est obligatoire\n");
        }
        
        if (pathField.getText().trim().isEmpty()) {
            errors.append("• Le chemin est obligatoire\n");
        } else {
            // Vérifier que le fichier existe
            File file = new File(pathField.getText().trim());
            if (!file.exists()) {
                errors.append("• Le fichier vidéo n'existe pas\n");
            }
        }
        
        if ((Integer) durationSpinner.getValue() < 0) {
            errors.append("• La durée ne peut pas être négative\n");
        }
        
        if (errors.length() > 0) {
            controller.showError("Erreurs de validation", errors.toString());
            return false;
        }
        
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Video getVideo() {
        return video;
    }
    
    /**
     * Listener pour la validation en temps réel
     */
    private class ValidationListener implements javax.swing.event.DocumentListener {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            validateRealTime();
        }
        
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            validateRealTime();
        }
        
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            validateRealTime();
        }
        
        private void validateRealTime() {
            // Changer la couleur des bordures en fonction de la validation
            if (titleField.getText().trim().isEmpty()) {
                titleField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(244, 67, 54), 1),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            } else {
                titleField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(com.vidora.view.MainView.SIDEBAR_BG, 1),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
            
            if (pathField.getText().trim().isEmpty()) {
                pathField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(244, 67, 54), 1),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            } else {
                File file = new File(pathField.getText().trim());
                if (file.exists()) {
                    pathField.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(76, 175, 80), 1),
                            new EmptyBorder(8, 12, 8, 12)
                    ));
                } else {
                    pathField.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(244, 67, 54), 1),
                            new EmptyBorder(8, 12, 8, 12)
                    ));
                }
            }
        }
    }
}
