package sn.l2gl.warriors.daara.view;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(MainFrame frame) {

        setLayout(new GridBagLayout());

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("Gestion Daara");
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 22f));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sousTitre = new JLabel("Choisissez un module a gerer");
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnMaitre = new JButton("Gerer les Maitres");
        JButton btnClasse = new JButton("Gerer les Classes");
        JButton btnTalibe = new JButton("Gerer les Talibes");
        JButton btnProgression = new JButton("Gerer les Progressions");

        for (JButton b : new JButton[]{btnMaitre, btnClasse, btnTalibe, btnProgression}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(240, 36));
        }

        btnMaitre.addActionListener(e -> frame.showView(MainFrame.MAITRE));
        btnClasse.addActionListener(e -> frame.showView(MainFrame.CLASS));
        btnTalibe.addActionListener(e -> frame.showView(MainFrame.TALIBE));
        btnProgression.addActionListener(e -> frame.showView(MainFrame.PROGRESSION));

        contenu.add(titre);
        contenu.add(Box.createVerticalStrut(5));
        contenu.add(sousTitre);
        contenu.add(Box.createVerticalStrut(20));
        contenu.add(btnMaitre);
        contenu.add(Box.createVerticalStrut(10));
        contenu.add(btnClasse);
        contenu.add(Box.createVerticalStrut(10));
        contenu.add(btnTalibe);
        contenu.add(Box.createVerticalStrut(10));
        contenu.add(btnProgression);

        add(contenu);
    }
}
