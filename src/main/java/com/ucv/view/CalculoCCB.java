package com.ucv.view;
import javax.swing.*;
import com.ucv.controller.ccbController;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CalculoCCB extends JFrame {

    // Colores institucionales
    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);
    private final Color AZUL_TARJETA = new Color(32, 86, 172);
    private final Color AMARILLO_BOTON = new Color(250, 210, 50);
    private final Color GRIS_CLARO = new Color(225, 225, 225);

    public CalculoCCB() {
        setTitle("Comedor UCV - Calcular CCB");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        // 1. ENCABEZADO
        add(crearEncabezadoExpandido(), BorderLayout.NORTH);

        // 2. CONTENEDOR INFERIOR
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);

        // Barra lateral con navegación (2 iconos: Casa y Billetera)
        contenedorInferior.add(crearBarraLateral(), BorderLayout.WEST);

        // Panel central de contenido (Formulario)
        contenedorInferior.add(crearPanelFormulario(), BorderLayout.CENTER);

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

        // Cápsula ajustada a 180px de alto para 2 iconos centrados
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

    private JPanel crearPanelFormulario() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setOpaque(false);

        // Título del formulario
        JLabel lblTituloForm = new JLabel("Calcular CCB...");
        lblTituloForm.setForeground(Color.WHITE);
        lblTituloForm.setFont(new Font("Arial", Font.PLAIN, 32));
        
        // Tarjeta Azul del Formulario
        PanelRedondeado tarjetaForm = new PanelRedondeado(30, AZUL_TARJETA);
        tarjetaForm.setPreferredSize(new Dimension(600, 450));
        tarjetaForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Estilo de etiquetas
        Font fontLabels = new Font("Arial", Font.PLAIN, 20);
        
        // Campos de inputs (se empiezan en la primera fila)

        // Filas de inputs (CF, CV, NB, Merma)
        String[] labels = {"CF:", "CV:", "NB:", "Merma:"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(fontLabels);
            tarjetaForm.add(lbl, gbc);

            gbc.gridx = 1;
            fields[i] = new JTextField();
            fields[i].setPreferredSize(new Dimension(200, 40));
            fields[i].setBackground(GRIS_CLARO);
            fields[i].setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            tarjetaForm.add(fields[i], gbc);
        }

        // Botón Asignar
        JButton btnAsignar = new JButton("Asignar a Variables");
        btnAsignar.setBackground(AMARILLO_BOTON);
        btnAsignar.setFont(new Font("Arial", Font.BOLD, 22));
        btnAsignar.setPreferredSize(new Dimension(300, 55));
        btnAsignar.setFocusPainted(false);
        btnAsignar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAsignar.addActionListener(e -> {
            try {
                float cf = Float.parseFloat(fields[0].getText().trim());
                float cv = Float.parseFloat(fields[1].getText().trim());
                float nb = Float.parseFloat(fields[2].getText().trim());
                float merma = Float.parseFloat(fields[3].getText().trim());
                float resultado = ccbController.calcularCCB(cf, cv, nb, merma);
                JOptionPane.showMessageDialog(this, String.format("Resultado CCB: %.2f", resultado));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Organizar todo en el panel principal
        JPanel layoutFinal = new JPanel();
        layoutFinal.setLayout(new BoxLayout(layoutFinal, BoxLayout.Y_AXIS));
        layoutFinal.setOpaque(false);

        lblTituloForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjetaForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAsignar.setAlignmentX(Component.CENTER_ALIGNMENT);

        layoutFinal.add(lblTituloForm);
        layoutFinal.add(Box.createRigidArea(new Dimension(0, 20)));
        layoutFinal.add(tarjetaForm);
        layoutFinal.add(Box.createRigidArea(new Dimension(0, 30)));
        layoutFinal.add(btnAsignar);

        panelContenedor.add(layoutFinal);
        return panelContenedor;
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
        SwingUtilities.invokeLater(() -> new CalculoCCB().setVisible(true));
    }
}