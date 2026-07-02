package sn.l2gl.warriors.daara.view;

import lombok.Getter;
import sn.l2gl.warriors.daara.model.models.Classe;
import sn.l2gl.warriors.daara.model.models.Maitre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vue (JPanel) de gestion des classes (halqas).
 * Le champ "maitre" est une liste deroulante (JComboBox) alimentee
 * depuis le controleur, jamais une saisie libre.
 */
@Getter
public class ClasseView extends JPanel {

    private final JTextField champCode = new JTextField(12);
    private final JTextField champLibelle = new JTextField(18);
    private final JTextField champNiveau = new JTextField(12);
    private final JComboBox<Maitre> comboMaitre = new JComboBox<>();

    private final JTextField champRecherche = new JTextField(20);
    private final JButton boutonChercher = new JButton("Rechercher");
    private final JButton boutonToutAfficher = new JButton("Tout afficher");

    private final JButton boutonEnregistrer = new JButton("Enregistrer");
    private final JButton boutonSupprimer = new JButton("Supprimer");
    private final JButton boutonNouveau = new JButton("Nouveau");
    private final JButton boutonExporter = new JButton("Exporter CSV");

    private final DefaultTableModel modeleTable =
            new DefaultTableModel(new Object[]{"Code", "Libelle", "Niveau", "Maitre"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
    private final JTable table = new JTable(modeleTable);

    public ClasseView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirePanneauRecherche(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(construirePanneauFormulaire(), BorderLayout.SOUTH);
    }

    private JPanel construirePanneauRecherche() {
        JPanel panneau = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panneau.setBorder(BorderFactory.createTitledBorder("Recherche par libelle"));
        panneau.add(new JLabel("Libelle :"));
        panneau.add(champRecherche);
        panneau.add(boutonChercher);
        panneau.add(boutonToutAfficher);
        return panneau;
    }

    private JPanel construirePanneauFormulaire() {
        JPanel formulaire = new JPanel(new GridLayout(2, 4, 8, 8));
        formulaire.setBorder(BorderFactory.createTitledBorder("Fiche classe"));

        formulaire.add(new JLabel("Code :"));
        formulaire.add(new JLabel("Libelle :"));
        formulaire.add(new JLabel("Niveau :"));
        formulaire.add(new JLabel("Maitre :"));

        formulaire.add(champCode);
        formulaire.add(champLibelle);
        formulaire.add(champNiveau);
        formulaire.add(comboMaitre);

        JPanel conteneur = new JPanel(new BorderLayout(8, 8));
        conteneur.add(formulaire, BorderLayout.CENTER);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutons.add(boutonNouveau);
        boutons.add(boutonEnregistrer);
        boutons.add(boutonSupprimer);
        boutons.add(boutonExporter);
        conteneur.add(boutons, BorderLayout.SOUTH);

        return conteneur;
    }

    /** Recharge la liste deroulante des maitres (appele par le controleur). */
    public void chargerMaitres(List<Maitre> maitres) {
        comboMaitre.removeAllItems();
        for (Maitre m : maitres) {
            comboMaitre.addItem(m);
        }
    }

    public void afficher(List<Classe> classes) {
        modeleTable.setRowCount(0);
        for (Classe c : classes) {
            modeleTable.addRow(new Object[]{
                    c.getCode(), c.getLibelle(), c.getNiveau(),
                    c.getMaitre() == null ? "" : c.getMaitre().toString()
            });
        }
    }

    public void remplir(Classe classe) {
        champCode.setText(classe.getCode());
        champCode.setEditable(false);
        champLibelle.setText(classe.getLibelle());
        champNiveau.setText(classe.getNiveau() == null ? "" : classe.getNiveau());
        if (classe.getMaitre() != null) {
            for (int i = 0; i < comboMaitre.getItemCount(); i++) {
                if (comboMaitre.getItemAt(i).getMatricule().equals(classe.getMaitre().getMatricule())) {
                    comboMaitre.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void reinitialiser() {
        champCode.setText("");
        champCode.setEditable(true);
        champLibelle.setText("");
        champNiveau.setText("");
        if (comboMaitre.getItemCount() > 0) {
            comboMaitre.setSelectedIndex(0);
        }
        table.clearSelection();
    }
}

