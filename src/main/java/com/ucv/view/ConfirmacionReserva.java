package com.ucv.view;

import com.ucv.model.DataBase;
import com.ucv.model.MenuDB;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.PrimaryButton2;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ConfirmacionReserva extends JFrame {

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AMARILLO_UCV = new Color(255, 210, 35);
    private String tipoSeleccionado;

        private final String usuarioID;

    // --- CONSTRUCTOR CORREGIDO ---
    public ConfirmacionReserva(String tipoSeleccionado, String usuarioID) {
        this.tipoSeleccionado = tipoSeleccionado; // Corregido el nombre de la variable
        this.usuarioID = usuarioID;

        setTitle("Confirmar Reserva · Comedor UCV");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // Componentes base
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // Contenido Central
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
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 55)); // Un poco más grande para resaltar
        lblMenu.setForeground(AMARILLO_UCV);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelCentral.add(lblMenu, gbc);

        // Botonera
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setOpaque(false);

        // BOTÓN CANCELAR: Regresa a la selección de menús
        PrimaryButton2 btnNo = new PrimaryButton2("Cancelar", new Color(180, 180, 180));
        btnNo.addActionListener(e -> {
            dispose();
            // Al regresar, suponemos true para que el usuario vuelva a ver las opciones
            new MenusUsuario(this.usuarioID).setVisible(true);
        });


        String fecha = java.time.LocalDate.now().toString();
        // BOTÓN CONFIRMAR: Concreta la acción
        PrimaryButton2 btnSi = new PrimaryButton2("Confirmar", AMARILLO_UCV);
        btnSi.setForeground(Color.BLACK);
        btnSi.addActionListener(e -> {
            dispose();
            // Abre la pantalla final de éxito
            MenuDB Menudb = new MenuDB();
            Menudb.CountMenu(fecha, tipoSeleccionado);
            new ReservaConcretada(this.tipoSeleccionado, this.usuarioID).setVisible(true);
        });

        panelBotones.add(btnNo);
        panelBotones.add(btnSi);

        gbc.gridy = 2;
        panelCentral.add(panelBotones, gbc);

        container.add(panelCentral, BorderLayout.CENTER);
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 50));

        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 18));
        lblCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();
                com.ucv.ComedorApp.main(null);
            }
        });

        footer.add(lblCerrar);
        return footer;
    }
}