package sn.l2gl.warriors.daara.model.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entite Progression : une evaluation de memorisation du Coran pour un talibe.
 * Contrairement aux 3 autres entites, sa cle est auto-generee (entier).
 */
@Entity
@Table(name = "progressions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Progression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String sourate;

    @Column(name = "nombre_versets", nullable = false)
    private Integer nombreVersets;

    @Column(name = "date_evaluation", nullable = false)
    private LocalDate dateEvaluation;

    @Column(length = 255)
    private String observation;

    /**
     * Relation obligatoire : une progression concerne toujours un talibe.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "talibe_matricule", nullable = false)
    private Talibe talibe;

    @Override
    public String toString() {
        return "Progression#" + id + " - " + sourate;
    }
}