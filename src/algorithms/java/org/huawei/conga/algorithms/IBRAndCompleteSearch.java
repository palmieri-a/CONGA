package org.huawei.conga.algorithms;

public class IBRAndCompleteSearch implements Executable {

	private IBRAlgorithm ibr;
	private Executable completeSearch;

	public IBRAndCompleteSearch(final IBRAlgorithm ibr, final Executable completeSearch) {
		this.ibr = ibr;
		this.completeSearch=completeSearch;
	}
	
	@Override
	public void execute() {
		ibr.execute();
		assert ibr.isHasFoundSol();
		completeSearch.execute();
	}

}
