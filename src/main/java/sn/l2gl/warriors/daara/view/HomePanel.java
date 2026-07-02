package sn.l2gl.warriors.daara.view;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(MainFrame frame) {

        setLayout(new FlowLayout());

        JLabel label = new JLabel("Accueil");

        JButton btn = new JButton("Aller vers Classes");

        btn.addActionListener(e -> frame.showView(MainFrame.CLASS));

        add(label);
        add(btn);
    }
}