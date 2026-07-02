package sn.l2gl.warriors.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sn.l2gl.warriors.daara.exception.ClasseDejaExistanteException;
import sn.l2gl.warriors.daara.exception.SuppressionImpossibleException;
import sn.l2gl.warriors.daara.model.models.Classe;
import sn.l2gl.warriors.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Acces aux donnees de l'entite Classe via Hibernate.
 */
public class ClasseDao implements Dao<Classe, String> {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Classe inserer(Classe classe) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Classe.class, classe.getCode()) != null) {
                throw new ClasseDejaExistanteException(classe.getCode());
            }
            session.beginTransaction();
            session.persist(classe);
            session.getTransaction().commit();
            return classe;
        }
    }

    @Override
    public Optional<Classe> trouver(String code) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Classe.class, code));
        }
    }

    @Override
    public List<Classe> listerTous() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Classe order by libelle", Classe.class).list();
        }
    }

    @Override
    public Optional<Classe> modifier(Classe classe) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Classe.class, classe.getCode()) == null) {
                return Optional.empty();
            }
            session.beginTransaction();
            Classe fusionnee = session.merge(classe);
            session.getTransaction().commit();
            return Optional.of(fusionnee);
        }
    }

    @Override
    public boolean supprimer(String code) {
        try (Session session = sessionFactory.openSession()) {
            Classe classe = session.find(Classe.class, code);
            if (classe == null) {
                return false;
            }
            Long nbTalibes = session.createQuery(
                            "select count(t) from Talibe t where t.classe.code = :c", Long.class)
                    .setParameter("c", code)
                    .uniqueResult();
            if (nbTalibes != null && nbTalibes > 0) {
                throw new SuppressionImpossibleException(
                        "Impossible de supprimer la classe " + code + " : elle contient encore " + nbTalibes + " talibe(s).");
            }
            session.beginTransaction();
            session.remove(classe);
            session.getTransaction().commit();
            return true;
        }
    }

    /** Recherche par critere : classes dont le libelle contient le texte saisi. */
    public List<Classe> rechercherParLibelle(String texte) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Classe c where lower(c.libelle) like lower(:l) order by c.libelle", Classe.class)
                    .setParameter("l", "%" + texte + "%")
                    .list();
        }
    }
}