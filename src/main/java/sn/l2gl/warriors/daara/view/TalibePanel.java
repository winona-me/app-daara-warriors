package sn.l2gl.warriors.daara.view;

import sn.l2gl.warriors.daara.controller.ControllerClasse;
import sn.l2gl.warriors.daara.controller.ControllerTalibe;
import sn.l2gl.warriors.daara.exception.DaaraException;
import sn.l2gl.warriors.daara.model.models.Classe;
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
 * Panel (JPanel) de gestion des talibes : relie TalibeView (Swing pur)
 * a ControllerTalibe (CRUD) et a ControllerClasse (alimentation de la
 * liste deroulante des classes, obligatoire pour chaque talibe).
 */
public class TalibePanel extends JPanel {

    private final ControllerTalibe controllerTalibe;
    private final ControllerClasse controllerClasse;
    private final TalibeView vue = new TalibeView();

    private List<Talibe> talibesAffiches = new ArrayList<>();
    private String matriculeSelectionne = null;

    public TalibePanel(ControllerTalibe controllerTalibe, ControllerClasse controllerClasse) {
        this.controllerTalibe = controllerTalibe;
        this.controllerClasse = controllerClasse;

        setLayout(new BorderLayout());
        add(vue, BorderLayout.CENTER);

        chargerClassesDansCombo();
        toutAfficher();

        vue.getBoutonToutAfficher().addActionListener(e -> toutAfficher());
        vue.getBoutonChercher().addActionListener(e -> rechercher());
        vue.getBoutonNouveau().addActionListener(e -> {
            vue.reinitialiser();
            matriculeSelectionne = null;
        });
        vue.getBoutonEnregistrer().addActionListener(e -> enregistrer());
        vue.getBoutonSupprimer().addActionListener(e -> supprimer());
        vue.getBoutonExporter().addActionListener(e -> exporter());

        vue.getTable().getSelectionModel().addListSelectionListener(this::surSelectionLigne);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                chargerClassesDansCombo();
                toutAfficher();
            }
        });
    }

    private void chargerClassesDansCombo() {
        vue.chargerClasses(controllerClasse.listerClasses());
    }

    private void toutAfficher() {
        talibesAffiches = controllerTalibe.listerTalibes();
        vue.afficher(talibesAffiches);
    }

    private void rechercher() {
        String texte = vue.getChampRecherche().getText().trim().toLowerCase();
        if (texte.isEmpty()) {
            toutAfficher();
            return;
        }
        talibesAffiches = controllerTalibe.listerTalibes().stream()
                .filter(t -> (t.getNom() != null && t.getNom().toLowerCase().contains(texte))
                        || (t.getPrenom() != null && t.getPrenom().toLowerCase().contains(texte)))
                .collect(Collectors.toList());
        vue.afficher(talibesAffiches);
    }

    private void surSelectionLigne(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int ligne = vue.getTable().getSelectedRow();
        if (ligne >= 0 && ligne < talibesAffiches.size()) {
            Talibe t = talibesAffiches.get(ligne);
            vue.remplir(t);
            matriculeSelectionne = t.getMatricule();
        }
    }

    private void enregistrer() {
        try {
            String matricule = vue.getChampMatricule().getText().trim();
            String prenom = vue.getChampPrenom().getText().trim();
            String nom = vue.getChampNom().getText().trim();
            String dateTexte = vue.getChampDateNaissance().getText().trim();
            String nomTuteur = vue.getChampNomTuteur().getText().trim();
            String telephoneTuteur = vue.getChampTelephoneTuteur().getText().trim();
            Classe classe = (Classe) vue.getComboClasse().getSelectedItem();

            if (matricule.isEmpty() || prenom.isEmpty() || nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Matricule, prenom et nom sont obligatoires.",
                        "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (classe == null) {
                JOptionPane.showMessageDialog(this, "Veuillez selectionner une classe.",
                        "Champ manquant", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dateNaissance = null;
            if (!dateTexte.isEmpty()) {
                try {
                    dateNaissance = LocalDate.parse(dateTexte);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Date de naissance invalide (format attendu AAAA-MM-JJ).",
                            "Format invalide", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            boolean existe = controllerTalibe.listerTalibes().stream()
                    .anyMatch(t -> t.getMatricule().equals(matricule));

            Talibe talibe = new Talibe();
            talibe.setMatricule(matricule);
            talibe.setPrenom(prenom);
            talibe.setNom(nom);
            talibe.setDateNaissance(dateNaissance);
            talibe.setNomTuteur(nomTuteur);
            talibe.setTelephoneTuteur(telephoneTuteur);
            talibe.setClasse(classe);

            if (existe) {
                boolean modeModification = matriculeSelectionne != null && matriculeSelectionne.equals(matricule);

                if (modeModification) {
                    controllerTalibe.modifierTalibe(talibe);
                } else {
                    controllerTalibe.ajouterTalibe(talibe);
                }

                vue.reinitialiser();
                matriculeSelectionne = null;
                toutAfficher();
            } else {
                controllerTalibe.ajouterTalibe(talibe);
            }

            vue.reinitialiser();
            toutAfficher();

        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        String matricule = vue.getChampMatricule().getText().trim();
        if (matricule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez d'abord un talibe.",
                    "Aucune selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer le talibe " + matricule
                        + " ? (ses progressions seront aussi supprimees)", "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) return;

        try {
            controllerTalibe.supprimerTalibe(matricule);
            vue.reinitialiser();
            toutAfficher();
        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("talibes.csv"));
        int resultat = chooser.showSaveDialog(this);
        if (resultat != JFileChooser.APPROVE_OPTION) return;

        try {
            List<String[]> lignes = talibesAffiches.stream()
                    .map(t -> new String[]{
                            t.getMatricule(), t.getPrenom(), t.getNom(),
                            t.getDateNaissance() == null ? "" : t.getDateNaissance().toString(),
                            t.getClasse() == null ? "" : t.getClasse().toString(),
                            t.getNomTuteur() == null ? "" : t.getNomTuteur()
                    })
                    .collect(Collectors.toList());

            CsvExporter.exporter(chooser.getSelectedFile(),
                    new String[]{"Matricule", "Prenom", "Nom", "Naissance", "Classe", "Tuteur"},
                    lignes);

            JOptionPane.showMessageDialog(this, "Export reussi.", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur export : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
