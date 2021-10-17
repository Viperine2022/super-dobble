#!/bin/bash

##bool=1              # 1 <=> FALSE et à <=> TRUE
##while [ ${bool} -lt 100 ]
##do
##  echo "bool : ${bool}"
##  bool=$((${bool}+1))
##done






SAT_PROBLEM=1              # 1 <=> FALSE et à <=> TRUE
cpt=0


while [ ${SAT_PROBLEM} -eq 1 ]
do

  # 1. Générer un jeu de 20 cartes et l'injecter dans le modèle
  ./run.sh 5 20

  # 2. Essayer de générer un jeu de 25 cartes
  ./run.sh 0 22 | grep -q "SAT, model:" && test $(echo $?) -eq 0 && SAT_PROBLEM=0
  if [ "${SAT_PROBLEM}" -ne 0 ]; then

  # 3. Si échec : Nier le modèle qui vient d'être généré
  replace=$(sed -n 76p Dobble.java | awk -F "(" '{print $1 "(context.mkNot(" $2 "(" $3 }' | head -n 1 | awk -F ")" '{print $1 ")))" $2 $3 }')
  sed -i "76s/^.*$/${replace}/" Dobble.java
  # Remettre à 0 le nombre de cartes déjà générées !!!
  ./run.sh 7
  # Incrémenter le compteur
  cpt=$((${cpt}+1))
  echo "FAIL n°${cpt}"
    sleep 1
  fi

done

echo "SUCCESS"


# ./run.sh 0 25 | grep -q "SAT, model:" && test $(echo $?) -eq 0 && x=YES || x=FALSE
