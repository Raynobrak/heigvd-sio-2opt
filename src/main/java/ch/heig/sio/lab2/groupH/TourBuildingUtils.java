package ch.heig.sio.lab2.groupH;
import ch.heig.sio.lab2.tsp.TspData;
import java.util.ArrayList;

/// Classe utilitaire pour la construction de tournées
/// Contient des méthodes statiques pour aider à la construction de tournées
/// @author Group H - Ancay Rémi, Charbonnier Lucas
public class TourBuildingUtils {
    /// Convertit une ArrayList d'entiers en tableau d'entiers
    /// @param tourList : la liste des villes de la tournée
    /// @return le tableau des villes de la tournée
    static int[] convertTourListToArray(ArrayList<Integer> tourList) {
        return tourList.stream().mapToInt(Integer::intValue).toArray();
    }

    /// Retourne le coût d'insertion du sommet "intermediate" entre les sommets first et second
    /// En d'autres termes, le coût du détour impliqué par l'ajout d'un sommet intermédiaire entre deux autres sommets
    /// @param first : le premier sommet
    /// @param second : le deuxième sommet
    /// @param intermediate : le sommet à insérer
    /// @param data : les données de notre graphe
    /// @return le coût d'insertion
    static int getInsertionCost(int first, int second, int intermediate, TspData data) {
        int distance = data.getDistance(first, second);
        int intermediateDistance1 = data.getDistance(first, intermediate);
        int intermediateDistance2 = data.getDistance(intermediate, second);
        return intermediateDistance1 + intermediateDistance2 - distance;
    }

    /// Insère la ville donnée au meilleur endroit de la tournée
    /// @param newCity : la ville à insérer
    /// @param tourList : la liste des villes de la tournée
    /// @param data : les données de notre graphe
    /// @return la distance en plus qu'implique l'ajout de la nouvelle ville
    static int insertNewCityAtBestIndex(int newCity, ArrayList<Integer> tourList, TspData data){
        int smallestInsertionCost = Integer.MAX_VALUE; // Coût d'insertion minimal
        int currentBestInsertionIndex = Integer.MAX_VALUE; // Index de la meilleure insertion
        int insertedCitiesCount = tourList.size(); // Nombre de villes déjà insérées

        for(int current = 0; current < insertedCitiesCount; ++current) {
            int next = (current + 1) % insertedCitiesCount;

            int cost = getInsertionCost(tourList.get(current), tourList.get(next), newCity, data);
            if(cost < smallestInsertionCost) {
                smallestInsertionCost = cost;
                currentBestInsertionIndex = (current + 1) % insertedCitiesCount;
            }
        }

        // Si la tournée est vide, on insère la ville à l'index 0
        if(insertedCitiesCount == 0) {
            currentBestInsertionIndex = 0;
            smallestInsertionCost = 0;
        }

        tourList.add(currentBestInsertionIndex, newCity);
        return smallestInsertionCost;
    }

    /// Retourne la ville hors-tournée la plus proche d'une ville de la tournée
    /// @param data : les données de notre graphe
    /// @param closestCityInTour : tableau des villes de la tournée les plus proches des villes hors tournées
    /// @param visited : tableau des villes déjà visitées
    /// @param furthest : true pour retourner la ville la plus éloignée, false pour la plus proche
    /// @return l'index de la ville hors-tournée la plus proche
    static int getClosestCityOfAll(TspData data, int[] closestCityInTour, boolean[] visited, boolean furthest) {
        int bestDistance = furthest ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestCityIndex = -1;

        for(int i = 0; i < closestCityInTour.length; ++i) {
            if(visited[i])
                continue;

            int distance = data.getDistance(i, closestCityInTour[i]);
            if(furthest && distance > bestDistance || !furthest && distance < bestDistance) {
                bestDistance = distance;
                bestCityIndex = i;
            }
        }
        return bestCityIndex;
    }

    /// Met à jour le tableau des villes de la tournée les plus proches des villes hors tournées
    /// @param addedCityIndex : l'index de la ville nouvellement ajoutée à la tournée
    /// @param data : les données de notre graphe
    /// @param visited : tableau des villes déjà visitées
    /// @param closestCityInTour : tableau des villes de la tournée les plus proches des villes hors tournées
    static void updateClosestCity(int addedCityIndex, TspData data, boolean[] visited, int[] closestCityInTour) {
        for(int i = 0; i < data.getNumberOfCities(); ++i) {
            // On ne parcours que les villes hors-tournées
            if(visited[i])
                continue;

            // On regarde si la ville nouvellement ajoutée à la tournée est plus proche que la ville retenue précédemment
            int currentDistance = data.getDistance(i, closestCityInTour[i]);
            int distance = data.getDistance(i, addedCityIndex);

            // On met à jour la distance si c'est mieux
            if(distance < currentDistance)
                closestCityInTour[i] = addedCityIndex;
        }
    }
}
