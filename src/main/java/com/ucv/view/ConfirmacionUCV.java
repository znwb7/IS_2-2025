package com.ucv.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConfirmacionUCV extends JFrame {

    LocalDate fechaActual = LocalDate.now();  // Obtiene fecha del sistema
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaTexto = fechaActual.format(formatter);

    // Colores institucionales
    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);
    private final Color GRIS_CLARO = new Color(225, 225, 225);

    public ConfirmacionUCV(String turnoSeleccionado) {
        setTitle("Comedor UCV - Reserva Concretada");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        // ENCABEZADO EXPANDIDO
        add(crearEncabezadoExpandido(), BorderLayout.NORTH);

        // CONTENEDOR DE CONTENIDO (Barra lateral + Cuerpo)
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);

        // Barra lateral unificada con 2 iconos (Casa y Billetera)
        contenedorInferior.add(crearBarraLateral(), BorderLayout.WEST);

        // Cuerpo Central (Mensaje de éxito)
        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new GridBagLayout()); 
        panelCuerpo.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(10, 3, 10, 10);
        gbc.fill = GridBagConstraints.CENTER;

        // Mensaje Principal con formato HTML
        String mensaje = "<html><center>"
                + "<font size='7' color='white'><b>Ha concretado su reserva para el " + turnoSeleccionado + ".</b></font><br><br>"
                + "<font size='5' color='white'>Diríjase al comedor en el horario<br>seleccionado para recibir su comida.</font><br><br><br><br>"
                + "<font size='7' color='white'><i>¡BUEN PROVECHO!</i></font>"
                + "</center></html>";
        
        JLabel lblMensaje = new JLabel(mensaje);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panelCuerpo.add(lblMensaje, gbc);

        // Botón "Ver ticket"
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(30, 0, 0, 0);
        JButton btnTicket = new JButton("Ver ticket");
        btnTicket.setFont(new Font("Arial", Font.BOLD, 18));
        btnTicket.setBackground(Color.WHITE);
        btnTicket.setForeground(Color.BLACK);
        btnTicket.setPreferredSize(new Dimension(180, 50));
        btnTicket.setFocusPainted(false);
        btnTicket.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        btnTicket.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnTicket.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Generando ticket digital con código QR...");
        });
        
        panelCuerpo.add(btnTicket, gbc);

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

        // Cápsula ajustada a 180px para 2 iconos bien centrados
        PanelRedondeado capsula = new PanelRedondeado(30, GRIS_CLARO);
        capsula.setBounds(15, 250, 60, 180); 
        capsula.setLayout(new GridLayout(2, 1, 0, 20));

        // 1. CASA
        JLabel casa = new JLabel("🏠", SwingConstants.CENTER);
        casa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 32));
        casa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        casa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                // Regresa a la ventana principal
                new PrincipalUCV("Usuario").setVisible(true);
            }
        });
        
        // 2. BILLETERA
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
        billetera.setCursor(new Cursor(Cursor.HAND_CURSOR));
        billetera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose(); 
                new BilleteraUCV().setVisible(true);
            }
        });

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
        SwingUtilities.invokeLater(() -> new ConfirmacionUCV("Turno de la Tarde").setVisible(true));
    }
}