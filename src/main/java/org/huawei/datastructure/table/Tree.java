package org.huawei.datastructure.table;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Tree<T> implements Iterable<T[]> {

	protected TreeNode<T> root = new TreeNode<T>(null, null);
	protected int nbTuple = 0;
	protected final int deep;
	protected final Class<T> type;
	/**
	 * The order is used to transform incrementally the tuple iterated.
	 */
	protected int[] order;
	protected int[] reverseOrder;

	public Tree(final int deep, Class<T> type) {
		this.deep = deep;
		this.type = type;
		this.order = new int[deep];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
	}

	public Tree(int deep, Class<T> type, int[] order) {
		this.deep = deep;
		this.type = type;
		this.order = order;
	}

	public Tree(int deep, TreeNode<T> root, Class<T> type, int[] order) {
		this.deep = deep;
		this.type = type;
		this.order = order;
		this.root = root;
	}

	public Tree(int deep, Class<T> type, TreeNode<T> pop) {
		this(deep, type);
		this.root = pop;
	}

	public int getNbTuple() {
		return nbTuple;
	}

	public int getDeep() {
		return deep;
	}

	public void add(T[] tuple) {

		// the current node;
		TreeNode<T> cNode = root, next = root;
		int index = 0;
		for (index = 0; index < tuple.length; index++) {
			next = cNode.getChild(tuple[index]);
			if (next == null) {
				break;
			}
			cNode = next;
		}

		// while it exits a child corresponding to the tuple element
		if (index < deep) {
			fillNode(tuple, index, cNode);
			nbTuple++;
		}
	}

	protected void fillNode(T[] tuple, int index, TreeNode<T> cNode) {
		assert cNode != null;
		for (int i = index; i < tuple.length; i++) {
			TreeNode<T> child = createNode(tuple[i], cNode);
			cNode.addChild(child);
			cNode = child;
		}
	}

	protected TreeNode<T> createNode(T t, TreeNode<T> cNode) {
		return new TreeNode<T>(t, cNode);
	}

	public void remove(T[] tuple) {
		this.root.removeChild(tuple, 0);
		nbTuple--;
	}

	public boolean contains(T[] tuple) {
		if (tuple.length > deep) {
			return false;
		}
		TreeNode<T> cNode = root;
		int deepToIterate = Math.min(tuple.length, deep);
		for (int i = 0; i < deepToIterate; i++) {
			cNode = cNode.getChild(tuple[i]);
			if (cNode == null) {
				return false;
			}
		}
		return true;
	}

	public boolean containsAndDelete(T[] tuple) {
		TreeNode<T> cNode = root;
		int tuple_index[] = new int[tuple.length];
		for (int i = 0; i < deep; i++) {
			tuple_index[i] = cNode.indexOf(tuple[i]);
			if (tuple_index[i] == -1) {
				return false;
			}
			cNode = cNode.getChildren().get(tuple_index[i]);
		}
		this.nbTuple--;
		cNode = cNode.getPrevious();
		for (int i = 0; i < tuple_index.length; i++) {
			cNode.remove(tuple_index[deep - 1 - i]);
			if (cNode.getNbChild() > 0) {
				return true;
			}

			cNode = cNode.getPrevious();
		}

		return true;
	}

	public TreeNode<T> getRoot() {
		return root;
	}

	@Override
	public Iterator<T[]> iterator() {
		return new TupleIterator(type, (Tree<T>) this);
	}

	public TupleIteratorDeletor getRemoverIterator() {
		return new TupleIteratorDeletor(type, this);
	}

	public void clear() {
		this.nbTuple = 0;
		this.root.clear();
	}

	protected class TupleIterator implements Iterator<T[]> {

		private Tree<T> table;
		private Class<T> typleClass;

		protected TreeNode<T> cNode;
		protected T[] first;

		private T[] last;

		private T[] current;
		private T[] currentOrdered;

		protected Integer tupleIndex[];

		@SuppressWarnings("unchecked")
		public T[] newArray(Class<T> t, int size) {
			// Create an array to hold the data
			T[] data = (T[]) Array.newInstance(t, size);
			return data;
		}

		public TupleIterator(Class<T> type, Tree<T> table) {
			this.table = table;
			this.typleClass = type;
			TreeNode<T> cNodeFirst;
			TreeNode<T> cNodeLast;
			TreeNode<T> root = cNodeFirst = cNodeLast = table.getRoot();
			first = newArray(typleClass, deep);
			last = newArray(typleClass, deep);
			current = newArray(typleClass, deep);
			currentOrdered = newArray(typleClass, deep);
			tupleIndex = new Integer[deep];
			Arrays.fill(tupleIndex, 0, deep, 0);
			int index = 0;
			cNode = root;
			while (!cNode.isLeaf()) {
				ArrayList<TreeNode<T>> elementsFirst = cNodeFirst.getChildren();
				ArrayList<TreeNode<T>> elementsLast = cNodeLast.getChildren();

				cNodeFirst = elementsFirst.get(0);
				cNodeLast = elementsLast.get(elementsLast.size() - 1);
				first[index] = cNodeFirst.getElement();

				last[index] = cNodeLast.getElement();
				index++;
				cNode = cNodeFirst;
			}
			// assert index==deep;

			System.arraycopy(first, 0, current, 0, first.length);
			generateFirstOrder();
		}

		private void generateFirstOrder() {
			reverseOrder = new int[order.length];
			for (int i = 0; i < order.length; i++) {
				reverseOrder[order[i]] = i;
				currentOrdered[i] = current[order[i]];
			}
		}

		@Override
		public boolean hasNext() {

			for (int i = 0; i < current.length; i++) {
				if (current[i] != last[i]) {
					return true;
				}
			}
			if (Arrays.equals(first, last)) {
				return true;
			}
			return false;
		}

		@Override
		public T[] next() {
			if (first != null) {
				first = null;
				return currentOrdered;
			}
			cNode = cNode.getPrevious();
			// while we can find another node
			int cDeep = deep - 1;
			while (cNode.getChildren().size() == tupleIndex[cDeep] + 1) {
				cNode = cNode.getPrevious();
				cDeep--;
			}
			// Here we reach a node for which we haven't visited a child.
			tupleIndex[cDeep]++;
			cNode = cNode.getChildren().get(tupleIndex[cDeep]);
			current[cDeep] = cNode.getElement();
			currentOrdered[reverseOrder[cDeep]] = cNode.getElement();
			for (int i = cDeep + 1; i < current.length; i++) {
				tupleIndex[i] = 0;
				cNode = cNode.getChildren().get(tupleIndex[i]);
				current[i] = cNode.getElement();
				currentOrdered[reverseOrder[i]] = cNode.getElement();
			}
			return currentOrdered;
		}

		public void clearTable() {
			table.clear();
		}
	};

	public class TupleIteratorDeletor extends TupleIterator implements Iterator<T[]> {

		public TupleIteratorDeletor(Class<T> type, Tree<T> table) {
			super(type, table);
		}

		@Override
		public boolean hasNext() {
			boolean hasNext = super.hasNext();// TODO Auto-generated method stub
			if (!hasNext) {
				removeLast();
			}
			return hasNext;
		}

		private void removeLast() {
			super.clearTable();
		}

		@Override
		public T[] next() {
			Integer cIndex[] = (Integer[]) Arrays.copyOf(super.tupleIndex, super.tupleIndex.length);
			super.next();
			int i = cIndex.length - 1;
			while (cNode.getNbChild() == 0 && i > 0) {
				cNode.getChildren().remove(cIndex[i--]);
			}
			nbTuple--;
			return super.current;
		}

	}

	/**
	 * Return null if the prefix has'nt searched yet, otherwise return the list
	 * of BR which correspond to another tuples.
	 * 
	 * @param tuple
	 * @param size
	 * @return
	 */
	public TreeNode<T> containsPrefix(T[] tuple, int size) {
		if (size == 0) {
			if (root.getChildren().size() == 0) {
				return null;
			} else {
				return root;
			}

		}
		TreeNode<T> cNode = root;
		for (int i = 0; i < size; i++) {
			cNode = cNode.getChild(tuple[i]);
			if (cNode == null) {
				return null;
			}
		}
		return cNode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("root:[" + System.lineSeparator());
		for (int i = 0; i < root.getNbChild(); i++) {
			System.out.println(root.getChildren().get(i));
		}
		sb.append("]");
		return sb.toString();
	}
}
