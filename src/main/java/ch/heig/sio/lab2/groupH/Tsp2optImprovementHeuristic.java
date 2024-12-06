package ch.heig.sio.lab2.groupH;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.TspTour;

import java.util.ArrayList;
import java.util.Arrays;

public class Tsp2optImprovementHeuristic implements ObservableTspImprovementHeuristic {
    public static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {

        int maxIterationsCount = 300000000; // todo : mettre dans ctor

        int[] tour = initialTour.tour().copy();
        var data = initialTour.data();
        final int N = tour.length;

        for(int iteration = 0; iteration < maxIterationsCount; ++iteration) {
            int bestImprovement = 0;
            int bestImprovementOriginA = Integer.MAX_VALUE;
            int bestImprovementOriginB = Integer.MAX_VALUE;

            // Itérer sur toutes les arêtes du tour actuel
            // (pas besoin de tester la dernière arête (i = N-1) car elle aura forcément déjà été testée auparavant
            for(int i = 0; i < N - 1; ++i) {
                int a = i;
                int b = (i + 1) % N;

                // Itérer sur toutes les arêtes avec lesquelles on peut échanger i, i+1
                for(int j = i + 2; j < N; ++j) {
                    // Cas particulier avec le premier sommet, on ne va pas check la dernière arête parce qu'elle est reliée au premier sommet
                    if(a == 0 && j == N-1)
                        continue;

                    int c = j;
                    int d = (j + 1) % N;

                    int currentDistance = data.getDistance(tour[a],tour[b]) + data.getDistance(tour[c],tour[d]);
                    int currentTryDistance = data.getDistance(tour[a],tour[c]) + data.getDistance(tour[b],tour[d]);
                    int improvement = currentDistance - currentTryDistance;
                    if(improvement > bestImprovement) {
                        bestImprovement = improvement;
                        bestImprovementOriginA = a;
                        bestImprovementOriginB = c;
                    }
                }
            }

            if(bestImprovement > 0) {
                // Trouver le côté le plus petit à inverser

                //boolean reverseInside = (bestImprovementOriginB - bestImprovementOriginA) < N / 2;

                // échanger les extrémités des deux arêtes sélectionnées
                int i = Math.min(bestImprovementOriginA, bestImprovementOriginB) + 1;
                int j = Math.max(bestImprovementOriginA, bestImprovementOriginB);

                int vertexI = tour[i];
                int vertexIBefore = tour[(i-1)%N];
                int vertexJ = tour[j];
                int vertexJafter = tour[(j+1)%N];
                System.out.println("Échange des arcs suivants :");
                System.out.println(vertexIBefore + " -> " + vertexI);
                System.out.println(vertexJ + " -> " + vertexJafter);

                while(i < j) {
                    swap(tour, i, j);
                    i = (i+1) % N;
                    j = (j-1) % N;
                }
            }
            else {
                // Pas d'amélioration trouvée, on
                break;
            }

            // todo : c'est dégueulasse
            ArrayList<Integer> lst = new ArrayList<>();
            for(int i = 0; i < N; ++i)
                lst.add(tour[i]);
            observer.update(new TraversalIterator(lst));
        }

        return new TspTour(data, tour, 12); // todo : mettre la bonne longueur
    }
}
