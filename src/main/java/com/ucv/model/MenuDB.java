package com.ucv.model;

import java.io.*;

public class MenuDB {

    private static final String SEPARATOR = File.separator;
    private static final String RUTA_ARCHIVO = System.getProperty("user.dir") 
            + SEPARATOR + "target" 
            + SEPARATOR + "Output" 
            + SEPARATOR + "MenuDB.txt";

    private void MakeArchive() throws IOException {
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
        MenuDB db = new MenuDB();
                String fechaPrueba = "2026-02-24";
                String tipoPrueba = "Almuerzo";

        try {
            System.out.println("--- PASO 1: Creando un menú nuevo ---");
            // Usamos false en Modify porque es un registro nuevo
            String resRegistro = db.WriteMenu(false, false, fechaPrueba, tipoPrueba, "Pollo Frito", "Jugo de Piña", "Fruta", "10", "20", "15");
            System.out.println("Estado: " + resRegistro);
            db.WriteMenu(false, false, "2026-02-25", tipoPrueba, "Pollo Frito", "Jugo de Piña", "Fruta", "10", "20", "15");

            System.out.println("\n--- PASO 2: Modificando el menú existente ---");
            // Cambiamos el plato fuerte y la bebida, manteniendo fecha y tipo
            String resModify = db.WriteMenu(true, false, fechaPrueba, "Almuerzo", "Pabellón Margariteño", "Papelón con Limón", "Quesillo", "12", "25", "20");
            System.out.println("Estado: " + resModify);

            System.out.println("\n--- PASO 3: Incrementando el contador de platos servidos ---");
            // Esto debería cambiar el "0" final a "1"
            String resRewrite = db.ReWriteSpace(fechaPrueba, tipoPrueba);
            System.out.println("Estado: " + resRewrite);

            System.out.println("\n--- PASO 4: Segundo incremento (simulando otro estudiante) ---");
            // Esto debería cambiar el "1" a "2"
            db.ReWriteSpace(fechaPrueba, tipoPrueba);
            System.out.println("Contador actualizado nuevamente.");

            System.out.println("\nPruebas finalizadas. Revisa el archivo en: " + RUTA_ARCHIVO);

        } catch (IOException e) {
            System.err.println("Error durante las pruebas: " + e.getMessage());
        }
    }

    public String WriteMenu(Boolean Modify, Boolean Type, String Fecha, String Tipo, String PlatoFuerte, String Bebida, String Postre, String PEstudiante, String PProfesor, String PEmpleado) throws IOException {
        // Normalización
        Tipo = Tipo.toLowerCase();
        PlatoFuerte = PlatoFuerte.toLowerCase();
        Bebida = Bebida.toLowerCase();
        Postre = Postre.toLowerCase();

        // SI ES MODIFICACIÓN, LLAMAMOS Y SALIMOS (IMPORTANTE EL RETURN)
        if (Modify) {
            return ModifyMenu(Type, Fecha, Tipo, PlatoFuerte, Bebida, Postre, PEstudiante, PProfesor, PEmpleado);
        }
        
        // SI NO ES MODIFICACIÓN, PROCEDEMOS A CREAR REGISTRO NUEVO
        MakeArchive();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            String NLine = Fecha + " | " + Tipo + " | " + PlatoFuerte + " | " + Bebida + " | " + Postre + " | " + PEstudiante + " | " + PProfesor + " | " + PEmpleado + " | " + "0";
            escritor.write(NLine);
            escritor.newLine();
            return "REGISTRO_EXITOSO";
        } catch (IOException e) {
            throw new IOException("Error al escribir en la base de datos local.");
        }
    }

    public String ReWriteSpace(String Fecha, String Tipo) throws IOException {
        Tipo = Tipo.toLowerCase();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return "ARCHIVO_NO_EXISTE";

        // 1. Lista para guardar todas las líneas temporalmente
        java.util.List<String> lineas = new java.util.ArrayList<>();
        boolean encontrado = false;

        // 2. Leer el archivo y buscar la coincidencia
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                // Separamos la línea por el caracter "|"
                String[] partes = linea.split("\\s*\\|\\s*");

                // Comparar Fecha (partes[0]) y Tipo (partes[1])
                if (partes.length >= 9 && partes[0].equals(Fecha) && partes[1].equals(Tipo)) {
                    
                    // Convertir la palabra 8 (índice 8) a int y sumar 1
                    int contador = Integer.parseInt(partes[8]);
                    contador++;
                    
                    // Actualizamos esa parte en el arreglo
                    partes[8] = String.valueOf(contador);
                    
                    // Reconstruimos la línea con el nuevo valor
                    linea = String.join(" | ", partes);
                    encontrado = true;
                }
                lineas.add(linea);
            }
        }

        // 3. Si encontramos la línea, sobreescribimos el archivo con la lista actualizada
        if (encontrado) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, false))) {
                for (String l : lineas) {
                    escritor.write(l);
                    escritor.newLine();
                }
            }
            return "REWRITE_EXITOSO";
        }

        return "NO_ENCONTRADO";
    }

    public String ModifyMenu(Boolean Type, String Fecha, String Tipo, String NPlato, String NBebida, String NPostre, String NPEst, String NPProf, String NPEmp) throws IOException {
        Tipo = Tipo.toLowerCase();
        NPlato = NPlato.toLowerCase();
        NBebida = NBebida.toLowerCase();
        NPostre = NPostre.toLowerCase();
            
        
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return "ARCHIVO_NO_EXISTE";

        java.util.List<String> lineas = new java.util.ArrayList<>();
        boolean modificado = false;

        if (Type) {
            if (Tipo.toLowerCase().equals("almuerzo")) {
                Tipo = "desayuno";
            } else {
                Tipo = "almuerzo";
            }
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("\\s*\\|\\s*");

                // 1. Buscamos la coincidencia con el Tipo original
                if (partes.length >= 9 && partes[0].equals(Fecha) && partes[1].equals(Tipo.toLowerCase())) {
                    
                    String contadorActual = partes[8];
                    
                    // 2. REUBICACIÓN DEL IF: Cambiamos el Tipo antes de armar la línea
                    if (Type) {
                        if (Tipo.toLowerCase().equals("almuerzo")) {
                            Tipo = "desayuno";
                        } else {
                            Tipo = "almuerzo";
                        }
                    }
                    
                    // 3. Sobreescribimos la variable 'linea' con los datos nuevos
                    linea = Fecha + " | " + Tipo + " | " + NPlato + " | " + NBebida + " | " + NPostre + " | " + NPEst + " | " + NPProf + " | " + NPEmp + " | " + contadorActual;
                    modificado = true;
                }
                lineas.add(linea);
            }
        }

        // Escribimos la lista completa (con la línea ya modificada) reemplazando el archivo
        if (modificado) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, false))) {
                for (String l : lineas) {
                    escritor.write(l);
                    escritor.newLine();
                }
            }
            return "ACTUALIZACION_EXITOSA";
        }
        return "MENU_NO_ENCONTRADO";
    }
}
