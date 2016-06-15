package aufgabe_4;
import java.awt.*;
import javax.swing.*;
/**
 * Eine Eingabemaske zur Darstellung von Mandelbrotmengen. Eingabe von Dateiname
 * oder Lesen aus Textfeldern zur Datendefinition. Starten und Zuruecksetzen mit
 * Button, sowie Zoom- und Navigationsbutton.
 * @author Jens Awisus
 */
@SuppressWarnings("serial")
public class PanelEingabe extends JPanel {
	/**
	 * String, der Checkbox beschreibt
	 */
	public static final String ITEM_LESEN = "Aus Datei lesen";
	/**
	 * String, der Button STARTEN beschreiben
	 */
	public static final String ACTION_NEU = "Neu";
	/**
	 * String, der Button STARTEN beschreiben
	 */
	public static final String ACTION_STARTEN = "Starten";
	/**
	 * String, der Button STARTEN beschreiben
	 */
	public static final String ACTION_KLEINER = "-";
	/**
	 * String, der Button STARTEN beschreiben
	 */
	public static final String ACTION_GROESSER = "+";
	/**
	 * Pfeil nach oben fuer Button HOCH
	 */
	public static final String ACTION_HOCH = "\u25b2";
	/**
	 * Pfeil nach links fuer Button LINKS
	 */
	public static final String ACTION_LINKS = "\u25c0";
	/**
	 * Pfeil nach rechts fuer Button RECHTS
	 */
	public static final String ACTION_RECHTS = "\u25b6";
	/**
	 * Pfeil nach unten fuer Button RUNTER
	 */
	public static final String ACTION_RUNTER = "\u25bc";
	/**
	 * Schwarzer Punkt fuer Button ZENTRIERT
	 */
	public static final String ACTION_ZENTRIERT = "\u25cf";
	/**
	 * Beschreibung fuer Einzoombutton
	 */
	public static final String ACTION_ZOOMEIN = "Zoom +";
	/**
	 *  Beschreibung fuer Auszoombutton
	 */
	public static final String ACTION_ZOOMAUS = "Zoom -";
	/**
	 * Eingabefeld Realteil untere Bereichsgrenze
	 */
	JTextField tfCAreal;
	/**
	 * Eingabefeld Imaginaerteil untere Bereichsgrenze
	 */
	JTextField tfCAimag;
	/**
	 * Eingabefeld Realteil obere Bereichsgrenze
	 */
	JTextField tfCEreal;
	/**
	 * Eingabefeld Imaginaerteil obere Bereichsgrenze
	 */
	JTextField tfCEimag;
	/**
	 * Eingabefeld Iterationenanzahl
	 */
	JTextField tfIter;
	/**
	 * Eingabefeld Breite
	 */
	JTextField tfBreite;
	/**
	 * Eingabefeld Hoehe
	 */
	JTextField tfHoehe;
	/**
	 * Eingabefeld Dateiname
	 */
	JTextField tfDatei;
	/**
	 * Checkbox zur Auswahl: Datei einlesen (ja/nein)
	 */
	JCheckBox cbDatei;

