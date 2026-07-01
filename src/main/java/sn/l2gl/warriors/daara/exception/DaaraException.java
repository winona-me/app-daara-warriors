package sn.l2gl.warriors.daara.exception;

/**
 * DaaraException — Exception de base du projet.
 *
 * Toutes les exceptions métier du projet héritent de cette classe.
 * Elle hérite de RuntimeException (non vérifiée) pour ne pas
 * obliger chaque méthode à déclarer "throws DaaraException".
 */
public class DaaraException extends RuntimeException {

    public DaaraException(String message) {
        super(message);
    }

    public DaaraException(String message, Throwable cause) {
        super(message, cause);
    }
}