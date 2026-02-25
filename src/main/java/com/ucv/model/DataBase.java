package com.ucv.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

// Enums para los estados de login y registro
public class DataBase {

    public enum LoginStatus {
        EXITO, PASSWORD_INCORRECTO, USUARIO_NO_ENCONTRADO, ARCHIVO_NO_EXISTE
    }

    public enum RegistroStatus {
        REGISTRO_EXITOSO, PERSONA_YA_EXISTENTE, USUARIO_NO_ENCONTRADO_SECRETARIA, ERROR_LECTURA_DB, FALTA_HASH_BDSECRETARIA
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

    public static void main(String[] args) {
       DataBase db = new DataBase();

        // 2. Llamamos al método a través de la instancia 'db'.
        String resultado = db.returnHash("31983764");

        // 3. Verificamos el resultado antes de imprimirlo
        if (resultado != null) {
            System.out.println("Hash encontrado: " + resultado);
        } else {
            System.out.println("No se encontró el usuario o el archivo no existe.");
        }
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
        if (findUser(id) == false) return RegistroStatus.USUARIO_NO_ENCONTRADO_SECRETARIA;

        RolUsuario rol = obtenerRol(id);
        String Hash = FindHash(id);
        if (Hash == null) return RegistroStatus.FALTA_HASH_BDSECRETARIA;

        if (usuarioYaExiste(id)) return RegistroStatus.PERSONA_YA_EXISTENTE;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = name + " | " + id + " | " + password + " | " + rol.name().toLowerCase() + " | " + Hash;
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

    private boolean findUser(String id) throws IOException {
        File file = new File(rutaBDSecretaria);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // Separar por el carácter '|' (manejando espacios)
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 2 && id.equals(partes[1].trim())) {
                    return true;
                }
            }
        }
        return false;
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

    public String CreateHash(String ruta) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(Paths.get(ruta))) {
                byte[] buffer = new byte[1024];
                int leido;
                while ((leido = is.read(buffer)) != -1) {
                    md.update(buffer, 0, leido);
                }
            }
            byte[] digest = md.digest();
            StringBuilder SBuilder = new StringBuilder();
            for (byte b : digest) {
                SBuilder.append(String.format("%02x", b));
            }
            return SBuilder.toString(); 
        } catch (Exception e) {
            return "Error al procesar la imagen: ";
        }
    }

    private String FindHash(String ID){
        try (BufferedReader br = new BufferedReader(new FileReader(rutaBDSecretaria))) {
            String Line;
            while ((Line = br.readLine()) != null) {
                String[] Word = Line.split("\\s*\\|\\s*");
                if (Line.isEmpty()) continue;
                if (Word[1].equals(ID)) {
                    return Word[3]; 
                }
            } 
            return null; 
        } catch (IOException e) { 
            return null; 
        }
    }

    public String returnHash(String ID){
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String Line;
            while ((Line = br.readLine()) != null) {
                String[] Word = Line.split("\\s*\\|\\s*");
                if (Line.isEmpty()) continue;
                if (Word[1].equals(ID)) {
                    return Word[4]; 
                }
            } 
            return null; 
        } catch (IOException e) { 
            return null; 
        }
    }




}