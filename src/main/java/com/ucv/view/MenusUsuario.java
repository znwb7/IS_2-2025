package com.ucv.view;

import com.ucv.controller.MenuController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.TarjetaMenuUsuario;
import com.ucv.view.components.BotonCerrarSesion; // Importado

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

        // --- 1. ENCABEZADO ESTÁNDAR (Logo circular a la derecha) ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- 3. LÓGICA DE DATOS ---
        MenuController controller = new MenuController();
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] datosDesayuno = controller.obtenerDatosMenu(fechaActual, "desayuno");
        String[] datosAlmuerzo = controller.obtenerDatosMenu(fechaActual, "almuerzo");

        // --- 4. PANEL CENTRAL (Tarjetas de Menú) ---
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

        // --- 5. PANEL DERECHO (Botón Cerrar Sesión) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        // Ancho estándar de 120px para que la pastilla respire bien
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        // Componente reutilizable con icono y lógica de logout
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Margen de 25px para alineación con el logo del Header
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }

    // El método crearFooter() original ha sido eliminado
}