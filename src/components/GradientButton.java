package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// Custom JButton for the gradient effect
public class GradientButton extends JButton {
    private final Color startColor;
    private final Color endColor;
    private boolean darkMode;

    public GradientButton(String text, Color startColor, Color endColor) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Create a rounded rectangle shape
        Shape shape = new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 8, 8);

        // Gradient fill
        GradientPaint gp = new GradientPaint(0, 0, startColor, 0, h, endColor);
        g2.setPaint(gp);
        g2.fill(shape);

        // Draw text
        super.paintComponent(g2);
        g2.dispose();
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        repaint();
    }
}