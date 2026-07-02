package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.model.dao.Dao;
import sn.l2gl.warriors.daara.model.models.Talibe;

import java.util.List;

/**
 * ControllerTalibe — Contrôleur MVC pour la gestion des Talibés.
 *
 * Fait le lien entre la vue (Swing) et le DAO Talibe.
 * Applique les règles métier avant les opérations CRUD.
 *
 * NOTE : Adapter le type de l'identifiant si nécessaire.
 */
public class ControllerTalibe {

    private final Dao<Talibe, String> talibeDao;

    public ControllerTalibe(Dao<Talibe, String> talibeDao) {
        this.talibeDao = talibeDao;
    }

    /**
     * Ajoute un nouveau talibé.
     */
    public Talibe ajouterTalibe(Talibe talibe) {

        if (talibe.getMatricule() != null
                && talibeDao.trouver(talibe.getMatricule()).isPresent()) {
            throw new IllegalArgumentException(
                    "Un talibé avec ce matricule existe déjà."
            );
        }

        return talibeDao.inserer(talibe);
    }

    /**
     * Recherche un talibé par matricule.
     */
    public Talibe trouverTalibe(String matricule) {
        return talibeDao.trouver(matricule)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Talibé introuvable : " + matricule
                        ));
    }

    /**
     * Retourne la liste de tous les talibés.
     */
    public List<Talibe> listerTalibes() {
        return talibeDao.listerTous();
    }

    /**
     * Modifie un talibé existant.
     */
    public Talibe modifierTalibe(Talibe talibe) {

        trouverTalibe(talibe.getMatricule());

        return talibeDao.modifier(talibe)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Talibé introuvable : " + talibe.getMatricule()
                        ));
    }

    /**
     * Supprime un talibé.
     */
    public void supprimerTalibe(String matricule) {

        trouverTalibe(matricule);

        boolean supprime = talibeDao.supprimer(matricule);

        if (!supprime) {
            throw new IllegalArgumentException(
                    "Talibé introuvable : " + matricule
            );
        }
    }
}