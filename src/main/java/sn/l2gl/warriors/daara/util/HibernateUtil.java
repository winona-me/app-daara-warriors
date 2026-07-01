package sn.l2gl.warriors.daara.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * HibernateUtil — Singleton de SessionFactory.
 *
 * Rôle : créer et fournir UNE SEULE instance de SessionFactory
 * pour toute l'application. Ouvrir/fermer une SessionFactory
 * est très coûteux : on le fait une fois au démarrage.
 */
public class HibernateUtil {

    // L'unique instance de SessionFactory pour tout le programme.
    // "static" = appartient à la classe, pas aux objets.
    // "final"  = ne peut jamais être réassignée.
    private static final SessionFactory SESSION_FACTORY;

    // Bloc static : exécuté UNE SEULE FOIS quand la classe est chargée par Java.
    // C'est ici qu'on construit la SessionFactory.
    static {
        try {
            // Configuration() lit automatiquement hibernate.cfg.xml
            // qui se trouve dans src/main/resources/
            SESSION_FACTORY = new Configuration()
                    .configure()          // lit hibernate.cfg.xml
                    .buildSessionFactory(); // construit la fabrique de sessions

        } catch (Exception ex) {
            // Si la connexion échoue (MySQL pas démarré, mot de passe faux...)
            // on lance une erreur critique pour arrêter l'application immédiatement.
            System.err.println("Erreur de création de SessionFactory : " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retourne la SessionFactory unique.
     * Tous les DAO l'appellent pour ouvrir une Session.
     */
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    /**
     * Ferme la SessionFactory proprement.
     * À appeler uniquement quand l'application se ferme.
     */
    public static void fermer() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}