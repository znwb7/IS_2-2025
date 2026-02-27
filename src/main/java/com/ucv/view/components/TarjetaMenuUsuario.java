package com.ucv.view.components;

import com.ucv.controller.MenuController;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TarjetaMenuUsuario extends JPanel {

    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AZUL_TEXTO = new Color(18, 71, 150);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);

    private final MenuController controller;
    private final String usuarioID;   // ✅ NUEVO

    public TarjetaMenuUsuario(String tipo,
                              String horario,
                              String[] datos,
                              String usuarioID,      // ✅ NUEVO
                              MenuController controller) {

        this.controller = controller;
        this.usuarioID = usuarioID;   // ✅ GUARDAMOS EL USUARIO

        setPreferredSize(new Dimension(420, 450));
        setOpaque(false);

        if (datos == null) {
            setLayout(new GridBagLayout());
            JLabel lblNoDisp = new JLabel("No disponible");
            lblNoDisp.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblNoDisp.setForeground(new Color(130, 130, 130));
            add(lblNoDisp);
        } else {
            setLayout(null);
            armarInterfazDisponible(tipo, horario, datos);
        }
    }

    private void armarInterfazDisponible(String tipo, String horario, String[] datos) {

        JLabel lblTipo = new JLabel(tipo, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 45, 110));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        lblTipo.setForeground(Color.WHITE);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTipo.setBounds(130, 15, 160, 30);
        add(lblTipo);

        int hReducida = 25;
        int xCampos = 40;

        add(crearCampoMock(datos[2].toUpperCase(), 55, 240, hReducida, xCampos));
        add(crearCampoMock(datos[3], 85, 240, hReducida, xCampos));
        add(crearCampoMock(datos[4], 115, 240, hReducida, xCampos));

        JLabel lblHorario = new JLabel("Horario:");
        lblHorario.setFont(new Font("Arial", Font.BOLD, 14));
        lblHorario.setBounds(40, 150, 100, 20);
        add(lblHorario);
        add(crearCampoMock(horario, 180, 240, hReducida, xCampos));

        String capacidadReal = (datos.length > 9)
                ? datos[9] + " raciones"
                : "500 raciones";

        String ccbReal = (datos.length > 10)
                ? datos[10]
                : "0.00";

        JLabel lblDisp = new JLabel("Disponibles:");
        lblDisp.setFont(new Font("Arial", Font.BOLD, 14));
        lblDisp.setBounds(40, 210, 100, 20);
        add(lblDisp);
        add(crearCampoMock(capacidadReal, 205, 150, hReducida, 130));

        JLabel lblCCB = new JLabel("Precio neto (CCB) :");
        lblCCB.setForeground(AZUL_TEXTO);
        lblCCB.setFont(new Font("Arial", Font.BOLD, 14));
        lblCCB.setBounds(40, 250, 150, 20);
        add(lblCCB);
        add(crearCampoMock(ccbReal, 245, 90, hReducida, 180));

        JLabel bs1 = new JLabel("Bs.");
        bs1.setBounds(310, 250, 30, 20);
        add(bs1);

        JLabel lblFinal = new JLabel("Precio final comensal :");
        lblFinal.setForeground(AZUL_TEXTO);
        lblFinal.setFont(new Font("Arial", Font.BOLD, 14));
        lblFinal.setBounds(40, 285, 200, 20);
        add(lblFinal);

        String[] categorias = {"Estudiante:", "Profesor:", "Empleado:"};
        String[] precios = {datos[5], datos[6], datos[7]};

        for (int i = 0; i < 3; i++) {
            JLabel lblCat = new JLabel(categorias[i], SwingConstants.RIGHT);
            lblCat.setBounds(30, 315 + (i * 30), 100, 20);
            add(lblCat);
            add(crearCampoMock(precios[i],
                    310 + (i * 30), 100, hReducida, 160));

            JLabel bs = new JLabel("Bs.");
            bs.setBounds(270, 315 + (i * 30), 30, 20);
            add(bs);
        }

        PrimaryButton2 btnSel = new PrimaryButton2("Seleccionar", AMARILLO_BOTON);
        btnSel.setBounds(150, 405, 120, 36);
        btnSel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSel.setForeground(Color.BLACK);

        btnSel.addActionListener(e -> {
            Window ventanaPadre = SwingUtilities.getWindowAncestor(this);
            JFrame jfPadre = (ventanaPadre instanceof JFrame)
                    ? (JFrame) ventanaPadre
                    : null;

            // ✅ AHORA PASAMOS EL USUARIO REAL
            controller.irAConfirmacionReserva(tipo, usuarioID, jfPadre);
        });

        add(btnSel);
    }

    private JTextField crearCampoMock(String texto,
                                      int y,
                                      int w,
                                      int h,
                                      int x) {

        JTextField f = new JTextField(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        f.setBounds(x, y, w, h);
        f.setEditable(false);
        f.setFocusable(false);
        f.setOpaque(false);
        f.setBackground(GRIS_INPUT);
        f.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        return f;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(GRIS_TARJETA);
        g2.fill(new RoundRectangle2D.Double(
                0, 0, getWidth(), getHeight(), 30, 30));
        g2.dispose();
    }
}