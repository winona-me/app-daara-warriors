package sn.l2gl.warriors.daara.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * CsvExporter — Classe utilitaire pour l'export CSV.
 *
 * Rôle : écrire un fichier CSV à partir d'une liste de lignes.
 * Elle est dans "util" car c'est un outil transversal,
 * utilisé par tous les contrôleurs.
 * Elle ne doit JAMAIS être appelée depuis la Vue.
 */
public class CsvExporter {

    // Constructeur privé : cette classe ne s'instancie pas.
    // Toutes ses méthodes sont statiques (outils).
    private CsvExporter() {}

    /**
     * Exporte une liste de données dans un fichier CSV.
     *
     * @param fichier  Le fichier de destination choisi par l'utilisateur
     * @param entetes  La ligne d'en-tête (noms des colonnes)
     * @param lignes   Les lignes de données
     * @throws IOException si l'écriture échoue
     */
    public static void exporter(File fichier,
                                String[] entetes,
                                List<String[]> lignes) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                fichier.toPath(), StandardCharsets.UTF_8)) {

            // Écriture de la ligne d'en-tête
            writer.write(String.join(",", entetes));
            writer.newLine();

            // Écriture de chaque ligne de données
            for (String[] ligne : lignes) {
                writer.write(String.join(",", ligne));
                writer.newLine();
            }
        }
    }
}