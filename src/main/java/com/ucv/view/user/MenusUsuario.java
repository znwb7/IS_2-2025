package com.ucv.view.user;

import com.ucv.controller.MenuController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.TarjetaMenuUsuario;
import com.ucv.view.components.BotonCerrarSesion;

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

        // --- 1. ENCABEZADO ESTÁNDAR ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- 3. LÓGICA DE DATOS ---
        MenuController controller = new MenuController();
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] datosDesayuno = controller.obtenerDatosMenu(fechaActual, "desayuno");
        String[] datosAlmuerzo = controller.obtenerDatosMenu(fechaActual, "almuerzo");

        // --- 4. PANEL CENTRAL (Contenido adaptado al prototipo) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Título Principal
        JLabel lblTitulo = new JLabel("Menus disponibles");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panelCentral.add(lblTitulo, gbc);

        // Contenedor de Tarjetas
        JPanel panelTarjetas = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panelTarjetas.setOpaque(false);

        TarjetaMenuUsuario tarjetaDesayuno = new TarjetaMenuUsuario("Desayuno", "7:00 am - 11:00 am", datosDesayuno, usuarioID, controller);
        TarjetaMenuUsuario tarjetaAlmuerzo = new TarjetaMenuUsuario("Almuerzo", "12:00 pm - 5:00 pm", datosAlmuerzo, usuarioID, controller);

        panelTarjetas.add(tarjetaDesayuno);
        panelTarjetas.add(tarjetaAlmuerzo);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCentral.add(panelTarjetas, gbc);

        // Footer Informativo
        JLabel lblInfo = new JLabel("<html><center><b>El pago se realiza en el momento de acceder al comedor</b><br>"
                + "Sera descontando del saldo que tenga en su monedero<br>"
                + "<i>Si usted no tiene dinero en su monedero, le sera denegado el acceso</i></center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblInfo.setForeground(new Color(220, 220, 220));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelCentral.add(lblInfo, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- 5. PANEL DERECHO (Botón Cerrar Sesión) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }
}