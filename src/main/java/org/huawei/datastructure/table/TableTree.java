package org.huawei.datastructure.table;

public class TableTree extends Tree<Number>implements Table {

	private boolean isCreated;

	public TableTree(int deep, TreeNode<Number> pop) {
		super(deep, Number.class, pop);
		this.root = pop;
	}

	public TableTree(int deep) {
		super(deep, Number.class);
	}

	public TableTree(TableTree t, int order[]) {
		super(t.getDeep(), Number.class);
		this.addTable(new Integer[] {}, t);
		this.order = order;
	}

	public TableTree(int deep, int[] order) {
		super(deep, Number.class, order);
	}

	/**
	 * Return true if the prefix has been created, false otherwise
	 * 
	 * @param prefix
	 * @param tt
	 */
	public boolean addTable(Number[] prefix, TableTree tt) {
		isCreated = false;
		TreeNode<Number> previous;
		TreeNode<Number> next;
		TreeNode<Number> cNode = getAndCreatePrefix(prefix, tt);
		if (tt.getNbTuple() == 0) {
			return isCreated;
		}
		previous = cNode;
		for (Number[] integer : tt) {
			int index = 0;
			for (index = 0; index < integer.length; index++) {
				next = cNode.getChild(integer[index]);
				if (next == null) {
					break;
				}
				cNode = next;
			}
			if (index < tt.getDeep()) {
				this.nbTuple++;
			}
			fillNode(integer, index, cNode);
			cNode = previous;
		}
		return isCreated;
	}

	protected TreeNode<Number> getAndCreatePrefix(Number[] prefix, TableTree tt) {

		int size = prefix.length;
		TreeNode<Number> cNode = root, previous = root, next = null;
		if (prefix.length > 0) {
			while (cNode != null && size > 0) {
				previous = cNode;
				cNode = cNode.getChild(prefix[prefix.length - size]);
				// child doest exit
				if (cNode == null) {

					cNode = previous;
					break;
				}
				size--;
			}
		}
		if (size > 0) {
			this.isCreated = true;
			fillNode(prefix, prefix.length - size, previous);
			while (size > 0) {
				cNode = cNode.getChild(prefix[prefix.length - size--]);
			}
		}
		return cNode;
	}

	public void resetRootNode() {
		this.root = new TreeNode<Number>(null, null);
	}
	

}
