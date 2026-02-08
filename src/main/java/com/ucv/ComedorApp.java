package com.ucv;

import com.ucv.view.LoginUCV;

import javax.swing.*;

public class ComedorApp {

    public static void main(String[] args) {

        // Look & Feel del sistema (mejor resolución y apariencia)
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        // Arranque de la aplicación
        SwingUtilities.invokeLater(() -> {
            new LoginUCV().setVisible(true);
        });
    }
}
