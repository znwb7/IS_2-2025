package com.ucv.view.components;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton2 extends JButton {

    // Color por defecto (El naranja del Login)
    private static final Color COLOR_POR_DEFECTO = new Color(255, 255, 0);

    // Constructor normal (Usará el color naranja)
    public PrimaryButton2(String texto) {
        super(texto);
        configurar(COLOR_POR_DEFECTO);
    }

    // Constructor especial (Por si quieres pasarle otro color, como el de la billetera)
    public PrimaryButton2(String texto, Color colorPersonalizado) {
        super(texto);
        configurar(colorPersonalizado);
    }

    private void configurar(Color colorFondo) {
        setBackground(colorFondo);
        setForeground(Color.BLACK);
        setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Medidas estándar que pediste
        setPreferredSize(new Dimension(280, 55));
        setMaximumSize(new Dimension(280, 55));

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}