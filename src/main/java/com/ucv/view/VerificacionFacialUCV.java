package com.ucv.view;

import com.ucv.controller.FacialController;
import com.ucv.model.DataBase;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class VerificacionFacialUCV extends JFrame {

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color GRIS_TARJETA = new Color(211, 211, 211);
    private final Color GRIS_CIRCULO = new Color(190, 190, 190);
    private final Color GRIS_BOTON = new Color(180, 180, 180);
    private final String usuarioID;

    public VerificacionFacialUCV(String usuarioID) {
        this.usuarioID = usuarioID;
        setTitle("Verificación Facial · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- COMPONENTES DE ESTRUCTURA ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. Título
        JLabel lblTitulo = new JLabel("Verificación facial");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelCentral.add(lblTitulo, gbc);

        // 2. Caja Gris de Carga
        JPanel panelCuadro = crearPanelCargaVisual();
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 20, 0);
        panelCentral.add(panelCuadro, gbc);

        // 3. Botón Volver
        JButton btnVolver = crearBotonEstilo("Volver", GRIS_BOTON);
        btnVolver.addActionListener(e -> {
            dispose();
            new PrincipalUsuario("Usuario").setVisible(true);
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        panelCentral.add(btnVolver, gbc);

        // 4. Texto informativo pie
        JLabel lblFooterText = new JLabel("Solo se aceptan archivos en formato .jpg");
        lblFooterText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFooterText.setForeground(new Color(180, 190, 210));
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        panelCentral.add(lblFooterText, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- IMPLEMENTACIÓN DEL FOOTER UNIFICADO ---
        container.add(crearCerrarSesionFooter(), BorderLayout.SOUTH);

        add(container);
    }

    private JPanel crearPanelCargaVisual() {
        JPanel p = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_TARJETA);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(GRIS_CIRCULO);
                int diametro = 180;
                int x = (getWidth() - diametro) / 2;
                int y = 90;
                g2.fill(new Ellipse2D.Double(x, y, diametro, diametro));
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(320, 310));
        p.setOpaque(false);

        JLabel lblCargar = new JLabel("Cargar imagen", SwingConstants.CENTER);
        lblCargar.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCargar.setBounds(0, 35, 320, 30);
        p.add(lblCargar);

        JLabel btnPlus = new JLabel("+", SwingConstants.CENTER);
        btnPlus.setFont(new Font("Arial", Font.PLAIN, 85));
        btnPlus.setForeground(new Color(60, 60, 60));
        btnPlus.setBounds(0, 90, 320, 180);
        btnPlus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPlus.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                FacialController facialController = new FacialController();
                com.ucv.controller.UserController.Response respuesta = facialController.LoadImage();

                if (respuesta.isSuccess()) {
                    dispose();
                    new VerificacionFacialExitosa(usuarioID).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                            VerificacionFacialUCV.this,
                            respuesta.getMessage(),
                            "Verificación de Identidad",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnPlus.setForeground(AZUL_FONDO);
                btnPlus.setFont(new Font("Arial", Font.BOLD, 95));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnPlus.setForeground(new Color(60, 60, 60));
                btnPlus.setFont(new Font("Arial", Font.PLAIN, 85));
            }
        });

        p.add(btnPlus);
        return p;
    }

    private JButton crearBotonEstilo(String texto, Color fondo) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(Color.BLACK);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setBackground(fondo);
        btn.setPreferredSize(new Dimension(150, 48));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // BLOQUE ACTUALIZADO
    private JPanel crearCerrarSesionFooter() {
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
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();
                com.ucv.ComedorApp.main(null); // Redirige al inicio de la app
            }
        });
        panelFooter.add(lblCerrar);
        return panelFooter;
    }
}