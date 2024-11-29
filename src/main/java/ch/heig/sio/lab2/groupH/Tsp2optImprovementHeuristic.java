package ch.heig.sio.lab2.groupH;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.TspTour;

public class Tsp2optImprovementHeuristic implements ObservableTspImprovementHeuristic {
    @Override
    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer, int maxIterationsCount) {

        int[] tour = initialTour.tour().copy();
        var data = initialTour.data();
        final int N = tour.length;

        int bestImprovement = 0;
        int bestImprovementOriginA = Integer.MAX_VALUE;
        int bestImprovementOriginB = Integer.MAX_VALUE;

        for(int iteration = 0; iteration < maxIterationsCount; ++iteration) {
            // Itérer sur toutes les arêtes du tour actuel
            // (pas besoin de tester la dernière arête (i = N-1) car elle aura forcément déjà été testée auparavant
            for(int i = 0; i < N - 1; ++i) {
                int a = i;
                int b = (i + 1) % N;

                // Itérer sur toutes les arêtes avec lesquelles on peut échanger i, i+1
                for(int j = i + 2; j < N; ++i) {
                    // Cas particulier avec le premier sommet, on ne va pas check la dernière arête parce qu'elle est reliée au premier sommet
                    if(a == 0 && j == N-1)
                        continue;

                    int c = j;
                    int d = (j + 1) % N;

                    int currentDistance = data.getDistance(a,b) + data.getDistance(c,d);
                    int currentTryDistance = data.getDistance(a,c) + data.getDistance(b,d);
                    int improvement = currentDistance - currentTryDistance;
                    if(improvement > bestImprovement) {
                        bestImprovementOriginA = a;
                        bestImprovementOriginB = b;
                    }
                }
            }

            if(bestImprovement > 0) {
                // todo: échange
            }
            else {
                // Pas d'amélioration trouvée, on
                break;
            }
        }



        return initialTour;

    }
}
