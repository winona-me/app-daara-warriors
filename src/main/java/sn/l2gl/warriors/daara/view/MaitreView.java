package sn.l2gl.warriors.daara.view;

import lombok.Getter;
import sn.l2gl.warriors.daara.model.models.Maitre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vue (JPanel) de gestion des maitres. Ne contient AUCUN acces base de
 * donnees ni logique metier : uniquement des composants Swing et des
 * methodes d'affichage (afficher / remplir / reinitialiser).
 */
@Getter
public class MaitreView extends JPanel {

    private final JTextField champMatricule = new JTextField(12);
    private final JTextField champPrenom = new JTextField(15);
    private final JTextField champNom = new JTextField(15);
    private final JTextField champTelephone = new JTextField(15);
    private final JTextField champSpecialite = new JTextField(15);

    private final JTextField champRecherche = new JTextField(20);
    private final JButton boutonChercher = new JButton("Rechercher");
    private final JButton boutonToutAfficher = new JButton("Tout afficher");

    private final JButton boutonEnregistrer = new JButton("Enregistrer");
    private final JButton boutonSupprimer = new JButton("Supprimer");
    private final JButton boutonNouveau = new JButton("Nouveau");
    private final JButton boutonExporter = new JButton("Exporter CSV");

    private final DefaultTableModel modeleTable =
            new DefaultTableModel(new Object[]{"Matricule", "Prenom", "Nom", "Telephone", "Specialite"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
    private final JTable table = new JTable(modeleTable);

    public MaitreView() {
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
        JPanel formulaire = new JPanel(new GridLayout(2, 5, 8, 8));
        formulaire.setBorder(BorderFactory.createTitledBorder("Fiche maitre"));

        formulaire.add(new JLabel("Matricule :"));
        formulaire.add(new JLabel("Prenom :"));
        formulaire.add(new JLabel("Nom :"));
        formulaire.add(new JLabel("Telephone :"));
        formulaire.add(new JLabel("Specialite :"));

        formulaire.add(champMatricule);
        formulaire.add(champPrenom);
        formulaire.add(champNom);
        formulaire.add(champTelephone);
        formulaire.add(champSpecialite);

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

    /** Affiche une liste de maitres dans la table. */
    public void afficher(List<Maitre> maitres) {
        modeleTable.setRowCount(0);
        for (Maitre m : maitres) {
            modeleTable.addRow(new Object[]{
                    m.getMatricule(), m.getPrenom(), m.getNom(), m.getTelephone(), m.getSpecialite()
            });
        }
    }

    /** Charge un maitre dans le formulaire (cas modification). */
    public void remplir(Maitre maitre) {
        champMatricule.setText(maitre.getMatricule());
        champMatricule.setEditable(false); // la cle saisie n'est pas modifiable
        champPrenom.setText(maitre.getPrenom());
        champNom.setText(maitre.getNom());
        champTelephone.setText(maitre.getTelephone() == null ? "" : maitre.getTelephone());
        champSpecialite.setText(maitre.getSpecialite() == null ? "" : maitre.getSpecialite());
    }

    /** Vide le formulaire (cas creation). */
    public void reinitialiser() {
        champMatricule.setText("");
        champMatricule.setEditable(true);
        champPrenom.setText("");
        champNom.setText("");
        champTelephone.setText("");
        champSpecialite.setText("");
        table.clearSelection();
    }
}
