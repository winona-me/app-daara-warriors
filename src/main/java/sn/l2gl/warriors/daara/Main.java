package sn.l2gl.warriors.daara;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sn.l2gl.warriors.daara.util.HibernateUtil;
import sn.l2gl.warriors.daara.view.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();
        session.getTransaction().commit();
        session.close();

        System.out.println("Hibernate OK");

        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}