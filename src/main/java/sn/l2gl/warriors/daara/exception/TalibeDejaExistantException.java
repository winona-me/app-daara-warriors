package sn.l2gl.warriors.daara.exception;

public class TalibeDejaExistantException extends DaaraException {
    public TalibeDejaExistantException(String matricule) {
        super("Un talibé avec le matricule " + matricule + " existe déjà.");
    }
}