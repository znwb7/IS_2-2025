package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import javax.swing.*;
import java.awt.*;

public class VerificacionFacialExitosa extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color AMARILLO_UCV = new Color(255, 210, 35);

    public VerificacionFacialExitosa(String usuarioID) {
        setTitle("Verificación Exitosa · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- COMPONENTES DE ESTRUCTURA ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- PANEL CENTRAL (Mensaje de Éxito) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. Línea Superior: ¡Verificación de identidad exitosa! (Blanco)
        JLabel lblExito = new JLabel("¡Verificación de identidad exitosa!");
        lblExito.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblExito.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelCentral.add(lblExito, gbc);

        // 2. Línea Inferior: Puede ingresar al Comedor (Amarillo e Itálica)
        JLabel lblIngreso = new JLabel("Puede ingresar al Comedor");
        lblIngreso.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 38));
        lblIngreso.setForeground(AMARILLO_UCV);
        gbc.gridy = 1;
        panelCentral.add(lblIngreso, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- FOOTER IMPLEMENTADO ---
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);
    }

    // BLOQUE DE CÓDIGO IMPLEMENTADO
    private JPanel crearFooter() {
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFooter.setOpaque(false);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));

        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 20));
        lblCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                // Redirección al punto de entrada principal
                com.ucv.ComedorApp.main(null);
            }
        });
        panelFooter.add(lblCerrar);
        return panelFooter;
    }
}