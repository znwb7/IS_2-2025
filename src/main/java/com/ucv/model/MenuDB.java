package com.ucv.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDB {

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

    public WriteMenuStatus WriteMenu(Boolean Modify, Boolean Type, String Fecha, String Tipo, String PlatoFuerte, String Bebida, String Postre, String PEstudiante, String PProfesor, String PEmpleado, String Capacidad, String CCB) throws IOException {
        Tipo = Tipo.toLowerCase();
        PlatoFuerte = PlatoFuerte.toLowerCase();
        Bebida = Bebida.toLowerCase();
        Postre = Postre.toLowerCase();

        if (Modify) {
            return ModifyMenu(Type, Fecha, Tipo, PlatoFuerte, Bebida, Postre, PEstudiante, PProfesor, PEmpleado, Capacidad, CCB);
        }

        MakeArchive();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            String NLine = Fecha + " | " + Tipo + " | " + PlatoFuerte + " | " + Bebida + " | " + Postre + " | " + PEstudiante + " | " + PProfesor + " | " + PEmpleado + " | " + "0" + " | " + Capacidad + " | " + CCB;
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

    public WriteMenuStatus ModifyMenu(Boolean Type, String Fecha, String Tipo, String NPlato, String NBebida, String NPostre, String NPEst, String NPProf, String NPEmp, String Capacidad, String CCB) throws IOException {
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

                    linea = Fecha + " | " + Tipo + " | " + NPlato + " | " + NBebida + " | " + NPostre + " | " + NPEst + " | " + NPProf + " | " + NPEmp + " | " + contadorActual + " | " + Capacidad + " | " + CCB;
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

    public boolean consultarExistencia(String fecha, String tipo) {
        return obtenerMenu(fecha, tipo) != null;
    }

    public String[] obtenerMenu(String fecha, String tipo) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split("\\s*\\|\\s*");
                if (partes.length >= 9 && partes[0].equals(fecha) && partes[1].equalsIgnoreCase(tipo)) {
                    return partes;
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo DB: " + e.getMessage());
        }
        return null;
    }
}