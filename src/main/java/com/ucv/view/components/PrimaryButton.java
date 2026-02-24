package com.ucv.view.components;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton {

    private static final Color COLOR_FONDO = new Color(250, 210, 50);

    public PrimaryButton(String texto) {
        super(texto);
        configurar();
    }

    private void configurar() {
        setBackground(COLOR_FONDO);
        setFont(new Font("Arial", Font.BOLD, 18));
        setPreferredSize(new Dimension(240, 45));
        setMaximumSize(new Dimension(240, 45));
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}