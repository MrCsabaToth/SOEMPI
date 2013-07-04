/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openhie.openempi.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class CsvColumnTransformer
{
	protected Logger log = Logger.getLogger(getClass());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: CsvColumnTransformer input-file column-order");
			System.out.println("Example: CsvColumnTransformer test.csv \"4,3,2,1\"");
			System.exit(-1);
		}
		CsvColumnTransformer transformer = new CsvColumnTransformer();
		try {
			transformer.transform(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void transform(String[] args) throws IOException {
		File inputFile = openInputFile(args[0]);
		List<Integer> columnOrder = parseColumnOrder(args[1]);
		transform(inputFile, columnOrder);
	}

	private void transform(File inputFile, List<Integer> columnOrder) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(inputFile));
	    String[] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	    	int index=0;
	    	for (int value : columnOrder) {
	    		System.out.print(nextLine[value].trim());
	    		index++;
	    		if (index < columnOrder.size()) {
	    			System.out.print(",");
	    		}
	    	}
	    	System.out.println();
	    }
	    reader.close();
	}

	private List<Integer> parseColumnOrder(String columnOrder) throws IOException {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(columnOrder));
		tokenizer.whitespaceChars(',', ',');
		tokenizer.parseNumbers();
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
//				System.out.println("The token is: " + tokenizer.nval);
				list.add(new Double(tokenizer.nval).intValue());
			}
		}
		int index = 0;
		for (Integer value : list) {
			System.out.println("Column " + index + " will have the content of input file column number " + value);
			index++;
		}
		return list;
	}

	private File openInputFile(String filename) {
		if (filename == null || filename.length() == 0) {
			log.error("The filename is invalid: " + filename);
			throw new RuntimeException("Invalid input filename.");
		}

		File inputFile = new File(filename);
		if (!inputFile.exists()) {
			log.error("The file " + filename + " does not exist.");
			throw new RuntimeException("File " + filename + " does not exist");
		}
		if (!inputFile.isFile() || !inputFile.canRead()) {
			log.error("File " + filename + " must be a readable CSV file.");
			throw new RuntimeException("File " + filename + " must be a readable CSV file");
		}
		return inputFile;
	}

}
