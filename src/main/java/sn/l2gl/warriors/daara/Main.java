package sn.l2gl.warriors.daara;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sn.l2gl.warriors.daara.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        session.beginTransaction();

        System.out.println("Hibernate démarré → tables générées si besoin");

        session.getTransaction().commit();
        session.close();
        factory.close();
    }
}