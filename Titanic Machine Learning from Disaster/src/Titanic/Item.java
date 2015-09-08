package Titanic;

import java.util.*;

public class Item {
	LinkedList<String> list;

	Item() {
		list = new LinkedList<String>();
	}

	public Item(String[] s) {
		list = new LinkedList<String>();
		for (int i = 0; i < s.length; i++)
			list.add(s[i]);
	}

	public Item(Item item) {
		list = new LinkedList<String>(item.list);
	}

	public String getLast() {
		return list.get(list.size() - 1);
	}

	public int size() {
		return list.size();
	}

	public void remove(int index) {
		list.remove(index);
	}

	public String get(int index) {
		return list.get(index);
	}

	public String toString() {
		String str = list.toString();
		return str;
	}

}
