package sn.l2gl.warriors.daara.model.dao;

import java.util.List;
import java.util.Optional;


/**
 * Dao<T, ID> — Interface générique pour tous les DAOs.
 *
 * T  = le type de l'entité (Maitre, Classe, Talibe, Progression)
 * ID = le type de la clé primaire (String pour Maitre/Classe/Talibe,
 *      Long pour Progression)
 *
 * Chaque DAO concret implémente cette interface
 * et ajoute ses propres méthodes de recherche.
 */
public interface Dao<T, ID> {

    T inserer(T entity);           // Créer

    Optional<T> trouver(ID id);    // Lire un seul (recherche unique par clé)

    List<T> listerTous();          // Lire tous

    Optional<T> modifier(T entity); // Mettre à jour

    boolean supprimer(ID id);      // Supprimer
}