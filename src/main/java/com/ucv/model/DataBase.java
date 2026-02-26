package com.ucv.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

// Enums para los estados de login y registro
public class DataBase {

    public enum LoginStatus {
        EXITO, PASSWORD_INCORRECTO, USUARIO_NO_ENCONTRADO, ARCHIVO_NO_EXISTE
    }

    public enum UpdateMoney {
        SALDO_ACTUALIZADO_CON_EXITO, ERROR_AL_RECARGAR, PAGOMOVIL_NO_ENCONTRADO, ARCHIVO_NO_EXISTE, FONDO_INSUFICIENTE
    }

    public enum RegistroStatus {
        REGISTRO_EXITOSO, PERSONA_YA_EXISTENTE, USUARIO_NO_ENCONTRADO_SECRETARIA, ERROR_LECTURA_DB, FALTA_HASH_BDSECRETARIA
    }

    public enum RolUsuario {
        ADMIN, SECRETARIA, ESTUDIANTE, ERROR
    }

    private final String rutaArchivo;
    private final String rutaPagoMovil;
    private final String rutaBDSecretaria;
    private static final String SEPARATOR = File.separator;

    // Constructor con rutas por defecto
    public DataBase() {
        this.rutaArchivo = System.getProperty("user.dir") + SEPARATOR + "target" + SEPARATOR + "Output" + SEPARATOR + "DataBase.txt";
        this.rutaBDSecretaria = System.getProperty("user.dir") + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "BaseDataSecretaria.txt";
        this.rutaPagoMovil = System.getProperty("user.dir") + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "DataBasePagos.txt";
    }

