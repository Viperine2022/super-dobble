#                                                   	Super Dobble



> **Le but de ce projet est de modéliser Dobble sous la forme d'un problème SAT pour déterminer s'il est possible d'y jouer avec plus de cartes, tout en respectant le principe du jeu.**



<img src="./images/dobble.png" alt="dobble" style="zoom:50%;" />



**Dobble** est un jeu de société comprenant 55 cartes, avec 8 symboles par carte pour un total de 57 symboles différents. Sa particularité est que deux cartes du jeu possèdent toujours exactement un symbole en commun. De cette particularité émane toute la complexité de ce jeu.

Le but du jeu est à chaque fois de trouver le symbole commun entre 2 cartes. Il s'agit donc d'un jeu de rapidité et de réactivité.

<p align="center">
<img src="./images/similar.png" alt="symboles communs" style="zoom:67%;" />
<ins>Exemple de 3 cartes et de leur symbole commun associé</ins>
</p>


Les caractéristiques de ce jeu font qu'on peut le modéliser sous la forme d'un **problème sous contraintes**. On choisit de le modéliser sous la forme d'un problème **SAT** (boolean satisfiability problem).

Le jeu se modélise de la façon suivante : 

- Le jeu possède exactement 55 cartes

- Sur chaque carte on trouve exactement 8 symboles

- Il existe 57 symboles différents dans le jeu

- Deux cartes du jeu ont toujours exactement un symbole en commun

<br/>

-------

<br/>

## Lancer le solver

- Le script ``run.sh`` permet de lancer le solver de jeu Dobble avec 2 arguments :
  - Le type d'affichage (un entier entre 0 et 3)
  - Le nombre de carte à générer dans le jeu

### Exemples

- ``./run.sh 2 N`` permet d'afficher les cartes générées, avec les 8 numéros de symbole sur chaque carte, étant donné que 1 ligne ⇔ 1 carte

```
$ ./run.sh 2 10
3 6 11 24 27 29 31 56 
1 7 12 13 15 24 39 54 
7 8 20 28 34 36 50 56 
12 14 20 29 37 40 43 49 
13 19 27 32 34 38 40 46 
0 3 14 26 34 39 51 53 
4 25 30 31 34 37 45 54 
12 23 25 26 32 33 35 56 
15 16 17 18 26 31 40 50 
1 14 16 38 45 48 55 56 
```

- ``./run.sh 3 N`` permet d'afficher les cartes générées, avec cette fois-ci les 8 symboles écrits textuellement (et non plus sous forme d'un numéro)

````
$ ./run.sh 3 10

GoutteDeau Fromage Araignee YingYang Bouche Crayon Cible Tortue 

Pomme Clef Coccinelle ToileAraignee Soleil YingYang Voiture Chess 

Clef Dauphin Lune Chien Oiseau Igloo Glacon Tortue 

Coccinelle Dinosaure Lune Crayon Chat Marteau LunettesDeSoleil BonhommeDeNeige 

ToileAraignee Interrogation Bouche TeteDeMort Oiseau PeintureVerte Marteau Exclamation 

Zebre GoutteDeau Dinosaure Fantome Oiseau Voiture Ancre OeilViolet 

Clown Dobble ClefDeSol Cible Oiseau Chat  Erable Chess 

Coccinelle Fleur Dobble Fantome TeteDeMort Bombe Flamme Tortue 

Soleil Coeur SensInterdit Cactus Fantome Cible Marteau Glacon 

Pomme Dinosaure Coeur PeintureVerte  Erable Bougie Eclair Tortue 
````



- ``./run.sh 4 N`` permet de **vérifier la bonne modélisation du jeu**. En effet, on affiche une matrice de **confusion** (ou correspondance *carte/symbole*). M\[i][j] correspond au nombre de symboles communs entre les cartes i et j. Pour que le jeu soit correct, la matrice ne doit avoir que des 8 sur la diagonale, et des 1 partout autre part.

````
./run.sh 4 10

8  1  1  1  1  1  1  1  1  1  
1  8  1  1  1  1  1  1  1  1  
1  1  8  1  1  1  1  1  1  1  
1  1  1  8  1  1  1  1  1  1  
1  1  1  1  8  1  1  1  1  1  
1  1  1  1  1  8  1  1  1  1  
1  1  1  1  1  1  8  1  1  1  
1  1  1  1  1  1  1  8  1  1  
1  1  1  1  1  1  1  1  8  1  
1  1  1  1  1  1  1  1  1  8  
````


## Commentaire - Analyse des limites

Il apparaît que **la complexité de la modélisation explose avec le nombre de cartes**. Pour le moment, avec cette modélisation le solver ne peut **pas générer un jeu de plus de 26 cartes**. Sinon, le temps d'excécution est quasi-infini. 

Ceci est assez problématique quand on sait que le jeu Dobble contient 55 cartes. Il doit y avoir un moyen d'améliorer la modélisation du jeu Dobble en problème SAT pour générer des jeux plus grands.

<br>
<br>

### Améliorations


## 1. Utilisation du jeu Dobble réel

Une idée qui nous vient à l'esprit est d'utiliser les cartes du jeu existant pour essayer de générer de plus gros jeux. On décide alors de "coder" 20 cartes arbitraires du jeu. Le fichier **jeuReel.txt** contient les informations de 20 cartes du modèle. Nous nous servons de ce dernier pour écrire le code Java qui affecte au modèle les variables booléennes de ces 20 cartes. Et nous l'injectons dans le fichier java **Dobble.java**.

Ceci est effectuée par la commande ``./run.sh  8``
````solver.add(context.mkAnd(v[0][1], v[0][15], v[0][27], v[0][38], v[0][43], v[0][50], v[0][53], v[0][54], v[1][5], v[1][14], v[1][21], v[1][24], v[1][31], v[1][33], v[1][39], v[1][54], v[2][22], v[2][7], v[2][36], v[2][12], v[2][33], v[2][41], v[2][43], v[2][18], v[3][28], v[3][2], v[3][55], v[3][56], v[3][47], v[3][36], v[3][54], v[3][10], v[4][45], v[4][32], v[4][3], v[4][27], v[4][17], v[4][56], v[4][33], v[4][35], v[5][48], v[5][9], v[5][26], v[5][10], v[5][43], v[5][29], v[5][45], v[5][5], v[6][0], v[6][1], v[6][2], v[6][3], v[6][4], v[6][5], v[6][6], v[6][7], v[7][7], v[7][16], v[7][25], v[7][31], v[7][53], v[7][8], v[7][47], v[7][45], v[8][42], v[8][32], v[8][16], v[8][39], v[8][18], v[8][1], v[8][29],
````


## 1. Première amélioration

La stratégie intéressante à mettre en place est la suivante :

- On génère un petit jeu d'environ 20 cartes. Ces cartes respectent le jeu Dobble (8 symboles par carte et 1 seul symbole commun entre chacune d'elles).
- On injecte le code de ce petit jeu généré. En d'autre termes, on affecte les variables booléennes traduisant le petit jeu.
- On se sert du code injecté pour générer de plus gros jeux. Ceci est possible car la complexité est fortement diminuée. En effet, il nous suffit d'ajouter les contraintes liées aux nouvelles cartes à générer. 
  Par exemple, si on se sert d'un jeu de 20 cartes pour générer un jeu de 25 cartes, il suffit d'ajouter les contraintes des cartes 21 à 25.