	JLabel lbMenge;
	/**
	 * Controller zur abnahme von Events (ActionListener, ItemListener)
	 */
	private Controller controller;
	/**
	 * Konstruktior; initiiert Controller, erzeugt Oberflaeche, setzt Titel
	 * @param controller Ueberwachender Controller
	 */
	public PanelEingabe(Controller controller) {
		this.controller = controller;
		this.makeView();
	}
	/**
	 * Zusammenbau der Eingabeoberflaeche
	 */
	private void makeView() {
		// Textfelder instanziieren
		this.init();

		// Layout
		this.setLayout(new BorderLayout());

		// Boxen mit Bedienelemeten erstellen und in Hauptbox einsetzen
		Box box = Box.createVerticalBox();

		box.add(this.makeBoxNord());

		Box subBox = Box.createHorizontalBox();
		subBox.add(this.makeBoxSuedNavigat());
		subBox.add(this.makeBoxSuedZoom());

		box.add(subBox);

		this.add(box, BorderLayout.NORTH);

		this.add(this.lbMenge, BorderLayout.SOUTH);
	}
	/**
	 * Aufbau einer Box, die im Norden des Panels eingefuegt wird. Enthaelt
	 * Textfelder fuer C_a, C_e, Groesse Bildbereich, Iterationen und Dateiname.
	 * Ebenso sind hier Buttons fuer das Erhoehen oder Senken der Iterationszahl,
	 * fuer das Ruecksetzen der Textfelder, das Zeichnen des Bildes und zum
	 * Schliessen der Anwendung enthalten.
	 * @return obere Box
	 */
	private Box makeBoxNord() {
		// Verpacken der Elemente in einer Box
		Box box = Box.createVerticalBox();

		// Felder fuer C_a
		String[] texte1 = {"Re:", "Im:"};
		Component[] komponenten1 = {this.tfCAreal, this.tfCAimag};
		box.add(makePanel1("Untere Intervallgrenze:"));
		box.add(makePanel2(texte1, komponenten1));

		// Felder fuer C_e
		komponenten1[0] = this.tfCEreal;
		komponenten1[1] = this.tfCEimag;
		box.add(makePanel1("Obere Intervallgrenze:"));
		box.add(makePanel2(texte1, komponenten1));

		// Felder fuer Bildgroesse
		texte1[0] = "Breite:";
		texte1[1] = " Höhe:";
		box.add(makePanel1("Bildbereich:"));
		komponenten1[0] = this.tfBreite;
		komponenten1[1] = this.tfHoehe;
		box.add(makePanel2(texte1, komponenten1));

		// Checkbox Datei
		String[] texte2 = {""};
		Component[] komponenten2 = {this.cbDatei};
		box.add(makePanel2(texte2, komponenten2));

		// Feld fuer Iterationen
		JButton[] btIter = {new JButton(ACTION_KLEINER),
			new JButton(ACTION_GROESSER), new JButton(ACTION_NEU),
			new JButton(ACTION_STARTEN)};
		btIter[0].setPreferredSize(new Dimension(29, 26));
		btIter[1].setPreferredSize(new Dimension(29, 26));

		Component[] komponenten3 = {this.tfIter, btIter[0], btIter[1],
			btIter[2], btIter[3]};
		String[] texte3 = {"", "", "", "    ", ""};

		box.add(makePanel1("Iterationen:"));
		box.add(makePanel2(texte3, komponenten3));

		// Felder fuer Dateiname
		box.add(makePanel1("Dateiname:"));
		komponenten2[0] = this.tfDatei;
		box.add(makePanel2(texte2, komponenten2));

		// ActionListener setzen
		this.cbDatei.addItemListener(this.controller);
		btIter[0].addActionListener(this.controller);
		btIter[1].addActionListener(this.controller);
		btIter[2].addActionListener(this.controller);
		btIter[3].addActionListener(this.controller);

		return box;
	}
	/**
	 * Ausfbau einer Box, die im Sueden des Panels eingefuegt wird.
	 * Hier gibt es 5 Buttons. 4 davon verschieben die Grafik nach links, rechts,
	 * oben oder unten.
	 * Der letzte, zentrale Button, stellt die Ausgangsgrafik wieder her.
	 * @return untere Box
	 */
	private Box makeBoxSuedNavigat() {
		// Suedliche Box mit Buttons zum verschieben der Sicht der Mandelbrotmenge
		Box box = Box.createVerticalBox();

		// Masze der Button
		int mass = 65;

		// Button Hoch
		JButton[] buttons1 = {new JButton(ACTION_HOCH)};
		buttons1[0].setPreferredSize(new Dimension(mass, mass));
		box.add(makePanel3(buttons1, FlowLayout.CENTER));

		// Buttons links, zentrieren, rechts
		JButton[] buttons2 = {new JButton(ACTION_LINKS),
			new JButton(ACTION_ZENTRIERT), new JButton(ACTION_RECHTS)};
		buttons2[0].setPreferredSize(new Dimension(mass, mass));
		buttons2[1].setPreferredSize(new Dimension(mass, mass));
		buttons2[2].setPreferredSize(new Dimension(mass, mass));
		box.add(makePanel3(buttons2, FlowLayout.CENTER));

		// Button runter
		JButton[] buttons3 = {new JButton(ACTION_RUNTER)};
		buttons3[0].setPreferredSize(new Dimension(mass, mass));
		box.add(makePanel3(buttons3, FlowLayout.CENTER));

		// ActionListener
		buttons1[0].addActionListener(this.controller);
		buttons2[0].addActionListener(this.controller);
		buttons2[1].addActionListener(this.controller);
		buttons2[2].addActionListener(this.controller);
		buttons3[0].addActionListener(this.controller);

		return box;
	}
	/**
	 * Aufbau einer Box, die die beiden Button zum ein- und auszoomen
	 * enthaelt
	 * @return Box mit Zoombuttons
	 */
	private Box makeBoxSuedZoom() {
		// Suedliche Box mit Buttons zum Ein- und Auszoomen
		Box box = Box.createVerticalBox();

		JButton[] buttons1 = {new JButton(ACTION_ZOOMEIN)};
		buttons1[0].setPreferredSize(new Dimension(75, 110));
		box.add(makePanel3(buttons1, FlowLayout.CENTER));

		JButton[] buttons2 = {new JButton(ACTION_ZOOMAUS)};
		buttons2[0].setPreferredSize(new Dimension(75, 110));
		box.add(makePanel3(buttons2, FlowLayout.CENTER));

		buttons1[0].addActionListener(this.controller);
		buttons2[0].addActionListener(this.controller);

		return box;
	}
	/**
	 * JPanel mit beliebiger Anzahl von Elementen im FlowLayout
	 * @param co Feld der einzufuegenden Komponenten
	 * @return JPanel mit den einzufuegenden Komponenten
	 */
	private JPanel makePanel3(Component[] co, int flow) {
		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(flow));

