package com.vjh0107.barcode.cutscene.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditDistanceRecursive {

	public static double calculate(String str1, String str2) {
	    List<String> pairs1 = wordLetterPairs(str1.toUpperCase());
	    List<String> pairs2 = wordLetterPairs(str2.toUpperCase());

	    int intersection = 0;
	    int union = pairs1.size() + pairs2.size();

	    for (String s : pairs1) {
	        for (int j = 0; j < pairs2.size(); j++) {
	            if (s.equals(pairs2.get(j))) {
	                intersection++;
	                pairs2.remove(j);
	                break;
	            }
	        }
	    }
	    return (2.0 * intersection) / union;
	}

	private static List<String> wordLetterPairs(String str) {
	    List<String> AllPairs = new ArrayList<>();
	    String[] Words = str.split("\\s");
	    for (String word : Words) {
	        if (StringUtils.isNotBlank(word)) {
	            String[] PairsInWord = letterPairs(word);
	            Collections.addAll(AllPairs, PairsInWord);
	        }
	    }
	    return AllPairs;
	}

	private static String[] letterPairs(String str) {
	    int numPairs = str.length() - 1;
	    String[] pairs = new String[numPairs];
	    for (int i = 0; i < numPairs; i++) {
	        try {
	            pairs[i] = str.substring(i, i + 2);
	        } catch (Exception e) {
	            pairs[i] = str.substring(i, numPairs);
	        }
	    }
	    return pairs;
	  }
}

