package com.ucv.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.ucv.controller.RedirectController;

public class BilleteraUCV extends JFrame {

    LocalDate fechaActual = LocalDate.now();  // Obtiene fecha del sistema
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaTexto = fechaActual.format(formatter);

    // Colores institucionales
    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);
    private final Color AZUL_TARJETA_SALDO = new Color(32, 86, 172);
    private final Color GRIS_CLARO = new Color(225, 225, 225);
    private final Color AMARILLO_BOTON = new Color(250, 210, 50);

    // Variable para mantener la sesión sin alterar la lógica original
    private String idSesion = "Usuario";

    // --- CONSTRUCTOR ORIGINAL (Para evitar errores en otras vistas) ---
    public BilleteraUCV() {
        this("Usuario");
    }

    // --- CONSTRUCTOR PARA NAVEGACIÓN ---
    public BilleteraUCV(String idUsuario) {
        this.idSesion = idUsuario;
        setTitle("Comedor UCV - Billetera Digital");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        // --- 1. ENCABEZADO ---
        add(crearEncabezadoExpandido(), BorderLayout.NORTH);

        // --- 2. CONTENEDOR PRINCIPAL ---
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);

        // Barra lateral (Actualizada a 2 iconos centrados)
        contenedorInferior.add(crearBarraLateral(), BorderLayout.WEST);

        // Panel de Contenido Central
        JPanel panelCuerpo = new JPanel(null);
        panelCuerpo.setOpaque(false);

        // Etiqueta "Saldo Disponible"
        JLabel lblSaldoTitulo = new JLabel("Saldo Disponible");
        lblSaldoTitulo.setForeground(Color.WHITE);
        lblSaldoTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblSaldoTitulo.setBounds(250, 60, 400, 45);
        panelCuerpo.add(lblSaldoTitulo);

        // TARJETA DE SALDO (MODIFICADA)
        PanelRedondeado tarjetaSaldo = new PanelRedondeado(40, AZUL_TARJETA_SALDO);
        tarjetaSaldo.setBounds(250, 110, 750, 250);
        tarjetaSaldo.setLayout(null);

        // Texto "Bs"
        JLabel lblBs = new JLabel("Bs");
        lblBs.setForeground(Color.WHITE);
        lblBs.setFont(new Font("Arial", Font.BOLD, 48));
        lblBs.setBounds(40, 80, 150, 70);
        tarjetaSaldo.add(lblBs);

        //MONTO DEL SALDO (0,00)
        JLabel lblMontoSaldo = new JLabel("0,00");
        lblMontoSaldo.setForeground(Color.WHITE);
        lblMontoSaldo.setFont(new Font("Arial", Font.BOLD, 72));
        lblMontoSaldo.setBounds(160, 60, 300, 100);
        tarjetaSaldo.add(lblMontoSaldo);

        // Botón Agregar Saldo
        JButton btnAgregar = new JButton("Agregar Saldo");
        btnAgregar.setBackground(AMARILLO_BOTON);
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 16));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder());
        btnAgregar.setBounds(580, 180, 140, 40);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tarjetaSaldo.add(btnAgregar);

        panelCuerpo.add(tarjetaSaldo);

        contenedorInferior.add(panelCuerpo, BorderLayout.CENTER);
        add(contenedorInferior, BorderLayout.CENTER);
    }

    private JPanel crearEncabezadoExpandido() {
        JPanel panelEncabezado = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_ENCABEZADO);
                int alto = 140;
                int arc = 60;
                g2.fillRoundRect(-30, 0, getWidth() + 60, alto, arc, arc);
                g2.fillRect(-30, 0, getWidth() + 60, alto / 2);
                g2.fillRect(-30, 0, 100, alto);
            }
        };
        panelEncabezado.setPreferredSize(new Dimension(0, 150));
        panelEncabezado.setOpaque(false);

        JLabel lblTitulo = new JLabel("Comedor UCV");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 70));
        lblTitulo.setBounds(40, 15, 600, 80);

        JLabel lblFecha = new JLabel(fechaTexto);
        lblFecha.setForeground(new Color(210, 210, 210));
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 22));
        lblFecha.setBounds(45, 85, 200, 30);

        JLabel lblLogo = new JLabel();
        try {
            java.net.URL res = getClass().getResource("/com/ucv/view/logoucv.png");
            if (res != null) {
                Image img = new ImageIcon(res).getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {}

        panelEncabezado.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                lblLogo.setBounds(panelEncabezado.getWidth() - 140, 15, 110, 110);
            }
        });

        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(lblFecha);
        panelEncabezado.add(lblLogo);
        return panelEncabezado;
    }

    private JPanel crearBarraLateral() {
        JPanel lateral = new JPanel(null);
        lateral.setPreferredSize(new Dimension(90, 0));
        lateral.setOpaque(false);

        PanelRedondeado capsula = new PanelRedondeado(30, GRIS_CLARO);
        capsula.setBounds(15, 250, 60, 180);
        capsula.setLayout(new GridLayout(2, 1, 0, 20));

        // HOME 
        JLabel casa = new JLabel("🏠", SwingConstants.CENTER);
        casa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 32));
        casa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        casa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                // Redirección dinámica basada en la sesión
                new RedirectController().ejecutarRedireccion(idSesion);
            }
        });

        // BILLETERA
        JLabel billetera = new JLabel("", SwingConstants.CENTER);
        try {
            java.net.URL resB = getClass().getResource("/com/ucv/view/billetera.png");
            if (resB != null) {
                Image imgB = new ImageIcon(resB).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                billetera.setIcon(new ImageIcon(imgB));
            } else {
                billetera.setText("💳");
            }
        } catch (Exception e) {
            billetera.setText("💳");
        }
        billetera.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        capsula.add(casa);
        capsula.add(billetera);

        lateral.add(capsula);
        return lateral;
    }

    class PanelRedondeado extends JPanel {
        private int r;
        private Color c;
        public PanelRedondeado(int radio, Color color) {
            this.r = radio;
            this.c = color;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), r, r));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BilleteraUCV().setVisible(true));
    }
}