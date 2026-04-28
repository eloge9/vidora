package com.vidora.view.playlists;

import com.vidora.controller.MainController;
import com.vidora.model.Playlist;
import com.vidora.view.playlists.dialogs.PlaylistDialog;
import com.vidora.view.playlists.dialogs.PlaylistVideosDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PlaylistsPanel extends JPanel {

    private MainController controller;

    private JTable playlistsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;

    private JButton btnAdd, btnEdit, btnDelete, btnViewVideos, btnRefresh;

    // 🎨 Palette moderne
    private final Color BG_MAIN = new Color(10, 10, 10);
    private final Color BG_CARD = new Color(20, 20, 20);
    private final Color BG_HOVER = new Color(35, 35, 35);

    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(170, 170, 170);

    private final Color ACCENT = new Color(255, 94, 0);
    private final Color SUCCESS = new Color(0, 200, 150);
    private final Color ERROR = new Color(255, 80, 80);

    public PlaylistsPanel(MainController controller) {
        this.controller = controller;
        initComponent();
        layoutComponents();
        setupEventHandlers();
        loadData();
    }

    private void initComponent() {
        setBackground(BG_MAIN);
        setLayout(new BorderLayout(0, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void layoutComponents() {
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    // 🔍 SEARCH PANEL
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BG_MAIN);

        searchField = new JTextField("Rechercher une playlist...");
        searchField.setForeground(Color.GRAY);
        searchField.setBackground(new Color(25, 25, 25));
        searchField.setCaretColor(ACCENT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Rechercher une playlist...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Rechercher une playlist...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(BG_MAIN);
        left.add(new JLabel("🔍"));
        left.add(searchField);

        btnAdd = createButton("➕ Ajouter", SUCCESS);
        btnEdit = createButton("✏️ Modifier", ACCENT);
        btnDelete = createButton("🗑️ Supprimer", ERROR);
        btnViewVideos = createButton("👁️ Voir vidéos", ACCENT);
        btnRefresh = createButton("🔄", TEXT_SECONDARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(BG_MAIN);

        right.add(btnAdd);
        right.add(btnEdit);
        right.add(btnDelete);
        right.add(btnViewVideos);
        right.add(btnRefresh);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // 📊 TABLE PANEL
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 40), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        String[] columns = {"Nom", "Vidéos", "Durée", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        playlistsTable = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);

                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(15,15,15) : new Color(25,25,25));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(ACCENT);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        };

        playlistsTable.setRowHeight(38);
        playlistsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        playlistsTable.setGridColor(new Color(40, 40, 40));
        playlistsTable.setShowVerticalLines(false);
        playlistsTable.setIntercellSpacing(new Dimension(0, 0));
        playlistsTable.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTableHeader header = playlistsTable.getTableHeader();
        header.setBackground(new Color(18, 18, 18));
        header.setForeground(TEXT_SECONDARY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ACCENT));

        sorter = new TableRowSorter<>(tableModel);
        playlistsTable.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(playlistsTable);
        scroll.getViewport().setBackground(new Color(15, 15, 15));
        scroll.setBorder(null);

        panel.add(scroll);

        return panel;
    }

    // 🎯 BUTTON STYLE
    private JButton createButton(String text, Color color) {
    JButton btn = new JButton(text);

    // Style de base
    btn.setBackground(new Color(30, 30, 30));
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setOpaque(true);
    btn.setContentAreaFilled(true);

    btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1, true),
            BorderFactory.createEmptyBorder(10, 18, 10, 18)
    ));

    // 🔥 FIX IMPORTANT : empêcher Swing de mettre du blanc auto
    btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());

    // 🎯 Gestion complète des états
    btn.getModel().addChangeListener(e -> {
        ButtonModel model = btn.getModel();

        if (model.isPressed()) {
            // 👉 quand tu cliques
            btn.setBackground(color.darker());
            btn.setForeground(Color.BLACK);
        } else if (model.isRollover()) {
            // 👉 hover
            btn.setBackground(color);
            btn.setForeground(Color.BLACK);
        } else {
            // 👉 normal
            btn.setBackground(new Color(30, 30, 30));
            btn.setForeground(Color.WHITE);
        }
    });

    return btn;
}

    // ⚙️ EVENTS
    private void setupEventHandlers() {

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        playlistsTable.getSelectionModel().addListSelectionListener(e -> updateButtons());

        playlistsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) viewVideos();
            }
        });

        btnAdd.addActionListener(e -> add());
        btnEdit.addActionListener(e -> edit());
        btnDelete.addActionListener(e -> delete());
        btnViewVideos.addActionListener(e -> viewVideos());
        btnRefresh.addActionListener(e -> refresh());
    }

    private void loadData() {
        try {
            populate(controller.getPlaylistService().getAllPlaylists());
        } catch (Exception e) {
            controller.showError("Erreur", e.getMessage());
        }
    }

    private void populate(ArrayList<Playlist> list) {
        tableModel.setRowCount(0);

        for (Playlist p : list) {
            tableModel.addRow(new Object[]{
                    p.getNom(),
                    p.getVideos().size(),
                    formatDuration(p.getDureeTotale()),
                    p.getDateCreation()
            });
        }
    }

    private void filter() {
        String text = searchField.getText();

        if (text.equals("Rechercher une playlist...") || text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0));
        }
    }

    private void updateButtons() {
        boolean selected = playlistsTable.getSelectedRow() != -1;
        btnEdit.setEnabled(selected);
        btnDelete.setEnabled(selected);
        btnViewVideos.setEnabled(selected);
    }

    private void add() {
        PlaylistDialog d = new PlaylistDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Ajouter", null, controller
        );
        d.setVisible(true);

        if (d.isConfirmed()) {
            controller.getPlaylistService().creerPlaylist(d.getPlaylist());
            refresh();
        }
    }

    private void edit() {
        int row = playlistsTable.getSelectedRow();
        if (row == -1) return;

        String nom = (String) tableModel.getValueAt(playlistsTable.convertRowIndexToModel(row), 0);

        // Trouver la playlist par son nom
        Playlist p = null;
        for (Playlist playlist : controller.getPlaylistService().getAllPlaylists()) {
            if (playlist.getNom().equals(nom)) {
                p = playlist;
                break;
            }
        }

        if (p != null) {
            PlaylistDialog d = new PlaylistDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    "Modifier", p, controller
            );

            d.setVisible(true);

            if (d.isConfirmed()) {
                controller.modifierPlaylist(d.getPlaylist());
                refresh();
            }
        }
    }

    private void delete() {
        int row = playlistsTable.getSelectedRow();
        if (row == -1) return;

        int modelRow = playlistsTable.convertRowIndexToModel(row);
        String nom = (String) tableModel.getValueAt(modelRow, 0);

        // Trouver la playlist par son nom
        for (Playlist playlist : controller.getPlaylistService().getAllPlaylists()) {
            if (playlist.getNom().equals(nom)) {
                controller.supprimerPlaylist(playlist.getId());
                refresh();
                break;
            }
        }
    }

    private void viewVideos() {
        int row = playlistsTable.getSelectedRow();
        if (row == -1) return;

        int modelRow = playlistsTable.convertRowIndexToModel(row);
        String nom = (String) tableModel.getValueAt(modelRow, 0);

        // Trouver la playlist par son nom
        for (Playlist playlist : controller.getPlaylistService().getAllPlaylists()) {
            if (playlist.getNom().equals(nom)) {
                new PlaylistVideosDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(this),
                        playlist.getNom(),
                        playlist.getId(),
                        controller
                ).setVisible(true);
                break;
            }
        }
    }

    public void refresh() {
        loadData();
        updateButtons();
    }

    private String formatDuration(int s) {
        int h = s / 3600;
        int m = (s % 3600) / 60;
        int sec = s % 60;

        return h > 0 ? String.format("%d:%02d:%02d", h, m, sec)
                     : String.format("%d:%02d", m, sec);
    }
}