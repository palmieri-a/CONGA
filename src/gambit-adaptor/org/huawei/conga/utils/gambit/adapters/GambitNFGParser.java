package org.huawei.conga.utils.gambit.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class GambitNFGParser {

	private int nbPlayers;
	private int actions[];
	private int maxActionValue[];
	private int minActionValue[];
	/**
	 * Just use to test the class
	 */
	private ArrayList<int[]> allowed_tuples = new ArrayList<>();
	private Tuples allowed = new Tuples();
	int c_indexAction = 0;
	int c_index = 0;
	private AbstractGameModel model;

	public AbstractGameModel getModel() {
		return model;
	}

	public void parse(File f) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		readHeader(br);
		readStrategies(br);
		buildModel();
	}

	private void buildModel() {
		// create variable for each actions..
		this.model = new AbstractGameModel(actions.length) {

			@Override
			public void buildModel(Model s) {
				IntVar[] strat = new IntVar[nbPlayers];
				IntVar[] reward = new IntVar[nbPlayers];
				IntVar[] constraintedVar = new IntVar[2 * nbPlayers];
				for (int i = 0; i < actions.length; i++) {
					strat[i] = s.intVar("strat[" + i + "]", 0, actions[i] + 1);// VariableFactory.enumerated("strat_"+i,
																				// 0,actions[i]+1,
																				// s);
					reward[i] = s.intVar("reward[" + i + "]", minActionValue[i], maxActionValue[i]);
					players[i + 1].own(strat[i]);
					players[i + 1].setObjective(ResolutionPolicy.MAXIMIZE,reward[i]);

					constraintedVar[i] = reward[i];
					constraintedVar[nbPlayers + i] = strat[i];
				}
				s.table(constraintedVar, allowed,"MDD+").post();
			}
		};
	}

	private void readStrategies(BufferedReader br) {
		String line = null;
		try {
			while (!(line = br.readLine()).matches(".*\\d+.*")) {
			}
			String[] a = line.split(" ");
			int player = 0;
			int tuple[] = new int[nbPlayers * 2];
			for (String i : a) {
				if (player != 0 && (player % nbPlayers) == 0) {
					allowed.add(tuple);
					allowed_tuples.add(tuple);
					tuple = Arrays.copyOf(tuple, tuple.length);
					updateAction(tuple, nbPlayers);

				}
				tuple[player % nbPlayers] = (int) Double.parseDouble(i);
				maxActionValue[player % nbPlayers] = Math.max(maxActionValue[player % nbPlayers],
						tuple[player % nbPlayers]);
				minActionValue[player % nbPlayers] = Math.min(minActionValue[player % nbPlayers],
						tuple[player % nbPlayers]);
				player++;
			}

			allowed_tuples.add(tuple);
			allowed.add(tuple);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void updateAction(int[] tuple, int nbPlayers) {

		tuple[nbPlayers] = (tuple[nbPlayers] + 1) % actions[0];
		// must increment this other
		if (tuple[nbPlayers] == 0) {
			int index = 1;
			while (index < actions.length) {
				if (tuple[nbPlayers + index] < actions[index]) {
					tuple[nbPlayers + index]++;
					if (tuple[nbPlayers + index] % actions[index] == 0) {
						tuple[nbPlayers + index] = 0;
						updateAction(tuple, nbPlayers + index + 1);

					}
					return;
				}
				index++;
			}
		}
	}

	@SuppressWarnings({ "resource", "unchecked" })
	private void readHeader(BufferedReader br) {
		String line = null;
		try {
			while (!(line = br.readLine()).contains("{")) {
			}
			StringTokenizer st = new StringTokenizer(line, "{");
			// fist empty, second player
			st.nextElement();
			Scanner in = new Scanner(st.nextElement().toString()).useDelimiter("[^0-9]+");
			@SuppressWarnings("rawtypes")
			ArrayList strat = new ArrayList<>();
			while (in.hasNext()) {
				strat.add(in.nextInt());
			}
			this.actions = new int[strat.size()];
			this.nbPlayers = actions.length;
			maxActionValue = new int[actions.length];
			minActionValue = new int[actions.length];
			for (int i = 0; i < actions.length; i++) {
				actions[i] = (int) strat.get(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<int[]> getAllowed_tuples() {
		return allowed_tuples;
	}

	public int[] getActions() {
		return actions;
	}

	@Override
	public String toString() {
		return "nbPlayer=" + nbPlayers + ", actions:" + Arrays.toString(actions) + ", minAction: "
				+ Arrays.toString(this.minActionValue) + ", maxActions: " + Arrays.toString(this.maxActionValue);
	}
}
