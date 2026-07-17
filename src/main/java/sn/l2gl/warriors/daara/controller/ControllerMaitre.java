package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.exception.MaitreDejaExistantException;
import sn.l2gl.warriors.daara.exception.MaitreIntrouvableException;
import sn.l2gl.warriors.daara.exception.SuppressionImpossibleException;
import sn.l2gl.warriors.daara.model.dao.MaitreDao;
import sn.l2gl.warriors.daara.model.models.Maitre;

import java.util.List;

/**
 * ControllerMaitre — Contrôleur MVC pour la gestion des Maîtres coraniques.
 *
 * Fait le lien entre la vue (Swing) et le DAO Maitre.
 * Applique les règles métier (unicité du matricule, non-suppression
 * d'un maître ayant des classes assignées, etc.) et lève les
 * exceptions métier appropriées en cas de violation.
 */
public class ControllerMaitre {

    private final MaitreDao maitreDao;

    public ControllerMaitre(MaitreDao maitreDao) {
        this.maitreDao = maitreDao;
    }

    /**
     * Ajoute un nouveau maître.
     * Règle métier : le matricule doit être unique.
     */
    public Maitre ajouterMaitre(Maitre maitre) {
        if (maitre.getMatricule() != null
                && maitreDao.trouver(maitre.getMatricule()).isPresent()) {
            throw new MaitreDejaExistantException(maitre.getMatricule());
        }
        return maitreDao.inserer(maitre);
    }

    /**
     * Recherche un maître par matricule.
     * Lève une exception si aucun maître n'est trouvé.
     */
    public Maitre trouverMaitre(String matricule) {
        return maitreDao.trouver(matricule)
                .orElseThrow(() -> new MaitreIntrouvableException(matricule));
    }

    /**
     * Liste tous les maîtres enregistrés.
     */
    public List<Maitre> listerMaitres() {
        return maitreDao.listerTous();
    }

    /**
     * Modifie les informations d'un maître existant.
     */
    public Maitre modifierMaitre(Maitre maitre) {
        trouverMaitre(maitre.getMatricule());

        return maitreDao.modifier(maitre)
                .orElseThrow(() -> new MaitreIntrouvableException(maitre.getMatricule()));
    }

    /**
     * Supprime un maître par matricule.
     * Règle métier : un maître ayant des classes assignées ne peut
     * pas être supprimé (il faut d'abord réassigner ou supprimer ses classes).
     */
    public void supprimerMaitre(String matricule) {
        trouverMaitre(matricule);   // vérifie juste qu'il existe

        long nbClasses = maitreDao.compterClasses(matricule);
        if (nbClasses > 0) {
            throw new SuppressionImpossibleException(
                    "Impossible de supprimer le maître " + matricule
                            + " : il a des classes assignées."
            );
        }

        boolean supprime = maitreDao.supprimer(matricule);
        if (!supprime) {
            throw new MaitreIntrouvableException(matricule);
        }
    }
}