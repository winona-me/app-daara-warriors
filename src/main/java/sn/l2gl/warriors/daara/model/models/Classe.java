package sn.l2gl.warriors.daara.model.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entite Classe (halqa). Une classe est obligatoirement encadree par un Maitre
 * et regroupe plusieurs Talibes.
 */
@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classe {

    @Id
    @Column(length = 20, unique = true, nullable = false)
    private String code;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(length = 30)
    private String niveau;

    /**
     * Relation obligatoire : une classe ne peut pas exister sans maitre.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "maitre_matricule", nullable = false)
    private Maitre maitre;

    @OneToMany(mappedBy = "classe", fetch = FetchType.LAZY)
    private List<Talibe> talibes = new ArrayList<>();

    @Override
    public String toString() {
        return code + " - " + libelle;
    }
}