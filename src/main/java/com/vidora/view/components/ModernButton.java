package com.vidora.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Bouton moderne avec effets hover et animations
 */
public class ModernButton extends JButton {
    // Couleurs par défaut YouTube
    private Color normalColor = com.vidora.view.MainView.ACCENT_COLOR;
    private Color hoverColor = com.vidora.view.MainView.BUTTON_HOVER;
    private Color pressedColor = new Color(153, 0, 0); // Rouge foncé YouTube
    private Color disabledColor = com.vidora.view.MainView.SECONDARY_TEXT; 
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    public ModernButton(String text) {
        this(text, com.vidora.view.MainView.ACCENT_COLOR);
    }
    
    public ModernButton(String text, Color normalColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = normalColor.brighter();
        this.pressedColor = normalColor.darker();
        
        // Configuration initiale
        setContentAreaFilled(false);
        setOpaque(true);
        setBorderPainted(false);
        setFocusPainted(false);
        setBackground(normalColor);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        
        // Gestion des effets hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (!isEnabled()) {
            // Couleur grise pour le bouton désactivé
            g2d.setColor(new Color(100, 100, 100));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            
            // Texte grisé
            g2d.setColor(new Color(180, 180, 180));
        } else {
            // Logique normale pour le bouton activé
            Color currentColor = normalColor;
            if (isPressed) {
                currentColor = pressedColor;
            } else if (isHovered) {
                currentColor = hoverColor;
            }
            
            g2d.setColor(currentColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2d.setColor(Color.WHITE);
        }
        
        // Dessiner le texte
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
        } else {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        repaint();
    }
    
    // Setters pour personnaliser les couleurs
    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }
    
    public void setPressedColor(Color pressedColor) {
        this.pressedColor = pressedColor;
    }
    
    public void setNormalColor(Color normalColor) {
        this.normalColor = normalColor;
        setBackground(normalColor);
    }
}
