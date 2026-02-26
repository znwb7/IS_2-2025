package com.ucv.view;

import com.ucv.model.DataBase;
import com.ucv.view.components.HeaderUCV;
import javax.swing.*;
import java.awt.*;

public class VerificacionFacialExitosa extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color AMARILLO_UCV = new Color(255, 210, 35);

    public VerificacionFacialExitosa() {
        DataBase dataBase = new DataBase();
        dataBase.MenuOut(dataBase.ReturnID());


        setTitle("Verificación Exitosa · Comedor UCV");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- COMPONENTES DE ESTRUCTURA ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

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
        gbc.insets = new Insets(0, 0, 10, 0); // Pequeño margen inferior
        panelCentral.add(lblExito, gbc);

        // 2. Línea Inferior: Puede ingresar al Comedor (Amarillo e Itálica para resaltar)
        JLabel lblIngreso = new JLabel("Puede ingresar al Comedor");
        lblIngreso.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 38));
        lblIngreso.setForeground(AMARILLO_UCV);
        gbc.gridy = 1;
        panelCentral.add(lblIngreso, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- FOOTER ---
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 50));

        JLabel cerrarSesion = new JLabel("<html><u>Cerrar Sesión</u></html>");
        cerrarSesion.setForeground(Color.WHITE);
        cerrarSesion.setFont(new Font("Arial", Font.PLAIN, 20));
        cerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();
                // Aquí podrías redirigir al Login
            }
        });

        footer.add(cerrarSesion);
        return footer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VerificacionFacialExitosa().setVisible(true));
    }
}