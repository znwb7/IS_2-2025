package com.ucv.model;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagoModel {

    public enum ResultadoValidacion {
        CAMPOS_INVALIDOS,
        PAGO_NO_ENCONTRADO,
        PAGO_YA_UTILIZADO,
        MONTO_INCORRECTO,
        RECARGA_EXITOSA,
        ERROR_SISTEMA
    }

    private static class Pago {
        private final String referencia;
        private final String cedula;
        private final String telefono;
        private final BigDecimal monto;
        private boolean utilizado;
        private LocalDate fechaUso;

        public Pago(String referencia, String cedula, String telefono,
                    BigDecimal monto, boolean utilizado, LocalDate fechaUso) {

            this.referencia = referencia;
            this.cedula = cedula;
            this.telefono = telefono;
            this.monto = monto;
            this.utilizado = utilizado;
            this.fechaUso = fechaUso;
        }

        public String getReferencia() { return referencia; }
        public String getCedula() { return cedula; }
        public BigDecimal getMonto() { return monto; }
        public boolean isUtilizado() { return utilizado; }

        public void marcarComoUtilizado(LocalDate fecha) {
            this.utilizado = true;
            this.fechaUso = fecha;
        }

        public String toFileFormat() {
            return referencia + " | " +
                   cedula + " | " +
                   telefono + " | " +
                   monto + " | " +
                   (utilizado ? "1" : "0") + " | " +
                   (fechaUso != null ? fechaUso : "");
        }
    }

    private final String carpetaOutput =
            System.getProperty("user.dir") + File.separator +
            "target" + File.separator + "output";

    private final String rutaArchivoOutput =
            carpetaOutput + File.separator + "DataBasePagos.txt";

    public PagoModel() {
        inicializarArchivoOutput();
    }

    /** Copia el archivo de resources a target/output si no existe */
    private void inicializarArchivoOutput() {
        try {
            File dir = new File(carpetaOutput);
            if (!dir.exists()) dir.mkdirs();

            File archivoOutput = new File(rutaArchivoOutput);
            if (!archivoOutput.exists()) {
                try (InputStream is = getClass().getClassLoader()
                        .getResourceAsStream("DataBasePagos.txt")) {
                    if (is == null) throw new RuntimeException("No se encontró DataBasePagos.txt en resources");
                    Files.copy(is, archivoOutput.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando archivo de pagos en target/output", e);
        }
    }

    /** Valida campos y marca pago como utilizado si es correcto */
    public ResultadoValidacion validarYProcesarPago(
            String cedulaInput,
            String montoInput,
            String referenciaInput) {

        try {
            if (cedulaInput == null || montoInput == null || referenciaInput == null)
                return ResultadoValidacion.CAMPOS_INVALIDOS;

            cedulaInput = cedulaInput.trim();
            referenciaInput = referenciaInput.trim();

            if (!referenciaInput.matches("\\d{4,}"))
                return ResultadoValidacion.CAMPOS_INVALIDOS;

            BigDecimal monto;
            try {
                monto = new BigDecimal(montoInput.trim());
                if (monto.compareTo(BigDecimal.ZERO) <= 0)
                    return ResultadoValidacion.CAMPOS_INVALIDOS;
            } catch (Exception e) {
                return ResultadoValidacion.CAMPOS_INVALIDOS;
            }

            List<Pago> pagos = cargarPagos();

            for (Pago p : pagos) {
                if (p.getReferencia().equals(referenciaInput)) {

                    if (!p.getCedula().equals(cedulaInput))
                        return ResultadoValidacion.PAGO_NO_ENCONTRADO;

                    if (p.isUtilizado())
                        return ResultadoValidacion.PAGO_YA_UTILIZADO;

                    if (p.getMonto().compareTo(monto) != 0)
                        return ResultadoValidacion.MONTO_INCORRECTO;

                    p.marcarComoUtilizado(LocalDate.now());
                    guardarPagos(pagos);
                    return ResultadoValidacion.RECARGA_EXITOSA;
                }
            }

            return ResultadoValidacion.PAGO_NO_ENCONTRADO;

        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoValidacion.ERROR_SISTEMA;
        }
    }

    /** Carga todos los pagos desde target/output */
    private List<Pago> cargarPagos() throws IOException {
        List<Pago> lista = new ArrayList<>();
        File archivo = new File(rutaArchivoOutput);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split("\\s*\\|\\s*");
                if (p.length < 5) continue;

                String referencia = p[0].trim();
                String cedula = p[1].trim();
                String telefono = p[2].trim();
                BigDecimal monto = new BigDecimal(p[3].trim());
                boolean utilizado = p[4].trim().equals("1");

                LocalDate fecha = null;
                if (p.length >= 6 && !p[5].trim().isEmpty())
                    fecha = LocalDate.parse(p[5].trim());

                lista.add(new Pago(referencia, cedula, telefono, monto, utilizado, fecha));
            }
        }

        return lista;
    }

    /** Guarda todos los pagos en target/output */
    private void guardarPagos(List<Pago> pagos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoOutput, false))) {
            for (Pago p : pagos) {
                bw.write(p.toFileFormat());
                bw.newLine();
            }
        }
    }
}