		for(int i = 0; i < co.length; i++) {
			panel.add(co[i]);
		}

		return panel;
	}
	/**
	 * JPanel mit beliebiger Anzahl von Elementen im FlowLayout nebst Labels
	 * @param text Feld der Einzufuegenden Labeltexte
	 * @param co Feld der einzufuegenden Komponenten
	 * @return JPanel mit (Label_1:Komponente_1:Label_2:Komponente_2:...Label_n:Komponente_n:)
	 */
	private JPanel makePanel2(String[] text, Component[] co) {
		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		for(int i = 0; i < co.length; i++) {
			panel.add(new JLabel(text[i]));
			panel.add(co[i]);
		}

		return panel;
	}
	/**
	 * Bau eines JLabel in eigenes JPanel
	 * @param text Labeltext
	 * @return JPanel mit JLabel
	 */
	private JPanel makePanel1(String text) {
		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		panel.add(new JLabel(text));

		return panel;
	}
	/**
	 * Initiieren der Textfelder mit ihren Breiten, sowie setzen der Checkbox
	 * auf false.
	 * Textbos Datei ist nicht aktiv
	 */
	private void init() {
		this.tfCAreal = new JTextField(10);
		this.tfCAreal.setHorizontalAlignment(JTextField.RIGHT);
		this.tfCAimag = new JTextField(10);
		this.tfCAimag.setHorizontalAlignment(JTextField.RIGHT);
		this.tfCEreal = new JTextField(10);
		this.tfCEreal.setHorizontalAlignment(JTextField.RIGHT);
		this.tfCEimag = new JTextField(10);
		this.tfCEimag.setHorizontalAlignment(JTextField.RIGHT);

		this.tfBreite = new JTextField(8);
		this.tfBreite.setHorizontalAlignment(JTextField.RIGHT);
		this.tfHoehe = new JTextField(8);
		this.tfHoehe.setHorizontalAlignment(JTextField.RIGHT);

		this.tfIter = new JTextField(8);
		this.tfIter.setHorizontalAlignment(JTextField.RIGHT);

		this.tfDatei = new JTextField(14);
		this.tfDatei.setHorizontalAlignment(JTextField.RIGHT);
		this.tfDatei.setEnabled(false);

		this.cbDatei = new JCheckBox(ITEM_LESEN, false);

		this.lbMenge = new JLabel("Mandelbrotmenge");
	}
	/**
	 * Setzen von Ausgangswerten in Textfelder. Zuruecksetzen der Checkbox
	 */
	void neu() {
		this.tfCAreal.setText("");
		this.tfCAimag.setText("");
		this.tfCEreal.setText("");
		this.tfCEimag.setText("");

		this.tfIter.setText("");

		this.tfDatei.setText("");

		this.tfBreite.setText("");
		this.tfHoehe.setText("");

		this.cbDatei.setSelected(false);
	}
	/**
	 * Loesen von controller
	 */
	public void release() {
		this.controller = null;
	}
}
