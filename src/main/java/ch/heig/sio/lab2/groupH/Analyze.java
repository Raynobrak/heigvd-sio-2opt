package ch.heig.sio.lab2.groupH;

import ch.heig.sio.lab2.tsp.RandomTour;
import ch.heig.sio.lab2.tsp.TspConstructiveHeuristic;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

import java.io.FileNotFoundException;

/**
 * Analyse les heuristiques constructives et d'amélioration
 * @Author Group H - Ancay Rémi, Charbonnier Lucas
 */
public final class Analyze {
  private static final Integer OPTIMISATIONS_COUNT = 50; // Nombre d'optimisations à effectuer pour chaque heuristique
  private static final Integer MAX_ITERATIONS_COUNT = 999999999; // Nombre d'itérations maximum pour l'heuristique 2-opt

  /**
   * Classe interne pour stocker les statistiques d'amélioration
   */
  private static class ImprovementStatistics {
    public long minLengthBeforeOpt = Integer.MAX_VALUE; // Longueur minimale avant optimisation
    public long maxLengthBeforeOpt = Integer.MIN_VALUE; // Longueur maximale avant optimisation
    public long sumOfLengthBeforeOpt = 0;               // Somme des longueurs avant optimisation
    public long meanLengthBeforeOpt = 0;                // Moyenne des longueurs avant optimisation

    public long minLengthAfterOpt = Integer.MAX_VALUE;  // Longueur minimale après optimisation
    public long maxLengthAfterOpt = Integer.MIN_VALUE;  // Longueur maximale après optimisation
    public long sumOfLengthAfterOpt = 0;                // Somme des longueurs après optimisation
    public long meanLengthAfterOpt = 0;                 // Moyenne des longueurs après optimisation

    public long sumOfElapsedTimes = 0;                  // Somme des temps d'exécution
    public long meanElapsedTime = 0;                    // Moyenne des temps

    /**
     * Met à jour les statistiques
     * @param lengthBeforeOpt longueur avant optimisation
     * @param lengthAfterOpt longueur après optimisation
     * @param elapsedTime temps d'exécution
     */
    public void updateStatistics(long lengthBeforeOpt, long lengthAfterOpt, long elapsedTime) {
      minLengthBeforeOpt = Math.min(minLengthBeforeOpt, lengthBeforeOpt);
      maxLengthBeforeOpt = Math.max(maxLengthBeforeOpt, lengthBeforeOpt);
      sumOfLengthBeforeOpt += lengthBeforeOpt;
      meanLengthBeforeOpt = sumOfLengthBeforeOpt / OPTIMISATIONS_COUNT;

      minLengthAfterOpt = Math.min(minLengthAfterOpt, lengthAfterOpt);
      maxLengthAfterOpt = Math.max(maxLengthAfterOpt, lengthAfterOpt);
      sumOfLengthAfterOpt += lengthAfterOpt;
      meanLengthAfterOpt = sumOfLengthAfterOpt / OPTIMISATIONS_COUNT;

      sumOfElapsedTimes += elapsedTime;
      meanElapsedTime = sumOfElapsedTimes / OPTIMISATIONS_COUNT;
    }

    /**
     * Convertit un nombre en pourcentage retourné en String
     * @param stat nombre à convertir
     * @param optimal valeur optimale
     * @return le nombre converti en pourcentage
     */
    private static String asPercentage(long stat, long optimal) {
      return String.format("%.2f", 100 * (stat - optimal) / (double)optimal);
    }

    /**
     * Affiche les statistiques en format ligne de tableau markdown
     * @param heuristicName nom de l'heuristique
     * @param optimalLength longueur optimale
     */
    public void printStatisticsMarkdown(String heuristicName, long optimalLength) {
      char bar = '|';
      System.out.print(bar + heuristicName + bar);
      System.out.print(meanElapsedTime + "" + bar);
      System.out.print(asPercentage(minLengthBeforeOpt, optimalLength) + bar);
      System.out.print(asPercentage(meanLengthBeforeOpt, optimalLength) + bar);
      System.out.print(asPercentage(maxLengthBeforeOpt, optimalLength) + bar);
      System.out.print(asPercentage(minLengthAfterOpt, optimalLength) + bar);
      System.out.print(asPercentage(meanLengthAfterOpt, optimalLength) + bar);
      System.out.print(asPercentage(maxLengthAfterOpt, optimalLength) + bar + '\n');
    }
  }

