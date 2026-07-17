package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.exception.TalibeDejaExistantException;
import sn.l2gl.warriors.daara.exception.TalibeIntrouvableException;
import sn.l2gl.warriors.daara.model.dao.TalibeDao;
import sn.l2gl.warriors.daara.model.models.Talibe;

import java.util.List;

public class ControllerTalibe {

    private final TalibeDao talibeDao;   // ✅ type concret

    public ControllerTalibe(TalibeDao talibeDao) {
        this.talibeDao = talibeDao;
    }

    public Talibe ajouterTalibe(Talibe talibe) {
        if (talibe.getMatricule() != null
                && talibeDao.trouver(talibe.getMatricule()).isPresent()) {
            throw new TalibeDejaExistantException(talibe.getMatricule());   // ✅
        }
        return talibeDao.inserer(talibe);
    }

    public Talibe trouverTalibe(String matricule) {
        return talibeDao.trouver(matricule)
                .orElseThrow(() -> new TalibeIntrouvableException(matricule));   // ✅
    }

    public List<Talibe> listerTalibes() {
        return talibeDao.listerTous();
    }

    public Talibe modifierTalibe(Talibe talibe) {
        trouverTalibe(talibe.getMatricule());
        return talibeDao.modifier(talibe)
                .orElseThrow(() -> new TalibeIntrouvableException(talibe.getMatricule()));   // ✅
    }

    public void supprimerTalibe(String matricule) {
        trouverTalibe(matricule);
        boolean supprime = talibeDao.supprimer(matricule);
        if (!supprime) {
            throw new TalibeIntrouvableException(matricule);   // ✅
        }
    }

    /** Recherche des talibés par nom/prénom — délègue au DAO concret. */
    public List<Talibe> rechercherTalibes(String texte) {
        return talibeDao.rechercherParNom(texte);
    }

    /** Liste les talibés d'une classe donnée — délègue au DAO concret. */
    public List<Talibe> listerTalibesParClasse(String codeClasse) {
        return talibeDao.listerParClasse(codeClasse);
    }
}