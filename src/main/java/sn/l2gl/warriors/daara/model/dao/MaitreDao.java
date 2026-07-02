package sn.l2gl.warriors.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sn.l2gl.warriors.daara.exception.MaitreDejaExistantException;
import sn.l2gl.warriors.daara.exception.SuppressionImpossibleException;
import sn.l2gl.warriors.daara.model.models.Maitre;
import sn.l2gl.warriors.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Acces aux donnees de l'entite Maitre via Hibernate.
 * Contient toutes les requetes HQL relatives aux maitres.
 */
public class MaitreDao implements Dao<Maitre, String> {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Maitre inserer(Maitre maitre) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Maitre.class, maitre.getMatricule()) != null) {
                throw new MaitreDejaExistantException(maitre.getMatricule());
            }
            session.beginTransaction();
            session.persist(maitre);
            session.getTransaction().commit();
            return maitre;
        }
    }

    @Override
    public Optional<Maitre> trouver(String matricule) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Maitre.class, matricule));
        }
    }

    @Override
    public List<Maitre> listerTous() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Maitre order by nom", Maitre.class).list();
        }
    }

    @Override
    public Optional<Maitre> modifier(Maitre maitre) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Maitre.class, maitre.getMatricule()) == null) {
                return Optional.empty();
            }
            session.beginTransaction();
            Maitre fusionne = session.merge(maitre);
            session.getTransaction().commit();
            return Optional.of(fusionne);
        }
    }

    @Override
    public boolean supprimer(String matricule) {
        try (Session session = sessionFactory.openSession()) {
            Maitre maitre = session.find(Maitre.class, matricule);
            if (maitre == null) {
                return false;
            }
            Long nbClasses = session.createQuery(
                            "select count(c) from Classe c where c.maitre.matricule = :mat", Long.class)
                    .setParameter("mat", matricule)
                    .uniqueResult();
            if (nbClasses != null && nbClasses > 0) {
                throw new SuppressionImpossibleException(
                        "Impossible de supprimer le maitre " + matricule + " : il encadre encore " + nbClasses + " classe(s).");
            }
            session.beginTransaction();
            session.remove(maitre);
            session.getTransaction().commit();
            return true;
        }
    }

    /** Recherche par critere : renvoie tous les maitres dont le nom ou prenom contient le texte saisi. */
    public List<Maitre> rechercherParNom(String texte) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Maitre m where lower(m.nom) like lower(:n) or lower(m.prenom) like lower(:n) order by m.nom",
                            Maitre.class)
                    .setParameter("n", "%" + texte + "%")
                    .list();
        }
    }
}