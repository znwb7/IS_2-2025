package com.ucv.controller;

import com.ucv.model.DataBase;
import com.ucv.model.DataBase.LoginStatus;
import com.ucv.model.DataBase.RegistroStatus;
import com.ucv.model.DataBase.RolUsuario;
import com.ucv.view.LoginUCV;

import java.io.IOException;

public class UserController {

    private final DataBase dataBase;

    public UserController() {
        this.dataBase = new DataBase();
    }

    public UserController(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    // ------------------- REGISTRO -------------------
    public Response register(String nombre, String apellido , String id, String password) {

        String regexLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

        if (!nombre.matches(regexLetras) || !apellido.matches(regexLetras)) {
            return new Response(false, "El nombre y el apellido deben contener solo letras");
        }
        if (!id.matches("\\d+")) {
            return new Response(false, "El ID debe contener solo números");
        }

        String name = nombre + " " + apellido;

        try {

            RegistroStatus resultado = dataBase.registrar(name, id, password);

            switch (resultado) {
                case REGISTRO_EXITOSO:
                    return new Response(true, "Usuario registrado correctamente");
                case USUARIO_NO_ENCONTRADO_SECRETARIA:
                    return new Response(false, "El ID no existe en la base de la secretaría");
                case PERSONA_YA_EXISTENTE:
                    return new Response(false, "El usuario ya está registrado");
                case ERROR_LECTURA_DB:
                default:
                    return new Response(false, "Error crítico de archivo o desconocido durante el registro");
            }

        } catch (IOException e) {
            return new Response(false, "Error crítico de archivo: " + e.getMessage());
        }
    }

    // ------------------- LOGIN -------------------
    public Response login(String id, String password) {
        try {
            LoginStatus resultado = dataBase.comprobarDatos(id, password);

            switch (resultado) {
                case EXITO:
                    return new Response(true, "Login exitoso");
                case PASSWORD_INCORRECTO:
                    return new Response(false, "Contraseña incorrecta");
                case USUARIO_NO_ENCONTRADO:
                    return new Response(false, "Usuario no encontrado");
                case ARCHIVO_NO_EXISTE:
                    return new Response(false, "Base de datos no inicializada");
                default:
                    return new Response(false, "Error desconocido en el login");
            }

        } catch (IOException e) {
            return new Response(false, "Error crítico de archivo: " + e.getMessage());
        }
    }

    // ------------------- EVENTOS DESDE LA VISTA -------------------
public void loginRequested(String id, String password, LoginUCV vista) {

    // ---------- VALIDACIONES ----------

    if (id.isEmpty() || id.equals("Cédula")) {
        javax.swing.JOptionPane.showMessageDialog(
                vista,
                "Debe ingresar su cédula",
                "Error de login",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    if (!id.matches("\\d+")) { // solo dígitos
        javax.swing.JOptionPane.showMessageDialog(
                vista,
                "Cédula inválida, solo se permiten números",
                "Error de login",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    if (password.isEmpty() || password.equals("Contraseña")) {
        javax.swing.JOptionPane.showMessageDialog(
                vista,
                "Debe ingresar su contraseña",
                "Error de login",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // ---------- LLAMADA AL MODELO ----------
    Response response = login(id, password);
    if (!response.isSuccess()) {
        javax.swing.JOptionPane.showMessageDialog(
                vista,
                response.getMessage(),
                "Error de login",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // ---------- REDIRECCIÓN ----------
    try {
        RolUsuario rol = dataBase.obtenerRol(id);
        vista.dispose();
        if (rol == RolUsuario.ADMIN) {
            new com.ucv.view.AdminUCV("Administrador").setVisible(true);
        } else {
            new com.ucv.view.PrincipalUCV(id).setVisible(true);
        }
    } catch (IOException e) {
        javax.swing.JOptionPane.showMessageDialog(
                vista,
                "Error al determinar el rol del usuario",
                "Error crítico",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}

    public void registerRequested(String nombre, String apellido , String id, String password, LoginUCV vista, javax.swing.JLabel lblMensaje) {
        Response response = register(nombre, apellido, id, password);
        lblMensaje.setText(response.getMessage());
        lblMensaje.setForeground(response.isSuccess() ? java.awt.Color.GREEN : java.awt.Color.RED);
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