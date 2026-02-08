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
        String resultado = miBaseDeDatos.Registro("Andy", "32983764", "1234");
        
        System.out.println("Resultado del registro: " + resultado);

    } catch (IOException e) {
        System.err.println("Error crítico de archivo: " + e.getMessage());
    }
    }

    public String ComprobarDatos(String ID, String Password) throws IOException {
       // 1. Usar la ruta dinámica, no el nombre suelto
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) return "ARCHIVO_NO_EXISTE";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Suponiendo que en DataBase.txt guardas con espacios: Juan 123 clave Rol
                String[] word = line.split("\\s*\\|\\s*"); 
                
                if (word.length >= 3) {
                    String idEnArchivo = word[1];
                    if (idEnArchivo.equals(ID)) {
                        return "Persona_ya_existente";
                    }
                }
            }
        }
        return "USUARIO_NO_ENCONTRADO"; // Terminó de leer y no halló el ID

    }

    public String Registro(String Name, String ID, String Password) throws IOException {
        CrearArchivo(); 
        String Rol = FindUser(ID); 
        if (Rol == null) {
            return "USUARIO_NO_ENCONTRADO_SECRETARIA"; 
        }
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            
            // Usamos el formato de pipes para ser consistentes con tu BD
            String NLine = Name + " | " + ID + " | " + Password + " | " + Rol;
            escritor.write(NLine);
            escritor.newLine();
            
            return "REGISTRO_EXITOSO"; // Mensaje positivo para el Controlador
            
        } catch (IOException e) {
            throw new IOException("Error al escribir en la base de datos local.");
        }
    }








   /*  public void Registro(String Name, String ID, String Password) {
        CrearArchivo();
        // Abrimos el archivo de la ruta específica para lectura y escritura
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            
            // Verificamos duplicados usando la misma ruta
             try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
                String Line;           
                while ((Line = br.readLine()) != null) {
                    String[] Word = Line.split("\"\\\\s*\\\\|\\\\s*\"");
                    if (Word.length > 1 && Word[1].equals(ID)) {
                        System.out.println("El ID ya existe en la base de datos.");
                        return;
                    }
                }
            }
            String Rol = FindUser(ID);            
            /*if (Rol == null){
                System.out.println("no hay vida");
                return;
            }else{} 

            String NLine = Name + " | " + ID + " | " + Password + " | " + Rol;
            escritor.write(NLine);
            escritor.newLine();
            

        } 
        catch (IOException e) {}
    } */

    private static String FindUser (String ID){
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_BDSecretaria))) {
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
}