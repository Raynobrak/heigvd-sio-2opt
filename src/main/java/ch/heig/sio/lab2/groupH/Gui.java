package ch.heig.sio.lab2.groupH;

import ch.heig.sio.lab2.display.HeuristicComboItem;
import ch.heig.sio.lab2.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspSolverGui;
import ch.heig.sio.lab2.tsp.RandomTour;
import com.formdev.flatlaf.FlatLightLaf;

public final class Gui {
  public static void main(String[] args) {
    ObservableTspConstructiveHeuristic[] constructiveHeuristics = {
            new HeuristicComboItem.Constructive("Random tour", new RandomTour()),
            new HeuristicComboItem.Constructive("Insertion la plus proche", new DistanceBasedTourBuilder(false)),
            new HeuristicComboItem.Constructive("Insertion la plus éloignée", new DistanceBasedTourBuilder(true)),
    };

    ObservableTspImprovementHeuristic[] improvementHeuristics = {
            new HeuristicComboItem.Improvement("2 opt", new Tsp2optImprovementHeuristic(1000000))
        // Add the new improvement heuristic
    };

    // May not work on all platforms, comment out if necessary
    System.setProperty("sun.java2d.opengl", "true");
    FlatLightLaf.setup();

    new TspSolverGui(1400, 800, "TSP solver", constructiveHeuristics, improvementHeuristics);
  }
}
