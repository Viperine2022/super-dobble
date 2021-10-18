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

  # 1. Générer un jeu de 25 cartes et l'injecter dans le modèle
  ./run.sh 5 25

  # 2. Essayer de générer un jeu de 26 cartes
  ./run.sh 0 26 | grep -q "SAT, model:" && test $(echo $?) -eq 0 && SAT_PROBLEM=0
  if [ "${SAT_PROBLEM}" -ne 0 ]; then

  lineToAdd=$(grep -n "Determiner les N" Dobble.java | cut -d ":" -f 1)

  # 3. Si échec : Nier le modèle qui vient d'être généré
  replace=$(sed -n $((${lineToAdd}+1))p Dobble.java | awk -F "(" '{print $1 "(context.mkNot(" $2 "(" $3 }' | head -n 1 | awk -F ")" '{print $1 ")))" $2 $3 }')


  ###echo "Line to add : $((${lineToAdd}+1))"
  ###exit 1
  sed -i "$((${lineToAdd}+1))s/^.*$/${replace}/" Dobble.java

  # Remettre à 0 le nombre de cartes déjà générées !!!
  ./run.sh 7
  # Incrémenter le compteur
  cpt=$((${cpt}+1))
  echo "FAIL n°${cpt}"
  fi

done

echo "SUCCESS"


