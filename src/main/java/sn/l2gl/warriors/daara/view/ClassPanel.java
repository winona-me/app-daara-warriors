package sn.l2gl.warriors.daara.view;

import sn.l2gl.warriors.daara.controller.ControllerClasse;
import sn.l2gl.warriors.daara.controller.ControllerMaitre;
import sn.l2gl.warriors.daara.exception.DaaraException;
import sn.l2gl.warriors.daara.model.models.Classe;
import sn.l2gl.warriors.daara.model.models.Maitre;
import sn.l2gl.warriors.daara.util.CsvExporter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel (JPanel) de gestion des classes : relie ClasseView (Swing pur)
 * a ControllerClasse (CRUD) et a ControllerMaitre (alimentation de la
 * liste deroulante des maitres, obligatoire pour chaque classe).
 */
public class ClassPanel extends JPanel {

    private final ControllerClasse controllerClasse;
    private final ControllerMaitre controllerMaitre;
    private final ClasseView vue = new ClasseView();

    private List<Classe> classesAffichees = new ArrayList<>();

    public ClassPanel(ControllerClasse controllerClasse, ControllerMaitre controllerMaitre) {
        this.controllerClasse = controllerClasse;
        this.controllerMaitre = controllerMaitre;

        setLayout(new BorderLayout());
        add(vue, BorderLayout.CENTER);

        chargerMaitresDansCombo();
        toutAfficher();

        vue.getBoutonToutAfficher().addActionListener(e -> toutAfficher());
        vue.getBoutonChercher().addActionListener(e -> rechercher());
        vue.getBoutonNouveau().addActionListener(e -> vue.reinitialiser());
        vue.getBoutonEnregistrer().addActionListener(e -> enregistrer());
        vue.getBoutonSupprimer().addActionListener(e -> supprimer());
        vue.getBoutonExporter().addActionListener(e -> exporter());

        vue.getTable().getSelectionModel().addListSelectionListener(this::surSelectionLigne);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                chargerMaitresDansCombo();
                toutAfficher();
            }
        });
    }

    private void chargerMaitresDansCombo() {
        vue.chargerMaitres(controllerMaitre.listerMaitres());
    }

    private void toutAfficher() {
        classesAffichees = controllerClasse.listerClasses();
        vue.afficher(classesAffichees);
    }

    private void rechercher() {
        String texte = vue.getChampRecherche().getText().trim().toLowerCase();
        if (texte.isEmpty()) {
            toutAfficher();
            return;
        }
        classesAffichees = controllerClasse.listerClasses().stream()
                .filter(c -> c.getLibelle() != null && c.getLibelle().toLowerCase().contains(texte))
                .collect(Collectors.toList());
        vue.afficher(classesAffichees);
    }

    private void surSelectionLigne(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int ligne = vue.getTable().getSelectedRow();
        if (ligne >= 0 && ligne < classesAffichees.size()) {
            vue.remplir(classesAffichees.get(ligne));
        }
    }

    private void enregistrer() {
        try {
            String code = vue.getChampCode().getText().trim();
            String libelle = vue.getChampLibelle().getText().trim();
            String niveau = vue.getChampNiveau().getText().trim();
            Maitre maitre = (Maitre) vue.getComboMaitre().getSelectedItem();

            if (code.isEmpty() || libelle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le code et le libelle sont obligatoires.",
                        "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (maitre == null) {
                JOptionPane.showMessageDialog(this, "Veuillez selectionner un maitre.",
                        "Champ manquant", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean existe = controllerClasse.listerClasses().stream()
                    .anyMatch(c -> c.getCode().equals(code));

            Classe classe = new Classe();
            classe.setCode(code);
            classe.setLibelle(libelle);
            classe.setNiveau(niveau);
            classe.setMaitre(maitre);

            if (existe) {
                controllerClasse.modifierClasse(classe);
            } else {
                controllerClasse.ajouterClasse(classe);
            }

            vue.reinitialiser();
            toutAfficher();

        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        String code = vue.getChampCode().getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez d'abord une classe.",
                    "Aucune selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer la classe " + code + " ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) return;

        try {
            controllerClasse.supprimerClasse(code);
            vue.reinitialiser();
            toutAfficher();
        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("classes.csv"));
        int resultat = chooser.showSaveDialog(this);
        if (resultat != JFileChooser.APPROVE_OPTION) return;

        try {
            List<String[]> lignes = classesAffichees.stream()
                    .map(c -> new String[]{
                            c.getCode(), c.getLibelle(),
                            c.getNiveau() == null ? "" : c.getNiveau(),
                            c.getMaitre() == null ? "" : c.getMaitre().toString()
                    })
                    .collect(Collectors.toList());

            CsvExporter.exporter(chooser.getSelectedFile(),
                    new String[]{"Code", "Libelle", "Niveau", "Maitre"},
                    lignes);

            JOptionPane.showMessageDialog(this, "Export reussi.", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur export : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
