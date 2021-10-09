package dobble;

import java.util.ArrayList;
import java.util.HashMap;
import com.microsoft.z3.*;

public class Dobble {

	/** Il existe 57 symboles différents */
	public final int NB_SYMBOLES = 57;
	/** Nombre de cartes choisi plus tard (55 dans le jeu complet) */
	public final int NB_CARTES;

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

		System.out.println(solver.getStatistics());

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
			System.out.println(""
					+ "\n####################################\n" 
					+ " Usage : java Dobble.java nb_cartes\n"
					+ " Exemple : java Dobble.java 10"
					+ "\n####################################\n");
			System.exit(1);
		}
		int nbCartes = 0;
		try {
			nbCartes = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println(""
					+ "\n####################################\n" 
					+ " Usage : java Dobble.java nb_cartes\n"
					+ " Exemple : java Dobble.java 10"
					+ "\n####################################\n");
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
