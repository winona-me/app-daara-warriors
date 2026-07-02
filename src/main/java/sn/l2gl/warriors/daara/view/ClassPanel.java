package sn.l2gl.warriors.daara.view;

import javax.swing.*;
import java.awt.*;

public class ClassPanel extends JPanel {

    public ClassPanel(MainFrame frame) {

        setLayout(new FlowLayout());

        JLabel label = new JLabel("Gestion des Classes");

        JButton back = new JButton("Retour Accueil");

        back.addActionListener(e -> frame.showView(MainFrame.HOME));

        add(label);
        add(back);
    }
}