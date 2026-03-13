package com.ucv.view.admin;

import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.BotonCerrarSesion;
import com.ucv.view.components.HeaderUCV;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Reporte extends JFrame {

    private final Color AZUL_FONDO = new Color(18, 71, 150);

    public Reporte() {
        setTitle("Comedor UCV - Reporte");
        setSize(1920, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        // --- Header ---
        add(new HeaderUCV(), BorderLayout.NORTH);

        // --- Contenedor Inferior ---
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);

        // Sidebar izquierdo
        contenedorInferior.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- Panel Central más pequeño y centrado ---
        JPanel panelCentralWrapper = new JPanel(new GridBagLayout());
        panelCentralWrapper.setOpaque(false);

        JPanel panelCentral = new JPanel(new BorderLayout(0, 10));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setPreferredSize(new Dimension(800, 400));
        panelCentral.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        // TextArea dentro del panel
        JTextArea textAreaArchivo = new JTextArea();
        textAreaArchivo.setEditable(false);
        textAreaArchivo.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JScrollPane scrollArchivo = new JScrollPane(textAreaArchivo);
        panelCentral.add(scrollArchivo, BorderLayout.CENTER);

        // Botón para descargar el archivo
        JButton btnDescargar = new JButton("Descargar archivo");
        btnDescargar.setFont(new Font("Arial", Font.BOLD, 16));
        btnDescargar.addActionListener(e -> {
            // Fecha de hoy en formato yyyyMMdd
            String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("ControlDeReservas_" + fechaHoy + ".txt"));
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File destino = fileChooser.getSelectedFile();
                try {
                    Files.copy(new File("target/Output/ControlReservas.txt").toPath(),
                            destino.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(this, "Archivo descargado correctamente.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al descargar el archivo: " + ex.getMessage());
                }
        }
        });
        panelCentral.add(btnDescargar, BorderLayout.SOUTH);

        panelCentralWrapper.add(panelCentral);
        contenedorInferior.add(panelCentralWrapper, BorderLayout.CENTER);

        // --- Cargar contenido del archivo ---
        String rutaArchivo = "target/Output/ControlReservas.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                textAreaArchivo.append(linea + "\n");
            }
        } catch (IOException e) {
            textAreaArchivo.setText("No se pudo leer el archivo: " + e.getMessage());
        }

        // --- Panel derecho (Botón Cerrar Sesión) ---
        contenedorInferior.add(crearContenedorBotonCerrar(), BorderLayout.EAST);

        add(contenedorInferior, BorderLayout.CENTER);
    }

    private JPanel crearContenedorBotonCerrar() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }
}