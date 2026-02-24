package com.ucv.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.PrimaryButton;

public class PrincipalUCV extends JFrame {

    private static final Color COLOR_FONDO = new Color(18, 71, 150);
    private static final Color COLOR_TARJETA = new Color(32, 86, 172);
    private static final Color COLOR_GRIS = new Color(225, 225, 225);

    public PrincipalUCV(String nombreUsuario) {
        configurarVentana();
        inicializarComponentes(nombreUsuario);
    }

    private void configurarVentana() {
        setTitle("Comedor UCV - Menús Disponibles");
        setSize(1100, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void inicializarComponentes(String nombreUsuario) {

        add(new HeaderUCV(), BorderLayout.NORTH);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);

        contenedor.add(new SideBar(this), BorderLayout.WEST);
        contenedor.add(crearPanelDerecho(nombreUsuario), BorderLayout.CENTER);

        add(contenedor, BorderLayout.CENTER);
    }

    private JPanel crearPanelDerecho(String nombreUsuario) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(crearContenidoCentral(nombreUsuario), BorderLayout.CENTER);
        panel.add(crearFooter(), BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane crearContenidoCentral(String nombreUsuario) {

        JPanel cuerpo = new JPanel();
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel titulo = new JLabel("Menús disponibles");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 60));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cuerpo.add(Box.createRigidArea(new Dimension(0, 10)));
        cuerpo.add(titulo);
        cuerpo.add(Box.createRigidArea(new Dimension(0, 25)));

        for (int i = 1; i <= 3; i++) {
            cuerpo.add(crearTarjetaMenu("Menú " + i, i));
            cuerpo.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        JScrollPane scroll = new JScrollPane(cuerpo);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    private JPanel crearFooter() {

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));

        JLabel cerrarSesion = new JLabel("<html><u>Cerrar Sesión</u></html>");
        cerrarSesion.setForeground(Color.WHITE);
        cerrarSesion.setFont(new Font("Arial", Font.PLAIN, 20));
        cerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                LoginUCV login = new LoginUCV();
                login.setController(new com.ucv.controller.UserController());
                login.setVisible(true);
            }
        });

        footer.add(cerrarSesion);
        return footer;
    }

    private JPanel crearTarjetaMenu(String titulo, int numero) {

        PanelRedondeado tarjeta = new PanelRedondeado(30, COLOR_TARJETA);
        tarjeta.setMaximumSize(new Dimension(850, 260));
        tarjeta.setLayout(new BorderLayout(30, 0));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        tarjeta.add(crearImagenPlaceholder(), BorderLayout.WEST);
        tarjeta.add(crearContenidoTarjeta(titulo, numero), BorderLayout.CENTER);

        return tarjeta;
    }

    private JPanel crearImagenPlaceholder() {

        PanelRedondeado imagen = new PanelRedondeado(20, COLOR_GRIS);
        imagen.setPreferredSize(new Dimension(180, 180));
        imagen.setLayout(new GridBagLayout());

        JLabel texto = new JLabel("<html><center>Img.<br>Referencia</center></html>");
        texto.setFont(new Font("Arial", Font.BOLD, 18));
        imagen.add(texto);

        return imagen;
    }

    private JPanel crearContenidoTarjeta(String titulo, int numero) {

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel lblDescripcion = new JLabel(
                "<html>Platos:<br>• Entrada " + numero +
                "<br>• Plato fuerte " + numero +
                "<br>• Postre " + numero + "</html>"
        );
        lblDescripcion.setForeground(Color.WHITE);
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 18));

        PrimaryButton boton = new PrimaryButton("Seleccionar");

        boton.addActionListener(e -> {
            dispose();
            new TurnosUCV(titulo).setVisible(true);
        });

        JPanel contenedorBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorBoton.setOpaque(false);
        contenedorBoton.add(boton);

        contenido.add(lblTitulo);
        contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        contenido.add(lblDescripcion);
        contenido.add(Box.createRigidArea(new Dimension(0, 15)));
        contenido.add(contenedorBoton);

        return contenido;
    }

    private static class PanelRedondeado extends JPanel {

        private final int radio;
        private final Color color;

        public PanelRedondeado(int radio, Color color) {
            this.radio = radio;
            this.color = color;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth(), getHeight(), radio, radio));
        }
    }
}