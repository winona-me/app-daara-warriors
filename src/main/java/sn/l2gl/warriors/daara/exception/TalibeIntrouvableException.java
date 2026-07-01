package sn.l2gl.warriors.daara.exception;

public class TalibeIntrouvableException extends DaaraException {
    public TalibeIntrouvableException(String matricule) {
        super("Aucun talibé trouvé pour le matricule : " + matricule);
    }
}