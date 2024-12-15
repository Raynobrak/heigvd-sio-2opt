# Résultats obtenus

### Informations concernant la génération des statistiques
- Tous les résultats, à l'exception du temps moyen d'exécution, sont exprimés en pourcentage relatif à la longueur de la tournée optimale. 
Par exemple, un résultat de 143% signifie que la tournée obtenue est 1,43 fois plus longue que la tournée optimale.
Le meilleur résultat possible serait donc de 0%. Un résultat négatif est, par définition, impossible.
- Les valeurs min, max et moyennes ont été calculées à partir d'un échantillon de 50 exécutions. 
- L'algorithme d'optimisation (2-opt) s'exécutait jusqu'à ce qu'aucune autre optimisation aie été trouvée. 
- Le temps moyen en millisecondes comprend la génération de la tournée initiale ET le temps d'amélioration avec l'heuristique 2-opt.

## Données pour le fichier : pcb442(longueur optimale : 50778)
| Initialisation             | Temps moyen (ms) | Min avant opt (%) | Moyenne avant opt (%) | Max avant opt (%) | Min après opt (%) | Moyenne après opt (%) | Max après opt (%) |
| -------------------------- | ---------------- | ----------------- | --------------------- | ----------------- | ----------------- | --------------------- | ----------------- |
| RandomTour                 | 203              | 1332,58           | 1422,80               | 1470,38           | 6,18              | 10,85                 | 14,58             |
| Insertion la plus proche   | 23               | 16,61             | 18,77                 | 20,48             | 7,10              | 8,94                  | 10,73             |
| Insertion la plus éloignée | 5                | 9,90              | 13,03                 | 17,41             | 8,20              | 11,64                 | 15,68             |
## Données pour le fichier : att532(longueur optimale : 86729)
| Initialisation             | Temps moyen (ms) | Min avant opt (%) | Moyenne avant opt (%) | Max avant opt (%) | Min après opt (%) | Moyenne après opt (%) | Max après opt (%) |
| -------------------------- | ---------------- | ----------------- | --------------------- | ----------------- | ----------------- | --------------------- | ----------------- |
| RandomTour                 | 357              | 1681,31           | 1755,95               | 1848,12           | 6,60              | 9,12                  | 12,76             |
| Insertion la plus proche   | 57               | 23,01             | 24,05                 | 25,38             | 10,94             | 12,25                 | 13,68             |
| Insertion la plus éloignée | 11               | 7,20              | 9,52                  | 12,47             | 6,35              | 8,20                  | 11,37             |
## Données pour le fichier : u574(longueur optimale : 36905)
| Initialisation             | Temps moyen (ms) | Min avant opt (%) | Moyenne avant opt (%) | Max avant opt (%) | Min après opt (%) | Moyenne après opt (%) | Max après opt (%) |
| -------------------------- | ---------------- | ----------------- | --------------------- | ----------------- | ----------------- | --------------------- | ----------------- |
| RandomTour                 | 446              | 1660,07           | 1746,77               | 1840,11           | 7,55              | 9,87                  | 12,34             |
| Insertion la plus proche   | 71               | 22,11             | 23,31                 | 24,96             | 9,20              | 10,46                 | 11,31             |
| Insertion la plus éloignée | 15               | 7,81              | 10,48                 | 13,86             | 6,36              | 9,07                  | 11,90             |
## Données pour le fichier : pcb1173(longueur optimale : 56892)
| Initialisation             | Temps moyen (ms) | Min avant opt (%) | Moyenne avant opt (%) | Max avant opt (%) | Min après opt (%) | Moyenne après opt (%) | Max après opt (%) |
| -------------------------- | ---------------- | ----------------- | --------------------- | ----------------- | ----------------- | --------------------- | ----------------- |
| RandomTour                 | 4267             | 2321,86           | 2382,65               | 2457,59           | 10,43             | 12,63                 | 15,19             |
| Insertion la plus proche   | 556              | 24,69             | 27,07                 | 27,91             | 9,70              | 10,75                 | 11,99             |
| Insertion la plus éloignée | 132              | 13,63             | 15,65                 | 18,04             | 11,85             | 13,51                 | 15,73             |
## Données pour le fichier : nrw1379(longueur optimale : 56638)
| Initialisation             | Temps moyen (ms) | Min avant opt (%) | Moyenne avant opt (%) | Max avant opt (%) | Min après opt (%) | Moyenne après opt (%) | Max après opt (%) |
| -------------------------- | ---------------- | ----------------- | --------------------- | ----------------- | ----------------- | --------------------- | ----------------- |
| RandomTour                 | 6972             | 2344,77           | 2418,06               | 2479,53           | 9,24              | 11,11                 | 12,39             |
| Insertion la plus proche   | 789              | 22,10             | 22,64                 | 23,09             | 12,22             | 12,88                 | 13,37             |
| Insertion la plus éloignée | 185              | 9,61              | 10,95                 | 12,60             | 8,75              | 9,67                  | 11,31             |
## Données pour le fichier : u1817(longueur optimale : 57201)
| Initialisation             | Temps moyen (ms) | Min avant opt (%) | Moyenne avant opt (%) | Max avant opt (%) | Min après opt (%) | Moyenne après opt (%) | Max après opt (%) |
| -------------------------- | ---------------- | ----------------- | --------------------- | ----------------- | ----------------- | --------------------- | ----------------- |
| RandomTour                 | 38451            | 3491,24           | 3600,22               | 3715,95           | 12,45             | 14,74                 | 16,68             |
| Insertion la plus proche   | 5127             | 22,95             | 23,95                 | 24,84             | 11,45             | 11,95                 | 12,60             |
| Insertion la plus éloignée | 1663             | 18,95             | 20,62                 | 23,38             | 15,90             | 18,31                 | 21,18             |

# Analyse

## Temps d'exécution

L'heuristique *RandomTour* donne les pires résultats au niveau du temps total d'exécution. C'est logique car moins la solution de départ est bonne, alors plus le temps d'optimisation sera élevé.

## Qualité des solutions avant optimisation

En comparant la qualité des solutions avant optimisation en fonction de l'heuristique d'initialisation choisie, on peut voir que l'initialisation avec l'heuristique "Insertion la plus éloignée" est systématiquement meilleure qu'avec l'insertion la plus proche. Évidemment, l'initialisation aléatoire est toujours très mauvaise par rapport aux deux précédentes.

## Qualité des solutions après optimisation

Lorsque on ajoute une étape d'optimisation avec l'heuristique 2-opt, on remarque que les solutions sont de meilleures qualités lorsqu'on initialise avec l'heuristique "insertion la plus proche". Cependant, cela n'est le cas que pour les deux fichiers représentant des circuits électroniques (pcb442 et pcb1173).

## Conclusion
En conclusion, les résultats ci-dessus nous montrent qu'il n'y a pas de "meilleur algorithme" pour le TSP. L'algorithme ou la combinaison d'algorithmes (heuristique d'initialisation + optimisation) à utiliser dépendra du problème et la meilleure manière de les trouver est de faire des tests empiriques.

Dans le cas des PCBs (pcb442 et pcb1173), où le but est de trouver un chemin le plus court permettant de diminuer le temps d'assemblage d'un circuit électronique par un bras robotisé, les meilleurs résultats ont été obtenu avec la combinaison "insertion la plus proche" + 2opt 

Pour les autres fichiers représentant des positions de villes, c'est la combinaison insertion la plus éloignée + 2opt qui a donné les meilleurs résultats.

