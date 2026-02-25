package com.ucv.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDB {

    // Enums para estandarizar los retornos de cada método
    public enum WriteMenuStatus {
        REGISTRO_EXITOSO, ERROR_ESCRITURA, ACTUALIZACION_EXITOSA, MENU_NO_ENCONTRADO, ARCHIVO_NO_EXISTE
    }

    public enum ReWriteStatus {
        REWRITE_EXITOSO, NO_ENCONTRADO, ARCHIVO_NO_EXISTE
    }

    private static final String SEPARATOR = File.separator;
    private static final String RUTA_ARCHIVO = System.getProperty("user.dir") 
            + SEPARATOR + "target" 
            + SEPARATOR + "Output" 
            + SEPARATOR + "MenuDB.txt";

    private void MakeArchive() throws IOException {
        File archivo = new File(RUTA_ARCHIVO);
        try {
            File directorio = archivo.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            throw new IOException("Error Para Crear el Archivo");
        }
    }

   public static void main(String[] args) {
    MenuDB db = new MenuDB();
    String fecha = "2026-02-25";

    try {
        System.out.println("=== PRUEBA DE TRANSICIONES DE TIPO (ALMUERZO <-> DESAYUNO) ===");

        // 1. REGISTRO INICIAL: Almuerzo
        System.out.println("\n[1] Registrando un Almuerzo inicial...");
        db.WriteMenu(false, false, fecha, "Almuerzo", "Pasta", "Jugo", "Fruta", "10", "20", "15");
        mostrarBD();

        // 2. CAMBIO: De Almuerzo a Desayuno
        // Usamos Modify=true y Type=true para disparar el cambio de tipo
        System.out.println("\n[2] Aplicando cambio: de Almuerzo -> DESAYUNO...");
        db.WriteMenu(true, true, fecha, "Almuerzo", "Empanadas", "Cafe", "Fruta", "5", "10", "8");
        mostrarBD();

        // 3. CAMBIO: De Desayuno a Almuerzo (Viceversa)
        System.out.println("\n[3] Aplicando cambio: de Desayuno -> ALMUERZO...");
        db.WriteMenu(true, true, fecha, "Desayuno", "Pabellon", "Papelon", "Quesillo", "12", "25,5", "20");
        mostrarBD();

        System.out.println("\n=== PRUEBA DE ERRORES DE TRANSICIÓN ===");
        
        // 4. Intento de cambiar un tipo que no existe en esa fecha
        System.out.println("\n[4] Intentando cambiar 'Cena' a otro tipo (No existe)...");
        WriteMenuStatus err = db.WriteMenu(true, true, fecha, "Cena", "Nada", "Nada", "Nada", "0", "0", "0");
        System.out.println("Resultado esperado: " + err);

    } catch (IOException e) {
        System.err.println("Error en la prueba: " + e.getMessage());
    }
}

// Método auxiliar para ver los cambios en la BD en tiempo real
private static void mostrarBD() {
    System.out.println("--- Contenido actual de MenuDB.txt ---");
    try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + "target" + File.separator + "Output" + File.separator + "MenuDB.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("BD -> " + line);
        }
    } catch (IOException e) {
        System.out.println("Archivo vacío o no encontrado.");
    }
    System.out.println("---------------------------------------");
}

    public WriteMenuStatus WriteMenu(Boolean Modify, Boolean Type, String Fecha, String Tipo, String PlatoFuerte, String Bebida, String Postre, String PEstudiante, String PProfesor, String PEmpleado) throws IOException {
        Tipo = Tipo.toLowerCase();
        PlatoFuerte = PlatoFuerte.toLowerCase();
        Bebida = Bebida.toLowerCase();
        Postre = Postre.toLowerCase();

        if (Modify) {
            return ModifyMenu(Type, Fecha, Tipo, PlatoFuerte, Bebida, Postre, PEstudiante, PProfesor, PEmpleado);
        }
        
        MakeArchive();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            String NLine = Fecha + " | " + Tipo + " | " + PlatoFuerte + " | " + Bebida + " | " + Postre + " | " + PEstudiante + " | " + PProfesor + " | " + PEmpleado + " | " + "0";
            escritor.write(NLine);
            escritor.newLine();
            return WriteMenuStatus.REGISTRO_EXITOSO;
        } catch (IOException e) {
            return WriteMenuStatus.ERROR_ESCRITURA;
        }
    }

    

    public ReWriteStatus ReWriteSpace(String Fecha, String Tipo) throws IOException {
        Tipo = Tipo.toLowerCase();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return ReWriteStatus.ARCHIVO_NO_EXISTE;

        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("\\s*\\|\\s*");

                if (partes.length >= 9 && partes[0].equals(Fecha) && partes[1].equals(Tipo)) {
                    int contador = Integer.parseInt(partes[8]);
                    contador++;
                    partes[8] = String.valueOf(contador);
                    linea = String.join(" | ", partes);
                    encontrado = true;
                }
                lineas.add(linea);
            }
        }

        if (encontrado) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, false))) {
                for (String l : lineas) {
                    escritor.write(l);
                    escritor.newLine();
                }
            }
            return ReWriteStatus.REWRITE_EXITOSO;
        }

        return ReWriteStatus.NO_ENCONTRADO;
    }

    public WriteMenuStatus ModifyMenu(Boolean Type, String Fecha, String Tipo, String NPlato, String NBebida, String NPostre, String NPEst, String NPProf, String NPEmp) throws IOException {
        Tipo = Tipo.toLowerCase();
        NPlato = NPlato.toLowerCase();
        NBebida = NBebida.toLowerCase();
        NPostre = NPostre.toLowerCase();
        
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return WriteMenuStatus.ARCHIVO_NO_EXISTE;

        List<String> lineas = new ArrayList<>();
        boolean modificado = false;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("\\s*\\|\\s*");

                if (partes.length >= 9 && partes[0].equals(Fecha) && partes[1].equals(Tipo.toLowerCase())) {
                    String contadorActual = partes[8];
                    
                    if (Type) {
                        Tipo = Tipo.toLowerCase().equals("almuerzo") ? "desayuno" : "almuerzo";
                    }
                    
                    linea = Fecha + " | " + Tipo + " | " + NPlato + " | " + NBebida + " | " + NPostre + " | " + NPEst + " | " + NPProf + " | " + NPEmp + " | " + contadorActual;
                    modificado = true;
                }
                lineas.add(linea);
            }
        }

        if (modificado) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, false))) {
                for (String l : lineas) {
                    escritor.write(l);
                    escritor.newLine();
                }
            }
            return WriteMenuStatus.ACTUALIZACION_EXITOSA;
        }
        return WriteMenuStatus.MENU_NO_ENCONTRADO;
    }
}