package com.vidora.view.scanner;

import com.vidora.controller.MainController;
import com.vidora.model.Video;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class ScannerPanel extends JPanel {

    private MainController controller;

    private JTextField pathField;
    private JButton btnBrowse, btnScan, btnImport;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    private ArrayList<Video> scannedVideos;
    private boolean isScanning = false;

    // 🎨 palette dark moderne
    private final Color BG_MAIN = new Color(10, 10, 10);
    private final Color BG_CARD = new Color(20, 20, 20);
    private final Color ACCENT = new Color(255, 94, 0);

    public ScannerPanel(MainController controller) {
        this.controller = controller;
        this.scannedVideos = new ArrayList<>();

        initComponent();
        layoutComponents();
        setupEventHandlers();
    }

    private void initComponent() {
        setBackground(BG_MAIN);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void layoutComponents() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_MAIN);

        main.add(createHeaderPanel(), BorderLayout.NORTH);
        main.add(createControlPanel(), BorderLayout.CENTER);

        add(main);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_MAIN);

        JLabel title = new JLabel("🔍 Scanner de vidéos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Scanner et importer automatiquement vos vidéos");
        sub.setForeground(Color.GRAY);

        panel.add(title, BorderLayout.NORTH);
        panel.add(sub, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_MAIN);

        panel.add(createSelectionPanel(), BorderLayout.NORTH);
        panel.add(createResultsPanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50,50,50), 1, true),
                new EmptyBorder(20,20,20,20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("📁 Dossier à scanner");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        panel.add(label, gbc);

        pathField = new JTextField();
        pathField.setBackground(new Color(25,25,25));
        pathField.setForeground(Color.WHITE);
        pathField.setBorder(new EmptyBorder(10,10,10,10));
        pathField.setEditable(false);

        gbc.gridy=1; gbc.gridwidth=1; gbc.weightx=1;
        panel.add(pathField, gbc);

        btnBrowse = createButton("📂 Parcourir", new Color(255,165,0));
        gbc.gridx=1; gbc.weightx=0;
        panel.add(btnBrowse, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(BG_CARD);

        btnScan = createButton("🔍 Scanner", new Color(100,149,237));
        btnImport = createButton("📥 Importer", new Color(50,205,50));

        btnScan.setEnabled(false);
        btnImport.setEnabled(false);

        btnPanel.add(btnScan);
        btnPanel.add(btnImport);

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2;
        panel.add(btnPanel, gbc);

        progressBar = new JProgressBar();
        progressBar.setBackground(new Color(20,20,20));
        progressBar.setForeground(ACCENT);
        progressBar.setStringPainted(true);

        gbc.gridy=3;
        panel.add(progressBar, gbc);

        statusLabel = new JLabel("Sélectionnez un dossier...");
        statusLabel.setForeground(Color.GRAY);

        gbc.gridy=4;
        panel.add(statusLabel, gbc);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(20,0,0,0));

        JLabel title = new JLabel("Résultats");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        tableModel = new DefaultTableModel(new String[]{"Titre","Catégorie","Durée","Chemin"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };

        resultsTable = new JTable(tableModel){
            public Component prepareRenderer(TableCellRenderer r,int row,int col){
                Component c = super.prepareRenderer(r,row,col);

                if(!isRowSelected(row)){
                    c.setBackground(row%2==0?new Color(15,15,15):new Color(25,25,25));
                    c.setForeground(Color.WHITE);
                }else{
                    c.setBackground(ACCENT);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        resultsTable.setRowHeight(30);
        resultsTable.setShowVerticalLines(false);
        resultsTable.setIntercellSpacing(new Dimension(0,0));

        JTableHeader header = resultsTable.getTableHeader();
        header.setBackground(new Color(18,18,18));
        header.setForeground(Color.GRAY);

        JScrollPane scroll = new JScrollPane(resultsTable);
        scroll.getViewport().setBackground(new Color(15,15,15));
        scroll.setBorder(null);

        panel.add(title,BorderLayout.NORTH);
        panel.add(scroll,BorderLayout.CENTER);

        return panel;
    }

    // 🔥 BOUTON FIX DEFINITIF
    private JButton createButton(String text, Color color){
        JButton btn = new JButton(text);

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        btn.setBackground(new Color(30,30,30));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60,60,60),1,true),
                BorderFactory.createEmptyBorder(10,20,10,20)
        ));

        btn.getModel().addChangeListener(e->{
            ButtonModel m = btn.getModel();

            if(m.isPressed()){
                btn.setBackground(color.darker());
                btn.setForeground(Color.BLACK);
            }else if(m.isRollover()){
                btn.setBackground(color);
                btn.setForeground(Color.BLACK);
            }else{
                btn.setBackground(new Color(30,30,30));
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    private void setupEventHandlers(){
        btnBrowse.addActionListener(e->browseDirectory());
        btnScan.addActionListener(e->startScan());
        btnImport.addActionListener(e->importVideos());
    }

    private void browseDirectory(){
        File dir = controller.selectDirectory(this);
        if(dir!=null){
            pathField.setText(dir.getAbsolutePath());
            btnScan.setEnabled(true);
        }
    }

    private void startScan(){
        String path = pathField.getText();
        if(path.isEmpty()) return;

        setScanningState(true);

        SwingWorker<ArrayList<Video>,Void> worker = new SwingWorker<>(){
            protected ArrayList<Video> doInBackground(){
                return controller.scannerDossier(path);
            }

            protected void done(){
                try{
                    scannedVideos = get();
                    updateResultsTable(scannedVideos);
                    btnImport.setEnabled(!scannedVideos.isEmpty());
                    statusLabel.setText(scannedVideos.size()+" vidéos trouvées");
                }catch(Exception e){
                    controller.showError("Erreur", e.getMessage());
                }
                setScanningState(false);
            }
        };

        worker.execute();
    }

    private void importVideos(){
        for(Video v: scannedVideos){
            controller.ajouterVideo(v);
        }
        statusLabel.setText("Import terminé");
        btnImport.setEnabled(false);
        
        // Rafraîchissement immédiat et dynamique du panneau vidéos
        refreshVideosPanelImmediate();
    }

    private void setScanningState(boolean s){
        isScanning=s;
        btnBrowse.setEnabled(!s);
        btnScan.setEnabled(!s && !pathField.getText().isEmpty());
    }

    private void updateResultsTable(ArrayList<Video> videos){
        tableModel.setRowCount(0);
        for(Video v: videos){
            tableModel.addRow(new Object[]{
                    v.getTitre(),
                    v.getCategorie(),
                    v.getDureeFormatee(),
                    v.getChemin()
            });
        }
    }

    /**
     * Rafraîchissement immédiat et dynamique du panneau vidéos après import
     */
    private void refreshVideosPanelImmediate() {
        // Exécuter dans le thread de l'interface graphique
        SwingUtilities.invokeLater(() -> {
            try {
                // Trouver le panneau vidéos principal et le rafraîchir
                java.awt.Container parent = getParent();
                while (parent != null && !(parent instanceof com.vidora.view.MainView)) {
                    parent = parent.getParent();
                }
                
                if (parent instanceof com.vidora.view.MainView) {
                    com.vidora.view.MainView mainView = (com.vidora.view.MainView) parent;
                    JPanel videosPanel = mainView.getVideosPanel();
                    if (videosPanel != null) {
                        // Utiliser la méthode de rafraîchissement dynamique du VideosPanel
                        try {
                            videosPanel.getClass().getMethod("refreshDataImmediate").invoke(videosPanel);
                            System.out.println("Rafraîchissement dynamique du panneau vidéos effectué après import");
                        } catch (Exception ex) {
                            // Fallback si la méthode n'existe pas
                            videosPanel.getClass().getMethod("refreshData").invoke(videosPanel);
                            System.out.println("Rafraîchissement standard du panneau vidéos effectué après import");
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du rafraîchissement du panneau vidéos : " + e.getMessage());
            }
        });
    }
    
    public void refreshData(){
        tableModel.setRowCount(0);
        scannedVideos.clear();
        pathField.setText("");
        btnScan.setEnabled(false);
        btnImport.setEnabled(false);
        statusLabel.setText("Réinitialisé");
    }
}