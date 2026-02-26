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
    private final Color AMARILLO_BOTON = new Color(255, 210, 35);

    public VerificacionFacialUCV() {
        setTitle("Verificación Facial · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- COMPONENTES DE ESTRUCTURA ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this), BorderLayout.WEST);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        // 1. Título
        JLabel lblTitulo = new JLabel("Verificación facial");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelCentral.add(lblTitulo, gbc);

        // 2. Caja Gris de Carga (Con el botón + interactivo)
        JPanel panelCuadro = crearPanelCargaVisual();
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 30, 0);
        panelCentral.add(panelCuadro, gbc);

        // 3. Botones Volver y Subir
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);

        JButton btnVolver = crearBotonEstilo("Volver", GRIS_BOTON);
        btnVolver.addActionListener(e -> {
            dispose();
            new PrincipalUsuario("Usuario").setVisible(true);
        });

        JButton btnSubir = crearBotonEstilo("Subir", AMARILLO_BOTON);
        btnSubir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Iniciando proceso de verificación...");
        });

        panelBotones.add(btnVolver);
        panelBotones.add(btnSubir);

        gbc.gridy = 2;
        panelCentral.add(panelBotones, gbc);

        // 4. Texto informativo pie
        JLabel lblFooterText = new JLabel("Solo se aceptan archivos en formato .jpg");
        lblFooterText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFooterText.setForeground(new Color(180, 190, 210));
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 0, 0, 0);
        panelCentral.add(lblFooterText, gbc);

        container.add(panelCentral, BorderLayout.CENTER);
        container.add(crearCerrarSesionFooter(), BorderLayout.SOUTH);

        add(container);
    }

    private JPanel crearPanelCargaVisual() {
        JPanel p = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo tarjeta gris claro
                g2.setColor(GRIS_TARJETA);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

                // Círculo interno gris oscuro (Agrandado para el +)
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

        // --- BOTÓN "+" CON EFECTO HOVER ---
        JLabel btnPlus = new JLabel("+", SwingConstants.CENTER);
        btnPlus.setFont(new Font("Arial", Font.PLAIN, 85));
        btnPlus.setForeground(new Color(60, 60, 60));
        btnPlus.setBounds(0, 90, 320, 180);
        btnPlus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPlus.addMouseListener(new java.awt.event.MouseAdapter() {
           @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 1. Instanciar el controlador de reconocimiento facial
                FacialController facialController = new FacialController();
                
                // 2. Llamar al método LoadImage y capturar el objeto Response
                // Importante: Usamos la ruta completa del paquete para evitar conflictos con otras clases "Response"
                com.ucv.controller.UserController.Response respuesta = facialController.LoadImage();

                // 3. Evaluar el resultado de la verificación
                if (respuesta.isSuccess()) {
                    // --- CASO ÉXITO ---
                    // Cerramos la ventana de carga actual
                    dispose(); 
                    
                    // Abrimos la ventana de éxito (el mensaje azul de "Ingreso permitido")
                    new VerificacionFacialExitosa().setVisible(true);
                    
                } else {
                    // --- CASO ERROR O CANCELADO ---
                    // Si el usuario cerró el selector, no es JPG o el rostro no coincide en la DB
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
                // Se ilumina y crece ligeramente
                btnPlus.setForeground(AZUL_FONDO);
                btnPlus.setFont(new Font("Arial", Font.BOLD, 95));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Vuelve al estado normal
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

                // Dibujar fondo redondeado
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Dibujar texto centrado
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

    private JPanel crearCerrarSesionFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        f.setOpaque(false);
        f.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 40));
        JLabel lbl = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();
            }
        });
        f.add(lbl);
        return f;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VerificacionFacialUCV().setVisible(true));
    }
}