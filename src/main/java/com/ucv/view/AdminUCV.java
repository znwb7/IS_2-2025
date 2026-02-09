package com.ucv.view;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AdminUCV extends JFrame {

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);
    private final Color VERDE_BOTON = new Color(45, 100, 60);
    private final Color GRIS_LATERAL = new Color(225, 225, 225);

    public AdminUCV(String nombreAdmin) {
        setTitle("Comedor UCV - Panel de Administración");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        // 1. ENCABEZADO
        add(crearEncabezadoDerecho(), BorderLayout.NORTH);

        // 2. CONTENEDOR INFERIOR
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);
        
        // BARRA LATERAL (2 iconos: Casa y Billetera)
        contenedorInferior.add(crearBarraLateral(), BorderLayout.WEST);

        // Panel central
        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(40, 50, 20, 50));

        JLabel lblBienvenida = new JLabel("Bienvenido, " + nombreAdmin);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCuerpo.add(lblBienvenida);
        
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 50)));

        // AGREGAR BOTONES
        panelCuerpo.add(crearBotonAdmin("Modificar Menus"));
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCuerpo.add(crearBotonAdmin("Modificar Turnos"));
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCuerpo.add(crearBotonAdmin("Inventario"));
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCuerpo.add(crearBotonAdmin("Generar reporte"));
        panelCuerpo.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // El botón de Calcular CCB ahora tendrá la acción vinculada
        panelCuerpo.add(crearBotonAdmin("Calcular CCB"));

        contenedorInferior.add(panelCuerpo, BorderLayout.CENTER);
        add(contenedorInferior, BorderLayout.CENTER);
    }

    private JPanel crearEncabezadoDerecho() {
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

        JLabel lblFecha = new JLabel("08/02/2026");
        lblFecha.setForeground(new Color(210, 210, 210));
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 22));
        lblFecha.setBounds(45, 85, 200, 30);

        JLabel lblLogo = new JLabel();
        try {
            java.net.URL res = getClass().getResource("/com/ucv/view/logoucv.png");
            if (res != null) {
                Image img = new ImageIcon(res).getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } else {
                lblLogo.setText("");
            }
        } catch (Exception e) {
            lblLogo.setText("");
        }

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

        PanelRedondeado capsula = new PanelRedondeado(30, GRIS_LATERAL);
        capsula.setBounds(15, 250, 60, 180); // Ajustado a 180 de alto y centrado en Y
        capsula.setLayout(new GridLayout(2, 1, 0, 20));

        JLabel casa = new JLabel("🏠", SwingConstants.CENTER);
        casa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 32));
        casa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        casa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new PrincipalUCV("Admin").setVisible(true);
            }
        });
        
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

    private JButton crearBotonAdmin(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(VERDE_BOTON);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 28));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(750, 85)); 
        btn.setPreferredSize(new Dimension(750, 85));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- LÓGICA DE NAVEGACIÓN ---
        btn.addActionListener(e -> {
            if (texto.equals("Calcular CCB")) {
                dispose(); // Cierra el panel de administración
                new CalculoCCB().setVisible(true); // Abre la interfaz del CCB
            } else {
                JOptionPane.showMessageDialog(this, "Función '" + texto + "' en desarrollo.");
            }
        });

        return btn;
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
        SwingUtilities.invokeLater(() -> new AdminUCV("Administrador").setVisible(true));
    }
}