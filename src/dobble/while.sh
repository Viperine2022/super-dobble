#!/bin/bash

#############################################################################################
#   Script qui cherche un jeu de 20 à 30 cartes "optimisé", c'est à dire, un set qui        #
#   permet de créer un jeu plus grand. Ceci est fait en injectant en code java la           #
#   négation de chaque modèle trouvé  qui ne permet pas d'obtenir un jeu plus grand         #
#############################################################################################


if [ "$#" -ne 2 ] || [ "$1" -gt "$2" ]; then
  echo -e "\nUsage : ./while.sh n1 n2      avec n1 < n2\nPar exemple, ./run.sh 15 22\n"
  exit 1
fi




SAT_PROBLEM=1              # 1 <=> FALSE et à <=> TRUE
cpt=0                      # compteur du nombre de génération de modèles nécessaires
i=25                       # compteur du nombre de modèles niés (pour ensuite créer de nouvelles méthodes java ne dépassant pas les 65535 bytes)
                           # Initialisé à 25 pour la robustesse

while [ ${SAT_PROBLEM} -eq 1 ]
do

  # chronomètre du temps passé à chaque entrée de boucle
  start=$(date +%s)

  # On détermine le numéro de méthode maximale déjà créée (pour éviter les doublons quand on devra en créer une autre)
  numMaxMethode=$(cat Dobble.java | grep "Errones" | cut -d "(" -f 1 | cut -d "s" -f 3 | sort -n | tail -n 1)

  # Si le code généré va dépasser la taille maximale possible d'une méthode (65535 bytes)
  if [ "${i}" -ge 25 ]; then

    echo "i = ${i} donc création d'une nouvelle méthode java, n°$((${numMaxMethode}+1))"

    # On définit une nouvelle méthode Java (injection du code)
    sed -i "/.*Determiner les N cartes du modele.*/a $(echo "public void ajoutModelesErrones$((${numMaxMethode}+1))\(\) {")" Dobble.java
    sed -i "/.*Determiner les N cartes du modele.*/a $(echo })" Dobble.java

    # On ajoute le code de l'appel de la méthode qu'on vient de créer
    sed -i "/.*Dobble dobble.*/a $(echo "dobble.ajoutModelesErrones$((${numMaxMethode}+1))\(\)\;")" Dobble.java

    # Enfin, on réinitialise i
    i=0
  fi


  # Générer un jeu de $1 cartes et l'injecter dans le modèle
  ./run.sh 5 $1

  # Essayer de générer un jeu plus grand, de $2 cartes (avec $2 > $1)
  ./run.sh 0 $2 | grep -q "SAT, model:" && test $(echo $?) -eq 0 && SAT_PROBLEM=0

  if [ "${SAT_PROBLEM}" -ne 0 ]; then
  # ON N'A PAS PU GENERER UN JEU DE $2 CARTES ( SAT_PROBLEM = 1 )

    echo "Echec de la generation d'un jeu de $2 cartes"
    lineToAdd=$(($(grep -n "Determiner les N" Dobble.java | cut -d ":" -f 1)+1))

    # Nier le modèle qui vient d'être généré (on ajoute le code java correspondant)
    replace=$(sed -n ${lineToAdd}p Dobble.java | awk -F "(" '{print $1 "(context.mkNot(" $2 "(" $3 }' | head -n 1 | awk -F ")" '{print $1 ")))" $2 $3 }')
    sed -i "${lineToAdd}s/^.*$/${replace}/" Dobble.java

    # Remettre à 0 le nombe de cartes déja générées
    ./run.sh 7

  else

    # ON A PU GENERER UN JEU DE $2 CARTES ( SAT_PROBLEM = 0 )
    echo "Succes : generation d'un jeu de $2 cartes"

    # On remet la valeur de SAT_PROBLEM à 1 par défaut
    SAT_PROBLEM=1
    ./run.sh 0 $(($2+2)) | grep -q "SAT, model:" && test $(echo $?) -eq 0 && SAT_PROBLEM=0

    if [ "${SAT_PROBLEM}" -ne 0 ]; then
    # ON N'A PAS PU GENERER UN JEU DE $2 + 2 CARTES

      echo "Echec de la generation d'un jeu de $(($2+2)) cartes, on reste dans la boucle"
      # On commente le modèle
      lineToAdd=$(($(grep -n "Determiner les N" Dobble.java | cut -d ":" -f 1)+1))
      replace=$(sed -n ${lineToAdd}p Dobble.java | awk -F "(" '{print $1 "(context.mkNot(" $2 "(" $3 }' | head -n 1 | awk -F ")" '{print $1 ")))" $2 $3 }')
      sed -i "${lineToAdd}s/^.*$/${replace}/" Dobble.java

      # Remettre à 0 le nombe de cartes déja générées
      ./run.sh 7
    fi
  fi

  # incrémenter le compteur de modèles déjà ajoutés
  i=$((${i}+1))

  # Incrémenter le compteur
  cpt=$((${cpt}+1))

  end=$(date +%s)
  echo "Fail n°${cpt}, in $((${end} - ${start})) seconds"


done

echo "SUCCESS"

# Message audio de sortie
spd-say "I found a solution for $(($2 + 2)) cards"


