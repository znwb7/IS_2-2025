package com.ucv.view.user;

import com.ucv.controller.PagoController;
import com.ucv.model.PagoModel;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.PrimaryButton2;
import com.ucv.view.components.BotonCerrarSesion;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class BilleteraUCV extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);

    private final PagoController controlador;
    private final String usuarioID;
    private JLabel lblMonto;

    public BilleteraUCV(String usuarioID) {
        PagoModel modelo = new PagoModel();
        controlador = new PagoController(modelo, usuarioID);
        this.usuarioID = usuarioID;

        setTitle("Mi Saldo · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor principal con fondo azul
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- 3. PANEL CENTRAL (Contenido de Saldo) ---
        JPanel panelCentral = new JPanel(null);
        panelCentral.setOpaque(false);

        JLabel lblTitulo = new JLabel("Saldo Disponible:");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(360, 160, 300, 40);
        panelCentral.add(lblTitulo);

        PanelRedondeado tarjetaSaldo = new PanelRedondeado(30, GRIS_TARJETA);
        tarjetaSaldo.setBounds(350, 200, 700, 220);
        tarjetaSaldo.setLayout(null);

        lblMonto = new JLabel("Bs 0.00");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblMonto.setForeground(Color.BLACK);
        lblMonto.setBounds(50, 70, 400, 60);
        tarjetaSaldo.add(lblMonto);

        PrimaryButton2 btnAgregar = new PrimaryButton2("Agregar saldo", AMARILLO_BOTON);
        btnAgregar.setBounds(500, 140, 160, 45);
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAgregar.addActionListener(e -> {
            PagoMovilUCV pagoFrame = new PagoMovilUCV(usuarioID, this);
            pagoFrame.setVisible(true);
            this.setVisible(false);
        });
        tarjetaSaldo.add(btnAgregar);

        panelCentral.add(tarjetaSaldo);
        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Boton Cerrar Sesión) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);

        actualizarSaldo();
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        // Componente reutilizable que ya tiene la lógica de confirmación y pastilla blanca
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Alineación estándar de 25px desde arriba
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }

    public void actualizarSaldo() {
        double saldo = controlador.obtenerSaldo();
        lblMonto.setText("Bs " + String.format("%.2f", saldo));
    }

    public void recargaExitosa() {
        actualizarSaldo();
        this.setVisible(true);
    }

    // Clase interna para la tarjeta de saldo
    static class PanelRedondeado extends JPanel {
        private int radio;
        private Color color;
        public PanelRedondeado(int radio, Color color) {
            this.radio = radio;
            this.color = color;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radio, radio));
            g2.dispose();
        }
    }
}