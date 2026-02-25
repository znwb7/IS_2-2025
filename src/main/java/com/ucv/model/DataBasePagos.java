package com.ucv.model;

import java.io.*;

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

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = id + " | " + ci + " | " + telefono + " | " + monto + " | " + "0";
            escritor.write(linea);
            escritor.newLine();
            return DataBasePagos.StatusRegistro.REGISTRO_EXITOSO;
        } catch (IOException e) {
            return DataBasePagos.StatusRegistro.ERROR_LECTURA_DB;
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
                        line = partes[0] + " | " + partes[1] + " | " + partes[2] + " | " + partes[3] + " | " + "1";
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