package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.PrimaryButton2;

import javax.swing.*;
import java.awt.*;

public class ConfirmacionReserva extends JFrame {

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AMARILLO_UCV = new Color(255, 210, 35);
    private String tipoSeleccionado;
    private String usuarioID;

    public ConfirmacionReserva(String tipoSeleccionado, String usuarioID) {
        this.tipoSeleccionado = tipoSeleccionado;
        this.usuarioID = usuarioID;

        setTitle("Confirmar Reserva · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- COMPONENTES BASE ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- CONTENIDO CENTRAL ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Etiqueta de pregunta
        JLabel lblPregunta = new JLabel("¿Desea confirmar su reserva para el menú?");
        lblPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        lblPregunta.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelCentral.add(lblPregunta, gbc);

        // Tipo de menú dinámico (Ej: Desayuno / Almuerzo)
        JLabel lblMenu = new JLabel(this.tipoSeleccionado);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 55));
        lblMenu.setForeground(AMARILLO_UCV);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelCentral.add(lblMenu, gbc);

        // --- BOTONERA ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setOpaque(false);

        // BOTÓN CANCELAR: Regresa a la selección de menús
        PrimaryButton2 btnNo = new PrimaryButton2("Cancelar", new Color(180, 180, 180));
        btnNo.addActionListener(e -> {
            dispose();
            new MenusUsuario(usuarioID).setVisible(true);
        });

        // BOTÓN CONFIRMAR: Concreta la acción
        PrimaryButton2 btnSi = new PrimaryButton2("Confirmar", AMARILLO_UCV);
        btnSi.setForeground(Color.BLACK);
        btnSi.addActionListener(e -> {
            dispose();
            new ReservaConcretada(this.tipoSeleccionado, usuarioID).setVisible(true);        });

        panelBotones.add(btnNo);
        panelBotones.add(btnSi);

        gbc.gridy = 2;
        panelCentral.add(panelBotones, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- IMPLEMENTACIÓN DEL FOOTER CON CIERRE DE SESIÓN ---
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);
    }

    private JPanel crearFooter() {
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFooter.setOpaque(false);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));

        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 20));
        lblCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Lógica de cierre de sesión unificada
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                com.ucv.ComedorApp.main(null);
            }
        });

        panelFooter.add(lblCerrar);
        return panelFooter;
    }
}