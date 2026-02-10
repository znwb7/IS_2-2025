package com.ucv.model;
import java.io.*;

public class DataBase {
   
    // Obtenemos la ruta dinámica del proyecto para que funcione en cualquier PC
    private static final String SEPARATOR = File.separator;
    private static final String RUTA_ARCHIVO = System.getProperty("user.dir") 
            + SEPARATOR + "target" 
            + SEPARATOR + "Output" 
            + SEPARATOR + "DataBase.txt";
    private static final String RUTA_BDSecretaria = System.getProperty("user.dir") 
        + SEPARATOR + "src" 
        + SEPARATOR + "main" 
        + SEPARATOR + "resources"
        + SEPARATOR + "BaseDataSecretaria.txt";

    private void CrearArchivo() throws IOException {
        File archivo = new File(RUTA_ARCHIVO);
        try {
            // Crear carpetas si no existen
            File directorio = archivo.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }
            // Crear archivo si no existe
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            throw new IOException("Error Para Crear el Archivo");
        }
    }
    
    public static void main(String[] args) {

       DataBase miBaseDeDatos = new DataBase();
    try {
        // 2. Llamamos al método a través del objeto
        // Como ahora devuelve un String y lanza IOException, lo manejamos así:
        String resultado = miBaseDeDatos.Registro("Valeria", "27444333", "1234");
        System.out.println("Resultado del registro: " + resultado);

    } catch (IOException e) {
        System.err.println("Error crítico de archivo: " + e.getMessage());
    }
    }

public String ComprobarDatos(String ID, String Password) throws IOException {

    File file = new File(RUTA_ARCHIVO);
    if (!file.exists()) return "ARCHIVO_NO_EXISTE";

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] word = line.split("\\s*\\|\\s*");

            if (word.length >= 4) {
                String idEnArchivo = word[1];
                String passEnArchivo = word[2];

                if (idEnArchivo.equals(ID)) {
                    if (passEnArchivo.equals(Password)) {
                        return "EXITO";
                    } else {
                        return "PASSWORD_INCORRECTO";
                    }
                }
            }
        }
    }

    return "USUARIO_NO_ENCONTRADO";
}

public String Registro(String Name, String ID, String Password) throws IOException {
    CrearArchivo(); 
    
    String Rol = FindUser(ID); 
    if (Rol == null) {
        return "USUARIO_NO_ENCONTRADO_SECRETARIA"; 
    }
    
    try {
        if (usuarioYaExiste(ID)) {
            return "PERSONA_YA_EXISTENTE";
        }
    } catch (IOException e) {
        return "ERROR_LECTURA_DB";
    }
    
    try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
        String NLine = Name + " | " + ID + " | " + Password + " | " + Rol;
        escritor.write(NLine);
        escritor.newLine();
        return "REGISTRO_EXITOSO";
    }
}

private boolean usuarioYaExiste(String id) throws IOException {
    File file = new File(RUTA_ARCHIVO);
    if (!file.exists()) return false;
    
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] partes = line.split("\\s*\\|\\s*");
            if (partes.length >= 2 && partes[1] != null && partes[1].trim().equals(id)) {
                return true;
            }
        }
    } catch (IOException e) {
        System.err.println("Error leyendo DataBase.txt: " + e.getMessage());
        return false; 
    }
    return false;
}

private static String FindUser(String ID) throws IOException {  // ← CAMBIO: Lanza IOException
    try (BufferedReader br = new BufferedReader(new FileReader(RUTA_BDSecretaria))) {
        String Line;
        while ((Line = br.readLine()) != null) {
            if (Line.trim().isEmpty()) continue;
            String[] Word = Line.split("\\s*\\|\\s*");
            if (Word.length >= 4 && Word[1] != null && Word[1].trim().equals(ID)) {
                return Word[3] != null ? Word[3].trim() : "estudiante";
            }
        } 
        return null; 
    }
}


}