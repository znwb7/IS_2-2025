package com.ucv.controller;

import com.ucv.model.DataBase;
import java.io.IOException;

public class UserController {

    private final DataBase dataBase;

    public UserController() {
        this.dataBase = new DataBase();
    }

    // REGISTRO
    public Response register(String name, String id, String password) {
        try {
            String resultado = dataBase.Registro(name, id, password);

            switch (resultado) {
                case "REGISTRO_EXITOSO":
                    return new Response(true, "Usuario registrado correctamente");

                case "USUARIO_NO_ENCONTRADO_SECRETARIA":
                    return new Response(false, "El ID no existe en la base de la secretaría");

                case "PERSONA_YA_EXISTENTE":
                    return new Response(false, "El usuario ya está registrado");
                    
                default:
                    return new Response(false, "Error desconocido durante el registro");
            }

        } catch (IOException e) {
            return new Response(false, "Error crítico de archivo: " + e.getMessage());
        }
    }

    
    // LOGIN
    public Response login(String id, String password) {
        try {
            String resultado = dataBase.ComprobarDatos(id, password);

            switch (resultado) {
                case "EXITO":
                    return new Response(true, "Login exitoso");

                case "PASSWORD_INCORRECTO":
                    return new Response(false, "Contraseña incorrecta");

                case "USUARIO_NO_ENCONTRADO":
                    return new Response(false, "Usuario no encontrado");

                case "ARCHIVO_NO_EXISTE":
                    return new Response(false, "Base de datos no inicializada");

                default:
                    return new Response(false, "Error desconocido en el login");
            }

        } catch (IOException e) {
            return new Response(false, "Error crítico de archivo: " + e.getMessage());
        }
    }

    
    // REDIRECCIÓN
    public void ejecutarRedireccion(String id, javax.swing.JFrame ventanaLogin) {
        String rol = "estudiante";
        String separador = java.io.File.separator;
        String rutaSecretaria = System.getProperty("user.dir")
                + separador + "src" + separador + "main" + separador + "resources"
                + separador + "BaseDataSecretaria.txt";

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaSecretaria))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] word = line.split("\\s*\\|\\s*");
                if (word.length >= 4 && word[1].equals(id)) {
                    rol = word[3].toLowerCase().trim();
                    break;
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Error al determinar rol: " + e.getMessage());
        }

        ventanaLogin.dispose();

        if (rol.equals("admin")) {
            new com.ucv.view.AdminUCV("Administrador").setVisible(true);
        } else {
            new com.ucv.view.PrincipalUCV(id).setVisible(true);
        }
    }

    // RESPONSE (DTO)
    public static class Response {
        private final boolean success;
        private final String message;

        public Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}