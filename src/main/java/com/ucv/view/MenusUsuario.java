package com.ucv.view;

import com.ucv.controller.MenuController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.TarjetaMenuUsuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MenusUsuario extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private final String usuarioID;

    public MenusUsuario(String usuarioID) {
        this.usuarioID = usuarioID;
        setTitle("Menús Disponibles · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- EXTRACCIÓN A TRAVÉS DEL CONTROLADOR (CUMPLE MVC) ---
        MenuController controller = new MenuController();
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // El controlador nos da el arreglo, la vista no sabe de dónde sale
        String[] datosDesayuno = controller.obtenerDatosMenu(fechaActual, "desayuno");
        String[] datosAlmuerzo = controller.obtenerDatosMenu(fechaActual, "almuerzo");

        // --- RENDERIZADO DEL PANEL CENTRAL ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40);

        // Tarjeta Desayuno
        TarjetaMenuUsuario tarjetaDesayuno = new TarjetaMenuUsuario("Desayuno", "7:00 AM - 9:00 AM", datosDesayuno, usuarioID, controller);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(tarjetaDesayuno, gbc);

        // Tarjeta Almuerzo
        TarjetaMenuUsuario tarjetaAlmuerzo = new TarjetaMenuUsuario("Almuerzo", "11:30 AM - 1:30 PM", datosAlmuerzo, usuarioID, controller);
        gbc.gridx = 1;
        panelCentral.add(tarjetaAlmuerzo, gbc);

        container.add(panelCentral, BorderLayout.CENTER);
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