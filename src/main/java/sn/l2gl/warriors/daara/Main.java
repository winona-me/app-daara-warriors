package sn.l2gl.warriors.daara;

import sn.l2gl.warriors.daara.controller.ControllerClasse;
import sn.l2gl.warriors.daara.controller.ControllerMaitre;
import sn.l2gl.warriors.daara.controller.ControllerProgression;
import sn.l2gl.warriors.daara.controller.ControllerTalibe;
import sn.l2gl.warriors.daara.model.dao.ClasseDao;
import sn.l2gl.warriors.daara.model.dao.MaitreDao;
import sn.l2gl.warriors.daara.model.dao.ProgressionDao;
import sn.l2gl.warriors.daara.model.dao.TalibeDao;
import sn.l2gl.warriors.daara.util.HibernateUtil;
import sn.l2gl.warriors.daara.view.MainFrame;

import javax.swing.*;

/**
 * Point d'entree de l'application.
 * Racine de composition : cree les DAO et les controleurs, puis lance
 * l'interface graphique (MainFrame) en lui injectant ces controleurs.
 */
public class Main {

    public static void main(String[] args) {

        // Verifie que la connexion Hibernate fonctionne avant de lancer l'IHM
        HibernateUtil.getSessionFactory();
        System.out.println("Hibernate OK");

        // Un seul DAO et un seul controleur par entite, partages par tous les panels
        ControllerMaitre controllerMaitre = new ControllerMaitre(new MaitreDao());
        ControllerClasse controllerClasse = new ControllerClasse(new ClasseDao());
        ControllerTalibe controllerTalibe = new ControllerTalibe(new TalibeDao());
        ControllerProgression controllerProgression = new ControllerProgression(new ProgressionDao());

        SwingUtilities.invokeLater(() -> new MainFrame(
                controllerMaitre, controllerClasse, controllerTalibe, controllerProgression));
    }
}
