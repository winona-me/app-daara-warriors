package sn.l2gl.warriors.daara.view;

import lombok.Getter;
import sn.l2gl.warriors.daara.model.models.Classe;
import sn.l2gl.warriors.daara.model.models.Talibe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vue (JPanel) de gestion des talibes.
 * Le champ "classe" est une liste deroulante alimentee depuis le controleur.
 *
 * NOTE : pour rester strictement dans les contraintes de l'enonce (Swing
 * uniquement, sans bibliotheque tierce d'UI), la date de naissance est
 * saisie via un simple JTextField au format AAAA-MM-JJ plutot qu'avec un
 * composant externe de type JDateChooser.
 */
@Getter
public class TalibeView extends JPanel {

    private final JTextField champMatricule = new JTextField(12);
    private final JTextField champPrenom = new JTextField(15);
    private final JTextField champNom = new JTextField(15);
    private final JTextField champDateNaissance = new JTextField(10); // format AAAA-MM-JJ
    private final JTextField champNomTuteur = new JTextField(15);
    private final JTextField champTelephoneTuteur = new JTextField(15);
    private final JComboBox<Classe> comboClasse = new JComboBox<>();

    private final JTextField champRecherche = new JTextField(20);
    private final JButton boutonChercher = new JButton("Rechercher");
    private final JButton boutonToutAfficher = new JButton("Tout afficher");

    private final JButton boutonEnregistrer = new JButton("Enregistrer");
    private final JButton boutonSupprimer = new JButton("Supprimer");
    private final JButton boutonNouveau = new JButton("Nouveau");
    private final JButton boutonExporter = new JButton("Exporter CSV");

    private final DefaultTableModel modeleTable = new DefaultTableModel(
            new Object[]{"Matricule", "Prenom", "Nom", "Naissance", "Classe", "Tuteur"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(modeleTable);

    public TalibeView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirePanneauRecherche(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(construirePanneauFormulaire(), BorderLayout.SOUTH);
    }

    private JPanel construirePanneauRecherche() {
        JPanel panneau = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panneau.setBorder(BorderFactory.createTitledBorder("Recherche par nom"));
        panneau.add(new JLabel("Nom :"));
        panneau.add(champRecherche);
        panneau.add(boutonChercher);
        panneau.add(boutonToutAfficher);
        return panneau;
    }

    private JPanel construirePanneauFormulaire() {
        JPanel formulaire = new JPanel(new GridLayout(2, 6, 8, 8));
        formulaire.setBorder(BorderFactory.createTitledBorder("Fiche talibe"));

        formulaire.add(new JLabel("Matricule :"));
        formulaire.add(new JLabel("Prenom :"));
        formulaire.add(new JLabel("Nom :"));
        formulaire.add(new JLabel("Naissance (AAAA-MM-JJ) :"));
        formulaire.add(new JLabel("Tuteur :"));
        formulaire.add(new JLabel("Tel. tuteur :"));

        formulaire.add(champMatricule);
        formulaire.add(champPrenom);
        formulaire.add(champNom);
        formulaire.add(champDateNaissance);
        formulaire.add(champNomTuteur);
        formulaire.add(champTelephoneTuteur);

        JPanel ligneClasse = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligneClasse.add(new JLabel("Classe :"));
        ligneClasse.add(comboClasse);

        JPanel conteneur = new JPanel(new BorderLayout(8, 8));
        conteneur.add(formulaire, BorderLayout.CENTER);
        conteneur.add(ligneClasse, BorderLayout.NORTH);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutons.add(boutonNouveau);
        boutons.add(boutonEnregistrer);
        boutons.add(boutonSupprimer);
        boutons.add(boutonExporter);
        conteneur.add(boutons, BorderLayout.SOUTH);

        return conteneur;
    }

    public void chargerClasses(List<Classe> classes) {
        comboClasse.removeAllItems();
        for (Classe c : classes) {
            comboClasse.addItem(c);
        }
    }

    public void afficher(List<Talibe> talibes) {
        modeleTable.setRowCount(0);
        for (Talibe t : talibes) {
            modeleTable.addRow(new Object[]{
                    t.getMatricule(), t.getPrenom(), t.getNom(),
                    t.getDateNaissance() == null ? "" : t.getDateNaissance().toString(),
                    t.getClasse() == null ? "" : t.getClasse().toString(),
                    t.getNomTuteur()
            });
        }
    }

    public void remplir(Talibe talibe) {
        champMatricule.setText(talibe.getMatricule());
        champMatricule.setEditable(false);
        champPrenom.setText(talibe.getPrenom());
        champNom.setText(talibe.getNom());
        champDateNaissance.setText(talibe.getDateNaissance() == null ? "" : talibe.getDateNaissance().toString());
        champNomTuteur.setText(talibe.getNomTuteur() == null ? "" : talibe.getNomTuteur());
        champTelephoneTuteur.setText(talibe.getTelephoneTuteur() == null ? "" : talibe.getTelephoneTuteur());
        if (talibe.getClasse() != null) {
            for (int i = 0; i < comboClasse.getItemCount(); i++) {
                if (comboClasse.getItemAt(i).getCode().equals(talibe.getClasse().getCode())) {
                    comboClasse.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void reinitialiser() {
        champMatricule.setText("");
        champMatricule.setEditable(true);
        champPrenom.setText("");
        champNom.setText("");
        champDateNaissance.setText("");
        champNomTuteur.setText("");
        champTelephoneTuteur.setText("");
        if (comboClasse.getItemCount() > 0) {
            comboClasse.setSelectedIndex(0);
        }
        table.clearSelection();
    }
}
