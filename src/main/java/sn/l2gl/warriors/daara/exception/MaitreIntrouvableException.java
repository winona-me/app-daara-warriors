package sn.l2gl.warriors.daara.exception;

public class MaitreIntrouvableException extends DaaraException {
    public MaitreIntrouvableException(String matricule) {
        super("Aucun maître trouvé pour le matricule : " + matricule);
    }
}