    // Constructor con rutas personalizadas (para testing o flexibilidad)
    public DataBase(String rutaArchivo, String rutaBDSecretaria, String rutaPagoMovil) {
        this.rutaArchivo = rutaArchivo;
        this.rutaBDSecretaria = rutaBDSecretaria;
        this.rutaPagoMovil = rutaPagoMovil;
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

        RolUsuario rol = EncontrarRolEnSecretaria(id);
        if (rol.name().toLowerCase().equals("error")) return RegistroStatus.ERROR_LECTURA_DB;
        String Hash = FindHash(id);
        if (Hash == null) return RegistroStatus.FALTA_HASH_BDSECRETARIA;

        if (usuarioYaExiste(id)) return RegistroStatus.PERSONA_YA_EXISTENTE;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = name + " | " + id + " | " + password + " | " + rol.name().toLowerCase() + " | " + Hash + " | " + "0";
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
            if (partes.length >= 3 && partes[1].trim().equals(id)) {
                String rol = partes[3].trim().toLowerCase();
                System.out.println("ROL OBTENIDO: " + rol);
                switch (rol) {
                    case "admin": return RolUsuario.ADMIN;
                    case "secretaria": return RolUsuario.SECRETARIA;
                    default: return RolUsuario.ESTUDIANTE;
                }
            }
        }
    }
    return RolUsuario.ESTUDIANTE;
}

    public RolUsuario obtenerRolSecretaria(String id) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(rutaBDSecretaria))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] partes = line.split("\\s*\\|\\s*");
            if (partes.length >= 3 && partes[1].trim().equals(id)) {
                String rol = partes[2].trim().toLowerCase(); // índice 2 en base secretaria
                switch (rol) {
                    case "admin": return RolUsuario.ADMIN;
                    case "secretaria": return RolUsuario.SECRETARIA;
                    default: return RolUsuario.ESTUDIANTE;
                }
            }
        }
    }
    return RolUsuario.ESTUDIANTE;
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

    public String returnHash(String Hash){
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String Line;
            while ((Line = br.readLine()) != null) {
                String[] Word = Line.split("\\s*\\|\\s*");
                if (Line.isEmpty()) continue;
                if (Word[4].equals(Hash)) {
                    return Word[4]; 
                }
            } 
            return null; 
        } catch (IOException e) { 
            return null; 
        }
    }

    private RolUsuario EncontrarRolEnSecretaria(String ID){

        File file = new File(rutaBDSecretaria);
        if (!file.exists()) return RolUsuario.ERROR;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // Separar por el carácter '|' (manejando espacios)
                String[] Word = line.split("\\s*\\|\\s*");
                if (Word[1].equals(ID)) {
                    String rol = Word[2].trim().toLowerCase();
                    switch (rol) {
                        case "admin": return RolUsuario.ADMIN;
                        case "secretaria": return RolUsuario.SECRETARIA;
                        case "estudiante" : return RolUsuario.ESTUDIANTE;
                        default: return RolUsuario.ERROR;
                    }
                }
            }
            return RolUsuario.ERROR;

        }catch( IOException e){

            return RolUsuario.ERROR;

        }
    }
    
    public UpdateMoney UpdateMoney(String ID, String Fecha) {

        File archivo = new File(rutaPagoMovil);
        if (!archivo.exists()) return UpdateMoney.ARCHIVO_NO_EXISTE;
        List<String> lineas = new ArrayList<>();

        boolean DaDaCo = false;
        double MontoEncontrado = 0; // Para guardar el monto del pago

        // 1. Buscamos el monto en la base de datos de Pagos
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] Word = linea.split("\\s*\\|\\s*");
                if (Word.length >= 6 && Word[1].equals(ID) && Word[5].equals(Fecha)) {
                    MontoEncontrado = Double.parseDouble(Word[3]);
                    DaDaCo = true;
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            return UpdateMoney.ARCHIVO_NO_EXISTE;
        }

        // 2. Si se encontró el pago, actualizamos la BD principal (rutaArchivo)
        if (DaDaCo) {
            List<String> lineasPrincipal = new ArrayList<>();
            File filePrincipal = new File(rutaArchivo);
            
            try (BufferedReader br = new BufferedReader(new FileReader(filePrincipal))) {
                String lineaP;
                while ((lineaP = br.readLine()) != null) {
                    String[] WordP = lineaP.split("\\s*\\|\\s*");
                    
                    // Si encontramos al usuario por ID en la BD Principal
                    if (WordP.length >= 6 && WordP[1].equals(ID)) {
                        MontoEncontrado = MontoEncontrado + Double.parseDouble(WordP[5]);
                        MontoEncontrado = Math.round(MontoEncontrado * 100.0) / 100.0;
                        // Reconstruimos la línea: Nombre | ID | Pass | Rol | Hash | Monto
                        String Monto = String.valueOf(MontoEncontrado);
                        lineaP = WordP[0] + " | " + WordP[1] + " | " + WordP[2] + " | " + WordP[3] + " | " + WordP[4] + " | " + Monto;
                    }
                    lineasPrincipal.add(lineaP);
                }
            } catch (IOException e) {
                return UpdateMoney.ERROR_AL_RECARGAR;
            }

            // 3. Escribimos los cambios de vuelta en la BD Principal
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
                for (String l : lineasPrincipal) {
                    escritor.write(l);
                    escritor.newLine();
                }
                return UpdateMoney.SALDO_ACTUALIZADO_CON_EXITO;
            } catch (IOException e) {
                return UpdateMoney.ERROR_AL_RECARGAR;
            }
        }

        return UpdateMoney.PAGOMOVIL_NO_ENCONTRADO;
    }

    public UpdateMoney ExtractMoney(String ID, String montoARestar) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return UpdateMoney.ARCHIVO_NO_EXISTE;

        List<String> lineasPrincipal = new ArrayList<>();
        boolean usuarioEncontrado = false;
        boolean saldoSuficiente = true;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] WordP = linea.split("\\s*\\|\\s*");

                if (WordP.length >= 6 && WordP[1].equals(ID)) {
                    usuarioEncontrado = true;
                    
                    // 1. Convertimos montos a double para la operación
                    double saldoActual = Double.parseDouble(WordP[5].trim());
                    double cantidadARestar = Double.parseDouble(montoARestar.trim().replace(',', '.'));

                    // 2. Verificación de saldo
                    if (saldoActual >= cantidadARestar) {
                        double nuevoSaldo = saldoActual - cantidadARestar;

                        // 3. Redondeo a 2 decimales
                        nuevoSaldo = Math.round(nuevoSaldo * 100.0) / 100.0;

                        // 4. Reconstruimos la línea con el nuevo saldo
                        linea = WordP[0] + " | " + WordP[1] + " | " + WordP[2] + " | " + WordP[3] + " | " + WordP[4] + " | " + nuevoSaldo;
                    } else {
                        saldoSuficiente = false;
                    }
                }
                lineasPrincipal.add(linea);
            }
        } catch (IOException | NumberFormatException e) {
            return UpdateMoney.ERROR_AL_RECARGAR;
        }

        if (!usuarioEncontrado) return UpdateMoney.PAGOMOVIL_NO_ENCONTRADO;
        if (!saldoSuficiente) return UpdateMoney.FONDO_INSUFICIENTE; // O un estado de "Saldo Insuficiente"

        // 5. Volcamos los datos actualizados al archivo
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
            for (String l : lineasPrincipal) {
                escritor.write(l);
                escritor.newLine();
            }
            return UpdateMoney.SALDO_ACTUALIZADO_CON_EXITO;
        } catch (IOException e) {
            return UpdateMoney.ERROR_AL_RECARGAR;
        }
    }


}
























