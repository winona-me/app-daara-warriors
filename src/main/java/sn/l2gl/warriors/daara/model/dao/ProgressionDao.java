package sn.l2gl.warriors.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sn.l2gl.warriors.daara.model.models.Progression;
import sn.l2gl.warriors.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Acces aux donnees de l'entite Progression via Hibernate.
 * La cle (id) est auto-generee : il n'y a donc pas de notion de
 * "DejaExistant" pour cette entite, seulement la validite des donnees.
 */
public class ProgressionDao implements Dao<Progression, Integer> {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Progression inserer(Progression progression) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(progression);
            session.getTransaction().commit();
            return progression;
        }
    }

    @Override
    public Optional<Progression> trouver(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Progression.class, id));
        }
    }

    @Override
    public List<Progression> listerTous() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Progression order by dateEvaluation desc", Progression.class).list();
        }
    }

    @Override
    public Optional<Progression> modifier(Progression progression) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Progression.class, progression.getId()) == null) {
                return Optional.empty();
            }
            session.beginTransaction();
            Progression fusionnee = session.merge(progression);
            session.getTransaction().commit();
            return Optional.of(fusionnee);
        }
    }

    @Override
    public boolean supprimer(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Progression progression = session.find(Progression.class, id);
            if (progression == null) {
                return false;
            }
            session.beginTransaction();
            session.remove(progression);
            session.getTransaction().commit();
            return true;
        }
    }

    /** Recherche par critere : toutes les progressions d'un talibe donne. */
    public List<Progression> listerParTalibe(String matriculeTalibe) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Progression p where p.talibe.matricule = :m order by p.dateEvaluation desc",
                            Progression.class)
                    .setParameter("m", matriculeTalibe)
                    .list();
        }
    }
}