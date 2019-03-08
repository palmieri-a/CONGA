package org.huawei.datastructure.graph;

import java.util.Comparator;

public class GraphComparator implements Comparator<IGraph> {

	public int compare(IGraph o1, IGraph o2) {
		if (o1.getNbNodes() != o2.getNbNodes()) {
			return -1;
		}
		for (int i = 0; i < o1.getNbNodes(); i++) {
			if (!o1.getNeigbors(i).equals(o2.getNeigbors(i))) {
				return -1;
			}
			;
		}
		return 0;
	}

}
