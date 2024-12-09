package ch.heig.sio.lab2.groupH;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.TspTour;

import java.util.ArrayList;
import java.util.Arrays;

public class Tsp2optImprovementHeuristic implements ObservableTspImprovementHeuristic {
    private final int maxIterationsCount;
    public Tsp2optImprovementHeuristic(int maxIterationsCount) {
        this.maxIterationsCount = maxIterationsCount;
    }

    private static void swap(int[] array, int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {
        int[] tour = initialTour.tour().copy();
        var data = initialTour.data();
        final int N = tour.length; // Nombre de sommets
        long totalLength = initialTour.length(); // Longueur de la tournée

        for(int iteration = 0; iteration < this.maxIterationsCount; ++iteration) {
            int bestImprovement = 0;

            int bestA = Integer.MAX_VALUE;
            int bestC = Integer.MAX_VALUE; // Index des extrémités des arêtes (a-b) et (c-d) du meilleur échange

            // Itérer sur toutes les arêtes (ab) du tour actuel
            // (pas besoin de tester la dernière arête (i = N-1) car elle aura forcément déjà été testée auparavant
            for(int a = 0; a < N - 1; ++a) {
                int b = (a + 1) % N;

                int distAB = data.getDistance(tour[a],tour[b]);

                // Itérer sur toutes les arêtes (cd) avec lesquelles on peut échanger l'arête (ab)
                for(int c = a + 2; c < N; ++c) {
                    int d = (c + 1) % N;

                    // Cas particulier avec le premier sommet, on ne va pas check la dernière arête parce qu'elle est reliée au premier sommet
                    if(a == 0 && c == N-1)
                        continue;

                    int currentDistance = distAB + data.getDistance(tour[c],tour[d]);
                    int currentTryDistance = data.getDistance(tour[a],tour[c]) + data.getDistance(tour[b],tour[d]);
                    int improvement = currentDistance - currentTryDistance;
                    if(improvement > bestImprovement) {
                        bestImprovement = improvement;
                        bestA = a;
                        bestC = c;
                    }
                }
            }

            if(bestImprovement == 0)
                break; // Pas d'amélioration trouvée -> fin de l'algorithme

            // Mise à jour de la longueur totale
            totalLength -= bestImprovement;

            // Inversion du parcours entre les deux arêtes
            int i = Math.min(bestA, bestC) + 1;
            int j = Math.max(bestA, bestC);
            while(i < j) {
                swap(tour, i, j);
                i = (i+1) % N;
                j = (j-1) % N;
            }

            observer.update(new TraversalIterator(new ArrayList<>(Arrays.stream(tour).boxed().toList())));
        }

        return new TspTour(data, tour, totalLength);
    }
}
