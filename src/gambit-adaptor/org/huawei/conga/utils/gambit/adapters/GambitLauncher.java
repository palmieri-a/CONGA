package org.huawei.conga.utils.gambit.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.huawei.datastructure.table.TableTree;

public class GambitLauncher {

	private TableTree PNE;
	private File gambitExec;
	private File game;
	private int[] actions;

	public GambitLauncher(File gambitExec, File game, int actions[]) {
		this.gambitExec = gambitExec;
		this.game = game;
		this.actions = actions;
		this.PNE = new TableTree(actions.length);
	}

	public void execute() {
		Process output;
		try {
			output = Runtime.getRuntime().exec(gambitExec.getAbsolutePath() + " " + game.getAbsolutePath());
			System.out.println("cmd: "+gambitExec.getAbsolutePath() + " " + game.getAbsolutePath());
			output.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(output.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] elem = line.split(",");
				Integer tuple[] = new Integer[actions.length];
				int nashPos = 0;
				int indexP = 0;
				for (int i = 1; i < elem.length; i++) {
					if (Integer.parseInt(elem[i]) == 1) {
						tuple[indexP] = nashPos;
					}
					nashPos++;
					if (nashPos % actions[indexP] == 0) {
						indexP++;
						nashPos = 0;
					}
				}
				System.out.println(Arrays.deepToString(tuple));
				PNE.add(tuple);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public TableTree getPNE() {
		return PNE;
	}
}
