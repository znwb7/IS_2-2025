package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.model.DataBase;
import com.ucv.view.components.ContenidoMenuDatos;
import com.ucv.view.components.ContenidoMenuVacio;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class FechaMenusNewUCV extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color GRIS_BOTON_VOLVER = new Color(190, 190, 190);

    private String fecha;

    public FechaMenusNewUCV(String fechaSeleccionada, boolean tieneDesayuno, boolean tieneAlmuerzo) {
        this.fecha = fechaSeleccionada;
        setTitle("Detalle de Menús · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // Componentes de estructura
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = new JPanel(null);
        panelCentral.setOpaque(false);

        // Título de la fecha
        JLabel lblTituloMenu = new JLabel("Menús para el " + fechaSeleccionada);
        lblTituloMenu.setFont(new Font("Arial", Font.BOLD, 32));
        lblTituloMenu.setForeground(Color.WHITE);
        lblTituloMenu.setBounds(320, 45, 600, 40);
        panelCentral.add(lblTituloMenu);

        // Botón Volver
        JButton btnVolver = crearBotonVolver();
        btnVolver.setBounds(920, 45, 130, 40);
        btnVolver.addActionListener(e -> {
            dispose();
            new GestionMenuUCV().setVisible(true);
        });
        panelCentral.add(btnVolver);

        // --- TARJETA IZQUIERDA (DESAYUNO) ---
        panelCentral.add(crearTarjetaDinamica("Desayuno", "7:00 am - 11:00 am", 294, tieneDesayuno));

        // --- TARJETA DERECHA (ALMUERZO) ---
        panelCentral.add(crearTarjetaDinamica("Almuerzo", "12:00 pm - 3:00 pm", 715, tieneAlmuerzo));

        container.add(panelCentral, BorderLayout.CENTER);
        container.add(crearFooter(), BorderLayout.SOUTH);
        add(container);
    }

    /**
     * Crea una tarjeta que decide automáticamente si mostrar datos o el botón de agregar.
     */
    private JPanel crearTarjetaDinamica(String tipo, String horario, int x, boolean tieneDatos) {
        // Panel contenedor con bordes redondeados
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

        if (tieneDatos) {
            // Usamos el componente de datos que ya tienes en tu paquete
            ContenidoMenuDatos datos = new ContenidoMenuDatos(tipo, horario, fecha, this);
            datos.setBounds(0, 0, 400, 450);
            contenedor.add(datos);
        } else {
            // Usamos el componente vacío (+) que ya tienes en tu paquete
            ContenidoMenuVacio vacio = new ContenidoMenuVacio(tipo, fecha, this);
            vacio.setBounds(0, 0, 400, 450);
            contenedor.add(vacio);
        }

        return contenedor;
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 40));
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

    public static void main(String[] args) {
        // Ejemplo de uso: Desayuno activo, Almuerzo vacío
        SwingUtilities.invokeLater(() -> new FechaMenusNewUCV("24/02/2026", true, false).setVisible(true));
    }
}