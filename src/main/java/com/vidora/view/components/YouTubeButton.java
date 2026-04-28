package com.vidora.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Bouton style YouTube avec effets hover et animations
 */
public class YouTubeButton extends JButton {
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    public YouTubeButton(String text) {
        super(text);
        
        // Configuration initiale style YouTube
        setContentAreaFilled(false);
        setOpaque(true);
        setBorderPainted(false);
        setFocusPainted(false);
        setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        setForeground(Color.WHITE);
        setFont(new Font("Roboto", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // Gestion des effets hover YouTube
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setBackground(com.vidora.view.MainView.BUTTON_HOVER);
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                setBackground(com.vidora.view.MainView.ACCENT_COLOR);
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                setBackground(new Color(153, 0, 0));
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                setBackground(isHovered ? com.vidora.view.MainView.BUTTON_HOVER : com.vidora.view.MainView.ACCENT_COLOR);
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (!isEnabled()) {
            // Style désactivé YouTube
            g2d.setColor(com.vidora.view.MainView.SECONDARY_TEXT);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
            g2d.setColor(new Color(200, 200, 200));
        } else {
            // Style normal YouTube avec coins légèrement arrondis
            Color currentColor = getBackground();
            g2d.setColor(currentColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
            g2d.setColor(Color.WHITE);
        }
        
        // Dessiner le texte avec police Roboto
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(getText(), textX, textY);
        
        g2d.dispose();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            setBackground(com.vidora.view.MainView.SECONDARY_TEXT);
        } else {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBackground(com.vidora.view.MainView.ACCENT_COLOR);
        }
        repaint();
    }
}
