package com.ucv.view.admin;

import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.BotonCerrarSesion;
import com.ucv.view.components.HeaderUCV;
import javax.swing.*;
import java.awt.*;

public class AdminUCV extends JFrame {

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color VERDE_BOTON_NUEVO = new Color(24, 116, 205);

    public AdminUCV(String nombreAdmin) {
        setTitle("Comedor UCV - Panel de Administración");
        setSize(1920, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FONDO);
        setLayout(new BorderLayout());

        // --- 1. ENCABEZADO (USANDO EL COMPONENTE CORREGIDO) ---
        add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. CONTENEDOR INFERIOR ---
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setOpaque(false);

        // Sidebar izquierdo
        contenedorInferior.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- 3. PANEL DE CUERPO (BOTONES CENTRALES) ---
        JPanel panelCuerpo = new JPanel(new GridBagLayout());
        panelCuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBienvenida = new JLabel("Bienvenido, " + nombreAdmin);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 32));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        panelCuerpo.add(lblBienvenida, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridy = 1;
        panelCuerpo.add(crearBotonAdmin("Configurar Menus"), gbc);
        gbc.gridy = 2;

        // --- CAMBIO DE TEXTO DEL BOTÓN ---
        panelCuerpo.add(crearBotonAdmin("Añadir Estatus (Estudiante)"), gbc);

        gbc.gridy = 3;
        panelCuerpo.add(crearBotonAdmin("Generar reporte"), gbc);

        contenedorInferior.add(panelCuerpo, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Boton Cerrar Sesión) ---
        contenedorInferior.add(crearContenedorBotonCerrar(), BorderLayout.EAST);

        add(contenedorInferior, BorderLayout.CENTER);
    }

    private JPanel crearContenedorBotonCerrar() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Margen para que la pastilla de cerrar sesión no choque con el header
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
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
        btn.setPreferredSize(new Dimension(600, 80));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

btn.addActionListener(e -> {
    if (texto.equals("Configurar Menus")) {
        dispose();
        new com.ucv.view.admin.FechaMenus().setVisible(true);
    } else if (texto.equals("Añadir Estatus (Estudiante)")) {
        com.ucv.controller.UserController controller = new com.ucv.controller.UserController();
        controller.irAAnadirEstatus(this);
    } else if (texto.equals("Generar reporte")) {
        dispose(); // cerramos la ventana actual
        new com.ucv.view.admin.Reporte().setVisible(true); // abrimos Reporte.java
    } else {
        JOptionPane.showMessageDialog(this, "Función '" + texto + "' en desarrollo.");
    }
});

        return btn;
    }
}