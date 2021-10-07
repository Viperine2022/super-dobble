java Dobble.java | grep -B 1 true | grep v | cut -d " " -f 2 | awk -F "_" '{print $2 " " $3}' | sort -k 1 -k 2 -n



java Dobble.java | grep -B 1 true | grep v | cut -d " " -f 2 | awk -F "_" '{print $2 " " $3}' | sort -k 1 -k 2 -n | sed '0~8G' | cut -d " " -f 2 | sed 's/^[[:space:]]*$/toto/' | xargs echo -e | sed 's/ toto / \n/g' | sed 's/toto//'


java Dobble.java | grep -B 1 true | grep v | cut -d " " -f 2 | awk -F "_" '{print $2 " " $3}' | sort -k 1 -k 2 -n | sed '0~8G' | cut -d " " -f 2 | sed 's/^[[:space:]]*$/toto/' | xargs echo -e | sed 's/ toto / \n/g' | sed 's/toto/ /' | sed 's/21 /Lune /; s/20 /Interrogation /; s/19 /Cactus /; s/18 /SensInterdit/; s/17 /Coeur /; s/16 /Soleil /; s/15 /Dinosaure /; s/14 /ToileAraignee /; s/13 /Coccinelle /; s/12 /Araignee /; s/11 /Cadenas /; s/10 /Biberon /; s/9 /Dauphin /; s/8 /Clef /; s/7 /Fromage /; s/6 /Ampoule /; s/5 / Clown /; s/4 /GoutteDeau /; s/3 /Bonhomme/; s/2 /Pomme /; s/1 /Zebre /' | sed '0~1G'
