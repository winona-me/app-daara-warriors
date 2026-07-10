package sn.l2gl.warriors.daara.view;

import sn.l2gl.warriors.daara.controller.ControllerClasse;
import sn.l2gl.warriors.daara.controller.ControllerMaitre;
import sn.l2gl.warriors.daara.controller.ControllerProgression;
import sn.l2gl.warriors.daara.controller.ControllerTalibe;

import javax.swing.*;
import java.awt.*;

/**
 * Fenetre principale. Utilise un CardLayout pour naviguer entre l'accueil
 * et les 4 modules (Maitres, Classes, Talibes, Progressions), chacun relie
 * a son controleur metier.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public static final String HOME = "home";
    public static final String MAITRE = "maitre";
    public static final String CLASS = "class";
    public static final String TALIBE = "talibe";
    public static final String PROGRESSION = "progression";

    public MainFrame(ControllerMaitre controllerMaitre,
                     ControllerClasse controllerClasse,
                     ControllerTalibe controllerTalibe,
                     ControllerProgression controllerProgression) {

        setTitle("Gestion Daara");
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        HomePanel homePanel = new HomePanel(this);
        MaitrePanel maitrePanel = new MaitrePanel(controllerMaitre);
        ClassPanel classPanel = new ClassPanel(controllerClasse, controllerMaitre);
        TalibePanel talibePanel = new TalibePanel(controllerTalibe, controllerClasse);
        ProgressionPanel progressionPanel = new ProgressionPanel(controllerProgression, controllerTalibe);

        mainPanel.add(homePanel, HOME);
        mainPanel.add(maitrePanel, MAITRE);
        mainPanel.add(classPanel, CLASS);
        mainPanel.add(talibePanel, TALIBE);
        mainPanel.add(progressionPanel, PROGRESSION);

        add(construireBarreNavigation(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /** Barre de boutons toujours visible pour changer de module. */
    private JPanel construireBarreNavigation() {
        JPanel barre = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        barre.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        JButton btnHome = new JButton("Accueil");
        JButton btnMaitre = new JButton("Maitres");
        JButton btnClasse = new JButton("Classes");
        JButton btnTalibe = new JButton("Talibes");
        JButton btnProgression = new JButton("Progressions");

        btnHome.addActionListener(e -> showView(HOME));
        btnMaitre.addActionListener(e -> showView(MAITRE));
        btnClasse.addActionListener(e -> showView(CLASS));
        btnTalibe.addActionListener(e -> showView(TALIBE));
        btnProgression.addActionListener(e -> showView(PROGRESSION));

        barre.add(btnHome);
        barre.add(btnMaitre);
        barre.add(btnClasse);
        barre.add(btnTalibe);
        barre.add(btnProgression);

        return barre;
    }

    // methode pour changer de vue
    public void showView(String name) {
        cardLayout.show(mainPanel, name);
    }
}
