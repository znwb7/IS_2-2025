package com.ucv.view.user;

import com.ucv.model.DataBase;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.BotonCerrarSesion;
import javax.swing.*;
import java.awt.*;

public class VerificacionFacialExitosa extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color AMARILLO_UCV = new Color(255, 210, 35);

    public VerificacionFacialExitosa(String usuarioID) {
        DataBase dataBase = new DataBase();
        // Consumimos el turno en la DB al mostrar este mensaje de éxito
        dataBase.MenuOut(dataBase.ReturnID());

        setTitle("Verificación Exitosa · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor principal con el fondo corporativo
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR (Logo circular a la derecha) ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- 3. PANEL CENTRAL (Mensaje de Éxito) ---
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

        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Botón Cerrar Sesión Estandarizado) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        // Ancho de 120px para mantener la misma estructura que las otras vistas
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        // Componente reutilizable con la pastilla blanca
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Margen de 25px para alineación perfecta con el logo del HeaderUCV
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }
}