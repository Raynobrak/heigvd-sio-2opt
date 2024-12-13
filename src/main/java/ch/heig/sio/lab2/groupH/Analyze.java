package ch.heig.sio.lab2.groupH;

import ch.heig.sio.lab2.tsp.RandomTour;
import ch.heig.sio.lab2.tsp.TspConstructiveHeuristic;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

import java.io.FileNotFoundException;

public final class Analyze {
  private static final Integer OPTIMISATIONS_COUNT = 2; // todo : mettre 50
  private static final Integer MAX_ITERATIONS_COUNT = 999999999;

  private static class ImprovementStatistics {
    public long minLengthBeforeOpt = Integer.MAX_VALUE;
    public long maxLengthBeforeOpt = Integer.MIN_VALUE;
    public long sumOfLengthBeforeOpt = 0;
    public long meanLengthBeforeOpt = 0;
    public long minLengthAfterOpt = Integer.MAX_VALUE;
    public long maxLengthAfterOpt = Integer.MIN_VALUE;
    public long sumOfLengthAfterOpt = 0;
    public long meanLengthAfterOpt = 0;
    public long sumOfElapsedTimes = 0;
    public long meanElapsedTime = 0;

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

    private static String asPercentage(long stat, long optimal) {
      return String.format("%.2f", 100 * (stat - optimal) / (double)optimal);
    }

    public void printStatisticsMarkdown(String heuristicName, long optimalLength) {
      //|Initialisation|Temps moyen|Moyenne avant opt|Min avant opt|Max avant opt|Moyenne après opt|Min après opt|Max après opt|
      char bar = '|';
      System.out.print(bar + heuristicName + bar);
      System.out.print(meanElapsedTime + bar);
      System.out.print(asPercentage(minLengthBeforeOpt, optimalLength) + bar);
      System.out.print(asPercentage(meanLengthBeforeOpt, optimalLength) + bar);
      System.out.print(asPercentage(maxLengthBeforeOpt, optimalLength) + bar);
      System.out.print(asPercentage(minLengthAfterOpt, optimalLength) + bar);
      System.out.print(asPercentage(meanLengthAfterOpt, optimalLength) + bar);
      System.out.print(asPercentage(maxLengthAfterOpt, optimalLength) + bar + '\n');
    }
  }

  private static ImprovementStatistics measureStatistics(TspData data, TspConstructiveHeuristic heuristic, TspTour permutations) {
    ImprovementStatistics stats = new ImprovementStatistics();
    for(int i = 0; i < OPTIMISATIONS_COUNT; ++i) {
      long start = System.currentTimeMillis();

      var initialTour = heuristic.computeTour(data, permutations.tour().get(i));
      long lengthBeforeOpt = initialTour.length();

      var optimizedTour = new Tsp2optImprovementHeuristic(MAX_ITERATIONS_COUNT).computeTour(initialTour);
      long lengthAfterOpt = optimizedTour.length();

      long elapsed = System.currentTimeMillis() - start;

      stats.updateStatistics(lengthBeforeOpt, lengthAfterOpt, elapsed);
    }

    return stats;
  }

  public static void main(String[] args) throws FileNotFoundException {
    // TODO
    //  - Renommer le package ;
    //  - Intégrer (et corriger si nécessaire) les heuristiques constructives du labo 1 dans ce package ;
    //  - Implémenter l'heuristique 2-opt utilisant la stratégie "meilleure amélioration" ;
    //  - Documentation soignée comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.

    // Longueurs optimales :
    // pcb442  : 50778
    // att532  : 86729
    // u574    : 36905
    // pcb1173 : 56892
    // nrw1379 : 56638
    // u1817   : 57201

    final String DATA_FOLDER = "data/";
    final String DATA_EXTENSION = ".dat";

    String[] filenames = {"pcb442", "att532", "u574", "pcb1173", "nrw1379", "u1817"};
    Integer[] optimalLengths = {50778, 86729, 36905, 56892, 56638, 57201};

    for(int i = 0; i < filenames.length; ++i) {
      var filename = filenames[i];
      var optimalLength = optimalLengths[i];
      System.out.println("## Données pour le fichier : " + filename + "(longueur optimale : " + optimalLength + ")");

      TspData data = TspData.fromFile(DATA_FOLDER + filename + DATA_EXTENSION);

      RandomTour randomTourGenerator = new RandomTour(0x134DAE9);
      RandomTour randomPermutationGenerator = new RandomTour(0x134DAE9);
      var permutations = randomPermutationGenerator.computeTour(data, -1);

      // todo : mettre les unités dans les colonnes
      System.out.println("|Initialisation|Temps moyen|Min avant opt|Moyenne avant opt|Max avant opt|Min après opt|Moyenne après opt|Max après opt|");
      System.out.println("|-|-|-|-|-|-|-|-|");
      var randomTourStats = measureStatistics(data, randomTourGenerator, permutations);
      randomTourStats.printStatisticsMarkdown("RandomTour", optimalLength);
      var closestInsertionStats = measureStatistics(data, new DistanceBasedTourBuilder(false), permutations);
      closestInsertionStats.printStatisticsMarkdown("Insertion la plus proche", optimalLength);
      var furthestInsertionStats = measureStatistics(data, new DistanceBasedTourBuilder(true), permutations);
      furthestInsertionStats.printStatisticsMarkdown("Insertion la plus éloignée", optimalLength);
    }
  }
}
