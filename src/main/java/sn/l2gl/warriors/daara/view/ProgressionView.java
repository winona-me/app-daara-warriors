package sn.l2gl.warriors.daara.view;

import lombok.Getter;
import sn.l2gl.warriors.daara.model.models.Progression;
import sn.l2gl.warriors.daara.model.models.Talibe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vue (JPanel) de gestion des progressions de memorisation du Coran.
 * Permet aussi de filtrer la liste par talibe (JComboBox).
 */
@Getter
public class ProgressionView extends JPanel {

    private final JTextField champId = new JTextField(8);
    private final JTextField champSourate = new JTextField(15);
    private final JTextField champNombreVersets = new JTextField(6);
    private final JTextField champDateEvaluation = new JTextField(10); // AAAA-MM-JJ
    private final JTextField champObservation = new JTextField(20);
    private final JComboBox<Talibe> comboTalibe = new JComboBox<>();

    private final JComboBox<Talibe> comboFiltreTalibe = new JComboBox<>();
    private final JButton boutonFiltrer = new JButton("Filtrer par talibe");
    private final JButton boutonToutAfficher = new JButton("Tout afficher");

    private final JButton boutonEnregistrer = new JButton("Enregistrer");
    private final JButton boutonSupprimer = new JButton("Supprimer");
    private final JButton boutonNouveau = new JButton("Nouveau");
    private final JButton boutonExporter = new JButton("Exporter CSV");

    private final DefaultTableModel modeleTable = new DefaultTableModel(
            new Object[]{"ID", "Sourate", "Nb versets", "Date", "Talibe", "Observation"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(modeleTable);

    public ProgressionView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirePanneauFiltre(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(construirePanneauFormulaire(), BorderLayout.SOUTH);
    }

    private JPanel construirePanneauFiltre() {
        JPanel panneau = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panneau.setBorder(BorderFactory.createTitledBorder("Filtrer par talibe"));
        panneau.add(new JLabel("Talibe :"));
        panneau.add(comboFiltreTalibe);
        panneau.add(boutonFiltrer);
        panneau.add(boutonToutAfficher);
        return panneau;
    }

    private JPanel construirePanneauFormulaire() {
        JPanel formulaire = new JPanel(new GridLayout(2, 5, 8, 8));
        formulaire.setBorder(BorderFactory.createTitledBorder("Fiche progression"));

        formulaire.add(new JLabel("ID (auto) :"));
        formulaire.add(new JLabel("Sourate :"));
        formulaire.add(new JLabel("Nb versets :"));
        formulaire.add(new JLabel("Date (AAAA-MM-JJ) :"));
        formulaire.add(new JLabel("Observation :"));

        champId.setEditable(false);
        formulaire.add(champId);
        formulaire.add(champSourate);
        formulaire.add(champNombreVersets);
        formulaire.add(champDateEvaluation);
        formulaire.add(champObservation);

        JPanel ligneTalibe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligneTalibe.add(new JLabel("Talibe :"));
        ligneTalibe.add(comboTalibe);

        JPanel conteneur = new JPanel(new BorderLayout(8, 8));
        conteneur.add(formulaire, BorderLayout.CENTER);
        conteneur.add(ligneTalibe, BorderLayout.NORTH);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutons.add(boutonNouveau);
        boutons.add(boutonEnregistrer);
        boutons.add(boutonSupprimer);
        boutons.add(boutonExporter);
        conteneur.add(boutons, BorderLayout.SOUTH);

        return conteneur;
    }

    /** Alimente a la fois la combo du formulaire et celle du filtre. */
    public void chargerTalibes(List<Talibe> talibes) {
        comboTalibe.removeAllItems();
        comboFiltreTalibe.removeAllItems();
        for (Talibe t : talibes) {
            comboTalibe.addItem(t);
            comboFiltreTalibe.addItem(t);
        }
    }

    public void afficher(List<Progression> progressions) {
        modeleTable.setRowCount(0);
        for (Progression p : progressions) {
            modeleTable.addRow(new Object[]{
                    p.getId(), p.getSourate(), p.getNombreVersets(),
                    p.getDateEvaluation() == null ? "" : p.getDateEvaluation().toString(),
                    p.getTalibe() == null ? "" : p.getTalibe().toString(),
                    p.getObservation()
            });
        }
    }

    public void remplir(Progression progression) {
        champId.setText(String.valueOf(progression.getId()));
        champSourate.setText(progression.getSourate());
        champNombreVersets.setText(String.valueOf(progression.getNombreVersets()));
        champDateEvaluation.setText(
                progression.getDateEvaluation() == null ? "" : progression.getDateEvaluation().toString());
        champObservation.setText(progression.getObservation() == null ? "" : progression.getObservation());
        if (progression.getTalibe() != null) {
            for (int i = 0; i < comboTalibe.getItemCount(); i++) {
                if (comboTalibe.getItemAt(i).getMatricule().equals(progression.getTalibe().getMatricule())) {
                    comboTalibe.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void reinitialiser() {
        champId.setText("");
        champSourate.setText("");
        champNombreVersets.setText("");
        champDateEvaluation.setText("");
        champObservation.setText("");
        if (comboTalibe.getItemCount() > 0) {
            comboTalibe.setSelectedIndex(0);
        }
        table.clearSelection();
    }
}
