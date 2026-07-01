package sn.l2gl.warriors.daara.exception;

public class ClasseIntrouvableException extends DaaraException {
    public ClasseIntrouvableException(String code) {
        super("Aucune classe trouvée pour le code : " + code);
    }
}