#!/bin/bash

#####################################################################
#   Script qui affiche les cartes déterminées par le solver SAT     #
#####################################################################


if [ -z "$1" ]; then
  echo -e "\nUsage : ./run.sh num_print nb_cartes \nExemple : ./run.sh 3 10\n" && exit 1 ;
fi

if [ -z "$2" ]; then
  # 10 cartes par défaut si l'utilisateur ne choisit pas.
  nbCartes=10;
else
  nbCartes=$2;
fi

case $1 in
  0) # Affichage BRUT (donc très long et illisible)
  java Dobble.java $nbCartes;;

  1) # Affichage par ligne assez peu lisible
  java Dobble.java $nbCartes | grep -B 1 true | grep v | cut -d " " -f 2 | awk -F "_" '{print $2 " " $3}' | sort -k 1 -k 2 -n;;

  2) # Affichage interessant avec les 8 numéros de symbole sur chaque ligne (= carte)
  java Dobble.java $nbCartes | grep -B 1 true | grep v | cut -d " " -f 2 | awk -F "_" '{print $2 " " $3}' | sort -k 1 -k 2 -n | sed '0~8G' | cut -d " " -f 2 | sed 's/^[[:space:]]*$/toto/' | xargs echo -e | sed 's/ toto / \n/g' | sed 's/toto//';;

  3) # Affichage PARFAIT avec les 8 symboles se trouvant sur chaque carte
  output=$(java Dobble.java $nbCartes | grep -B 1 true | grep v | cut -d " " -f 2 | awk -F "_" '{print expr $2 + 1 " " expr $3 + 1}' | sort -k 1 -k 2 -n | sed '0~8G' | cut -d " " -f 2 | sed 's/^[[:space:]]*$/toto/' | xargs echo -e | sed 's/ toto / \n/g' | sed 's/toto/ /' | sed 's/57 /Tortue /; s/56 /Eclair /; s/55 /Chess /; s/54 /OeilViolet /; s/53 /Arbre /; s/52 /Ancre /; s/51 /Glacon /; s/50 /BonhommeDeNeige /; s/49 /Bougie /; s/48 /Dragon /; s/47 /Exclamation /; s/46 / Erable /; s/45 /Ciseaux /; s/44 /LunettesDeSoleil /; s/43 /Carotte /; s/42 /Trefle4Feuilles /; s/41 /Marteau /; s/40 /Voiture /; s/39 /PeintureVerte /; s/38 /Chat /; s/37 /Igloo /; s/36 /Flamme /; s/35 /Oiseau /; s/34 /Bombe /; s/33 /TeteDeMort /; s/32 /Cible /; s/31 /ClefDeSol /; s/30 /Crayon /; s/29 /Chien /; s/28 /Bouche /; s/27 /Fantome /; s/26 /Dobble /; s/25 /YingYang /; s/24 /Fleur /; s/23 /Horloge /; s/22 /Flocon /; s/21 /Lune /; s/20 /Interrogation /; s/19 /Cactus /; s/18 /SensInterdit /; s/17 /Coeur /; s/16 /Soleil /; s/15 /Dinosaure /; s/14 /ToileAraignee /; s/13 /Coccinelle /; s/12 /Araignee /; s/11 /Cadenas /; s/10 /Biberon /; s/9 /Dauphin /; s/8 /Clef /; s/7 /Fromage /; s/6 /Ampoule /; s/5 /Clown /; s/4 /GoutteDeau /; s/3 /Bonhomme /; s/2 /Pomme /; s/1 /Zebre /' | sed '0~1G') && echo -e "\n${output}\n";;

  4) # Affichage de la matrice de correspondance symboles-communs / cartes
  ./run.sh 2 $nbCartes > output.txt && java CheckModel.java output.txt $nbCartes;;

  5) # 1 : Génération du code java pour affecter les cartes déterminées au modèle
     # 2 : Puis mise à jour de la variable NB_CARTES_DEJA_GENEREES dans le fichier Dobble.java
  output=$(./run.sh 1 $nbCartes | awk '{print "v[" $1 "][" $2 "],"}') && echo -n "solver.add(context.mkAnd(${output%?}));" > outputYES.txt && addCards=$(cat outputYES.txt) && sed -i "/.*Determiner les N cartes du modele.*/a $(echo $addCards)" Dobble.java && sed -i "s/.*public final int NB_CARTES_DEJA_GENEREES.*/    public final int NB_CARTES_DEJA_GENEREES = ${nbCartes};/" Dobble.java;;

  6) # Nier la solution en cours
  output=$(./run.sh 1 $nbCartes | awk '{print "v[" $1 "][" $2 "],"}') && echo -n "solver.add(context.mkNot(context.mkAnd(${output%?})));" > outputNOT.txt && addLines=$(cat outputNOT.txt) && sed -i "/.*Nier la solution en cours pour en obtenir une nouvelle.*/a $(echo $addLines)" Dobble.java;;
  7) # Reset le jeu Dobble <=> NB_CARTES_DEJA_GENEREES = 0 et pas de solution niée
  sed -i "s/.*public final int NB_CARTES_DEJA_GENEREES.*/    public final int NB_CARTES_DEJA_GENEREES = 0;/" Dobble.java;;

*) # Afficher l'erreur
  echo "./run.sh N avec N de 0 à 7";;
esac



