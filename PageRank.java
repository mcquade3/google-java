package edu.canisius.graph;

import java.util.*;

public class PageRank {
	public static void main(String[] args) {
		HashMap<String, ArrayList<String>> test = new HashMap<String, ArrayList<String>>();
		test.put("http://udacity.com/cs101x/urank/kathleen.html", new ArrayList<String>());
		test.put("http://udacity.com/cs101x/urank/zinc.html", new ArrayList<>(Arrays.asList("http://udacity.com/cs101x/urank/nickel.html", "http://udacity.com/cs101x/urank/arsenic.html")));
		test.put("http://udacity.com/cs101x/urank/hummus.html", new ArrayList<String>());
		test.put("http://udacity.com/cs101x/urank/arsenic.html", new ArrayList<>(Arrays.asList("http://udacity.com/cs101x/urank/nickel.html")));
		test.put("http://udacity.com/cs101x/urank/index.html", new ArrayList<>(Arrays.asList("http://udacity.com/cs101x/urank/hummus.html", "http://udacity.com/cs101x/urank/arsenic.html", "http://udacity.com/cs101x/urank/kathleen.html", "http://udacity.com/cs101x/urank/nickel.html", "http://udacity.com/cs101x/urank/zinc.html")));
		test.put("http://udacity.com/cs101x/urank/nickel.html", new ArrayList<>(Arrays.asList("http://udacity.com/cs101x/urank/kathleen.html")));
		System.out.println(compute_ranks(test));
	}

	public static void addPage(HashMap<String, ArrayList<String>> index, String keyword, String url){
		if (index.containsKey(keyword)){
			ArrayList<String> newList = index.get(keyword);
			newList.add(url);
			index.put(keyword, newList);
		} else {
			index.put(keyword, new ArrayList<String>(Arrays.asList(url)));
		}
	}
	
	public static HashMap<String,Double> compute_ranks(HashMap<String, ArrayList<String>> graph){
	    final double D = 0.85; // damping factor
	    int numloops = 10;
	    
	    HashMap<String,Double> ranks = new HashMap<String, Double>();
	    int npages = graph.size();
	    for (String page : graph.keySet()){
	        ranks.put(page, (double) (1/npages));
	    }
	    
	    for (int i = 0; i < numloops; i++){
	        HashMap<String,Double> newranks = new HashMap<String, Double>();
	        for (String page : graph.keySet()){
	            double newrank = (1 - D) / npages;
	            for (String node : graph.keySet()){
	                if (graph.get(node).contains(page)){
	                    newrank += D * ((double) ranks.get(node) / graph.get(node).size());
	                }
	            }
	            newranks.put(page,newrank);
	        }
	        ranks = newranks;
	    }
	    return ranks;
	}
}
