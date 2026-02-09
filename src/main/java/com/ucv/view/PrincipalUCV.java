package com.ucv.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PrincipalUCV extends JFrame {

    LocalDate fechaActual = LocalDate.now(); 
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaTexto = fechaActual.format(formatter);

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);
    private final Color AZUL_TARJETA = new Color(32, 86, 172);
    private final Color AMARILLO_BOTON = new Color(250, 210, 50);
    private final Color GRIS_CLARO = new Color(225, 225, 225);

    public PrincipalUCV(String nombreUsuario) {
        setTitle("Comedor UCV - Menús Disponibles");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        add(crearEncabezadoExpandido(), BorderLayout.NORTH);

        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);
        contenedorInferior.add(crearBarraLateral(), BorderLayout.WEST);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);

        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel lblBienvenida = new JLabel("Bienvenido, " + nombreUsuario);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 22));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCuerpo.add(lblBienvenida);

        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lblMenus = new JLabel("Menús disponibles");
        lblMenus.setForeground(Color.WHITE);
        lblMenus.setFont(new Font("Arial", Font.BOLD, 60));
        lblMenus.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCuerpo.add(lblMenus);

        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 25)));

        panelCuerpo.add(crearTarjetaMenu("Menú 1", 1));
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCuerpo.add(crearTarjetaMenu("Menú 2", 2));
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCuerpo.add(crearTarjetaMenu("Menú 3", 3));

        JScrollPane scroll = new JScrollPane(panelCuerpo);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panelDerecho.add(scroll, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFooter.setOpaque(false);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));

        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 20));
        lblCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new LoginUCV().setVisible(true);
            }
        });
        panelFooter.add(lblCerrar);

        panelDerecho.add(panelFooter, BorderLayout.SOUTH);
        contenedorInferior.add(panelDerecho, BorderLayout.CENTER);
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
        capsula.setBounds(15, 300, 60, 180);
        capsula.setLayout(new GridLayout(2, 1, 0, 20));

        JLabel casa = new JLabel("🏠", SwingConstants.CENTER);
        casa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 32));
        casa.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        JLabel billetera = new JLabel("", SwingConstants.CENTER);
        try {
            java.net.URL resB = getClass().getResource("/com/ucv/view/billetera.png");
            if (resB != null) {
                Image img = new ImageIcon(resB).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                billetera.setIcon(new ImageIcon(img));
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

    private JPanel crearTarjetaMenu(String titulo, int n) {
        PanelRedondeado tarjeta = new PanelRedondeado(30, AZUL_TARJETA);
        tarjeta.setMaximumSize(new Dimension(850, 260));
        tarjeta.setLayout(new BorderLayout(30, 0));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        PanelRedondeado boxImg = new PanelRedondeado(20, GRIS_CLARO);
        boxImg.setPreferredSize(new Dimension(180, 180));
        boxImg.setLayout(new GridBagLayout());
        JLabel tImg = new JLabel("<html><center>Img.<br>Referencia</center></html>");
        tImg.setFont(new Font("Arial", Font.BOLD, 18));
        boxImg.add(tImg);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel lblT = new JLabel(titulo);
        lblT.setForeground(Color.WHITE);
        lblT.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel lblP = new JLabel("<html>Platos:<br>• Entrada "+n+"<br>• Plato fuerte "+n+"<br>• Postre "+n+"</html>");
        lblP.setForeground(Color.WHITE);
        lblP.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton btn = new JButton("Seleccionar");
        btn.setBackground(AMARILLO_BOTON);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // CORRECCIÓN: Ahora abre la selección de turnos
        btn.addActionListener(e -> {
            dispose();
            new TurnosUCV(titulo).setVisible(true);
        });

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pBtn.setOpaque(false);
        pBtn.add(btn);

        centro.add(lblT);
        centro.add(Box.createRigidArea(new Dimension(0, 10)));
        centro.add(lblP);
        centro.add(Box.createRigidArea(new Dimension(0, 15)));
        centro.add(pBtn);

        tarjeta.add(boxImg, BorderLayout.WEST);
        tarjeta.add(centro, BorderLayout.CENTER);
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
        SwingUtilities.invokeLater(() -> new PrincipalUCV("Usuario Ejemplo").setVisible(true));
    }
}