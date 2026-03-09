package com.ucv.view.components;

import com.ucv.controller.MenuController;
import com.ucv.model.DataBase;
import com.ucv.model.MenuDB;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class TarjetaMenuUsuario extends JPanel {

    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AZUL_OSCURO = new Color(20, 30, 90);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);

    private final MenuController controller;
    private final String usuarioID;

    public TarjetaMenuUsuario(String tipo, String horario, String[] datos, String usuarioID, MenuController controller) {
        this.controller = controller;
        this.usuarioID = usuarioID;

        setPreferredSize(new Dimension(380, 420));
        setOpaque(false);

        if (datos == null) {
            setLayout(new GridBagLayout());
            JLabel lblNoDisp = new JLabel("No disponible");
            lblNoDisp.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblNoDisp.setForeground(new Color(130, 130, 130));
            add(lblNoDisp);
        } else {
            setLayout(null); // Usamos null para replicar la precisión del prototipo
            armarInterfazDisponible(tipo, horario, datos);
        }
    }

    private void armarInterfazDisponible(String tipo, String horario, String[] datos) {

        // 1. Título (Desayuno / Almuerzo)
        JLabel lblTipo = new JLabel(tipo, SwingConstants.CENTER);
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTipo.setForeground(Color.BLACK);
        lblTipo.setBounds(0, 20, 380, 35);
        add(lblTipo);

        // 2. Comida (Plato, Bebida, Postre)
        int yText = 70;
        String[] platos = {datos[2], datos[3], datos[4]};
        for (String p : platos) {
            JLabel lblP = new JLabel(p);
            lblP.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            lblP.setForeground(Color.BLACK);
            lblP.setBounds(30, yText, 320, 30);
            add(lblP);
            yText += 30;
        }

        // 3. Horario
        JLabel lblHorarioT = new JLabel("Horario");
        lblHorarioT.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHorarioT.setForeground(Color.BLACK);
        lblHorarioT.setBounds(30, 180, 320, 20);
        add(lblHorarioT);

        JLabel lblHorarioV = new JLabel(horario);
        lblHorarioV.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblHorarioV.setForeground(Color.BLACK);
        lblHorarioV.setBounds(30, 200, 320, 25);
        add(lblHorarioV);

        // 4. Panel de Disponibilidad (Caja azul oscura)
        JPanel pnlDisp = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_OSCURO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        pnlDisp.setOpaque(false);
        pnlDisp.setBounds(30, 245, 320, 75);

        JLabel lblDTitulo = new JLabel("Disponibles:");
        lblDTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDTitulo.setForeground(Color.WHITE);
        lblDTitulo.setBounds(15, 15, 290, 20);
        pnlDisp.add(lblDTitulo);

        // Cálculo dinámico de raciones
        int disp = new MenuDB().CantidadDisponible(tipo.toLowerCase());
        String maxCap = (datos.length > 9) ? datos[9] : "500";
        String txtDisp = (disp >= 0 ? disp : 0) + " / " + maxCap + " comensales";

        JLabel lblDValor = new JLabel(txtDisp);
        lblDValor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDValor.setForeground(Color.WHITE);
        lblDValor.setBounds(15, 40, 290, 20);
        pnlDisp.add(lblDValor);

        add(pnlDisp);

        // 5. Precio Personalizado según el Rol
        String precioFinal = "0.00";
        try {
            DataBase.RolUsuario rol = new DataBase().obtenerRol(usuarioID);
            if (rol == DataBase.RolUsuario.PROFESOR && datos.length > 6) precioFinal = datos[6];
            else if (rol == DataBase.RolUsuario.EMPLEADO && datos.length > 7) precioFinal = datos[7];
            else if (datos.length > 5) precioFinal = datos[5]; // Por defecto cobra como Estudiante
        } catch (IOException ignored) {}

        JLabel lblPrecio = new JLabel("Bs " + precioFinal);
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblPrecio.setForeground(Color.BLACK);
        lblPrecio.setBounds(30, 340, 160, 45);
        add(lblPrecio);

        // 6. Botón de Selección
        PrimaryButton2 btnSel = new PrimaryButton2("Seleccionar", AMARILLO_BOTON);
        btnSel.setBounds(190, 345, 160, 40);
        btnSel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSel.setForeground(Color.BLACK);

        btnSel.addActionListener(e -> {
            Window ventanaPadre = SwingUtilities.getWindowAncestor(this);
            JFrame jfPadre = (ventanaPadre instanceof JFrame) ? (JFrame) ventanaPadre : null;
            controller.irAConfirmacionReserva(tipo, usuarioID, jfPadre);
        });
        add(btnSel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(GRIS_TARJETA);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.dispose();
    }
}