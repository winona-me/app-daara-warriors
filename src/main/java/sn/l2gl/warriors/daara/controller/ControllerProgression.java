package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.exception.ProgressionInvalideException;
import sn.l2gl.warriors.daara.model.dao.Dao;
import sn.l2gl.warriors.daara.model.models.Progression;

import java.util.List;

public class ControllerProgression {

    private final Dao<Progression, Integer> progressionDao;

    public ControllerProgression(Dao<Progression, Integer> progressionDao) {
        this.progressionDao = progressionDao;
    }

    private void valider(Progression progression) {
        if (progression.getSourate() == null || progression.getSourate().trim().isEmpty()) {
            throw new ProgressionInvalideException("La sourate ne peut pas être vide.");
        }
        if (progression.getNombreVersets() < 0) {
            throw new ProgressionInvalideException("Le nombre de versets ne peut pas être négatif.");
        }
    }

    public Progression ajouterProgression(Progression progression) {
        valider(progression);   // ✅

        if (progression.getId() != null
                && progressionDao.trouver(progression.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Une progression avec l'identifiant " + progression.getId() + " existe déjà."
            );
        }
        return progressionDao.inserer(progression);
    }

    public Progression trouverProgression(Integer id) {
        return progressionDao.trouver(id)
                .orElseThrow(() -> new IllegalArgumentException("Progression introuvable : " + id));
    }

    public List<Progression> listerProgressions() {
        return progressionDao.listerTous();
    }

    public Progression modifierProgression(Progression progression) {
        valider(progression);   //
        trouverProgression(progression.getId());
        return progressionDao.modifier(progression)
                .orElseThrow(() -> new IllegalArgumentException("Progression introuvable : " + progression.getId()));
    }

    public void supprimerProgression(Integer id) {
        trouverProgression(id);
        boolean supprime = progressionDao.supprimer(id);
        if (!supprime) {
            throw new IllegalArgumentException("Progression introuvable : " + id);
        }
    }
}