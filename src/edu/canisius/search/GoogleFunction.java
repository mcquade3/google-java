package edu.canisius.search;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map;
import edu.canisius.web.WebPageParser;

/**
 * The GoogleFunction class is a utility class for the GoogleGUI used to
 * interact with the URLs, search terms, and other user commands.
 * 
 * @author Jack Smith, Alex Dote, Mike McQuade
 */
public class GoogleFunction {

	/**
	 * {@code finalMap} is the map that stores all the words found at the added
	 * URLs, with pointers to the URLs they are found on
	 */
	public static HashMap<String, ArrayList<URL>> finalMap = new HashMap<String, ArrayList<URL>>();

	/**
	 * Adds the words found at the given URL to {@link #finalMap} as the keys.
	 * The value for each key is an ArrayList of URLs in which the word is found
	 * on.
	 * 
	 * @param input
	 *            the URL that the user wants to add to the search database
	 * @throws IOException
	 *             if URL is empty or not formatted correctly
	 */
	public static void addURL(String input) throws IOException {
		URL url = new URL(input);
		WebPageParser parser = new WebPageParser(url);
		Iterable<String> itable = parser.getText();
		Iterator<String> itator = itable.iterator();
		while (itator.hasNext()) {
			String lineText = itator.next();
			Scanner lineScanner = new Scanner(lineText);
			processLine(lineScanner, url);
		}
	}

	/**
	 * Processes every line in {@code url}, which means it takes every word on
	 * the web-page and adds it to {@link #finalMap} as the key. The key's value
	 * is the ArrayList of URLs in which it's found.
	 * 
	 * @param line
	 *            Scanner that consists of all the words on the line that is to
	 *            be processed
	 * @param url
	 *            URL of the page that is being processed
	 */
	public static void processLine(Iterator<String> line, URL url) {
		while (line.hasNext()) {
			String word = line.next();
			ArrayList<URL> urls = finalMap.get(word);
			if (urls != null) {
				if (!urls.contains(url)) {
					urls.add(url);
				}
			} else {
				urls = new ArrayList<URL>();
				urls.add(url);
				finalMap.put(word, urls);
			}
		}
	}

	/**
	 * Removes all values of the given URL from {@link #finalMap}
	 * 
	 * @param input
	 *            User-typed URL that is to be removed
	 * @throws MalformedURLException
	 *             if URL is empty or not formatted correctly.
	 */
	public static void removeURL(String input) throws MalformedURLException {
		URL url = new URL(input);
		Iterator<Map.Entry<String, ArrayList<URL>>> itator = finalMap
				.entrySet().iterator();
		while (itator.hasNext()) {
			Map.Entry<String, ArrayList<URL>> entry = itator.next();
			ArrayList<URL> urls = entry.getValue();
			if (urls.contains(url)) {
				urls.remove(url);
			}
		}
		clean();
	}

	/**
	 * Helper method for {@link #removeURL(String)}. Searches for any empty
	 * ArrayLists in entries of {@link #finalMap} and deletes them.
	 */
	public static void clean() {
		HashMap<String, ArrayList<URL>> newfinal = new HashMap<String, ArrayList<URL>>();
		Iterator<Map.Entry<String, ArrayList<URL>>> itator = finalMap
				.entrySet().iterator();
		while (itator.hasNext()) {
			Map.Entry<String, ArrayList<URL>> entry = itator.next();
			ArrayList<URL> urls = entry.getValue();
			if (!urls.isEmpty()) {
				newfinal.put(entry.getKey(), entry.getValue());
			}
		}
		finalMap = newfinal;
	}

	/**
	 * Returns the ArrayList of all URLs in which the word is found
	 * 
	 * @param word
	 *            User-typed word for which the user is searching.
	 * @return ArrayList<URL> of URLs in which the word is found.
	 */
	public static ArrayList<URL> listUrls(String word) {
		return finalMap.get(word);
	}

	/**
	 * Given a word to search for, this class first goes through
	 * {@link #finalMap} looking to see if the word has been found on any of the
	 * possible URLs. If so, it scans through those URLs to find how many times
	 * the word is mentioned. It then returns a HashMap consisting of each URL
	 * it is found on, and the amount of times it is used on that URL.
	 * 
	 * @param str
	 *            User-Typed word that the user is searching for.
	 * @return HashMap of the URLs in which the word is found, and the amount of
	 *         times it is used.
	 * @throws IOException
	 *             if URL is empty or not formatted correctly.
	 */
	public static HashMap<URL, Integer> findUrl(String str) throws IOException {
		HashMap<URL, Integer> value = new HashMap<URL, Integer>();
		if (finalMap.get(str) == null) {
			return value;
		}
		int count = 1;
		ArrayList<URL> vals = finalMap.get(str);
		for (URL url : vals) {
			WebPageParser parser = new WebPageParser(url);
			Iterable<String> itable = parser.getText();
			Iterator<String> itator = itable.iterator();
			while (itator.hasNext()) {
				String lineText = itator.next();
				Scanner lineScanner = new Scanner(lineText);
				while (lineScanner.hasNext()) {
					String word = lineScanner.next();
					if (str.equals(word)) {
						count++;
					}
				}
				lineScanner.close();
			}
			value.put(url, count);
			count = 1;
		}
		return value;
	}
}
