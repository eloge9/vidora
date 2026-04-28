package com.vidora.view.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Tableau moderne avec design personnalisé
 */
public class ModernTable extends JTable {
    
    public ModernTable() {
        super();
        setupModernLook();
    }
    
    public ModernTable(DefaultTableModel model) {
        super(model);
        setupModernLook();
    }
    
    private void setupModernLook() {
        // Configuration générale
        setBackground(com.vidora.view.MainView.PANEL_BG);
        setForeground(com.vidora.view.MainView.TEXT_COLOR);
        setSelectionBackground(com.vidora.view.MainView.ACCENT_COLOR);
        setSelectionForeground(Color.WHITE);
        setGridColor(com.vidora.view.MainView.SIDEBAR_BG);
        setRowHeight(30);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setTableHeader(createModernHeader());
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 1));
        
        // Désactiver la réorganisation des colonnes
        getTableHeader().setReorderingAllowed(false);
        
        // Personnaliser le renderer
        setDefaultRenderer(Object.class, new ModernCellRenderer());
    }
    
    private JTableHeader createModernHeader() {
        JTableHeader header = getTableHeader();
        header.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
        header.setForeground(com.vidora.view.MainView.TEXT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, com.vidora.view.MainView.DARK_BG));
        header.setResizingAllowed(true);
        
        return header;
    }
    
    /**
     * Renderer personnalisé pour les cellules
     */
    private static class ModernCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            
            // Configuration du composant
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                
                if (isSelected) {
                    label.setBackground(com.vidora.view.MainView.ACCENT_COLOR);
                    label.setForeground(Color.WHITE);
                } else {
                    // Couleurs alternées pour les lignes
                    if (row % 2 == 0) {
                        label.setBackground(com.vidora.view.MainView.PANEL_BG);
                    } else {
                        label.setBackground(com.vidora.view.MainView.SIDEBAR_BG);
                    }
                    label.setForeground(com.vidora.view.MainView.TEXT_COLOR);
                }
                
                // Alignement du texte selon le type de données
                if (value instanceof Number) {
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                }
            }
            
            return component;
        }
    }
    
    /**
     * Méthode pour configurer les largeurs de colonnes par défaut
     */
    public void setDefaultColumnWidths(int... widths) {
        for (int i = 0; i < Math.min(widths.length, getColumnCount()); i++) {
            getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }
    
    /**
     * Méthode pour ajuster automatiquement les largeurs de colonnes
     */
    public void autoResizeColumns() {
        autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;
        
        for (int column = 0; column < getColumnCount(); column++) {
            TableColumn tableColumn = getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            
            for (int row = 0; row < getRowCount(); row++) {
                TableCellRenderer cellRenderer = getCellRenderer(row, column);
                Component c = prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
            }
            
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Ajouter un effet de surbrillance pour la ligne sélectionnée
        if (getSelectedRow() != -1) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int rowHeight = getRowHeight();
            int y = getSelectedRow() * rowHeight;
            
            // Effet de glow subtil
            g2d.setColor(new Color(com.vidora.view.MainView.ACCENT_COLOR.getRed(),
                                 com.vidora.view.MainView.ACCENT_COLOR.getGreen(),
                                 com.vidora.view.MainView.ACCENT_COLOR.getBlue(), 30));
            g2d.fillRect(0, y, getWidth(), rowHeight);
            
            g2d.dispose();
        }
    }
}
