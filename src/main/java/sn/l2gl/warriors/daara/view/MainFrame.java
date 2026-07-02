package sn.l2gl.warriors.daara.view;
import javax.swing.*;
        import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static final String HOME = "home";
    public static final String CLASS = "class";

    public MainFrame() {
        setTitle("Gestion Daara");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Vues
        HomePanel homePanel = new HomePanel(this);
        ClassPanel classPanel = new ClassPanel(this);

        mainPanel.add(homePanel, HOME);
        mainPanel.add(classPanel, CLASS);

        add(mainPanel);

        setVisible(true);
    }

    // méthode pour changer de vue
    public void showView(String name) {
        cardLayout.show(mainPanel, name);
    }
}