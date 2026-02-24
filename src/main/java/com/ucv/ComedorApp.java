package com.ucv;

import com.ucv.view.LoginUCV;
import com.ucv.controller.UserController;

import javax.swing.*;

public class ComedorApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crear la vista
            LoginUCV login = new LoginUCV();

            // Crear el controlador
            UserController controller = new UserController();

            // Inyectar el controlador en la vista
            login.setController(controller);

            // Mostrar la ventana
            login.setVisible(true);
        });
    }
}