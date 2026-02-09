package com.ucv;

import com.ucv.view.LoginUCV;

import javax.swing.*;

public class ComedorApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUCV().setVisible(true);
        });
    }
}
