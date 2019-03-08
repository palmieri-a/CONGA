package org.huawei.conga;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Settings;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.algorithms.Nash;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.concepts.NashEquilibriumConcept;
import org.huawei.conga.equilibria.concepts.builder.EqConceptProvider;
import org.huawei.conga.equilibria.concepts.builder.NashBuilder;
import org.huawei.conga.equilibria.constraint.agent.builder.AgentPropagatorCheckerBuilder;
import org.huawei.conga.equilibria.constraint.agent.builder.AgentPropagatorsBuilder;
import org.huawei.conga.equilibria.constraint.builder.ConstraintBuilder;
import org.huawei.conga.equilibria.constraint.builder.MetaConstraintBuilder;
import org.huawei.conga.equilibria.constraint.builder.MetaConstraintLastLevelBuilder;
import org.huawei.conga.equilibria.nash.applyer.BFSRecorderApplyer;
import org.huawei.conga.equilibria.nash.brrecorder.builder.ISolutionRecorderBuilder;
import org.huawei.conga.equilibria.nash.brrecorder.builder.ISolutionRecorderImplBuilder;
import org.huawei.conga.equilibria.nash.verificator.NaiveLexVerificationOrder;
import org.huawei.conga.equilibria.nash.verificator.VerificationStrategy;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.beans.VariableDecorator;
import org.huawei.conga.model.beans.updater.NaiveDependenciesUpdater;
import org.huawei.conga.model.beans.updater.PlayerDependenciesUpdater;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.model.helper.VarHelperImpl;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.conga.search.policy.builder.AllBRBuilder;
import org.huawei.conga.search.policy.builder.ISearchPolicyBuilder;
import org.huawei.utils.provider.Provider;
import org.huawei.utils.provider.ProviderImpl;

public class CongaSolver {

	protected AbstractGameModel model;
	protected Model modelCSP, modelPlayerSolver;
	protected Solver csp, cspPlayer;
	protected Agent[] agents;
	protected final VarHelper varHelper = new VarHelperImpl();
	protected long seed=0;
	

	// parameters
	protected ISearchPolicy[] brPolicies;
	protected Provider<IEquilibriumConcept> equilibriumConcept = new ProviderImpl<IEquilibriumConcept>();
	protected PlayerDependenciesUpdater dependenciesProvider;
	protected ISolutionRecorderBuilder solutionRecorderBuilder = new ISolutionRecorderImplBuilder();
	protected AgentPropagatorsBuilder apb = new AgentPropagatorCheckerBuilder();
	protected ISearchPolicyBuilder policyBuilder = new AllBRBuilder();
	protected Provider<VerificationStrategy> strategiVerif = new ProviderImpl<VerificationStrategy>();
	// Builder
	protected EqConceptProvider eqBuilder = new NashBuilder();

	protected ConstraintBuilder constraintBuilder;
	private long modelConstructionTime;
	private Constraint[] nashConstraints;

	public CongaSolver(AbstractGameModel model) {
		this.model = model;
		modelCSP = new Model("CSP");
		modelCSP.set(new Settings() {
			 @Override
	            public boolean enableTableSubstitution() {
	            return false; }
		});
		csp = modelCSP.getSolver();
		csp.unplugAllSearchMonitors();
		modelPlayerSolver = new Model("Players");
		modelPlayerSolver.set(new Settings() {
	            @Override
	            public boolean checkDeclaredConstraints() {
	                return false;
	            }
	            @Override
	            public boolean enableTableSubstitution() {
	            return false; }
	            
	            @Override
	            public boolean checkModel(Solver solver) {
	            	return true;}
	        });
		cspPlayer = modelPlayerSolver.getSolver();
		cspPlayer.unplugAllSearchMonitors();
		
		// build the models
		model.buildModel(modelCSP);
		model.buildModel(modelPlayerSolver);
		//define optimization condition for the nash equilibria
		model.defineObjective(modelCSP);
		varHelper.init(modelCSP.getVars(), modelPlayerSolver.getVars(), model.getNBPlayers());
		model.init(this);
		retriveDecisionObjectiveVariables();

	}

	private void retriveDecisionObjectiveVariables() {
		for (int i = 1; i < agents.length; i++) {
			VariableDecorator[] objectivesCSP = agents[i].getObjectiveCSP();
			if (agents[i].hasObjectif()) {
				for (int j = 0; j < objectivesCSP.length; j++) {
					// if the objective is controled ..
					if (varHelper.getOwnerByVarID(objectivesCSP[j].getId()) == i) {
						varHelper.addDecisionObjective(i, objectivesCSP[j].getVariable().getId(),
								objectivesCSP[j].getVariable().getTypeAndKind());
					}
				}
			}
		}
	}


