package org.huawei.datastructure.table;

import java.util.ArrayList;

public class TreeNode<T> {
	protected T element;
	protected ArrayList<TreeNode<T>> children = new ArrayList<TreeNode<T>>();
	protected TreeNode<T> previous;

	public TreeNode(T element, TreeNode<T> previous) {
		this.element = element;
		this.previous = previous;
	}

	public TreeNode<T> getPrevious() {
		return previous;
	}

	public void addChild(TreeNode<T> child) {
		children.add(child);
	}

	public int getNbChild() {
		return children.size();
	}

	public ArrayList<TreeNode<T>> getChildren() {
		return children;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public void remove(TreeNode<T> element) {
		children.remove(element);
	}

	public void remove(int index) {
		this.children.remove(index);
	}

	public int indexOf(T element) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).getElement().equals(element)) {
				return i;
			}
		}
		return -1;
	}

	public TreeNode<T> removeChild(int index) {
		return children.remove(index);
	}

	public T getElement() {
		return element;
	}
	
	public void setElement(T element) {
		this.element = element;
	}

	/**
	 * Return if the node has a children with the element return it. return null
	 * otherwise. Equals function must be defined
	 * 
	 * @param element
	 * @return
	 */
	public TreeNode<T> getChild(T element) {
		if (children.size() == 0) {
			return null;
		}
		for (int i = 0; i < children.size(); i++) {
			TreeNode<T> node = children.get(i);
			if (node.getElement().equals(element)) {
				return node;
			}
		}
		return null;
	}

	public void removeChild(T[] tuple, int index) {
		if (index < tuple.length) {
			this.getChild(tuple[index]).removeChild(tuple, index + 1);
			if (children.size() == 0 && this.previous != null) {
				previous.remove(this);
				;
			}
		} else {
			previous.remove(this);
		}

	}

	public void removeChild(T t) {
		int i = 0;
		for (; i < children.size(); i++) {
			if (children.get(i).getElement() == t) {
				children.remove(i);
				break;
			}
		}
	}

	public void clear() {
		this.element = null;
		for (TreeNode<T> node : children) {
			node.clear();
		}
		children.clear();
	}

	public boolean contains(T[] generatePostFixTuple) {
		TreeNode<T> cNode = this.getChild(generatePostFixTuple[0]);
		for (int i = 1; i < generatePostFixTuple.length; i++) {
			if (cNode == null) {
				return false;
			}
			cNode = cNode.getChild(generatePostFixTuple[i]);
		}
		return cNode != null;
	}

	public void setPrevious(TreeNode<T> cNode) {
		this.previous = cNode;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(element.toString());
		for (int i = 0; i < children.size(); i++) {
			sb.append(System.lineSeparator() + "[");
			sb.append(children.get(i).toString());
			if (i != children.size() - 1) {
				sb.append("],");

			} else {
				sb.append(System.lineSeparator() + "]");
			}
		}
		return sb.toString();
	}

}
