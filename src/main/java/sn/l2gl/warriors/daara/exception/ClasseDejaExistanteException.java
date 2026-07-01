package sn.l2gl.warriors.daara.exception;

public class ClasseDejaExistanteException extends DaaraException {
    public ClasseDejaExistanteException(String code) {
        super("Une classe avec le code " + code + " existe déjà.");
    }
}