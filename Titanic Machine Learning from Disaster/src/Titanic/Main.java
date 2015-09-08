package Titanic;

import java.util.*;

public class Main {
	public static double esp = 0.5;

	public static void main(String[] args) {
		CSV_Operation csv = new CSV_Operation();

		ArrayList<String[]> trainList = csv.readCSV("train.csv");
		ArrayList<String[]> refinedDataList = refineTrainList(trainList);

		LinkedList<Item> dataSet = new LinkedList<Item>();

		int c = 0;
		for (Iterator<String[]> iterator = refinedDataList.iterator(); iterator
				.hasNext();) {
			String[] str = iterator.next();
			c++;
			// if (c <= 10) {
			// for (int i = 0; i < str.length; i++)
			// System.out.printf("%10s", str[i]);
			// System.out.println();
			// }
			dataSet.add(new Item(str));
		}

		LinkedList<String> labels = new LinkedList<String>();
		labels.add("Pclass");
		labels.add("Sex");
		labels.add("Age");
		labels.add("SibSp");
		labels.add("Embarked");
		labels.add("survived");

		ID3 id3 = new ID3();
		DecisionTreeNode root = id3.createTree(dataSet, labels);
		// id3.print(root);

		ArrayList<String[]> testList = csv.readCSV("test.csv");
		ArrayList<String[]> refinedTestList = refineTestList(testList);

		LinkedList<String> allFeatureLabels = new LinkedList<String>();
		allFeatureLabels.add("Pclass");
		allFeatureLabels.add("Sex");
		allFeatureLabels.add("Age");
		allFeatureLabels.add("SibSp");
		allFeatureLabels.add("Embarked");
		for (Iterator<String[]> iterator = refinedTestList.iterator(); iterator
				.hasNext();) {
			String[] str = iterator.next();
			LinkedList<String> testVector = new LinkedList<String>();
			for (int i = 0; i < str.length; i++)
				testVector.add(str[i]);
			LinkedList<String> featureLabels = new LinkedList<String>(
					allFeatureLabels);
			String ans = id3.classify(root, featureLabels, testVector);

			int a;
			if (ans.equals(""))
				a = 0;
			else
				a = Integer.parseInt(ans);
			System.out.println(a);
		}

		// System.out.println();
		// System.out.println(ID3.n);
	}

	public static ArrayList<String[]> refineTrainList(ArrayList<String[]> list) {
		ArrayList<String[]> neoList = new ArrayList<String[]>();
		double[] ages = new double[list.size()];
		int index = 0;
		for (Iterator<String[]> iterator = list.iterator(); iterator.hasNext();) {
			String[] str = iterator.next();

			String[] neoStr = new String[6];
			// Pclass
			neoStr[0] = str[2];

			// Sex
			neoStr[1] = str[4];

			// Age
			if (str[5].equals("")) {
				neoStr[2] = "-1";
				ages[index++] = -1;
			} else {
				neoStr[2] = str[5];
				ages[index++] = Double.parseDouble(str[5]);
			}

			// Sibsp
			if (Integer.parseInt(str[6]) > 2)
				neoStr[3] = "2";
			else
				neoStr[3] = str[6];

			// Embarked
			if (str[11].equals("C"))
				neoStr[4] = "1";
			else if (str[11].equals("Q"))
				neoStr[4] = "2";
			else if (str[11].equals("S"))
				neoStr[4] = "3";
			else
				neoStr[4] = "4";

			// survived
			neoStr[5] = str[1];

			neoList.add(neoStr);
		}

		index = -1;
		double median = getMedian(ages);
		for (Iterator<String[]> iterator = neoList.iterator(); iterator
				.hasNext();) {
			index++;
			String[] neoStr = iterator.next();

			double age = Double.parseDouble(neoStr[2]);
			if (age + 1 >= -esp && age + 1 <= esp)
				age = median;

			if (age < 15)
				neoStr[2] = "1";
			else if (age < 40)
				neoStr[2] = "2";
			else
				neoStr[2] = "3";
		}

		return neoList;
	}

	public static ArrayList<String[]> refineTestList(ArrayList<String[]> list) {
		ArrayList<String[]> neoList = new ArrayList<String[]>();
		double[] ages = new double[list.size()];
		int index = 0;
		for (Iterator<String[]> iterator = list.iterator(); iterator.hasNext();) {
			String[] str = iterator.next();

			String[] neoStr = new String[5];
			// Pclass
			neoStr[0] = str[1];

			// Sex
			neoStr[1] = str[3];

			// Age
			if (str[4].equals("")) {
				neoStr[2] = "-1";
				ages[index++] = -1;
			} else {
				neoStr[2] = str[4];
				ages[index++] = Double.parseDouble(str[4]);
			}

			// Sibsp
			if (Integer.parseInt(str[5]) > 2)
				neoStr[3] = "2";
			else
				neoStr[3] = str[5];

			// Embarked
			if (str[10].equals("C"))
				neoStr[4] = "1";
			else if (str[10].equals("Q"))
				neoStr[4] = "2";
			else if (str[10].equals("S"))
				neoStr[4] = "3";
			else
				neoStr[4] = "4";

			neoList.add(neoStr);
		}

		index = -1;
		double median = getMedian(ages);
		for (Iterator<String[]> iterator = neoList.iterator(); iterator
				.hasNext();) {
			index++;
			String[] neoStr = iterator.next();

			double age = Double.parseDouble(neoStr[2]);
			if (age + 1 >= -esp && age + 1 <= esp)
				age = median;

			if (age < 15)
				neoStr[2] = "1";
			else if (age < 40)
				neoStr[2] = "2";
			else
				neoStr[2] = "3";
		}

		return neoList;
	}

	public static double getMedian(double[] ages) {
		Arrays.sort(ages);
		int i = 0;
		for (; i < ages.length; i++)
			if (ages[i] != -1.0)
				break;
		int j = ages.length - 1;
		while (i < j) {
			i++;
			j--;
		}
		return ages[i];
	}
}
