package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.PrimaryButton2;
import com.ucv.view.components.BotonCerrarSesion;

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

        // Contenedor principal
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR (Logo a la derecha, título a la izquierda) ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- 3. CONTENIDO CENTRAL ---
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

        // Tipo de menú dinámico
        JLabel lblMenu = new JLabel(this.tipoSeleccionado);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 55));
        lblMenu.setForeground(AMARILLO_UCV);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelCentral.add(lblMenu, gbc);

        // Botonera de Confirmación
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setOpaque(false);

        PrimaryButton2 btnNo = new PrimaryButton2("Cancelar", new Color(180, 180, 180));
        btnNo.addActionListener(e -> {
            dispose();
            new MenusUsuario(usuarioID).setVisible(true);
        });

        PrimaryButton2 btnSi = new PrimaryButton2("Confirmar", AMARILLO_UCV);
        btnSi.setForeground(Color.BLACK);
        btnSi.addActionListener(e -> {
            dispose();
            new ReservaConcretada(this.tipoSeleccionado, usuarioID).setVisible(true);
        });

        panelBotones.add(btnNo);
        panelBotones.add(btnSi);

        gbc.gridy = 2;
        panelCentral.add(panelBotones, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Boton Cerrar Sesión) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        // Ancho suficiente para la pastilla blanca
        contenedor.setPreferredSize(new Dimension(120, 0));

        // Componente con icono y lógica de confirmación integrada
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Alineación visual con el centro del logo del header (25px)
        gbc.insets = new Insets(25, 0, 0, 0);

        contenedor.add(btnCerrar, gbc);
        return contenedor;
    }
}