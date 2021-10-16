package dobble;

import java.util.ArrayList;
import java.util.HashMap;
import com.microsoft.z3.*;

public class Dobble {

	/** Il existe 57 symboles différents */
	public final int NB_SYMBOLES = 57;
	/** Nombre de cartes choisi plus tard (55 dans le jeu complet) */
	public final int NB_CARTES;
	/** Nombre de cartes déjà déterminées par le modèle  NE PAS MODIFIER dans la ligne ce qui suit ce qui se trouve avant le "=" */
    public final int NB_CARTES_DEJA_GENEREES = 0;

	/** Le modèle du jeu de Dobble */
	BoolExpr[][] v; // 1ère dimension : le numéro de chaque carte
					// 2ème dimension : la possibilité de placer le symbole sur la carte
					// v[i][j] = TRUE <=> sur la carte n°i on trouve le symbole j
	HashMap<String, String> cfg;

	Solver solver;
	Context context;

	public Dobble(int nb_cartes) {
		this.NB_CARTES = nb_cartes;
		this.v = new BoolExpr[this.NB_CARTES][this.NB_SYMBOLES];
		this.cfg = new HashMap<String, String>();
		cfg.put("model", "true");
		cfg.put("proof", "true");
		this.context = new Context(cfg);
		this.solver = this.context.mkSimpleSolver();

		// création des nb_cartes * nb_symboles variables booléenes du problème
		for (int i = 0; i < this.NB_CARTES; i++) {
			for (int j = 0; j < this.NB_SYMBOLES; j++) {
				v[i][j] = context.mkBoolConst("v" + "_" + i + "_" + j);
			}
		}

		// On trouve exactement 8 symboles sur chaque carte
		for (int i = 0; i < this.NB_CARTES; i++) {
			solver.add(context.mkAtLeast(v[i], 8));
			solver.add(context.mkAtMost(v[i], 8));
		}

		// deux cartes du jeu possèdent exactement un symbole en commun
		for (int i = 0; i < this.NB_CARTES; i++) {
			for (int j = i + 1; j < this.NB_CARTES; j++) {
				/** Si les cartes 0..N ont déjà été générées, la contrainte
				 *     "1 seul symbole commun entre toutes les cartes du modèle", devient
				 *  -> "1 seul symbole commun entre les cartes ajoutées et les anciennes"
				 *  
				 *  Donc le problème diminue en complexité.
				 */
				if (i >= NB_CARTES_DEJA_GENEREES || j >= NB_CARTES_DEJA_GENEREES) {
					ArrayList<BoolExpr> conjuncts = new ArrayList<>();
					for (int k = 0; k < this.NB_SYMBOLES; k++) {
						for (int p = 0; p < this.NB_SYMBOLES; p++) {
							if (k == p) {
								BoolExpr SymbIdentiques = context.mkAnd(v[i][k], v[j][p]);
								conjuncts.add(SymbIdentiques);
							}
						}
					}
					BoolExpr[] nbIdentiques = conjuncts.stream().toArray(BoolExpr[]::new);
					solver.add(exactlyOne(nbIdentiques));
				}
			}
		}


		
		
		// Determiner les N cartes du modele    <--    (ne PAS modifier ce commentaire)		
		
		
		
		
		// Nier la solution en cours pour en obtenir une nouvelle    <--    (ne PAS modifier ce commentaire)
		



		// Jeu Dobble
		
		/**
		solver.add(context.mkAnd(v[0][1]));
		solver.add(context.mkAnd(v[0][15]));
		solver.add(context.mkAnd(v[0][27]));
		solver.add(context.mkAnd(v[0][38]));
		solver.add(context.mkAnd(v[0][43]));
		solver.add(context.mkAnd(v[0][50]));
		solver.add(context.mkAnd(v[0][53]));
		solver.add(context.mkAnd(v[0][54]));
		solver.add(context.mkAnd(v[1][5]));
		solver.add(context.mkAnd(v[1][14]));
		solver.add(context.mkAnd(v[1][21]));
		solver.add(context.mkAnd(v[1][24]));
		solver.add(context.mkAnd(v[1][31]));
		solver.add(context.mkAnd(v[1][33]));
		solver.add(context.mkAnd(v[1][39]));
		solver.add(context.mkAnd(v[1][54]));
		solver.add(context.mkAnd(v[2][7]));
		solver.add(context.mkAnd(v[2][12]));
		solver.add(context.mkAnd(v[2][18]));
		solver.add(context.mkAnd(v[2][22]));
		solver.add(context.mkAnd(v[2][33]));
		solver.add(context.mkAnd(v[2][36]));
		solver.add(context.mkAnd(v[2][41]));
		solver.add(context.mkAnd(v[2][43]));
		solver.add(context.mkAnd(v[3][2]));
		solver.add(context.mkAnd(v[3][10]));
		solver.add(context.mkAnd(v[3][28]));
		solver.add(context.mkAnd(v[3][36]));
		solver.add(context.mkAnd(v[3][47]));
		solver.add(context.mkAnd(v[3][54]));
		solver.add(context.mkAnd(v[3][55]));
		solver.add(context.mkAnd(v[3][56]));
		solver.add(context.mkAnd(v[4][3]));
		solver.add(context.mkAnd(v[4][17]));
		solver.add(context.mkAnd(v[4][27]));
		solver.add(context.mkAnd(v[4][32]));
		solver.add(context.mkAnd(v[4][33]));
		solver.add(context.mkAnd(v[4][35]));
		solver.add(context.mkAnd(v[4][45]));
		solver.add(context.mkAnd(v[4][56]));
		solver.add(context.mkAnd(v[5][5]));
		solver.add(context.mkAnd(v[5][9]));
		solver.add(context.mkAnd(v[5][10]));
		solver.add(context.mkAnd(v[5][26]));
		solver.add(context.mkAnd(v[5][29]));
		solver.add(context.mkAnd(v[5][43]));
		solver.add(context.mkAnd(v[5][45]));
		solver.add(context.mkAnd(v[5][48]));
		solver.add(context.mkAnd(v[6][0]));
		solver.add(context.mkAnd(v[6][1]));
		solver.add(context.mkAnd(v[6][2]));
		solver.add(context.mkAnd(v[6][3]));
		solver.add(context.mkAnd(v[6][4]));
		solver.add(context.mkAnd(v[6][5]));
		solver.add(context.mkAnd(v[6][6]));
		solver.add(context.mkAnd(v[6][7]));
		solver.add(context.mkAnd(v[7][7]));
		solver.add(context.mkAnd(v[7][8]));
		solver.add(context.mkAnd(v[7][16]));
		solver.add(context.mkAnd(v[7][25]));
		solver.add(context.mkAnd(v[7][31]));
		solver.add(context.mkAnd(v[7][45]));
		solver.add(context.mkAnd(v[7][47]));
		solver.add(context.mkAnd(v[7][53]));
		solver.add(context.mkAnd(v[8][1]));
		solver.add(context.mkAnd(v[8][16]));
		solver.add(context.mkAnd(v[8][18]));
		solver.add(context.mkAnd(v[8][29]));
		solver.add(context.mkAnd(v[8][32]));
		solver.add(context.mkAnd(v[8][39]));
		solver.add(context.mkAnd(v[8][42]));
		solver.add(context.mkAnd(v[8][55]));
		solver.add(context.mkAnd(v[9][6]));
		solver.add(context.mkAnd(v[9][13]));
		solver.add(context.mkAnd(v[9][22]));
		solver.add(context.mkAnd(v[9][40]));
		solver.add(context.mkAnd(v[9][42]));
		solver.add(context.mkAnd(v[9][45]));
		solver.add(context.mkAnd(v[9][51]));
		solver.add(context.mkAnd(v[9][54]));
		solver.add(context.mkAnd(v[10][2]));
		solver.add(context.mkAnd(v[10][9]));
		solver.add(context.mkAnd(v[10][12]));
		solver.add(context.mkAnd(v[10][27]));
		solver.add(context.mkAnd(v[10][31]));
		solver.add(context.mkAnd(v[10][37]));
		solver.add(context.mkAnd(v[10][42]));
		solver.add(context.mkAnd(v[10][44]));
		solver.add(context.mkAnd(v[11][1]));
		solver.add(context.mkAnd(v[11][10]));
		solver.add(context.mkAnd(v[11][13]));
		solver.add(context.mkAnd(v[11][25]));
		solver.add(context.mkAnd(v[11][33]));
		solver.add(context.mkAnd(v[11][37]));
		solver.add(context.mkAnd(v[11][49]));
		solver.add(context.mkAnd(v[11][52]));
		solver.add(context.mkAnd(v[12][1]));
		solver.add(context.mkAnd(v[12][11]));
		solver.add(context.mkAnd(v[12][20]));
		solver.add(context.mkAnd(v[12][21]));
		solver.add(context.mkAnd(v[12][36]));
		solver.add(context.mkAnd(v[12][44]));
		solver.add(context.mkAnd(v[12][45]));
		solver.add(context.mkAnd(v[12][46]));
		solver.add(context.mkAnd(v[13][3]));
		solver.add(context.mkAnd(v[13][16]));
		solver.add(context.mkAnd(v[13][26]));
		solver.add(context.mkAnd(v[13][30]));
		solver.add(context.mkAnd(v[13][41]));
		solver.add(context.mkAnd(v[13][44]));
		solver.add(context.mkAnd(v[13][49]));
		solver.add(context.mkAnd(v[13][54]));
		solver.add(context.mkAnd(v[14][0]));
		solver.add(context.mkAnd(v[14][9]));
		solver.add(context.mkAnd(v[14][16]));
		solver.add(context.mkAnd(v[14][20]));
		solver.add(context.mkAnd(v[14][28]));
		solver.add(context.mkAnd(v[14][33]));
		solver.add(context.mkAnd(v[14][50]));
		solver.add(context.mkAnd(v[14][51]));
		solver.add(context.mkAnd(v[15][1]));
		solver.add(context.mkAnd(v[15][8]));
		solver.add(context.mkAnd(v[15][12]));
		solver.add(context.mkAnd(v[15][14]));
		solver.add(context.mkAnd(v[15][26]));
		solver.add(context.mkAnd(v[15][34]));
		solver.add(context.mkAnd(v[15][51]));
		solver.add(context.mkAnd(v[15][56]));
		solver.add(context.mkAnd(v[16][6]));
		solver.add(context.mkAnd(v[16][23]));
		solver.add(context.mkAnd(v[16][33]));
		solver.add(context.mkAnd(v[16][34]));
		solver.add(context.mkAnd(v[16][44]));
		solver.add(context.mkAnd(v[16][48]));
		solver.add(context.mkAnd(v[16][53]));
		solver.add(context.mkAnd(v[16][55]));
		solver.add(context.mkAnd(v[17][4]));
		solver.add(context.mkAnd(v[17][14]));
		solver.add(context.mkAnd(v[17][16]));
		solver.add(context.mkAnd(v[17][27]));
		solver.add(context.mkAnd(v[17][36]));
		solver.add(context.mkAnd(v[17][40]));
		solver.add(context.mkAnd(v[17][48]));
		solver.add(context.mkAnd(v[17][52]));
		solver.add(context.mkAnd(v[18][6]));
		solver.add(context.mkAnd(v[18][10]));
		solver.add(context.mkAnd(v[18][12]));
		solver.add(context.mkAnd(v[18][15]));
		solver.add(context.mkAnd(v[18][16]));
		solver.add(context.mkAnd(v[18][17]));
		solver.add(context.mkAnd(v[18][19]));
		solver.add(context.mkAnd(v[18][21]));
		solver.add(context.mkAnd(v[19][0]));
		solver.add(context.mkAnd(v[19][19]));
		solver.add(context.mkAnd(v[19][25]));
		solver.add(context.mkAnd(v[19][39]));
		solver.add(context.mkAnd(v[19][40]));
		solver.add(context.mkAnd(v[19][43]));
		solver.add(context.mkAnd(v[19][44]));
		solver.add(context.mkAnd(v[19][56]));
		solver.add(context.mkAnd(v[20][4]));
		solver.add(context.mkAnd(v[20][11]));
		solver.add(context.mkAnd(v[20][19]));
		solver.add(context.mkAnd(v[20][26]));
		solver.add(context.mkAnd(v[20][33]));
		solver.add(context.mkAnd(v[20][38]));
		solver.add(context.mkAnd(v[20][42]));
		solver.add(context.mkAnd(v[20][47]));
		solver.add(context.mkAnd(v[21][7]));
		solver.add(context.mkAnd(v[21][15]));
		solver.add(context.mkAnd(v[21][20]));
		solver.add(context.mkAnd(v[21][24]));
		solver.add(context.mkAnd(v[21][42]));
		solver.add(context.mkAnd(v[21][48]));
		solver.add(context.mkAnd(v[21][49]));
		solver.add(context.mkAnd(v[21][56]));
		solver.add(context.mkAnd(v[22][6]));
		solver.add(context.mkAnd(v[22][18]));
		solver.add(context.mkAnd(v[22][24]));
		solver.add(context.mkAnd(v[22][25]));
		solver.add(context.mkAnd(v[22][26]));
		solver.add(context.mkAnd(v[22][27]));
		solver.add(context.mkAnd(v[22][28]));
		solver.add(context.mkAnd(v[22][46]));
		solver.add(context.mkAnd(v[23][1]));
		solver.add(context.mkAnd(v[23][9]));
		solver.add(context.mkAnd(v[23][17]));
		solver.add(context.mkAnd(v[23][23]));
		solver.add(context.mkAnd(v[23][24]));
		solver.add(context.mkAnd(v[23][40]));
		solver.add(context.mkAnd(v[23][41]));
		solver.add(context.mkAnd(v[23][47]));
		solver.add(context.mkAnd(v[24][1]));
		solver.add(context.mkAnd(v[24][19]));
		solver.add(context.mkAnd(v[24][22]));
		solver.add(context.mkAnd(v[24][28]));
		solver.add(context.mkAnd(v[24][30]));
		solver.add(context.mkAnd(v[24][31]));
		solver.add(context.mkAnd(v[24][35]));
		solver.add(context.mkAnd(v[24][48]));
		solver.add(context.mkAnd(v[25][4]));
		solver.add(context.mkAnd(v[25][17]));
		solver.add(context.mkAnd(v[25][31]));
		solver.add(context.mkAnd(v[25][43]));
		solver.add(context.mkAnd(v[25][46]));
		solver.add(context.mkAnd(v[25][49]));
		solver.add(context.mkAnd(v[25][51]));
		solver.add(context.mkAnd(v[25][55]));
		solver.add(context.mkAnd(v[26][7]));
		solver.add(context.mkAnd(v[26][13]));
		solver.add(context.mkAnd(v[26][14]));
		solver.add(context.mkAnd(v[26][17]));
		solver.add(context.mkAnd(v[26][28]));
		solver.add(context.mkAnd(v[26][29]));
		solver.add(context.mkAnd(v[26][38]));
		solver.add(context.mkAnd(v[26][44]));
		solver.add(context.mkAnd(v[27][5]));
		solver.add(context.mkAnd(v[27][11]));
		solver.add(context.mkAnd(v[27][12]));
		solver.add(context.mkAnd(v[27][28]));
		solver.add(context.mkAnd(v[27][32]));
		solver.add(context.mkAnd(v[27][40]));
		solver.add(context.mkAnd(v[27][49]));
		solver.add(context.mkAnd(v[27][53]));
		solver.add(context.mkAnd(v[28][5]));
		solver.add(context.mkAnd(v[28][15]));
		solver.add(context.mkAnd(v[28][18]));
		solver.add(context.mkAnd(v[28][35]));
		solver.add(context.mkAnd(v[28][44]));
		solver.add(context.mkAnd(v[28][47]));
		solver.add(context.mkAnd(v[28][51]));
		solver.add(context.mkAnd(v[28][52]));
		solver.add(context.mkAnd(v[29][5]));
		solver.add(context.mkAnd(v[29][17]));
		solver.add(context.mkAnd(v[29][25]));
		solver.add(context.mkAnd(v[29][30]));
		solver.add(context.mkAnd(v[29][34]));
		solver.add(context.mkAnd(v[29][36]));
		solver.add(context.mkAnd(v[29][42]));
		solver.add(context.mkAnd(v[29][50]));
		solver.add(context.mkAnd(v[30][2]));
		solver.add(context.mkAnd(v[30][8]));
		solver.add(context.mkAnd(v[30][15]));
		solver.add(context.mkAnd(v[30][29]));
		solver.add(context.mkAnd(v[30][30]));
		solver.add(context.mkAnd(v[30][33]));
		solver.add(context.mkAnd(v[30][40]));
		solver.add(context.mkAnd(v[30][46]));
		solver.add(context.mkAnd(v[31][7]));
		solver.add(context.mkAnd(v[31][9]));
		solver.add(context.mkAnd(v[31][19]));
		solver.add(context.mkAnd(v[31][32]));
		solver.add(context.mkAnd(v[31][34]));
		solver.add(context.mkAnd(v[31][46]));
		solver.add(context.mkAnd(v[31][52]));
		solver.add(context.mkAnd(v[31][54]));
		solver.add(context.mkAnd(v[32][0]));
		solver.add(context.mkAnd(v[32][8]));
		solver.add(context.mkAnd(v[32][11]));
		solver.add(context.mkAnd(v[32][17]));
		solver.add(context.mkAnd(v[32][18]));
		solver.add(context.mkAnd(v[32][37]));
		solver.add(context.mkAnd(v[32][48]));
		solver.add(context.mkAnd(v[32][54]));
		solver.add(context.mkAnd(v[33][5]));
		solver.add(context.mkAnd(v[33][8]));
		solver.add(context.mkAnd(v[33][13]));
		solver.add(context.mkAnd(v[33][19]));
		solver.add(context.mkAnd(v[33][20]));
		solver.add(context.mkAnd(v[33][27]));
		solver.add(context.mkAnd(v[33][41]));
		solver.add(context.mkAnd(v[33][55]));
		solver.add(context.mkAnd(v[34][4]));
		solver.add(context.mkAnd(v[34][12]));
		solver.add(context.mkAnd(v[34][20]));
		solver.add(context.mkAnd(v[34][23]));
		solver.add(context.mkAnd(v[34][25]));
		solver.add(context.mkAnd(v[34][29]));
		solver.add(context.mkAnd(v[34][35]));
		solver.add(context.mkAnd(v[34][54]));
		solver.add(context.mkAnd(v[35][4]));
		solver.add(context.mkAnd(v[35][8]));
		solver.add(context.mkAnd(v[35][10]));
		solver.add(context.mkAnd(v[35][22]));
		solver.add(context.mkAnd(v[35][24]));
		solver.add(context.mkAnd(v[35][32]));
		solver.add(context.mkAnd(v[35][44]));
		solver.add(context.mkAnd(v[35][50]));
		solver.add(context.mkAnd(v[36][3]));
		solver.add(context.mkAnd(v[36][10]));
		solver.add(context.mkAnd(v[36][18]));
		solver.add(context.mkAnd(v[36][20]));
		solver.add(context.mkAnd(v[36][31]));
		solver.add(context.mkAnd(v[36][34]));
		solver.add(context.mkAnd(v[36][38]));
		solver.add(context.mkAnd(v[36][40]));
		solver.add(context.mkAnd(v[37][3]));
		solver.add(context.mkAnd(v[37][8]));
		solver.add(context.mkAnd(v[37][21]));
		solver.add(context.mkAnd(v[37][23]));
		solver.add(context.mkAnd(v[37][28]));
		solver.add(context.mkAnd(v[37][42]));
		solver.add(context.mkAnd(v[37][43]));
		solver.add(context.mkAnd(v[37][52]));
		solver.add(context.mkAnd(v[38][7]));
		solver.add(context.mkAnd(v[38][10]));
		solver.add(context.mkAnd(v[38][11]));
		solver.add(context.mkAnd(v[38][23]));
		solver.add(context.mkAnd(v[38][27]));
		solver.add(context.mkAnd(v[38][30]));
		solver.add(context.mkAnd(v[38][39]));
		solver.add(context.mkAnd(v[38][51]));
		solver.add(context.mkAnd(v[39][0]));
		solver.add(context.mkAnd(v[39][13]));
		solver.add(context.mkAnd(v[39][15]));
		solver.add(context.mkAnd(v[39][23]));
		solver.add(context.mkAnd(v[39][26]));
		solver.add(context.mkAnd(v[39][31]));
		solver.add(context.mkAnd(v[39][32]));
		solver.add(context.mkAnd(v[39][36]));
		solver.add(context.mkAnd(v[40][6]));
		solver.add(context.mkAnd(v[40][8]));
		solver.add(context.mkAnd(v[40][9]));
		solver.add(context.mkAnd(v[40][35]));
		solver.add(context.mkAnd(v[40][36]));
		solver.add(context.mkAnd(v[40][38]));
		solver.add(context.mkAnd(v[40][39]));
		solver.add(context.mkAnd(v[40][49]));
		solver.add(context.mkAnd(v[41][0]));
		solver.add(context.mkAnd(v[41][10]));
		solver.add(context.mkAnd(v[41][14]));
		solver.add(context.mkAnd(v[41][35]));
		solver.add(context.mkAnd(v[41][41]));
		solver.add(context.mkAnd(v[41][42]));
		solver.add(context.mkAnd(v[41][46]));
		solver.add(context.mkAnd(v[41][53]));
		solver.add(context.mkAnd(v[42][0]));
		solver.add(context.mkAnd(v[42][21]));
		solver.add(context.mkAnd(v[42][22]));
		solver.add(context.mkAnd(v[42][27]));
		solver.add(context.mkAnd(v[42][29]));
		solver.add(context.mkAnd(v[42][34]));
		solver.add(context.mkAnd(v[42][47]));
		solver.add(context.mkAnd(v[42][49]));
		solver.add(context.mkAnd(v[43][3]));
		solver.add(context.mkAnd(v[43][19]));
		solver.add(context.mkAnd(v[43][24]));
		solver.add(context.mkAnd(v[43][29]));
		solver.add(context.mkAnd(v[43][36]));
		solver.add(context.mkAnd(v[43][37]));
		solver.add(context.mkAnd(v[43][51]));
		solver.add(context.mkAnd(v[43][53]));
		solver.add(context.mkAnd(v[44][4]));
		solver.add(context.mkAnd(v[44][15]));
		solver.add(context.mkAnd(v[44][28]));
		solver.add(context.mkAnd(v[44][34]));
		solver.add(context.mkAnd(v[44][37]));
		solver.add(context.mkAnd(v[44][39]));
		solver.add(context.mkAnd(v[44][41]));
		solver.add(context.mkAnd(v[44][45]));
		solver.add(context.mkAnd(v[45][3]));
		solver.add(context.mkAnd(v[45][9]));
		solver.add(context.mkAnd(v[45][11]));
		solver.add(context.mkAnd(v[45][14]));
		solver.add(context.mkAnd(v[45][15]));
		solver.add(context.mkAnd(v[45][22]));
		solver.add(context.mkAnd(v[45][25]));
		solver.add(context.mkAnd(v[45][55]));
		solver.add(context.mkAnd(v[46][2]));
		solver.add(context.mkAnd(v[46][11]));
		solver.add(context.mkAnd(v[46][13]));
		solver.add(context.mkAnd(v[46][16]));
		solver.add(context.mkAnd(v[46][24]));
		solver.add(context.mkAnd(v[46][34]));
		solver.add(context.mkAnd(v[46][35]));
		solver.add(context.mkAnd(v[46][43]));
		solver.add(context.mkAnd(v[47][2]));
		solver.add(context.mkAnd(v[47][14]));
		solver.add(context.mkAnd(v[47][18]));
		solver.add(context.mkAnd(v[47][19]));
		solver.add(context.mkAnd(v[47][23]));
		solver.add(context.mkAnd(v[47][45]));
		solver.add(context.mkAnd(v[47][49]));
		solver.add(context.mkAnd(v[47][50]));
		solver.add(context.mkAnd(v[48][6]));
		solver.add(context.mkAnd(v[48][11]));
		solver.add(context.mkAnd(v[48][29]));
		solver.add(context.mkAnd(v[48][31]));
		solver.add(context.mkAnd(v[48][41]));
		solver.add(context.mkAnd(v[48][50]));
		solver.add(context.mkAnd(v[48][52]));
		solver.add(context.mkAnd(v[48][56]));
		solver.add(context.mkAnd(v[49][4]));
		solver.add(context.mkAnd(v[49][9]));
		solver.add(context.mkAnd(v[49][13]));
		solver.add(context.mkAnd(v[49][18]));
		solver.add(context.mkAnd(v[49][21]));
		solver.add(context.mkAnd(v[49][30]));
		solver.add(context.mkAnd(v[49][53]));
		solver.add(context.mkAnd(v[49][56]));
		solver.add(context.mkAnd(v[50][5]));
		solver.add(context.mkAnd(v[50][16]));
		solver.add(context.mkAnd(v[50][22]));
		solver.add(context.mkAnd(v[50][23]));
		solver.add(context.mkAnd(v[50][37]));
		solver.add(context.mkAnd(v[50][38]));
		solver.add(context.mkAnd(v[50][46]));
		solver.add(context.mkAnd(v[50][56]));
		*/



	}

