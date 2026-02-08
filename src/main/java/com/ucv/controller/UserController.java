package com.ucv.controller;

import com.ucv.model.DataBase;
import java.io.IOException;

public class UserController {

    private final DataBase dataBase;

    public UserController() {
        this.dataBase = new DataBase();
    }

    // ======================
    // REGISTRO
    // ======================
    public Response register(String name, String id, String password) {
        try {
            String resultado = dataBase.Registro(name, id, password);

            switch (resultado) {
                case "REGISTRO_EXITOSO":
                    return new Response(true, "Usuario registrado correctamente");

                case "USUARIO_NO_ENCONTRADO_SECRETARIA":
                    return new Response(false, "El ID no existe en la base de la secretaría");

                case "Persona_ya_existente":
                    return new Response(false, "El usuario ya está registrado");

                default:
                    return new Response(false, "Error desconocido durante el registro");
            }

        } catch (IOException e) {
            return new Response(false, "Error crítico de archivo: " + e.getMessage());
        }
    }

    // ======================
    // LOGIN
    // ======================
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

    // ======================
    // RESPONSE (DTO)
    // ======================
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
