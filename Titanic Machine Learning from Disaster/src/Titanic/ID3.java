package Titanic;

import java.util.*;

public class ID3 {

	// public static void print(DecisionTreeNode node) {
	// String str = node.label;
	// if (node.isLeaf()) {
	// System.out.println(str);
	// return;
	// }
	//
	// str = str + " --- ";
	// for (Iterator<String> iterator = node.sons.keySet().iterator(); iterator
	// .hasNext();) {
	// String featureVal = iterator.next();
	// DecisionTreeNode n = node.sons.get(featureVal);
	// print(n);
	// str = str + "¡¡< " + featureVal + " , " + n.label + " > ";
	// }
	//
	// System.out.println(str);
	// }

	public static double calcShannonEntropy(LinkedList<Item> list) {
		double numEntries = list.size();
		TreeMap<String, Integer> labelCounts = new TreeMap<String, Integer>();

		Iterator<Item> iterator = list.iterator();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			String label = item.getLast();
			if (!labelCounts.containsKey(label))
				labelCounts.put(label, 1);
			else {
				int num = labelCounts.get(label);
				labelCounts.put(label, num + 1);
			}
		}

		double prob;
		double shannonEntropy = 0;

		Iterator<String> iterator2 = labelCounts.keySet().iterator();
		while (iterator2.hasNext()) {
			String str = iterator2.next();
			prob = labelCounts.get(str) / numEntries;
			shannonEntropy -= prob * (Math.log(prob) / Math.log(2.0));
		}

		return shannonEntropy;
	}

	public static LinkedList<Item> splitDataSet(LinkedList<Item> dataSet,
			int axis, String value) {
		LinkedList<Item> retDataSet = new LinkedList<Item>();
		for (Iterator<Item> iterator = dataSet.iterator(); iterator.hasNext();) {
			Item item = iterator.next();
			if (item.get(axis).equals(value)) {
				Item reducedFeatVec = new Item(item);
				reducedFeatVec.remove(axis);
				retDataSet.add(reducedFeatVec);
			}
		}
		return retDataSet;
	}

	public static LinkedList<String> getList(LinkedList<Item> dataSet, int axis) {
		LinkedList<String> featureList = new LinkedList<String>();
		for (Iterator<Item> iterator = dataSet.iterator(); iterator.hasNext();) {
			Item item = iterator.next();
			featureList.add(item.get(axis));
		}
		return featureList;
	}

	public static int chooseBestFeatureToSplit(LinkedList<Item> dataSet) {
		int featureNumber = dataSet.get(0).size() - 1;
		double baseEntropy = calcShannonEntropy(dataSet);

		double bestInfoGain = 0;
		int bestFeature = -1;

		for (int i = 0; i < featureNumber; i++) {
			LinkedList<String> featureList = getList(dataSet, i);
			HashSet<String> uniqueFeatureSet = new HashSet<String>(featureList);
			double neoEntropy = 0.0;

			for (Iterator<String> iterator = uniqueFeatureSet.iterator(); iterator
					.hasNext();) {
				String featureValue = iterator.next();
				LinkedList<Item> subDataSet = splitDataSet(dataSet, i,
						featureValue);
				// if (featureNumber == 5) {
				// System.out.println(subDataSet.size());
				// }

				double prob = 1.0 * subDataSet.size() / dataSet.size();
				neoEntropy += prob * calcShannonEntropy(subDataSet);
			}

			double infoGain = baseEntropy - neoEntropy;

			// if (featureNumber == 5) {
			// System.out
			// .printf("this is the first choice, infoGain for feature %d is %f, and the neoEntropy is %f\n",
			// i, infoGain, neoEntropy);
			//
			// }

			if (infoGain >= bestInfoGain) {
				bestInfoGain = infoGain;
				bestFeature = i;
			}
		}

		// if (featureNumber == 5)
		// System.out.println();

		// System.out.println(featureNumber + " features in all and  " +
		// bestFeature+" is best");

		return bestFeature;
	}

	public static LinkedList<String> getClassList(LinkedList<Item> dataSet) {
		LinkedList<String> classList = new LinkedList<String>();
		for (Iterator<Item> iterator = dataSet.iterator(); iterator.hasNext();) {
			Item item = iterator.next();
			classList.add(item.getLast());
		}
		return classList;
	}

	public static boolean isAllInOneClass(LinkedList<String> classList) {
		String mark = classList.getFirst();
		for (Iterator<String> iterator = classList.iterator(); iterator
				.hasNext();) {
			if (!iterator.next().equals(mark))
				return false;
		}
		return true;
	}

	public static String majorityCnt(LinkedList<String> classList) {
		String mostFrequentClassName = classList.get(0);
		int mostFrequentClassNumber = 1;

		TreeMap<String, Integer> classCount = new TreeMap<String, Integer>();
		for (Iterator<String> iterator = classList.iterator(); iterator
				.hasNext();) {
			String str = iterator.next();
			int tempNumber = 1;
			if (classCount.containsKey(str))
				tempNumber = classCount.get(str) + 1;
			classCount.put(str, tempNumber);

			if (tempNumber > mostFrequentClassNumber) {
				mostFrequentClassName = str;
				mostFrequentClassNumber = tempNumber;
			}
		}

		return mostFrequentClassName;
	}

	public static DecisionTreeNode createTree(LinkedList<Item> dataSet,
			LinkedList<String> labels) {
		LinkedList<String> classList = getClassList(dataSet);
		if (isAllInOneClass(classList))
			return new DecisionTreeNode(classList.get(0));
		if (dataSet.get(0).size() == 1)
			return new DecisionTreeNode(majorityCnt(classList));

		int bestFeature = chooseBestFeatureToSplit(dataSet);
		String bestFeatureLabel = labels.get(bestFeature);
		LinkedList<String> featureValues = getList(dataSet, bestFeature);
		HashSet<String> uniqueValues = new HashSet<String>(featureValues);

		DecisionTreeNode node = new DecisionTreeNode(bestFeatureLabel);

		for (Iterator<String> iterator = uniqueValues.iterator(); iterator
				.hasNext();) {
			String featureValue = iterator.next();
			LinkedList<String> subLabels = new LinkedList<String>(labels);
			subLabels.remove(bestFeature);

			LinkedList<Item> splitDataSet = splitDataSet(dataSet, bestFeature,
					featureValue);
			node.add(featureValue, createTree(splitDataSet, subLabels));
		}

		return node;
	}

	static int n = 0;

	public static String classify(DecisionTreeNode root,
			LinkedList<String> featureLabels, LinkedList<String> testVector) {

		DecisionTreeNode node = root;
		while (true) {
			if (node == null) {
				n++;
				return "";
			}
			if (node.isLeaf())
				return node.label;
			String featureName = node.label;
			String featureNum = "";

			for (int i = 0; i < featureLabels.size(); i++) {
				if (featureLabels.get(i).equals(featureName)) {
					featureNum = testVector.get(i);
					break;
				}
			}

			node = node.sons.get(featureNum);
		}
	}
}
