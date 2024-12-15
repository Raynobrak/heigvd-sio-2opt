package ch.heig.sio.lab2.groupH;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.TspTour;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Heuristique d'amélioration 2-opt
 * Permet d'améliorer une tournée en échangeant deux arêtes à chaque itération
 * @Author Group H - Ancay Rémi, Charbonnier Lucas
 */
public class Tsp2optImprovementHeuristic implements ObservableTspImprovementHeuristic {
    public static final int INFINITE_ITERATIONS = -1;
    private final int maxIterationsCount; // Nombre d'itérations maximum
    public Tsp2optImprovementHeuristic(int maxIterationsCount) {
        this.maxIterationsCount = maxIterationsCount;
    }

    /**
     * Echange deux valeurs (i et j) dans un tableau (array)
     */
    private static void swap(int[] array, int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    /**
     * Améliore la tournée donnée avec des améliorations 2-opt
     * @param initialTour La tournée existante
     * @param observer
     * @return
     */
    @Override
    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {
        int[] tour = initialTour.tour().copy(); // Copie de la tournée dans un tableau pour avoir un objet local
        var data = initialTour.data(); // Données du tour
        final int N = tour.length; // Nombre de sommets
        long totalLength = initialTour.length(); // Longueur de la tournée

        // Tant qu'on a pas dépassé le nombre d'itérations maximum de l'algorithme...
        for(int iteration = 0; iteration < this.maxIterationsCount || this.maxIterationsCount == INFINITE_ITERATIONS; ++iteration) {
            int bestImprovement = 0; // Meilleure amélioration trouvée

            // Indices des extrémités des arêtes (a-b) et (c-d) du meilleur échange trouvé
            int bestA = Integer.MAX_VALUE;
            int bestC = Integer.MAX_VALUE;

            // Itérer sur toutes les arêtes (a-b) du tour actuel
            // (pas besoin de tester la dernière arête (a = N-1) car elle aura forcément déjà été testée auparavant
            for(int a = 0; a < N - 1; ++a) {
                int b = (a + 1) % N;

                int distAB = data.getDistance(tour[a],tour[b]);

                // Itérer sur toutes les arêtes (c-d) avec lesquelles on peut échanger l'arête (a-b)
                for(int c = a + 2; c < N; ++c) {
                    int d = (c + 1) % N;

                    // Cas particulier avec le premier sommet, on ne va pas check la dernière arête parce qu'elle est reliée au premier sommet
                    if(a == 0 && c == N-1)
                        continue;

                    // Calcul de la longueur actuelle et de la longueur si on échangeait les arêtes
                    int currentDistance = distAB + data.getDistance(tour[c],tour[d]);
                    int currentTryDistance = data.getDistance(tour[a],tour[c]) + data.getDistance(tour[b],tour[d]);
                    int improvement = currentDistance - currentTryDistance;
                    if(improvement > bestImprovement) { // Si l'amélioration courante est meilleure que la meilleure amélioration trouvée jusqu'à maintenant
                        bestImprovement = improvement;
                        bestA = a;
                        bestC = c;
                    }
                }
            }

            if(bestImprovement == 0) // Pas d'amélioration trouvée
                break; // On ne cherche pas plus loin, l'algorithme est terminé

            totalLength -= bestImprovement; // Mise à jour de la longueur totale

            // Échange des deux arêtes et inversion du parcours
            int i = Math.min(bestA, bestC) + 1;
            int j = Math.max(bestA, bestC);
            while(i < j) {
                swap(tour, i, j);
                i = (i+1) % N;
                j = (j-1) % N;
            }

            // Mise à jour de l'interface graphique
            observer.update(new TraversalIterator(new ArrayList<>(Arrays.stream(tour).boxed().toList())));
        }

        return new TspTour(data, tour, totalLength);
    }
}
