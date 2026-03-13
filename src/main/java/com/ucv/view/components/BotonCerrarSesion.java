package com.ucv.view.components;

import com.ucv.model.DataBase;
import com.ucv.view.LoginUCV;
import com.ucv.controller.UserController;
import javax.swing.*;
import javax.swing.border.EmptyBorder; // Necesario para el margen
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class BotonCerrarSesion extends JPanel {

    public BotonCerrarSesion(JFrame ventanaActual) {
        // 1. Configuramos el panel principal como transparente
        setLayout(new BorderLayout());
        setOpaque(false);

        // 2. ESTA ES LA CLAVE: Añadimos un margen a la derecha
        // (Top, Left, Bottom, Right) -> Aumenta el 30 para moverlo más a la izquierda
        setBorder(new EmptyBorder(0, 0, 0, 60));

        // 3. Creamos la "Pastilla" blanca como un sub-panel
        JPanel pastilla = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        pastilla.setOpaque(false);
        pastilla.setPreferredSize(new Dimension(50, 50));
        pastilla.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 4. Icono (tu lógica original)
        URL imgUrl = getClass().getResource("/com/ucv/view/cerrar-sesion.png");
        JLabel lblIcono;
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(new ImageIcon(imgUrl).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            lblIcono = new JLabel(icon);
        } else {
            lblIcono = new JLabel("X");
            lblIcono.setFont(new Font("Arial", Font.BOLD, 18));
        }
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        pastilla.add(lblIcono, BorderLayout.CENTER);

        // 5. Evento de clic (en la pastilla)
        pastilla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                        ventanaActual,
                        "¿Estás seguro de Cerrar Sesión?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    new DataBase().LogedOut();
                    ventanaActual.dispose();

                    LoginUCV login = new LoginUCV();
                    login.setController(new UserController());
                    login.setVisible(true);
                }
            }
        });

        // Añadimos la pastilla al centro de este componente (que tiene el borde invisible)
        add(pastilla, BorderLayout.CENTER);
    }
}