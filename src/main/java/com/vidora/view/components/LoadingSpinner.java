package com.vidora.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Composant de spinner de chargement animé
 */
public class LoadingSpinner extends JComponent {
    private int angle = 0;
    private Timer timer;
    private Color spinnerColor;
    private int arcSize = 10;
    
    public LoadingSpinner() {
        this(com.vidora.view.MainView.ACCENT_COLOR);
    }
    
    public LoadingSpinner(Color color) {
        this.spinnerColor = color;
        setPreferredSize(new Dimension(40, 40));
        
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angle = (angle + 15) % 360;
                repaint();
            }
        });
    }
    
    public void start() {
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }
    
    public boolean isRunning() {
        return timer.isRunning();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int size = Math.min(getWidth(), getHeight());
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;
        
        // Dessiner les arcs du spinner
        for (int i = 0; i < 8; i++) {
            int currentAngle = (angle + i * 45) % 360;
            double opacity = 1.0 - (i * 0.1);
            
            Color arcColor = new Color(
                    spinnerColor.getRed(),
                    spinnerColor.getGreen(),
                    spinnerColor.getBlue(),
                    (int) (255 * opacity)
            );
            
            g2d.setColor(arcColor);
            g2d.fillArc(
                    x + arcSize / 2,
                    y + arcSize / 2,
                    size - arcSize,
                    size - arcSize,
                    currentAngle,
                    30
            );
        }
        
        g2d.dispose();
    }
}
