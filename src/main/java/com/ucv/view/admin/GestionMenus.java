package com.ucv.view.admin;

import com.ucv.controller.MenuController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.ContenidoMenuDatos;
import com.ucv.view.components.ContenidoMenuVacio;
import com.ucv.view.components.BotonCerrarSesion;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GestionMenus extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color GRIS_BOTON_VOLVER = new Color(190, 190, 190);

    private String fecha;
    private MenuController controller;

    public GestionMenus(String fechaSeleccionada, String[] datosDesayuno, String[] datosAlmuerzo, MenuController controller) {
        this.fecha = fechaSeleccionada;
        this.controller = controller;

        setTitle("Detalle de Menús · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor principal
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- 3. PANEL CENTRAL (Tarjetas de Menú) ---
        JPanel panelCentral = new JPanel(null);
        panelCentral.setOpaque(false);

        JLabel lblTituloMenu = new JLabel("Menús para el " + fechaSeleccionada);
        lblTituloMenu.setFont(new Font("Arial", Font.BOLD, 32));
        lblTituloMenu.setForeground(Color.WHITE);
        lblTituloMenu.setBounds(320, 45, 600, 40);
        panelCentral.add(lblTituloMenu);

        JButton btnVolver = crearBotonVolver();
        btnVolver.setBounds(920, 45, 130, 40);
        btnVolver.addActionListener(e -> {
            controller.volverAGestionMenu(this);
        });
        panelCentral.add(btnVolver);

        // Tarjetas dinámicas de Desayuno y Almuerzo
        panelCentral.add(crearTarjetaDinamica("Desayuno", "7:00 am - 11:00 am", 294, datosDesayuno));
        panelCentral.add(crearTarjetaDinamica("Almuerzo", "12:00 pm - 3:00 pm", 715, datosAlmuerzo));

        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Boton Cerrar Sesión) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setPreferredSize(new Dimension(120, 0));

        // Componente con pastilla blanca e icono
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Margen superior estandarizado a 25px para alinear con el logo
        gbc.insets = new Insets(25, 0, 0, 0);

        contenedor.add(btnCerrar, gbc);
        return contenedor;
    }

    private JPanel crearTarjetaDinamica(String tipo, String horario, int x, String[] datosMenu) {
        JPanel contenedor = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_TARJETA);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
                g2.dispose();
            }
        };
        contenedor.setOpaque(false);
        contenedor.setBounds(x, 100, 400, 450);

        if (datosMenu != null) {
            ContenidoMenuDatos datos = new ContenidoMenuDatos(tipo, horario, fecha, datosMenu, this, controller);
            datos.setBounds(0, 0, 400, 450);
            contenedor.add(datos);
        } else {
            ContenidoMenuVacio vacio = new ContenidoMenuVacio(tipo, fecha, this, controller);
            vacio.setBounds(0, 0, 400, 450);
            contenedor.add(vacio);
        }
        return contenedor;
    }

    public boolean confirmarModificacion(String tipoMenu) {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de modificar el " + tipoMenu + "?",
                "Confirmar Modificación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return respuesta == JOptionPane.YES_OPTION;
    }

    private JButton crearBotonVolver() {
        JButton btn = new JButton("Volver") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_BOTON_VOLVER);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}