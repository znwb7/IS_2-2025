package com.ucv.model;

import java.io.*;
import java.time.LocalDate;

public class DataBasePagos{
    // Enums para los estados de registro del Pago
    public enum StatusRegistro {
        REGISTRO_EXITOSO, PAGO_YA_EXISTE, PAGO_NO_ENCONTRADO_DB, ERROR_LECTURA_DB,
    }
    public enum StatusPago {
        PAGO_FUE_UTILIZADO, PAGO_NO_FUE_UTILIZADO, PAGO_NO_EXISTE,
    }

    private final String rutaArchivo;
    private static final String SEPARATOR = File.separator;

    // Constructor con rutas por defecto
    public DataBasePagos(){
        this.rutaArchivo = System.getProperty("user.dir") + SEPARATOR + "target" + SEPARATOR + "Output" + SEPARATOR + "DataBasePagos.txt";
    }

    // Constructor con rutas personalizadas (para testing o flexibilidad)
    public DataBasePagos(String rutaArchivo){
        this.rutaArchivo = rutaArchivo;
    }

    public DataBasePagos.StatusRegistro registrar(String id, String telefono, String ci, String monto) throws IOException{
        crearArchivo();

        if (pagoExiste(id)) return DataBasePagos.StatusRegistro.PAGO_YA_EXISTE;
        LocalDate fechaActual = LocalDate.now(); 
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = id + " | " + ci + " | " + telefono + " | " + monto + " | " + "0" + " | " +fechaActual;
            escritor.write(linea);
            escritor.newLine();
            return DataBasePagos.StatusRegistro.REGISTRO_EXITOSO;
        } catch (IOException e) {
            return DataBasePagos.StatusRegistro.ERROR_LECTURA_DB;
        }
    }

    public static void main(String[] args) {
        // Instanciamos la base de datos
        DataBasePagos db = new DataBasePagos();

        try {
            System.out.println("--- Iniciando Pruebas de DataBasePagos ---\n");

            // 1. Probar registro exitoso
            System.out.println("1. Registrando pago nuevo (ID: 101)...");
            DataBasePagos.StatusRegistro reg1 = db.registrar("101", "04121234567", "25666777", "150.00");
            System.out.println("Resultado: " + reg1);

            // 2. Probar registro de ID duplicado
            System.out.println("\n2. Intentando registrar el mismo ID (101)...");
            DataBasePagos.StatusRegistro reg2 = db.registrar("101", "04240000000", "11222333", "50.0");
            System.out.println("Resultado: " + reg2);

            // 3. Verificar estado de un pago que no ha sido usado (debe ser 0 por defecto)
            System.out.println("\n3. Verificando si el pago 101 ha sido utilizado...");
            DataBasePagos.StatusPago status1 = db.pagoUtilizado("101");
            System.out.println("Estado actual: " + status1);

            // 4. Marcar pago como utilizado
            System.out.println("\n4. Marcando pago 101 como UTILIZADO...");
            DataBasePagos.StatusPago statusUpdate = db.marcarPagoComoUtilizado("101");
            System.out.println("Resultado de la operación: " + statusUpdate);

            // 5. Verificar que el cambio se guardó en el archivo
            System.out.println("\n5. Verificando estado del pago 101 después del cambio...");
            DataBasePagos.StatusPago status2 = db.pagoUtilizado("101");
            System.out.println("Nuevo estado: " + status2);

            // 6. Probar con un ID que no existe
            System.out.println("\n6. Buscando un ID inexistente (999)...");
            System.out.println("Resultado: " + db.pagoUtilizado("999"));

            System.out.println("\n--- Pruebas Finalizadas ---");
            System.out.println("Puedes revisar el archivo en: target/Output/DataBasePagos.txt");

        } catch (IOException e) {
            System.err.println("Error durante la ejecución de las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }








    private void crearArchivo() throws IOException{
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();

        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
    }

    private boolean pagoExiste(String id) throws IOException{
        File file = new File(rutaArchivo);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 1 && partes[0].trim().equals(id)) return true;
            }
        }
        return false;
    }

    public DataBasePagos.StatusPago pagoUtilizado(String id) throws IOException{
        File file = new File(rutaArchivo);
        if (!file.exists()) return StatusPago.PAGO_NO_EXISTE;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 1 && partes[0].trim().equals(id)){
                    if (partes.length >= 5 && partes[4].trim().equals("0")) {
                        return StatusPago.PAGO_NO_FUE_UTILIZADO;
                    }else{
                        return StatusPago.PAGO_FUE_UTILIZADO;
                    }
                }
            }
        }
        return StatusPago.PAGO_NO_EXISTE;
    }

    public DataBasePagos.StatusPago marcarPagoComoUtilizado(String id) throws IOException{
        File file = new File(rutaArchivo);
        if (!file.exists()) return StatusPago.PAGO_NO_EXISTE;

        StringBuilder nuevoContenido = new StringBuilder();
        StatusPago resultado = StatusPago.PAGO_NO_EXISTE;

        LocalDate fechaActual = LocalDate.now();
        boolean huboCambio = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    nuevoContenido.append(line).append(System.lineSeparator());
                    continue;
                }
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 5 && partes[0].trim().equals(id)) {
                    if (partes[4].trim().equals("0")) {
                        // Modificamos la línea para que ahora tenga un "1"
                        line = partes[0] + " | " + partes[1] + " | " + partes[2] + " | " + partes[3] + " | " + "1" + " | " + fechaActual;
                        resultado = StatusPago.PAGO_FUE_UTILIZADO;
                        huboCambio = true;
                    } else {
                        resultado = StatusPago.PAGO_FUE_UTILIZADO; // Ya estaba usado
                    }
                }
                nuevoContenido.append(line).append(System.lineSeparator());
            }
        }
        // Solo cambiamos el estado del pago si realmente encontramos el ID y cambiamos su estado
        if(huboCambio){
            try(BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, false))){
                escritor.write(nuevoContenido.toString());
            }
        }
        return resultado;
    }
}