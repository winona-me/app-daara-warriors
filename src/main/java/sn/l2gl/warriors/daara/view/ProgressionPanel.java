package sn.l2gl.warriors.daara.view;

import sn.l2gl.warriors.daara.controller.ControllerProgression;
import sn.l2gl.warriors.daara.controller.ControllerTalibe;
import sn.l2gl.warriors.daara.exception.DaaraException;
import sn.l2gl.warriors.daara.model.models.Progression;
import sn.l2gl.warriors.daara.model.models.Talibe;
import sn.l2gl.warriors.daara.util.CsvExporter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel (JPanel) de gestion des progressions : relie ProgressionView
 * (Swing pur) a ControllerProgression (CRUD) et a ControllerTalibe
 * (alimentation de la liste deroulante des talibes et du filtre).
 */
public class ProgressionPanel extends JPanel {

    private final ControllerProgression controllerProgression;
    private final ControllerTalibe controllerTalibe;
    private final ProgressionView vue = new ProgressionView();

    private List<Progression> progressionsAffichees = new ArrayList<>();

    public ProgressionPanel(ControllerProgression controllerProgression, ControllerTalibe controllerTalibe) {
        this.controllerProgression = controllerProgression;
        this.controllerTalibe = controllerTalibe;

        setLayout(new BorderLayout());
        add(vue, BorderLayout.CENTER);

        chargerTalibesDansCombo();
        toutAfficher();

        vue.getBoutonToutAfficher().addActionListener(e -> toutAfficher());
        vue.getBoutonFiltrer().addActionListener(e -> filtrerParTalibe());
        vue.getBoutonNouveau().addActionListener(e -> vue.reinitialiser());
        vue.getBoutonEnregistrer().addActionListener(e -> enregistrer());
        vue.getBoutonSupprimer().addActionListener(e -> supprimer());
        vue.getBoutonExporter().addActionListener(e -> exporter());

        vue.getTable().getSelectionModel().addListSelectionListener(this::surSelectionLigne);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                chargerTalibesDansCombo();
                toutAfficher();
            }
        });
    }

    private void chargerTalibesDansCombo() {
        vue.chargerTalibes(controllerTalibe.listerTalibes());
    }

    private void toutAfficher() {
        progressionsAffichees = controllerProgression.listerProgressions();
        vue.afficher(progressionsAffichees);
    }

    private void filtrerParTalibe() {
        Talibe talibe = (Talibe) vue.getComboFiltreTalibe().getSelectedItem();
        if (talibe == null) {
            toutAfficher();
            return;
        }
        progressionsAffichees = controllerProgression.listerProgressions().stream()
                .filter(p -> p.getTalibe() != null
                        && p.getTalibe().getMatricule().equals(talibe.getMatricule()))
                .collect(Collectors.toList());
        vue.afficher(progressionsAffichees);
    }

    private void surSelectionLigne(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int ligne = vue.getTable().getSelectedRow();
        if (ligne >= 0 && ligne < progressionsAffichees.size()) {
            vue.remplir(progressionsAffichees.get(ligne));
        }
    }

    private void enregistrer() {
        try {
            String idTexte = vue.getChampId().getText().trim();
            String sourate = vue.getChampSourate().getText().trim();
            String nbTexte = vue.getChampNombreVersets().getText().trim();
            String dateTexte = vue.getChampDateEvaluation().getText().trim();
            String observation = vue.getChampObservation().getText().trim();
            Talibe talibe = (Talibe) vue.getComboTalibe().getSelectedItem();

            if (sourate.isEmpty() || nbTexte.isEmpty() || dateTexte.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Sourate, nombre de versets et date sont obligatoires.",
                        "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (talibe == null) {
                JOptionPane.showMessageDialog(this, "Veuillez selectionner un talibe.",
                        "Champ manquant", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int nombreVersets;
            try {
                nombreVersets = Integer.parseInt(nbTexte);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le nombre de versets doit etre un entier.",
                        "Format invalide", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dateEvaluation;
            try {
                dateEvaluation = LocalDate.parse(dateTexte);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Date invalide (format attendu AAAA-MM-JJ).",
                        "Format invalide", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Progression progression = new Progression();
            progression.setSourate(sourate);
            progression.setNombreVersets(nombreVersets);
            progression.setDateEvaluation(dateEvaluation);
            progression.setObservation(observation);
            progression.setTalibe(talibe);

            if (!idTexte.isEmpty()) {
                progression.setId(Integer.parseInt(idTexte));
                controllerProgression.modifierProgression(progression);
            } else {
                controllerProgression.ajouterProgression(progression);
            }

            vue.reinitialiser();
            toutAfficher();

        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        String idTexte = vue.getChampId().getText().trim();
        if (idTexte.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez d'abord une progression.",
                    "Aucune selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer la progression #" + idTexte + " ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) return;

        try {
            controllerProgression.supprimerProgression(Integer.parseInt(idTexte));
            vue.reinitialiser();
            toutAfficher();
        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("progressions.csv"));
        int resultat = chooser.showSaveDialog(this);
        if (resultat != JFileChooser.APPROVE_OPTION) return;

        try {
            List<String[]> lignes = progressionsAffichees.stream()
                    .map(p -> new String[]{
                            String.valueOf(p.getId()), p.getSourate(),
                            String.valueOf(p.getNombreVersets()),
                            p.getDateEvaluation() == null ? "" : p.getDateEvaluation().toString(),
                            p.getTalibe() == null ? "" : p.getTalibe().toString(),
                            p.getObservation() == null ? "" : p.getObservation()
                    })
                    .collect(Collectors.toList());

            CsvExporter.exporter(chooser.getSelectedFile(),
                    new String[]{"ID", "Sourate", "Nb versets", "Date", "Talibe", "Observation"},
                    lignes);

            JOptionPane.showMessageDialog(this, "Export reussi.", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur export : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
