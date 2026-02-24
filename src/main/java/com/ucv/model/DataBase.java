package com.ucv.model;

import java.io.*;

// Enums para los estados de login y registro
public class DataBase {

    public enum LoginStatus {
        EXITO, PASSWORD_INCORRECTO, USUARIO_NO_ENCONTRADO, ARCHIVO_NO_EXISTE
    }

    public enum RegistroStatus {
        REGISTRO_EXITOSO, PERSONA_YA_EXISTENTE, USUARIO_NO_ENCONTRADO_SECRETARIA, ERROR_LECTURA_DB
    }

    public enum RolUsuario {
        ADMIN, SECRETARIA, ESTUDIANTE
    }

    private final String rutaArchivo;
    private final String rutaBDSecretaria;
    private static final String SEPARATOR = File.separator;

    // Constructor con rutas por defecto
    public DataBase() {
        this.rutaArchivo = System.getProperty("user.dir") + SEPARATOR + "target" + SEPARATOR + "Output" + SEPARATOR + "DataBase.txt";
        this.rutaBDSecretaria = System.getProperty("user.dir") + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "BaseDataSecretaria.txt";
    }

    // Constructor con rutas personalizadas (para testing o flexibilidad)
    public DataBase(String rutaArchivo, String rutaBDSecretaria) {
        this.rutaArchivo = rutaArchivo;
        this.rutaBDSecretaria = rutaBDSecretaria;
    }

    public LoginStatus comprobarDatos(String id, String password) throws IOException {
        File file = new File(rutaArchivo);
        if (!file.exists()) return LoginStatus.ARCHIVO_NO_EXISTE;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 4) {
                    String idEnArchivo = partes[1];
                    String passEnArchivo = partes[2];
                    if (idEnArchivo.equals(id)) {
                        return passEnArchivo.equals(password) ? LoginStatus.EXITO : LoginStatus.PASSWORD_INCORRECTO;
                    }
                }
            }
        }
        return LoginStatus.USUARIO_NO_ENCONTRADO;
    }

    public RegistroStatus registrar(String name, String id, String password) throws IOException {
        crearArchivo(); 
        RolUsuario rol = findUser(id);
        if (rol == null) return RegistroStatus.USUARIO_NO_ENCONTRADO_SECRETARIA;

        if (usuarioYaExiste(id)) return RegistroStatus.PERSONA_YA_EXISTENTE;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = name + " | " + id + " | " + password + " | " + rol.name().toLowerCase();
            escritor.write(linea);
            escritor.newLine();
            return RegistroStatus.REGISTRO_EXITOSO;
        } catch (IOException e) {
            return RegistroStatus.ERROR_LECTURA_DB;
        }
    }

    private void crearArchivo() throws IOException {
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
    }

    private boolean usuarioYaExiste(String id) throws IOException {
        File file = new File(rutaArchivo);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 2 && partes[1].trim().equals(id)) return true;
            }
        }
        return false;
    }

    private RolUsuario findUser(String id) throws IOException {
        File file = new File(rutaBDSecretaria);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 4 && partes[1].trim().equals(id)) {
                    String rol = partes[3] != null ? partes[3].trim().toLowerCase() : "estudiante";
                    switch (rol) {
                        case "admin":
                            return RolUsuario.ADMIN;
                        case "secretaria":
                            return RolUsuario.SECRETARIA;
                        default:
                            return RolUsuario.ESTUDIANTE;
                    }
                }
            }
        }
        return null;
    }

public RolUsuario obtenerRol(String id) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] partes = line.split("\\s*\\|\\s*");
            if (partes.length >= 4 && partes[1].trim().equals(id)) {
                String rol = partes[3].trim().toLowerCase();
                switch (rol) {
                    case "admin": return RolUsuario.ADMIN;
                    case "secretaria": return RolUsuario.SECRETARIA;
                    default: return RolUsuario.ESTUDIANTE;
                }
            }
        }
    }
    return RolUsuario.ESTUDIANTE; // default
}
}