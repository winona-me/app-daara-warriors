package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.exception.SuppressionImpossibleException;
import sn.l2gl.warriors.daara.model.dao.Dao;
import sn.l2gl.warriors.daara.model.models.Classe;

import java.util.List;

/**
 * ControllerClasse — Contrôleur MVC pour la gestion des Classes.
 *
 * Fait le lien entre la vue (Swing) et le DAO Classe.
 * Applique les règles métier avant les opérations CRUD.
 *
 * NOTE : Dépend de l'interface générique Dao<Classe, String>.
 */
public class ControllerClasse {

    private final Dao<Classe, String> classeDao;

    public ControllerClasse(Dao<Classe, String> classeDao) {
        this.classeDao = classeDao;
    }

    /**
     * Ajoute une nouvelle classe.
     * Le code doit être unique.
     */
    public Classe ajouterClasse(Classe classe) {

        if (classe.getCode() != null
                && classeDao.trouver(classe.getCode()).isPresent()) {
            throw new IllegalArgumentException(
                    "Une classe avec le code " + classe.getCode() + " existe déjà."
            );
        }

        return classeDao.inserer(classe);
    }

    /**
     * Recherche une classe par son code.
     */
    public Classe trouverClasse(String code) {
        return classeDao.trouver(code)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Classe introuvable : " + code
                        ));
    }

    /**
     * Retourne toutes les classes.
     */
    public List<Classe> listerClasses() {
        return classeDao.listerTous();
    }

    /**
     * Modifie une classe existante.
     */
    public Classe modifierClasse(Classe classe) {

        // Vérifie que la classe existe
        trouverClasse(classe.getCode());

        return classeDao.modifier(classe)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Classe introuvable : " + classe.getCode()
                        ));
    }

    /**
     * Supprime une classe.
     * Une classe contenant encore des talibés ne peut pas être supprimée.
     */
    public void supprimerClasse(String code) {

        Classe classe = trouverClasse(code);

        if (classe.getTalibes() != null && !classe.getTalibes().isEmpty()) {
            throw new SuppressionImpossibleException(
                    "Impossible de supprimer la classe " + code
                            + " : des talibés y sont encore inscrits."
            );
        }

        boolean supprime = classeDao.supprimer(code);

        if (!supprime) {
            throw new IllegalArgumentException(
                    "Classe introuvable : " + code
            );
        }
    }
}