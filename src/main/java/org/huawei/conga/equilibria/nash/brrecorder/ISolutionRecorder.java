package org.huawei.conga.equilibria.nash.brrecorder;

import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.huawei.conga.model.beans.Agent;
import org.huawei.datastructure.table.TableTree;

public interface ISolutionRecorder extends IMonitorSolution {

	TableTree getData();

	void init(Agent agent);

	void reset();
}
