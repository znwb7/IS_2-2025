package com.ucv.view;

import com.ucv.model.DataBase;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.BotonCerrarSesion;

import javax.swing.*;
import java.awt.*;

public class PrincipalUsuario extends JFrame {

    private final String usuarioID;
    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color AZUL_BOTON = new Color(24, 116, 205);

    public PrincipalUsuario(String usuarioID) {
        this.usuarioID = usuarioID;

        setTitle("Comedor UCV - Menú de Usuario");
        setSize(1920, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor principal con fondo azul
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // --- 3. PANEL CENTRAL (Botones de Acción) ---
        JPanel panelCuerpo = new JPanel(new GridBagLayout());
        panelCuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblOpcion = new JLabel("Seleccione una opción");
        lblOpcion.setForeground(Color.WHITE);
        lblOpcion.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblOpcion.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCuerpo.add(lblOpcion, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridy = 1;
        panelCuerpo.add(crearBotonUsuario("Ver menus"), gbc);

        gbc.gridy = 2;
        panelCuerpo.add(crearBotonUsuario("Entrar al comedor"), gbc);

        container.add(panelCuerpo, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Botón Cerrar Sesión) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        // Componente reutilizable con la pastilla blanca
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Margen estandarizado a 25px para alineación con el logo circular
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }

    private JButton crearBotonUsuario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_BOTON);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45);
                g2.setColor(Color.WHITE);

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btn.setPreferredSize(new Dimension(450, 80));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (texto.equals("Ver menus")) {
                new MenusUsuario(usuarioID).setVisible(true);
                dispose();
            } else if (texto.equals("Entrar al comedor")) {
                DataBase db = new DataBase(); // Instancia local para la verificación
                if ("1".equals(db.GetFoodFlag(usuarioID))) {
                    new VerificacionFacialUCV(usuarioID).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Antes debe generar un turno de comida.",
                            "Aviso del Sistema",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new PrincipalUsuario("Invitado_UCV").setVisible(true);
        });
    }
}