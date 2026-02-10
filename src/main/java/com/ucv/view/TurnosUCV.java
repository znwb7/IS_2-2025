package com.ucv.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TurnosUCV extends JFrame {

    LocalDate fechaActual = LocalDate.now();  // Obtiene fecha del sistema
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaTexto = fechaActual.format(formatter);

    // Colores institucionales
    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);
    private final Color AZUL_TARJETA = new Color(32, 86, 172);
    private final Color AMARILLO_BOTON = new Color(250, 210, 50);
    private final Color GRIS_CLARO = new Color(225, 225, 225);

    public TurnosUCV(String nombreMenu) {
        setTitle("Comedor UCV - Selección de Turnos");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        
        setLayout(new BorderLayout());

        // ENCABEZADO
        add(crearEncabezadoExpandido(), BorderLayout.NORTH);

        // CONTENEDOR INFERIOR
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);

        // Barra lateral 
        contenedorInferior.add(crearBarraLateral(), BorderLayout.WEST);

        // Panel central de contenido
        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));

        // Frase de selección
        JLabel lblSeleccion = new JLabel("Usted ha seleccionado: " + nombreMenu);
        lblSeleccion.setForeground(Color.WHITE);
        lblSeleccion.setFont(new Font("Arial", Font.PLAIN, 28));
        lblSeleccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCuerpo.add(lblSeleccion);
        
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 30)));

        // Título "Turnos"
        JPanel contenedorTurnosIndependiente = new JPanel(null); 
        contenedorTurnosIndependiente.setOpaque(false);
        contenedorTurnosIndependiente.setPreferredSize(new Dimension(850, 60));
        contenedorTurnosIndependiente.setMaximumSize(new Dimension(850, 60));

        JLabel lblTurnosTitulo = new JLabel("Turnos disponibles");
        lblTurnosTitulo.setForeground(Color.WHITE);
        lblTurnosTitulo.setFont(new Font("Arial", Font.BOLD, 29));
        lblTurnosTitulo.setBounds(20, 20, 300, 35); 

        contenedorTurnosIndependiente.add(lblTurnosTitulo);
        panelCuerpo.add(contenedorTurnosIndependiente);
        
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tarjetas de Turnos
        panelCuerpo.add(crearTarjetaTurno("Turno de la mañana", "7:00 am - 11:00 am", "850", "45"));
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 25)));
        panelCuerpo.add(crearTarjetaTurno("Turno de la tarde", "12:00 pm - 5:00 pm", "920", "12"));

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
                new PrincipalUCV("Usuario").setVisible(true);
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

    private JPanel crearTarjetaTurno(String titulo, String horario, String dispEst, String dispEmp) {
        PanelRedondeado tarjeta = new PanelRedondeado(25, AZUL_TARJETA);
        tarjeta.setMaximumSize(new Dimension(850, 230)); 
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel panelTexto = new JPanel();
        panelTexto.setOpaque(false);
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));

        JLabel lblT = new JLabel(titulo);
        lblT.setForeground(Color.WHITE);
        lblT.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel lblInfo = new JLabel("<html>" + horario + "<br><br>" +
                                    "Capacidad:<br>1000 estudiantes / 100 empleados<br><br>" +
                                    "<font color='#F0F0F0'>Disponibles:<br><b>" + dispEst + "</b> estudiantes / <b>" + 
                                    dispEmp + "</b> empleados</font></html>");
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 15));

        panelTexto.add(lblT);
        panelTexto.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTexto.add(lblInfo);

        JPanel panelBotonContenedor = new JPanel(null);
        panelBotonContenedor.setOpaque(false);
        panelBotonContenedor.setPreferredSize(new Dimension(170, 180));

        JButton btnReservar = new JButton("Reservar");
        btnReservar.setBackground(AMARILLO_BOTON);
        btnReservar.setFont(new Font("Arial", Font.BOLD, 16));
        btnReservar.setFocusPainted(false);
        btnReservar.setBorder(BorderFactory.createEmptyBorder());
        btnReservar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReservar.setBounds(0, 135, 150, 40); 
        
        btnReservar.addActionListener(e -> {
            dispose(); 
            new ConfirmacionUCV(titulo).setVisible(true);
        });
        
        panelBotonContenedor.add(btnReservar);

        tarjeta.add(panelTexto, BorderLayout.CENTER);
        tarjeta.add(panelBotonContenedor, BorderLayout.EAST);

        return tarjeta;
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
        SwingUtilities.invokeLater(() -> new TurnosUCV("Pabellón Criollo").setVisible(true));
    }
}