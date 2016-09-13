package edu.canisius.search;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 * GoogleGUI generates the GUI that will be used to add URLs to the database and
 * interact with them. It consists of a text area, text field, label, and five
 * buttons (Add, Remove, Find, List, and Quit). The text field is editable, and
 * is used by the user to enter a URL, search term, etc. The buttons are used to
 * interact with the text in the text field, and the results of these
 * interactions are displayed in the text area.
 * 
 * @author Jack Smith, Alex Dote, Mike McQuade
 * 
 */
public class GoogleGUI extends JFrame {

	/**
	 * Declares local variables
	 */
	private static final long serialVersionUID = 1L;
	private JPanel content;
	private JTextField textField;
	private JTextArea textArea;
	private final Highlighter hilit;
	private final Highlighter.HighlightPainter painter;

	/**
	 * Creates an instance of the GUI and makes it visible to the user.
	 */
	JButton addButton, findButton, quitButton, removeButton;

	public static void main(String[] args) {
		GoogleGUI frame = new GoogleGUI();
		frame.setVisible(true);
	}

	/**
	 * Creates the frame, including all buttons with ActionHandlers built in,
	 * using the functions from GoogleFunction.
	 */
	private GoogleGUI() {
		event e = new event();
		setTitle("Google");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 800);
		content = new JPanel();
		content.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(content);
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		content.add(panel);
		panel.setLayout(null);

		JLabel labl = new JLabel();
		labl.setText("Enter text here:");
		labl.setBounds(645, 100, 100, 20);
		panel.add(labl);

		textField = new JTextField();
		textField.setBounds(530, 125, 325, 25);
		panel.add(textField);
		textField.setColumns(10);

		textArea = new JTextArea();
		textArea.setEditable(false);
		hilit = new DefaultHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(Color.BLUE);
		textArea.setHighlighter(hilit);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 10, 500, 700);
		panel.add(scrollPane);

		JButton findButton = new JButton("Find");
		findButton.addActionListener(e);
		findButton.setBounds(580, 168, 97, 25);
		panel.add(findButton);

		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(e);
		quitButton.setBounds(750, 300, 100, 25);
		panel.add(quitButton);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(e);
		addButton.setBounds(580, 210, 97, 25);
		panel.add(addButton);

		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(e);
		removeButton.setBounds(700, 210, 100, 25);
		panel.add(removeButton);

		JButton listButton = new JButton("List");
		listButton.addActionListener(e);
		listButton.setBounds(700, 168, 100, 25);
		panel.add(listButton);
	}

	/**
	 * Handles all actions using simple if-statements that test the user's input
	 * and act accordingly.
	 */
	private class event implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();

			if (command.equals("Add")) {
				String text = textField.getText();
				try {
					GoogleFunction.addURL(text);
				} catch (IOException e1) {
					textArea.append("Not a URL or Incorrect Format! \n");
				}
			} else if (command.equals("List")) {
				String text = textField.getText();
				text = text.toLowerCase();
				ArrayList<URL> urls = GoogleFunction.listUrls(text);
				if(urls == null){
					textArea.append("Word not found! \n");
					textField.setText("");
					return;
				}
				textArea.append(text + ": \n");
				for (URL x : urls) {
					textArea.append(x.toString() + "\n");
				}
				textArea.append("****************\n");
			} else if (command.equals("Remove")) {
				String text = textField.getText();
				try {
					GoogleFunction.removeURL(text);
				} catch (IOException e1) {
					textArea.append("Not a URL or Incorrect Format! \n");
				}
			} else if (command.equals("Quit")) {
				System.exit(0);
			} else if (command.equals("Find")) {
				String text = textField.getText();
				text = text.toLowerCase();
				try {
					HashMap<URL, Integer> x = GoogleFunction.findUrl(text);
					if (x.isEmpty()) {
						textArea.append("Word not found! \n");
						textField.setText("");
						return;
					}
					Set<URL> urls = x.keySet();
					Iterator<URL> itator = urls.iterator();
					textArea.append(text + ": \n");
					while (itator.hasNext()) {
						URL url = itator.next();
						textArea.append(url.toString() + " : " + x.get(url)
								+ "\n");
					}
					textArea.append("****************\n");
				} catch (IOException e1) {
					textArea.append("Incorrect Format! \n");
				}
			}
			textField.setText("");
		}
	}
}
