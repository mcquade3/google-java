package edu.canisius.graph;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;

import javax.sound.midi.Sequence;

import edu.canisius.graph.Edge;
import edu.canisius.graph.ALDGraph;
import edu.canisius.graph.Vertex;
import edu.canisius.web.WebPageParser;
import net.htmlparser.jericho.Source;

public class PageRankV2 {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addPage(Vertex userPage, ALDGraph<Vertex,Object> machineNames){
		Sequence results;
		Queue worklist;
		worklist.add(userPage);
		while (!worklist.isEmpty()){
			Object page = worklist.remove();
			WebPageParser data = new WebPageParser((String) page);
		  	try {
			    results.equals(map(data));
			    Iterable<String> links = ((Object) data).getLinks(machineNames);
			    ArrayList<String> seen = new ArrayList();
			    for (String url : links){
			    	if (!seen.contains(url)){
						seen.add(url);
			        	worklist.add(url);
			        	machineNames.insertEdge(page, url, data);
			    	} else {
			        	Vertex oldPage = url;
			        	machineNames.insertEdge(page, oldPage);
			        }
			    }
		  	} catch (IOException ioe){
		  		machineNames.removeVertex(page);
		  	}
		}
		finalDict.reduce();
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap<Vertex, Double> compute_ranks(ALDGraph<Object,Object> graph){
		HashMap<Vertex, Double> ranks = new HashMap<Vertex, Double>();
		for (Vertex v : graph.vertices()){
			ranks.put(v, 1.0);
		}
		double delta;
		HashMap<String, Double> newRanks = new HashMap<String, Double>();
		do {
			delta = 0.0;
			for (Vertex v : graph.vertices()){
				// Compute the new page rank for v
				double sumRanks = 0.0;
				for (Edge e : graph.edges()){ 
					if (e.equals(v)){
				    	Vertex src = e.getEndVertices()[0];
				    	sumRanks += (src.weight()) / (src.numElements());
					}
				}
				sumRanks = (1 - 0.85) + (0.85 * sumRanks);
				// Perform some action to record sumRank as next weight of v WITHOUT changing v's weight
				newRanks.set(v, sumRanks);
			}
			for (Vertex v : graph.vertices()){
				double newWeight = newRanks.get(v);
				delta += Math.abs(ranks.get(v) - newWeight);
				ranks.set(v, newWeight);
			}
		} while (delta > 0.001);
	}
}
