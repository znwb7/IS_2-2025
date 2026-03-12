package com.ucv.view.comedor;

import com.ucv.view.components.HeaderUCV;

import javax.swing.*;
import java.awt.*;

public class VerificacionFacialExitosa extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color AMARILLO_UCV = new Color(255, 210, 35);

    public VerificacionFacialExitosa() {
        setTitle("Verificación Exitosa - Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor principal
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR 
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL (Mensaje de Éxito) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Título de éxito
        JLabel lblExito = new JLabel("¡Verificación de identidad exitosa!");
        lblExito.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblExito.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelCentral.add(lblExito, gbc);

        // Mensaje de permiso de ingreso
        JLabel lblIngreso = new JLabel("Puede ingresar al Comedor");
        lblIngreso.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 38));
        lblIngreso.setForeground(AMARILLO_UCV);
        gbc.gridy = 1;
        panelCentral.add(lblIngreso, gbc);

        // --- BOTÓN VOLVER A VERIFICACIÓN FACIAL ---
        JButton btnVolver = new JButton("Verificar Otro Usuario");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnVolver.setBackground(AMARILLO_UCV);
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setFocusPainted(false);
        btnVolver.setPreferredSize(new Dimension(350, 60));

        btnVolver.addActionListener(e -> {
            // Cierra la ventana actual
            dispose();
            // Abre nuevamente la ventana de verificación facial
            new VerificacionFacialUCV().setVisible(true);
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 0, 0); // espacio superior
        panelCentral.add(btnVolver, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        add(container);
    }
}