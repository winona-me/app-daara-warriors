package sn.l2gl.warriors.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sn.l2gl.warriors.daara.exception.TalibeDejaExistantException;
import sn.l2gl.warriors.daara.model.models.Talibe;
import sn.l2gl.warriors.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Acces aux donnees de l'entite Talibe via Hibernate.
 */
public class TalibeDao implements Dao<Talibe, String> {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Talibe inserer(Talibe talibe) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Talibe.class, talibe.getMatricule()) != null) {
                throw new TalibeDejaExistantException(talibe.getMatricule());
            }
            session.beginTransaction();
            session.persist(talibe);
            session.getTransaction().commit();
            return talibe;
        }
    }

    @Override
    public Optional<Talibe> trouver(String matricule) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Talibe.class, matricule));
        }
    }

    @Override
    public List<Talibe> listerTous() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Talibe order by nom", Talibe.class).list();
        }
    }

    @Override
    public Optional<Talibe> modifier(Talibe talibe) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Talibe.class, talibe.getMatricule()) == null) {
                return Optional.empty();
            }
            session.beginTransaction();
            Talibe fusionne = session.merge(talibe);
            session.getTransaction().commit();
            return Optional.of(fusionne);
        }
    }

    /**
     * Supprime un talibe. Le cascade ALL + orphanRemoval defini sur
     * Talibe.progressions supprime automatiquement ses progressions.
     */
    @Override
    public boolean supprimer(String matricule) {
        try (Session session = sessionFactory.openSession()) {
            Talibe talibe = session.find(Talibe.class, matricule);
            if (talibe == null) {
                return false;
            }
            session.beginTransaction();
            session.remove(talibe);
            session.getTransaction().commit();
            return true;
        }
    }

    /** Recherche par critere : talibes dont le nom ou prenom contient le texte saisi. */
    public List<Talibe> rechercherParNom(String texte) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Talibe t where lower(t.nom) like lower(:n) or lower(t.prenom) like lower(:n) order by t.nom",
                            Talibe.class)
                    .setParameter("n", "%" + texte + "%")
                    .list();
        }
    }

    /** Recherche par critere : tous les talibes d'une classe donnee. */
    public List<Talibe> listerParClasse(String codeClasse) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Talibe t where t.classe.code = :c order by t.nom", Talibe.class)
                    .setParameter("c", codeClasse)
                    .list();
        }
    }
}