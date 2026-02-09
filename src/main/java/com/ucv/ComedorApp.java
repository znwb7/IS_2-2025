package com.ucv;

import com.ucv.view.LoginUCV;

import javax.swing.*;

public class ComedorApp {

    public static void main(String[] args) {
        // Arranque de la aplicación
        SwingUtilities.invokeLater(() -> {
            new LoginUCV().setVisible(true);
        });
    }
}
