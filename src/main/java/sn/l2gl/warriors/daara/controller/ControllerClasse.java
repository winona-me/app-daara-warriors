package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.exception.SuppressionImpossibleException;
import sn.l2gl.warriors.daara.model.dao.ClasseDao;
import sn.l2gl.warriors.daara.model.models.Classe;

import java.util.List;

public class ControllerClasse {

    private final ClasseDao classeDao;   // ✅ type concret, plus Dao<Classe, String>

    public ControllerClasse(ClasseDao classeDao) {
        this.classeDao = classeDao;
    }

    public Classe ajouterClasse(Classe classe) {
        if (classe.getCode() != null
                && classeDao.trouver(classe.getCode()).isPresent()) {
            throw new IllegalArgumentException(
                    "Une classe avec le code " + classe.getCode() + " existe déjà."
            );
        }
        return classeDao.inserer(classe);
    }

    public Classe trouverClasse(String code) {
        return classeDao.trouver(code)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable : " + code));
    }

    public List<Classe> listerClasses() {
        return classeDao.listerTous();
    }

    public Classe modifierClasse(Classe classe) {
        trouverClasse(classe.getCode());
        return classeDao.modifier(classe)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable : " + classe.getCode()));
    }

    public void supprimerClasse(String code) {
        trouverClasse(code);

        long nbTalibes = classeDao.compterTalibes(code);   // ✅ requête, plus getTalibes()
        if (nbTalibes > 0) {
            throw new SuppressionImpossibleException(
                    "Impossible de supprimer la classe " + code
                            + " : des talibés y sont encore inscrits."
            );
        }

        boolean supprime = classeDao.supprimer(code);
        if (!supprime) {
            throw new IllegalArgumentException("Classe introuvable : " + code);
        }
    }
}