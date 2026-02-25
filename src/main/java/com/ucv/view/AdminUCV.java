package com.ucv.view;

import com.ucv.view.components.SIdeBar2;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminUCV extends JFrame {

    LocalDate fechaActual = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaTexto = fechaActual.format(formatter);

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);
    private final Color VERDE_BOTON_NUEVO = new Color(24, 116, 205);

    public AdminUCV(String nombreAdmin) {
        setTitle("Comedor UCV - Panel de Administración");
        setSize(1920, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        // --- 1. ENCABEZADO ---
        add(crearEncabezadoDerecho(), BorderLayout.NORTH);

        // --- 2. CONTENEDOR INFERIOR (SIDEBAR + CUERPO) ---
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);

        // Usamos SIdeBar2 (la versión de un solo icono que regresa aquí)
        contenedorInferior.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- 3. PANEL DE CUERPO (CENTRADO TOTAL) ---
        // Usamos GridBagLayout para centrado vertical y horizontal automático
        JPanel panelCuerpo = new JPanel(new GridBagLayout());
        panelCuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre botones

        // Bienvenida
        JLabel lblBienvenida = new JLabel("Bienvenido, " + nombreAdmin);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 32));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0); // Más espacio después del título
        panelCuerpo.add(lblBienvenida, gbc);

        // Botones (Configurar Turnos eliminado)
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 1;
        panelCuerpo.add(crearBotonAdmin("Configurar Menus"), gbc);

        gbc.gridy = 2;
        panelCuerpo.add(crearBotonAdmin("Inventario"), gbc);

        gbc.gridy = 3;
        panelCuerpo.add(crearBotonAdmin("Generar reporte"), gbc);

        contenedorInferior.add(panelCuerpo, BorderLayout.CENTER);
        add(contenedorInferior, BorderLayout.CENTER);

        // --- 4. FOOTER ---
        add(crearFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezadoDerecho() {
        JPanel panelEncabezado = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_ENCABEZADO);
                int alto = 140;
                int arc = 60;
                g2.fillRoundRect(-30, 0, getWidth() + 60, alto, arc, arc);
                g2.fillRect(-30, 0, getWidth() + 60, alto / 2);
                g2.dispose();
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

        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(lblFecha);
        return panelEncabezado;
    }

    private JPanel crearFooter() {
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFooter.setOpaque(false);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));

        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 20));
        lblCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                com.ucv.ComedorApp.main(null);
            }
        });
        panelFooter.add(lblCerrar);
        return panelFooter;
    }

    private JButton crearBotonAdmin(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        btn.setBackground(VERDE_BOTON_NUEVO);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 28));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(600, 80)); // Tamaño fijo para uniformidad
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (texto.equals("Configurar Menus")) {
                dispose();
                new com.ucv.view.GestionMenuUCV().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Función '" + texto + "' en desarrollo.");
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUCV("Administrador").setVisible(true));
    }
}