  /**
   * Mesure les statistiques d'amélioration pour une heuristique constructive
   * @param data données du problème
   * @param heuristic heuristique constructive
   * @param permutations permutations des villes
   * @return les statistiques d'amélioration
   */
  private static ImprovementStatistics measureStatistics(TspData data, TspConstructiveHeuristic heuristic, TspTour permutations) {
    ImprovementStatistics stats = new ImprovementStatistics();
    for(int i = 0; i < OPTIMISATIONS_COUNT; ++i) {
      long start = System.currentTimeMillis();

      var initialTour = heuristic.computeTour(data, permutations.tour().get(i)); // Crée une tournée initiale
      long lengthBeforeOpt = initialTour.length();

      var optimizedTour = new Tsp2optImprovementHeuristic(MAX_ITERATIONS_COUNT).computeTour(initialTour); // Optimise la tournée
      long lengthAfterOpt = optimizedTour.length();

      long elapsed = System.currentTimeMillis() - start;

      stats.updateStatistics(lengthBeforeOpt, lengthAfterOpt, elapsed); // Met à jour les statistiques
    }

    return stats;
  }

  /**
   * Analyse les heuristiques constructives et d'amélioration pour différents fichiers de données
   */
  public static void main(String[] args) throws FileNotFoundException {
    final String DATA_FOLDER = "data/"; // Dossier contenant les données
    final String DATA_EXTENSION = ".dat"; // Extension des fichiers de données

    String[] filenames = {"pcb442", "att532", "u574", "pcb1173", "nrw1379", "u1817"}; // Noms des fichiers de données
    Integer[] optimalLengths = {50778, 86729, 36905, 56892, 56638, 57201}; // Longueurs optimales des tournées

    for(int i = 0; i < filenames.length; ++i) { // Pour chaque fichier de données
      var filename = filenames[i];
      var optimalLength = optimalLengths[i];
      TspData data = TspData.fromFile(DATA_FOLDER + filename + DATA_EXTENSION); // Charge les données du fichier

      RandomTour randomTourGenerator = new RandomTour(0x134DAE9); // Initialisation du générateur de tournée aléatoire
      RandomTour randomPermutationGenerator = new RandomTour(0x134DAE9); // Initialisation du générateur de permutation aléatoire pour le choix de la ville de départ dans les heuristiques
      var permutations = randomPermutationGenerator.computeTour(data, -1);

      System.out.println("## Données pour le fichier : " + filename + "(longueur optimale : " + optimalLength + ")");
      System.out.println("|Initialisation|Temps moyen (ms)|Min avant opt (%)|Moyenne avant opt (%)|Max avant opt (%)|Min après opt (%)|Moyenne après opt (%)|Max après opt (%)|");
      System.out.println("|-|-|-|-|-|-|-|-|");
      var randomTourStats = measureStatistics(data, randomTourGenerator, permutations); // Mesure les statistiques pour la tournée aléatoire
      randomTourStats.printStatisticsMarkdown("RandomTour", optimalLength);
      var closestInsertionStats = measureStatistics(data, new DistanceBasedTourBuilder(false), permutations); // Mesure les statistiques pour l'insertion la plus proche
      closestInsertionStats.printStatisticsMarkdown("Insertion la plus proche", optimalLength);
      var furthestInsertionStats = measureStatistics(data, new DistanceBasedTourBuilder(true), permutations); // Mesure les statistiques pour l'insertion la plus éloignée
      furthestInsertionStats.printStatisticsMarkdown("Insertion la plus éloignée", optimalLength);
    }
  }
}
