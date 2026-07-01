package sn.l2gl.warriors.daara.model.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entite Maitre (serigne) qui encadre une ou plusieurs classes (halqas).
 * La cle (matricule) est saisie par l'utilisateur, elle n'est pas auto-generee.
 */
@Entity
@Table(name = "maitres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maitre {

    @Id
    @Column(length = 20, unique = true, nullable = false)
    private String matricule;

    @Column(nullable = false, length = 80)
    private String prenom;

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(length = 20)
    private String telephone;

    @Column(length = 120)
    private String specialite;

    /**
     * Classes encadrees par ce maitre. Cote inverse de la relation,
     * ne genere pas de colonne supplementaire (mappedBy).
     */
    @OneToMany(mappedBy = "maitre", fetch = FetchType.LAZY)
    private List<Classe> classes = new ArrayList<>();

    @Override
    public String toString() {
        // Utilise notamment pour l'affichage dans les JComboBox
        return matricule + " - " + prenom + " " + nom;
    }
}