	/** Expression vraie ssi exactement une des exprs est vraie. */
	private BoolExpr exactlyOne(BoolExpr... exprs) {
		return context.mkAnd(context.mkOr(exprs), atMostOne(exprs));
	}

	/** Expression vraie ssi au plus une des exprs est vraie. */
	private BoolExpr atMostOne(BoolExpr... exprs) {
		ArrayList<BoolExpr> conjuncts = new ArrayList<>();

		for (BoolExpr expr : exprs) {
			ArrayList<BoolExpr> otherExprs = new ArrayList<>();
			for (BoolExpr e : exprs) {
				if (e != expr) {
					otherExprs.add(e);
				}
			}
			BoolExpr bigOr = context.mkOr(otherExprs.stream().toArray(BoolExpr[]::new));
			BoolExpr res = context.mkImplies(expr, context.mkNot(bigOr));
			conjuncts.add(res);
		}
		return context.mkAnd(conjuncts.stream().toArray(BoolExpr[]::new));
	}

	static Model check(Solver solver) {
		if (solver.check() == Status.SATISFIABLE) {
			return solver.getModel();
		} else {
			return null;
		}
	}

	static void checkAndPrint(Solver solver) {

		System.out.println("\n\n*** NumAssertions : " + solver.getNumAssertions());
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//System.out.println("\n\n*** NumScopes : " + solver.getNumScopes());
		//System.out.println("\n\n*** Statistics : " + solver.getStatistics());

		Status q = solver.check();
		switch (q) {
		case UNKNOWN:
			System.out.println("  Unknown because:\n" + solver.getReasonUnknown());
			break;
		case SATISFIABLE:
			System.out.println("  SAT, model:\n" + solver.getModel());
			break;
		case UNSATISFIABLE:
			System.out.println("UNSAT :'( ");
			// System.out.println(" UNSAT, proof:\n" + solver.getProof());
			break;
		}
	}

	public void solveGame() {
		Dobble.checkAndPrint(this.solver);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("" + "\n####################################\n" + " Usage : java Dobble.java nb_cartes\n"
					+ " Exemple : java Dobble.java 10" + "\n####################################\n");
			System.exit(1);
		}
		int nbCartes = 0;
		try {
			nbCartes = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("" + "\n####################################\n" + " Usage : java Dobble.java nb_cartes\n"
					+ " Exemple : java Dobble.java 10" + "\n####################################\n");
			e.printStackTrace();
			System.exit(1);
		}

		Dobble dobble = new Dobble(nbCartes);

		long start = System.currentTimeMillis();
		dobble.solveGame();
		long end = System.currentTimeMillis();
		System.out.println("Time : " + (end - start) + " ms");
	}
}
