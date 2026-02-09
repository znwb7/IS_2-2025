package com.ucv.controller;

import com.ucv.view.AdminUCV;
import com.ucv.view.PrincipalUCV;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RedirectController {

    private static final String SEPARATOR = File.separator;
    private static final String RUTA_BD_SECRETARIA = System.getProperty("user.dir")
            + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "resources"
            + SEPARATOR + "BaseDataSecretaria.txt";

    public void ejecutarRedireccion(String id) {
        String rol = "estudiante"; // Valor por defecto

        // Leemos el archivo para buscar el rol del ID [cite: 1]
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_BD_SECRETARIA))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] word = line.split("\\s*\\|\\s*");

                if (word.length >= 4 && word[1].equals(id)) {
                    rol = word[3].toLowerCase().trim(); // Ejemplo: "admin", "profesor" [cite: 1, 2]
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al determinar rol para redirección: " + e.getMessage());
        }

        // Decisión de Vista
        if (rol.equals("admin")) {
            new AdminUCV("Administrador").setVisible(true);
        } else {
            // "profesor", "empleado" o "estudiante" van a la vista de comensal [cite: 1, 2]
            new PrincipalUCV(id).setVisible(true);
        }
    }
}