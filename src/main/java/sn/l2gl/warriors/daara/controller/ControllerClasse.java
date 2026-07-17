package sn.l2gl.warriors.daara.controller;

import sn.l2gl.warriors.daara.exception.ClasseDejaExistanteException;
import sn.l2gl.warriors.daara.exception.ClasseIntrouvableException;
import sn.l2gl.warriors.daara.exception.SuppressionImpossibleException;
import sn.l2gl.warriors.daara.model.dao.ClasseDao;
import sn.l2gl.warriors.daara.model.models.Classe;

import java.util.List;

public class ControllerClasse {

    private final ClasseDao classeDao;

    public ControllerClasse(ClasseDao classeDao) {
        this.classeDao = classeDao;
    }

    public Classe ajouterClasse(Classe classe) {
        if (classe.getCode() != null
                && classeDao.trouver(classe.getCode()).isPresent()) {
            throw new ClasseDejaExistanteException(classe.getCode());
        }
        return classeDao.inserer(classe);
    }

    public Classe trouverClasse(String code) {
        return classeDao.trouver(code)
                .orElseThrow(() -> new ClasseIntrouvableException(code));
    }

    public List<Classe> listerClasses() {
        return classeDao.listerTous();
    }

    public Classe modifierClasse(Classe classe) {
        trouverClasse(classe.getCode());
        return classeDao.modifier(classe)
                .orElseThrow(() -> new ClasseIntrouvableException(classe.getCode()));
    }

    public void supprimerClasse(String code) {
        trouverClasse(code);

        long nbTalibes = classeDao.compterTalibes(code);
        if (nbTalibes > 0) {
            throw new SuppressionImpossibleException(
                    "Impossible de supprimer la classe " + code
                            + " : des talibés y sont encore inscrits."
            );
        }

        boolean supprime = classeDao.supprimer(code);
        if (!supprime) {
            throw new ClasseIntrouvableException(code);   //
        }
    }
}