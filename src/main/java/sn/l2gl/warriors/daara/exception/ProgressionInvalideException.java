package sn.l2gl.warriors.daara.exception;

public class ProgressionInvalideException extends DaaraException {
    public ProgressionInvalideException(String raison) {
        super("Progression invalide : " + raison);
    }
}