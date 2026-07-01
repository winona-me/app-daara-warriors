package sn.l2gl.warriors.daara.model.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entite Talibe (eleve). Rattache obligatoirement a une Classe et possede
 * un historique de Progressions dans la memorisation du Coran.
 */
@Entity
@Table(name = "talibes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Talibe {

    @Id
    @Column(length = 20, unique = true, nullable = false)
    private String matricule;

    @Column(nullable = false, length = 80)
    private String prenom;

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "nom_tuteur", length = 120)
    private String nomTuteur;

    @Column(name = "telephone_tuteur", length = 20)
    private String telephoneTuteur;

    /**
     * Relation obligatoire : un talibe appartient toujours a une classe.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "classe_code", nullable = false)
    private Classe classe;

    /**
     * Suppression d'un talibe => suppression en cascade de ses progressions
     * (regle metier explicite de l'enonce).
     */
    @OneToMany(mappedBy = "talibe", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progression> progressions = new ArrayList<>();

    @Override
    public String toString() {
        return matricule + " - " + prenom + " " + nom;
    }
}