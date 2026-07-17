package sn.l2gl.warriors.daara.view;

import sn.l2gl.warriors.daara.controller.ControllerMaitre;
import sn.l2gl.warriors.daara.exception.DaaraException;
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
 * Panel (JPanel) de gestion des maitres : relie MaitreView (Swing pur)
 * a ControllerMaitre (logique metier / DAO). Aucun acces base de donnees
 * ici, uniquement des appels au controleur.
 */
public class MaitrePanel extends JPanel {

    private final ControllerMaitre controllerMaitre;
    private final MaitreView vue = new MaitreView();
    private String matriculeSelectionne = null;   // null = mode "création"

    private List<Maitre> maitresAffiches = new ArrayList<>();

    public MaitrePanel(ControllerMaitre controllerMaitre) {
        this.controllerMaitre = controllerMaitre;

        setLayout(new BorderLayout());
        add(vue, BorderLayout.CENTER);

        toutAfficher();

        vue.getBoutonToutAfficher().addActionListener(e -> toutAfficher());
        vue.getBoutonChercher().addActionListener(e -> rechercher());
        vue.getBoutonNouveau().addActionListener(e -> {vue.reinitialiser();matriculeSelectionne = null;});  // retour en mode création
        vue.getBoutonEnregistrer().addActionListener(e -> enregistrer());
        vue.getBoutonSupprimer().addActionListener(e -> supprimer());
        vue.getBoutonExporter().addActionListener(e -> exporter());

        vue.getTable().getSelectionModel().addListSelectionListener(this::surSelectionLigne);

        // Recharge la liste a chaque fois que l'onglet redevient visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                toutAfficher();
            }
        });
    }

    private void toutAfficher() {
        maitresAffiches = controllerMaitre.listerMaitres();
        vue.afficher(maitresAffiches);
    }

    private void rechercher() {
        String texte = vue.getChampRecherche().getText().trim().toLowerCase();
        if (texte.isEmpty()) {
            toutAfficher();
            return;
        }
        maitresAffiches = controllerMaitre.listerMaitres().stream()
                .filter(m -> (m.getNom() != null && m.getNom().toLowerCase().contains(texte))
                        || (m.getPrenom() != null && m.getPrenom().toLowerCase().contains(texte)))
                .collect(Collectors.toList());
        vue.afficher(maitresAffiches);
    }

    private void surSelectionLigne(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int ligne = vue.getTable().getSelectedRow();
        if (ligne >= 0 && ligne < maitresAffiches.size()) {
            Maitre m = maitresAffiches.get(ligne);
            vue.remplir(m);
            matriculeSelectionne = m.getMatricule();   // ✅ on retient qu'on édite CE maître
        }
    }

    private void enregistrer() {
        try {
            String matricule = vue.getChampMatricule().getText().trim();
            String prenom = vue.getChampPrenom().getText().trim();
            String nom = vue.getChampNom().getText().trim();
            String telephone = vue.getChampTelephone().getText().trim();
            String specialite = vue.getChampSpecialite().getText().trim();

            if (matricule.isEmpty() || prenom.isEmpty() || nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Matricule, prenom et nom sont obligatoires.",
                        "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Maitre maitre = new Maitre();
            maitre.setMatricule(matricule);
            maitre.setPrenom(prenom);
            maitre.setNom(nom);
            maitre.setTelephone(telephone);
            maitre.setSpecialite(specialite);

            // ✅ modification SEULEMENT si on édite le même matricule qu'on avait sélectionné
            boolean modeModification = matriculeSelectionne != null
                    && matriculeSelectionne.equals(matricule);

            if (modeModification) {
                controllerMaitre.modifierMaitre(maitre);
            } else {
                controllerMaitre.ajouterMaitre(maitre);   // ✅ lèvera MaitreDejaExistantException si le matricule existe
            }

            vue.reinitialiser();
            matriculeSelectionne = null;
            toutAfficher();

        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        String matricule = vue.getChampMatricule().getText().trim();
        if (matricule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez d'abord un maitre.",
                    "Aucune selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer le maitre " + matricule + " ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) return;

        try {
            controllerMaitre.supprimerMaitre(matricule);
            vue.reinitialiser();
            toutAfficher();
        } catch (DaaraException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("maitres.csv"));
        int resultat = chooser.showSaveDialog(this);
        if (resultat != JFileChooser.APPROVE_OPTION) return;

        try {
            List<String[]> lignes = maitresAffiches.stream()
                    .map(m -> new String[]{
                            m.getMatricule(), m.getPrenom(), m.getNom(),
                            m.getTelephone() == null ? "" : m.getTelephone(),
                            m.getSpecialite() == null ? "" : m.getSpecialite()
                    })
                    .collect(Collectors.toList());

            CsvExporter.exporter(chooser.getSelectedFile(),
                    new String[]{"Matricule", "Prenom", "Nom", "Telephone", "Specialite"},
                    lignes);

            JOptionPane.showMessageDialog(this, "Export reussi.", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur export : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
