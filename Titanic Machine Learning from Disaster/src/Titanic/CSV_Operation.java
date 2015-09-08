package Titanic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import com.csvreader.*;

public class CSV_Operation {
	public static void main(String[] args) {
		CSV_Operation csv = new CSV_Operation();
		ArrayList<String[]> list = csv.readCSV("test.csv");
		csv.print(list);
	}

	public ArrayList<String[]> readCSV(String fileName) {
		ArrayList<String[]> csvList = new ArrayList<String[]>();

		String csvFilePath = fileName;
		CsvReader reader;
		try {
			reader = new CsvReader(csvFilePath);
			reader.readHeaders();
			while (reader.readRecord()) {
				csvList.add(reader.getValues());
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return csvList;
	}

	public void writeCsv(String fileName, ArrayList<String[]> contents) {
		try {
			String filePath = fileName;

			CsvWriter writer = new CsvWriter(filePath);
			for (Iterator<String[]> iterator = contents.iterator(); iterator
					.hasNext();) {
				String[] str = iterator.next();
				writer.writeRecord(str);
			}
			writer.flush();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void print(ArrayList<String[]> csvList) {
		for (Iterator<String[]> iterator = csvList.iterator(); iterator
				.hasNext();) {
			String[] str = iterator.next();
			for (int i = 0; i < str.length; i++)
				System.out.printf("%6s", str[i]);
			System.out.println();
		}
	}

}
