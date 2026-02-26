package com.ucv.view;



import com.ucv.model.DataBase;
import com.ucv.view.components.HeaderUCV;

import com.ucv.view.components.SideBar;

import com.ucv.view.components.PrimaryButton2;

import javax.swing.*;

import java.awt.*;

import java.awt.geom.RoundRectangle2D;


public class MenusUsuario extends JFrame {

    private final String usuarioID;

    private static final Color AZUL_FONDO = new Color(18, 71, 150);

    private static final Color GRIS_TARJETA = new Color(225, 225, 225);

    private static final Color AZUL_DISPONIBLES = new Color(20, 35, 100);

    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);

    public MenusUsuario(String usuarioID) {
        this.usuarioID = usuarioID;

        setTitle("Menús Disponibles · Comedor UCV");

        setSize(1920, 800);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());



        JPanel container = new JPanel(new BorderLayout());

        container.setBackground(AZUL_FONDO);



        container.add(new HeaderUCV(), BorderLayout.NORTH);

        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);



// --- PANEL CENTRAL ---

        JPanel panelCentral = new JPanel(new GridBagLayout());

        panelCentral.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;

        gbc.insets = new Insets(10, 0, 10, 0);



// 1. Título

        JLabel lblTitulo = new JLabel("Menus disponibles");

        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));

        lblTitulo.setForeground(Color.WHITE);

        gbc.gridy = 0;

        panelCentral.add(lblTitulo, gbc);



// 2. CONTENEDOR DE TARJETAS

        JPanel panelTarjetas = new JPanel(new GridBagLayout());

        panelTarjetas.setOpaque(false);

        GridBagConstraints gbcCards = new GridBagConstraints();

        gbcCards.insets = new Insets(0, 20, 0, 20);



// Tarjeta Desayuno

        gbcCards.gridx = 0;

        panelTarjetas.add(crearTarjetaMenu("Desayuno", "7:00 am - 11:00 am", "300", "0.00"), gbcCards);



// Tarjeta Almuerzo

        gbcCards.gridx = 1;

        panelTarjetas.add(crearTarjetaMenu("Almuerzo", "12:00 pm - 5:00 pm", "500", "0.00"), gbcCards);



        gbc.gridy = 1;

        gbc.insets = new Insets(30, 0, 30, 0);

        panelCentral.add(panelTarjetas, gbc);



// 3. Texto Informativo

        JLabel lblInfo = new JLabel("<html><div style='text-align: center;'>" +

                "El pago se realiza en el momento de acceder al comedor. " +

                "Sera descontado del saldo que tenga en su <u>monedero</u>.<br>" +

                "<font color='#B0C4DE'>Si usted no tiene dinero en su monedero, le sera denegado el acceso</font>" +

                "</div></html>");

        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        lblInfo.setForeground(Color.WHITE);

        gbc.gridy = 2;

        gbc.weightx = 1.0; // IMPORTANTE: Da peso para que se estire

        gbc.fill = GridBagConstraints.HORIZONTAL; // IMPORTANTE: Ocupa todo el ancho

        gbc.insets = new Insets(0, 270, 0, 50); // Márgenes (Arriba, Izquierda, Abajo, Derecha)

        panelCentral.add(lblInfo, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        container.add(crearFooter(), BorderLayout.SOUTH);





        add(container);

    }



    private JPanel crearTarjetaMenu(String tipo, String horario, String maxCupos, String precio) {

        PanelRedondeado tarjeta = new PanelRedondeado(30, GRIS_TARJETA);

        tarjeta.setPreferredSize(new Dimension(420, 370));

        tarjeta.setMinimumSize(new Dimension(420, 370));

        tarjeta.setLayout(null);



        JLabel lblTipo = new JLabel(tipo, SwingConstants.CENTER);

        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 30));

        lblTipo.setBounds(0, 30, 420, 40);

        tarjeta.add(lblTipo);



        JLabel lblCuerpo = new JLabel("<html><div style='line-height: 80%;'>" +

                "<b>Plato fuerte</b><br>" +

                "<b>Bebida<br>" +

                "Postre / Fruta</div></html>");

        lblCuerpo.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        lblCuerpo.setBounds(50, 70, 320, 90);

        tarjeta.add(lblCuerpo);



        JLabel lblHorarioT = new JLabel("Horario");

        lblHorarioT.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lblHorarioT.setBounds(50, 160, 100, 25);

        tarjeta.add(lblHorarioT);



        JLabel lblHorarioV = new JLabel(horario);

        lblHorarioV.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        lblHorarioV.setBounds(50, 180, 300, 25);

        tarjeta.add(lblHorarioV);



        PanelRedondeado cajaAzul = new PanelRedondeado(20, AZUL_DISPONIBLES);

        cajaAzul.setBounds(50, 220, 320, 70);

        cajaAzul.setLayout(null);



        JLabel lblDispT = new JLabel("Disponibles:");

        lblDispT.setForeground(Color.WHITE);

        lblDispT.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lblDispT.setBounds(20, 10, 200, 25);

        cajaAzul.add(lblDispT);



        JLabel lblDispV = new JLabel("XXX / " + maxCupos + " comensales");

        lblDispV.setForeground(Color.WHITE);

        lblDispV.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        lblDispV.setBounds(20, 37, 280, 25);

        cajaAzul.add(lblDispV);

        tarjeta.add(cajaAzul);



// Ajustado para que quepa dentro de los 370px de altura de la tarjeta

        JLabel lblBs = new JLabel("Bs " + precio);

        lblBs.setFont(new Font("Segoe UI", Font.BOLD, 28));

        lblBs.setBounds(50, 305, 150, 45);

        tarjeta.add(lblBs);



        PrimaryButton2 btnSel = new PrimaryButton2("Seleccionar", AMARILLO_BOTON);

        btnSel.setBounds(210, 300, 170, 50);

        btnSel.setForeground(Color.BLACK);

        btnSel.setFont(new Font("Segoe UI", Font.BOLD, 17));



// REDIRECCIONAMIENTO

        btnSel.addActionListener(e -> {
            dispose();
            new ConfirmacionReserva(tipo, usuarioID).setVisible(true);
        });



        tarjeta.add(btnSel);



        return tarjeta;

    }



    private JPanel crearFooter() {

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        footer.setOpaque(false);

        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));

        JLabel cerrarSesion = new JLabel("<html><u>Cerrar Sesión</u></html>");

        cerrarSesion.setForeground(Color.WHITE);

        cerrarSesion.setFont(new Font("Arial", Font.PLAIN, 20));

        cerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent e) {
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();

            }

        });

        footer.add(cerrarSesion);

        return footer;

    }



    static class PanelRedondeado extends JPanel {

        private int radio;

        private Color color;

        public PanelRedondeado(int radio, Color color) {

            this.radio = radio; this.color = color;

            setOpaque(false);

        }

        @Override

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(color);

            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radio, radio));

            g2.dispose();

        }

    }
}