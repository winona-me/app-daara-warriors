package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.model.dao.Dao;
import sn.l2gl.warriors.daara.model.models.Progression;

import java.util.List;

/**
 * ControllerProgression — Contrôleur MVC pour la gestion des Progressions.
 *
 * Fait le lien entre la vue Swing et le DAO Progression.
 * Applique les règles métier avant les opérations CRUD.
 */
public class ControllerProgression {

    private final Dao<Progression, Integer> progressionDao;

    public ControllerProgression(Dao<Progression, Integer> progressionDao) {
        this.progressionDao = progressionDao;
    }

    /**
     * Ajoute une nouvelle progression.
     */
    public Progression ajouterProgression(Progression progression) {

        // Si un id est déjà renseigné, vérifier qu'il n'existe pas déjà
        if (progression.getId() != null
                && progressionDao.trouver(progression.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Une progression avec l'identifiant "
                            + progression.getId() + " existe déjà."
            );
        }

        return progressionDao.inserer(progression);
    }

    /**
     * Recherche une progression par son identifiant.
     */
    public Progression trouverProgression(Integer id) {
        return progressionDao.trouver(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Progression introuvable : " + id
                        ));
    }

    /**
     * Retourne la liste de toutes les progressions.
     */
    public List<Progression> listerProgressions() {
        return progressionDao.listerTous();
    }

    /**
     * Modifie une progression existante.
     */
    public Progression modifierProgression(Progression progression) {

        trouverProgression(progression.getId());

        return progressionDao.modifier(progression)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Progression introuvable : "
                                        + progression.getId()
                        ));
    }

    /**
     * Supprime une progression.
     */
    public void supprimerProgression(Integer id) {

        trouverProgression(id);

        boolean supprime = progressionDao.supprimer(id);

        if (!supprime) {
            throw new IllegalArgumentException(
                    "Progression introuvable : " + id
            );
        }
    }
}