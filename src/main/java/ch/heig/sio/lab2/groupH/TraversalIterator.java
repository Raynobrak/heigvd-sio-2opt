package ch.heig.sio.lab2.groupH;
import ch.heig.sio.lab2.tsp.Edge;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

/// Itérateur pour parcourir les arêtes d'une tournée
/// Permet de parcourir les arêtes d'une tournée de manière circulaire
/// @author Group H - Ancay Rémi, Charbonnier Lucas
  public class TraversalIterator implements Iterator<Edge> { 
    private final int n; // Nombre de villes
    private int i = 0; // Indice de la ville actuelle
    private ArrayList<Integer> cities; // Liste des villes de la tournée

    TraversalIterator(ArrayList<Integer> cities) {
        this.n = cities.size();
        this.cities = cities;
    }

    @Override
    public boolean hasNext() {
      return i < n;
    }

    @Override
    public Edge next() {
      if (!hasNext())
        throw new NoSuchElementException();
      return new Edge(cities.get(i), cities.get(++i % n));
    }
  }
