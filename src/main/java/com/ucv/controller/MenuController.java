package com.ucv.controller;

import com.ucv.model.DataBase;
import com.ucv.model.MenuDB;
import com.ucv.view.admin.AdminUCV;
import com.ucv.view.admin.AgregarPlatoUCV;
import com.ucv.view.admin.GestionMenus;
import com.ucv.view.admin.FechaMenus;
import com.ucv.view.user.ConfirmacionReserva;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class MenuController {

    private final MenuDB menuDB;
    private final DataBase dataBase;

    public MenuController() {
        this.menuDB = new MenuDB();
        this.dataBase = new DataBase();
    }

    public String[] obtenerDatosMenu(String fecha, String tipo) {
        return menuDB.obtenerMenu(fecha, tipo);
    }

    public void procesarFechaSeleccionada(String fecha, JFrame vistaActual) {
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);

            java.time.LocalDate fechaIngresada = java.time.LocalDate.parse(fecha, formatter);
            java.time.LocalDate hoy = java.time.LocalDate.now();

            if (fechaIngresada.isBefore(hoy)) {
                javax.swing.JOptionPane.showMessageDialog(vistaActual, "No se pueden registrar ni modificar menús de fechas pasadas.", "Fecha Inválida", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (java.time.format.DateTimeParseException e) {
            javax.swing.JOptionPane.showMessageDialog(vistaActual, "La fecha seleccionada no es válida en el calendario.", "Fecha Inexistente", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] datosDesayuno = menuDB.obtenerMenu(fecha, "desayuno");
        String[] datosAlmuerzo = menuDB.obtenerMenu(fecha, "almuerzo");

        if (vistaActual != null) vistaActual.dispose();
        new GestionMenus(fecha, datosDesayuno, datosAlmuerzo, this).setVisible(true);
    }

    public void irAAgregarPlato(String fecha, String tipo, JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new AgregarPlatoUCV(fecha, tipo, this).setVisible(true);
    }

public void registrarNuevoPlato(String fecha, String tipo, String plato, String bebida, String postre,
                                String pEst, String pProf, String pEmp, String pBec,
                                String capacidad, String ccb,
                                String tarifaEst, String tarifaProf, String tarifaEmp, String tarifaBec,
                                JFrame vistaActual) {

    try {

        float tEst = Float.parseFloat(tarifaEst.replace(",", "."));
        float tProf = Float.parseFloat(tarifaProf.replace(",", "."));
        float tEmp = Float.parseFloat(tarifaEmp.replace(",", "."));
        float tBec = Float.parseFloat(tarifaBec.replace(",", "."));

        StringBuilder erroresRangos = new StringBuilder();

        if (tEst < 20f || tEst > 30f)
            erroresRangos.append("La tarifa de Estudiante debe estar entre 20% y 30%.\n");

        if (tProf < 70f || tProf > 90f)
            erroresRangos.append("La tarifa de Profesor debe estar entre 70% y 90%.\n");

        if (tEmp < 90f || tEmp > 110f)
            erroresRangos.append("La tarifa de Empleado debe estar entre 90% y 110%.\n");

        if (tBec < 5f || tBec > 19f)
            erroresRangos.append("La tarifa de Becado debe estar entre 5% y 19%.\n");

        if (erroresRangos.length() > 0) {
            javax.swing.JOptionPane.showMessageDialog(
                    vistaActual,
                    erroresRangos.toString().trim(),
                    "Rango Inválido",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

    } catch (NumberFormatException e) {

        javax.swing.JOptionPane.showMessageDialog(
                vistaActual,
                "Las tarifas deben ser valores numéricos válidos.",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    String platoFormat = capitalizarPalabras(plato);
    String bebidaFormat = capitalizarPalabras(bebida);
    String postreFormat = capitalizarPalabras(postre);

    try {

        boolean existe = menuDB.consultarExistencia(fecha, tipo);

        MenuDB.WriteMenuStatus status = menuDB.WriteMenu(
                existe,
                false,
                fecha,
                tipo,
                platoFormat,
                bebidaFormat,
                postreFormat,
                pEst,
                pProf,
                pEmp,
                pBec,
                capacidad,
                ccb
        );

        if (status == MenuDB.WriteMenuStatus.REGISTRO_EXITOSO ||
            status == MenuDB.WriteMenuStatus.ACTUALIZACION_EXITOSA) {

            if (vistaActual != null) vistaActual.dispose();
            procesarFechaSeleccionada(fecha, null);

        } else {

            javax.swing.JOptionPane.showMessageDialog(
                    vistaActual,
                    "Error al guardar el menú: " + status
            );
        }

    } catch (java.io.IOException e) {

        javax.swing.JOptionPane.showMessageDialog(
                vistaActual,
                "Error crítico de archivo: " + e.getMessage()
        );
    }
}

    private String capitalizarPalabras(String texto) {
        if (texto == null || texto.trim().isEmpty() || texto.equals("N/A")) return texto;
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    public void volverAGestionMenu(JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new FechaMenus(this).setVisible(true);
    }

    public void volverAAdmin(JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new AdminUCV("Administrador").setVisible(true);
    }

    public void irAConfirmacionReserva(String tipo, String usuarioID, JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new ConfirmacionReserva(tipo, usuarioID).setVisible(true);
    }

    // --- LÓGICA DE PROCESAMIENTO DE RESERVAS---
    public boolean procesarReserva(String tipo, String usuarioID) {

        String fechaActual = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        VerificarYCrearArchivoReservas(fechaActual);


        // 2. Verificar cupos
        if (menuDB.CantidadDisponible(tipo) <= 0) {
            return false;
        }

        // 3. Aumentar contador del menú
        MenuDB.ReWriteStatus status = menuDB.CountMenu(fechaActual, tipo);

        if (status == MenuDB.ReWriteStatus.REWRITE_EXITOSO) {

            registrarReservaLocal(usuarioID, fechaActual, tipo);

            if (tipo.equalsIgnoreCase("desayuno")) {
                dataBase.MenuDesayunoActive(usuarioID);
            }

            if (tipo.equalsIgnoreCase("almuerzo")) {
                dataBase.MenuAlmuerzoActive(usuarioID);
            }

            // REGISTRAR PRECIO DE LA COMIDA EN LA BD
            dataBase.PrecioComida(usuarioID, tipo);

            return true;
        }

            return false;
    }



    public static void main(String[] args) {
        System.out.println("=== INICIANDO DEBUG DE MENU_CONTROLLER ===");
        MenuController controller = new MenuController();

        // 1. CONFIGURACIÓN DEL ESCENARIO DE PRUEBA
        String idPrueba = "31983764"; // El ID que usamos antes
        String tipoComida = "almuerzo";
        String fechaHoy = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.println("Escenario: Reserva de " + tipoComida + " para el ID: " + idPrueba);

        // 2. EJECUCIÓN DEL PROCESO
        // Nota: Esto disparará VerificarYCrearArchivoReservas y registrarReservaLocal
        boolean resultado = controller.procesarReserva(tipoComida, idPrueba);

        // 3. VERIFICACIÓN DE RESULTADOS
        if (resultado) {
            System.out.println("✅ RESULTADO: Reserva procesada con éxito.");
            System.out.println("Acciones realizadas:");
            System.out.println("- Se verificó/creó ControlReservas.txt");
            System.out.println("- Se aumentó el contador en MenuDB");
            System.out.println("- Se registró la reserva local por Rol");
            System.out.println("- Se activó la bandera de menú en DataBase");
        } else {
            System.out.println("❌ RESULTADO: La reserva falló.");
            System.out.println("Causas posibles: No hay cupos en MenuDB o error al escribir archivos.");
        }

        System.out.println("=== FIN DEL DEBUG ===");
    }

    // --- MÉTODOS AUXILIARES DE CONTROL DE RESERVAS ---

    private void VerificarYCrearArchivoReservas(String fechaActual) {
        String ruta = System.getProperty("user.dir") + java.io.File.separator + "target" + java.io.File.separator + "Output" + java.io.File.separator + "ControlReservas.txt";
        java.io.File archivo = new java.io.File(ruta);
        boolean debeCrear = false;

        // 1. Verificación: ¿Existe el archivo o es un día nuevo?
        if (!archivo.exists()) {
            debeCrear = true; 
        } else {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(archivo))) {
                String linea = br.readLine();
                if (linea != null && linea.contains("|")) {
                    String fechaEnArchivo = linea.split("\\|")[1].trim();
                    if (!fechaEnArchivo.equals(fechaActual)) {
                        debeCrear = true; 
                    }
                }
            } catch (java.io.IOException e) {
                debeCrear = true; 
            }
        }

        // 2. Creación/Sobreescritura con el formato solicitado
        if (debeCrear) {
            if (archivo.getParentFile() != null) {
                archivo.getParentFile().mkdirs();
            }

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(archivo, false))) {
                // Formato: Rol Tipo : Cantidad | Fecha
                bw.write("estudiante Desayuno : 0 | " + fechaActual); bw.newLine();
                bw.write("estudiante Almuerzo : 0 | " + fechaActual); bw.newLine();
                bw.write("profesor Desayuno : 0 | " + fechaActual); bw.newLine();
                bw.write("profesor Almuerzo : 0 | " + fechaActual); bw.newLine();
                bw.write("empleado Desayuno : 0 | " + fechaActual); bw.newLine();
                bw.write("empleado Almuerzo : 0 | " + fechaActual); bw.newLine();
                bw.write("exonerado Desayuno : 0 | " + fechaActual); bw.newLine();
                bw.write("exonerado Almuerzo : 0 | " + fechaActual); bw.newLine();
                bw.write("becado Desayuno : 0 | " + fechaActual); bw.newLine();
                bw.write("becado Almuerzo : 0 | " + fechaActual); bw.newLine();
                
                System.out.println("-> Archivo ControlReservas.txt actualizado para: " + fechaActual);
            } catch (java.io.IOException e) {
                System.err.println("Error al crear ControlReservas.txt: " + e.getMessage());
            }
        }
    }

    private void registrarReservaLocal(String usuarioID, String fecha, String tipo) {
        String ruta = System.getProperty("user.dir") + java.io.File.separator + "target" + java.io.File.separator + "Output" + java.io.File.separator + "ControlReservas.txt";
        java.io.File archivo = new java.io.File(ruta);
        
        if (!archivo.exists()) return;

        List<String> lineasActualizadas = new ArrayList<>();
        DataBase db = new DataBase();
        
        try {
            // 1. Obtenemos el rol del usuario desde la DB principal
            // Nota: asumo que obtenerRol devuelve el Enum RolUsuario
            String rolUsuario = db.obtenerRol(usuarioID).name().toLowerCase();
            
            // 2. Leemos el archivo de reservas para buscar la línea a modificar
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;

                    // Separamos por el ":" y por el "|" para identificar los campos
                    // Formato esperado: rol tipo : cantidad | fecha
                    String[] partesPorPipe = linea.split("\\s*\\|\\s*");
                    String[] partesPorDosPuntos = partesPorPipe[0].split("\\s*:\\s*");
                    String encabezado = partesPorDosPuntos[0].trim().toLowerCase(); // "estudiante desayuno"

                    // Verificamos si esta línea coincide con el Rol y el Tipo de comida buscado
                    if (encabezado.equals(rolUsuario + " " + tipo.toLowerCase())) {
                        int cantidadActual = Integer.parseInt(partesPorDosPuntos[1].trim());
                        int nuevaCantidad = cantidadActual + 1;
                        
                        // Reconstruimos la línea con el nuevo valor
                        linea = rolUsuario + " " + tipo.toLowerCase() + " : " + nuevaCantidad + " | " + partesPorPipe[1];
                    }
                    
                    lineasActualizadas.add(linea);
                }
            }

            // 3. Sobrescribimos el archivo con los nuevos conteos
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(archivo, false))) {
                for (String l : lineasActualizadas) {
                    bw.write(l);
                    bw.newLine();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al registrar reserva local: " + e.getMessage());
        }
    }
}