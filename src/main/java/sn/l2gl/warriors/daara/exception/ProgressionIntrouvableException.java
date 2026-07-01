package sn.l2gl.warriors.daara.exception;

public class ProgressionIntrouvableException extends DaaraException {
    public ProgressionIntrouvableException(Long id) {
        super("Aucune progression trouvée pour l'id : " + id);
    }
}