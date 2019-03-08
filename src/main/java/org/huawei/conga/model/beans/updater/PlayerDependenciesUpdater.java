package org.huawei.conga.model.beans.updater;

import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;

public interface PlayerDependenciesUpdater {

	void update(Agent[] agents, VarHelper vh);

}
