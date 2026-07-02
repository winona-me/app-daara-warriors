package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.exception.MaitreDejaExistantException;
import sn.l2gl.warriors.daara.exception.MaitreIntrouvableException;
import sn.l2gl.warriors.daara.exception.SuppressionImpossibleException;
import sn.l2gl.warriors.daara.model.dao.Dao;
import sn.l2gl.warriors.daara.model.models.Maitre;

import java.util.List;

/**
 * ControllerMaitre — Contrôleur MVC pour la gestion des Maîtres coraniques.
 *
 * Fait le lien entre la vue (Swing) et le DAO Maitre.
 * Applique les règles métier (unicité du matricule, non-suppression
 * d'un maître ayant des classes assignées, etc.) et lève les
 * exceptions métier appropriées en cas de violation.
 *
 * NOTE: Dépend de l'interface générique Dao<Maitre, String>.
 * À adapter si le DAO concret (feature/dao-maitre-classe) expose
 * des méthodes de recherche supplémentaires (ex: findByNom).
 */
public class ControllerMaitre {

    private final Dao<Maitre, String> maitreDao;

    public ControllerMaitre(Dao<Maitre, String> maitreDao) {
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
        // Vérifie que le maître existe avant modification
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
        Maitre maitre = trouverMaitre(matricule);

        if (maitre.getClasses() != null && !maitre.getClasses().isEmpty()) {
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