	public void initAgents(Agent[] players) {
		agents = new Agent[players.length];
		brPolicies = new ISearchPolicy[agents.length];
		System.arraycopy(players, 0, agents, 0, players.length);
	}

	public Solver prepareAndGetSolver() {
		prepareSolver();
		return modelCSP.getSolver();
	}

	public Solver getSolver() {
		return modelCSP.getSolver();
	}

	private void prepareSolver() {

		for (int i = 0; i < brPolicies.length; i++) {
			brPolicies[i] = policyBuilder.build(agents[i]);
		}
		// dependencies provider see Graphical Constraint Games
		if (this.dependenciesProvider == null) {
			this.dependenciesProvider = new NaiveDependenciesUpdater();
		}
		dependenciesProvider.update(agents, varHelper);


		// eq concept

		if (constraintBuilder == null) {
			constraintBuilder = new MetaConstraintLastLevelBuilder();
		}
		
		if(this.equilibriumConcept.isNull()){
			this.equilibriumConcept.set(new NashEquilibriumConcept(cspPlayer, this, new BFSRecorderApplyer() ));
		}

		if (strategiVerif.isNull()) {
			assert agents!=null;
			strategiVerif.set(new NaiveLexVerificationOrder(agents));
		}
		modelCSP.post(buildConstraints());
		
		
		modelConstructionTime = System.currentTimeMillis() - model.gettInit();
	}

	public Constraint[] buildConstraints() {
		this.nashConstraints=constraintBuilder.build(agents, equilibriumConcept,this.strategiVerif,cspPlayer, varHelper, apb,
				seed);
		return this.nashConstraints;
	}

	public void setBRPolicy(int indexAgent, ISearchPolicy isp) {
		this.brPolicies[indexAgent] = isp;
	}

	public Agent[] getAgents() {
		return agents;
	}

	public long getModelConstructionTime() {
		return modelConstructionTime;
	}

	public void setEquilibriumConcept(IEquilibriumConcept method) {
		this.equilibriumConcept.set(method);
		method.initCSP(csp, cspPlayer);
	}


	public final VarHelper getVarHelper() {
		return varHelper;
	}

	public ISearchPolicy getBrPolicies(int agent) {
		return brPolicies[agent];
	}


	@Deprecated
	public void setAgentPropagatorsBuilder(AgentPropagatorsBuilder apb) {
		this.apb = apb;
	}



	public void setConstraintBuilder(ConstraintBuilder constraitBuilder) {
		this.constraintBuilder = constraitBuilder;
	}


	public AbstractGameModel getGameModel() {
		return model;
	}

	public void setDependenciesProvider(PlayerDependenciesUpdater dependenciesProvider) {
		this.dependenciesProvider = dependenciesProvider;
	}

	public void setSolutionRecorderBuilder(ISolutionRecorderBuilder solutionRecorderBuilder) {
		this.solutionRecorderBuilder = solutionRecorderBuilder;
	}

	public void setPolicyBuilder(ISearchPolicyBuilder policyBuilder) {
		this.policyBuilder = policyBuilder;
	}

	public Provider<IEquilibriumConcept> getEquilibriumConcept() {
		return equilibriumConcept;
	}

	public IntVar[] retrieveIntDecisionVar(boolean boolVar) {
		int nbIntDvar =0;
		for (int i = 1; i < agents.length; i++) {
			nbIntDvar+= agents[i].getNbIntVars();	
		};
		IntVar[]intDVar = new IntVar[nbIntDvar];
		int idx =0;
		for (int i = 0; i < varHelper.getDecisionVars().length; i++) {
			if(((varHelper.getDecisionVars()[i].getTypeAndKind()& Variable.BOOL) !=0 )&& boolVar){
				intDVar[idx++]=(IntVar) varHelper.getDecisionVars()[i];
			}else if(((varHelper.getDecisionVars()[i].getTypeAndKind()& Variable.INT) !=0 )){
				intDVar[idx++]=(IntVar) varHelper.getDecisionVars()[i];
			}
			
		}
		return intDVar;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	public long getSeed() {
		return seed;
	}

	public IntVar[] retrieveIntDecisionVarCSPPlayer(){
		ArrayList<IntVar> vars = new ArrayList<>();
		for (int i = 1; i < agents.length; i++) {
			for (int j = 0; j < agents[i].getNbIntVars(); j++) {
				vars.add(agents[i].getIntVariablesCSPPlayer()[j]);
			}
		}
		IntVar varsArray[] = new IntVar[vars.size()];
		vars.toArray(varsArray);
		return varsArray;
	}
	
	public Constraint[] getNashConstraints() {
		return nashConstraints;
	}
}
