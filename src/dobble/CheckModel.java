package dobble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CheckModel {

	int nbSymboles = 8;
	int nbCartes;
	// La matrice (nbCartes * nbSymboles) des symboles associés aux cartes du jeu
	int[][] v;
	// La matrice (nbCartes * nbCartes) des symboles communs entre les différentes
	// cartes du jeu Dobble
	int[][] M;

	public CheckModel(int nbCartes) {
		this.nbCartes = nbCartes;
		v = new int[this.nbCartes][this.nbSymboles];
		M = new int[this.nbCartes][this.nbCartes];
	}

	/**
	 * Lire le fichier de sortie du solver SAT. Ce dernier se présente sous la forme
	 * d'une succession de lignes, sur chaque ligne se trouvant 8 entiers
	 * correspondant aux 8 numéros de symbole affectés à chaque carte.
	 * 
	 * On remplit de ce fait la matrice v des symboles affectés à chaque carte du
	 * jeu. Ceci permettra de créer la matrice M, la plus importante.
	 */
	public void readOutput(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		for (int i = 0; i < nbCartes; i++) {
			// On lit chaque ligne qui contient les 8 symboles attribués à une carte
			line = br.readLine();
			String values[] = line.split(" ");
			for (int j = 0; j < nbSymboles; j++) {
				v[i][j] = Integer.parseInt(values[j]);
				// System.out.println("v[" + i + "][" + j + "] = " + v[i][j]);
			}
		}
		br.close();
	}

	/**
	 * Remplir et afficher la matrice M de correspondance des symboles entre les
	 * cartes. M[i][j] correspond au nombre de symboles communs entre les cartes i
	 * et j. Pour que le jeu soit correct, la matrice ne doit avoir que des 8 sur la
	 * diagonale, et des 1 partout autre part (car il faut toujours 1 symbole commun
	 * entre deux cartes).
	 */
	public void printMatrix() {
		// On remplit la matrice M
		for (int i = 0; i < nbCartes; i++) {
			for (int j = 0; j < nbSymboles; j++) {
				for (int k = 0; k < nbCartes; k++) {
					for (int l = 0; l < nbSymboles; l++) {
						if (v[i][j] == v[k][l]) {
							M[i][k]++;
						}
					}
					// System.out.println("mat[" + i + "][" + k + "] = " + mat[i][k]);
				}
			}
		}
		String matrix = "";
		for (int i = 0; i < nbCartes; i++) {
			for (int j = 0; j < nbCartes; j++) {
				matrix = matrix + M[i][j] + "  ";
			}
			matrix = matrix + "\n";
		}
		System.out.println("\n" + matrix);
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(""
					+ "\n################################################\n "
					+ "Usage : java CheckModel.java filename nbCartes\n "
					+ "Exemple : java CheckModel.java output.txt 20 "
					+ "\n################################################\n");
			System.exit(1);
		}
		try {
			CheckModel cm = new CheckModel(Integer.parseInt(args[1]));
			cm.readOutput(args[0]);
			cm.printMatrix();
		} catch (Exception e) {
			System.out.println(""
					+ "\n################################################\n "
					+ "Usage : java CheckModel.java filename nbCartes\n "
					+ "Exemple : java CheckModel.java output.txt 20 "
					+ "\n################################################\n");
			e.printStackTrace();
		}
	}
}
