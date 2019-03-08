package org.huawei.conga.tests.gambit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.ValidityTest;
import org.huawei.conga.algorithms.IBRAlgorithm;
import org.huawei.conga.model.factory.ConstraintFactory;
import org.huawei.conga.utils.gambit.adapters.GambitLauncher;
import org.huawei.conga.utils.gambit.adapters.GambitNFGParser;
import org.junit.Before;

public class AbstractValidityGambitTestClass extends ValidityTest {

	protected static final String PREFIX = "resources/gamut/";
	static {
		OS = System.getProperty("os.name").toLowerCase();
	}

	private GambitNFGParser gp;
	private static String GAMBIT_HOME = System.getenv("GAMBIT_HOME");

	private static final String GAMBIT_PATH = (GAMBIT_HOME + (GAMBIT_HOME.endsWith("/") ? "" : "/") + "gambit-enumpure"
			+ ((isWindows()) ? ".exe" : ""));
	private final File gambit_exe = new File(GAMBIT_PATH);

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	@Before
	public void setup() {
		gp = new GambitNFGParser();
	}
	

	public void testGame(String gameName) {
		System.out.println("IBR");
		testIBR(gameName);
		System.out.println("BOUND_CHECKER");
		launchGame(gameName, ConstraintFactory.BOUND_CONSTRAINT);
		System.out.println("NAIVE");
		launchGame(gameName, ConstraintFactory.LAST_LEVEL);
		
		
		
	}

	private void testIBR(String gameName) {
		gp = new GambitNFGParser();
		File game = new File(gameName);
		gp.parse(game);

		CongaSolver cg = new CongaSolver(gp.getModel());
		GambitLauncher gl = new GambitLauncher(gambit_exe, game, gp.getActions());
		gl.execute();
		
		IBRAlgorithm ibr =  new IBRAlgorithm(cg);
		ibr.execute();
		
		System.out.println(Arrays.deepToString(ibr.getTuple()));
		assertTrue(gl.getPNE().contains(ibr.getTuple()));
		
		
	}

	private void launchGame(String gameName, ConstraintFactory naiveConstraint) {
		File game = new File(gameName);
		gp.parse(game);
		CongaSolver cg = new CongaSolver(gp.getModel());
		cg.setConstraintBuilder(naiveConstraint);
		Solver s = cg.prepareAndGetSolver();
		GambitLauncher gl = new GambitLauncher(gambit_exe, game, gp.getActions());
		gl.execute();
		s.setSearch(Search.inputOrderLBSearch(cg.retrieveIntDecisionVar(true)));
		s.showSolutions();
		s.showContradiction();
		while (s.solve()) {
			assertNotNull(gl);
			assertNotNull(gl.getPNE());
			assertNotNull(cg.getVarHelper());
			assertNotNull(cg.getVarHelper().getDecisionVars());
			assertTrue(gl.getPNE().contains(retrieveSolution(s, cg.retrieveIntDecisionVar(true))));
		}

		for (Number[] element : gl.getPNE()) {
			System.out.println(Arrays.deepToString(element));
		}
		assertEquals(s.getSolutionCount(), gl.getPNE().getNbTuple());
	}

}
