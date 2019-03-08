package org.huawei.datastructure.set;

import java.util.Iterator;

public class SparseSet implements Iterator<Integer> {

	protected int size;
	protected final int[] maps;
	protected final int[] elements;

	protected int pos = 0;

	public SparseSet(final int size,final  boolean full) {
		maps = new int[size];
		elements = new int[size];
		for (int i = 0; i < size; i++) {
			maps[i] = i;
			elements[i] = i;
		}
		if (full) {
			this.size = size;
		} else {
			this.size = 0;
		}
	}

	public int getSize() {
		return size;
	}

	public int getMaxSize() {
		return maps.length;
	}

	public boolean contains(final int element) {
		return maps[element] < size;
	}

	public void swap(final int i,final  int j) {
		int container;
		container = elements[i];
		elements[i] = elements[j];
		elements[j] = container;
		maps[elements[i]] = i;
		maps[elements[j]] = j;
	}

	public void remove(final int value) {
		if (maps[value] < size) {
			swap(maps[value], size - 1);
			size--;
		}
	}

	public void restaure(final int value) {
		if (maps[value] > size) {
			swap(maps[value], size);
		}
		size++;
	}

	public void reset() {
		size = maps.length;
	}

	@Override
	public boolean hasNext() {
		return this.pos < this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public void resetIterator() {
		pos = 0;
	}

	@Override
	public Integer next() {
		return elements[pos++];
	}

	public void removeAll() {
		size=0;
	}
	
	public int getElementAtPos(final int i){
		return this.elements[i];
	}
	
	
	public static void main(String[] args) {
		SparseSet set = new SparseSet(10, false);
		set.restaure(0);
		System.out.println(set.size);
	}
}
