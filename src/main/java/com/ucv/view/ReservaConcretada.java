package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.BotonCerrarSesion;
import javax.swing.*;
import java.awt.*;

public class ReservaConcretada extends JFrame {
    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color AMARILLO_TEXTO = new Color(255, 210, 35);

    public ReservaConcretada(String tipoMenuSeleccionado, String usuarioID) {
        setTitle("Reserva Concretada · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor principal
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR (Logo a la derecha) ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- 3. PANEL CENTRAL (Mensaje de Éxito) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblExito = new JLabel("<html><center>Ha concretado su reserva<br>para el menú</center></html>");
        lblExito.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblExito.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelCentral.add(lblExito, gbc);

        JLabel lblMenu = new JLabel(tipoMenuSeleccionado);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 42)); // Un poco más grande para resaltar
        lblMenu.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCentral.add(lblMenu, gbc);

        JLabel lblInstruccion = new JLabel("<html><center>Diríjase al comedor en el horario<br>seleccionado para recibir su comida.</center></html>");
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblInstruccion.setForeground(AMARILLO_TEXTO);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelCentral.add(lblInstruccion, gbc);

        JLabel lblProvecho = new JLabel("¡BUEN PROVECHO!");
        lblProvecho.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 30));
        lblProvecho.setForeground(Color.WHITE);
        gbc.gridy = 3;
        panelCentral.add(lblProvecho, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Botón Cerrar Sesión Estandarizado) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0)); // Ancho corregido

        // Usamos el componente pastilla que ya tiene la lógica de confirmación
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Insets de 25px para alinearse con el logo circular del Header
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }

}