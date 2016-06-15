package aufgabe_4;
import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.util.*;
/**
 * View fuer Anzeige einer Mandelbrotmenge. Enthaelt den Darstellungsbereich des
 * Bildes und eine Menueleiste, welche Funktionen zum Schliessen, Exportieren,
 * Speichern, Iterationen setzen, Farbe aendern bereitstellt. Ebenso gibt es zur
 * Dateneingabe Textfelder sowie Buttons zum Zuruecksetzen, Starten, Zoomen und
 * Navigieren in der Mandelbrotmenge.
 * @author Jens Awisus
 */
@SuppressWarnings("serial")
public class View extends JFrame implements Observer {
    /**
     * Speichernstring
     */
	public static final String ACTION_SPEICHERN = "Werte in Datei speichern";
    /**
     * Exportierenstring
     */
	public static final String ACTION_EXPORTIEREN = "Exportieren als .png";
    /**
     * Schliessenstring
     */
	public static final String ACTION_BEENDEN = "Schliessen";
	/**
	 * Anfang des Untersuchungsbereiches
	 */
	private Complex ca;
	/**
	 * Ende des Untersuchungsbereiches
	 */
	private Complex ce;
	/**
	 * Iterationenlabel
	 */
	private JLabel lbIter;
	/**
	 * Punktelabel
	 */
	JLabel lbPunkt;
	/**
	 * Bereich der Eingabeelemente
	 */
	private PanelEingabe panelEingabe;
	/**
	 * Bereich fuer Mandelbrotmenge
	 */
	private Bild bild;
	/**
	 * Darstellungsbereich
	 */
	private JPanel contentPane;
	/**
	 * Mandelbrotmodell
	 */
	private Mandelbrot mandelbrot;
	/**
	 * Juliamodell
	 */
	private Julia julia;
	/**
	 * Referenz auf das aktuelle Modell
	 */
	private Mandelbrot modelAktuell;
	/**
	 * Controller
	 */
    private Controller controller;
    /**
     * Konstruktor fuer Anzeige
     * @param breite Breite des Darstellungsbereiches
     * @param hoehe Hoehe des Darstellungsbereiches
	 * @param ca untere Bereichsgrenze
	 * @param ce obere Bereichsgrenze
	 * @param iterationen Anzahl der Iterationen
     */
	public View(int breite, int hoehe, Complex ca, Complex ce, int iterationen)
	{
		// Titel
		super("Mandelbrot");

		// Merken der Ausgangsgrenzen, um Sicht wiederherstellen zu koennen
		this.ca = ca;
		this.ce = ce;

		// Mandelbrot- und Juliamodell instanziieren
		this.mandelbrot = new Mandelbrot(ca, ce, iterationen, breite, hoehe);
		this.julia = new Julia(iterationen,	breite, hoehe);

		// Startmodell Mandelbrot
		this.modelAktuell = mandelbrot;
		this.modelAktuell.berechnen();

		// Dem Observer zufuehren
		this.mandelbrot.addObserver(this);
		this.julia.addObserver(this);

		// Controller zu aktuellem Model
		this.controller = new Controller(this.modelAktuell, this);

		// Bereich: Einsetzen des Bildes und des Eingabeblocks
		this.contentPane = new JPanel();
		this.bild = new Bild(this.modelAktuell);
		this.panelEingabe = new PanelEingabe(this.controller);

		// Listener ansetzen
		this.bild.addMouseListener(this.controller);
		this.bild.addMouseMotionListener(this.controller);

		this.addMouseWheelListener(this.controller);

		// View erzeugen
		this.makeView();

		// Textfelder zeigen Daten der aktuellen Grafik
		this.setFelder();

		// Standards
		this.setContentPane(this.contentPane);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
	/**
	 * Erzgeugen und anfuegen einer Menueleiste, Setzen eines Layoutes des
	 * ContentPane, eines Images des Programmes und einsetzen der JPanel
	 * Bild und panelEingabe
	 */
	private void makeView() {
		// Icon setzen
		this.setIconImage(new ImageIcon("icon.png").getImage());

		// Layout
		this.contentPane.setLayout(new BorderLayout());

		// Menueleiste
		this.makeMenueLeiste();

		contentPane.add(this.bild, BorderLayout.WEST);
		contentPane.add(this.panelEingabe, BorderLayout.EAST);
	}
	/**
	 * Hilfmethode zum Erzegen und setzen einer Menueleiste. Enthaelt
	 * Untermenues Datei, Iterationen, Farbe
	 */
	private void makeMenueLeiste() {
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(makeManueDatei());
		menuBar.add(makeMenueIter());
		menuBar.add(makeMenueFarbe());

		// Abstand
		menuBar.add(Box.createHorizontalGlue());

		// Label, das Iterationenanzahl zeigt
		this.lbIter = new JLabel("Iterationen: " +this.mandelbrot.getIterationen());
		lbIter.setForeground(Color.GRAY);
		menuBar.add(this.lbIter);

		menuBar.add(Box.createHorizontalGlue());

		// Label zur Anzeige des aktuellen Punktes der komplexen Ebene
		this.lbPunkt = new JLabel("Punkt: " +(new Complex(0, 0).toString()));
		lbPunkt.setForeground(Color.GRAY);
		menuBar.add(this.lbPunkt);

		this.setJMenuBar(menuBar);
	}
	/**
	 * Hilfmethode zum Erzeugen eines Menueeintrages Datei der Menueleiste.
	 * Bietet: Speichern der Bildstartdaten, Exportieren als png und Beenden
	 * der Anwendung
	 * @return MenueBar Untermenue Datei
	 */
	private JMenu makeManueDatei() {
		// Untermenue Datei
		JMenu dateiMenu = new JMenu("Datei");

		// Eintrage von Datei
		JMenuItem speichern = new JMenuItem(ACTION_SPEICHERN);
		JMenuItem oeffnen = new JMenuItem(ACTION_EXPORTIEREN);
		JMenuItem schliessen = new JMenuItem(ACTION_BEENDEN);

		// Hoere drauf, lieber Controller
		speichern.addActionListener(controller);
		oeffnen.addActionListener(controller);
		schliessen.addActionListener(controller);

		// Ins Dateimenue einfuegen
		dateiMenu.add(speichern);
		dateiMenu.add(oeffnen);
		dateiMenu.add(schliessen);

		return dateiMenu;
	}
	/**
	 * Hilfmethode zum Erzeugen eines Menueeintrages der Menueleiste zur Auswahl
	 * einer anderen Itrationenanzahl
	 * @return MenueBar Untermenue Iterationen
	 */
	private JMenu makeMenueIter() {
		// Untermenue Iterationen
		JMenu iterMenu = new JMenu("Iterationen");

		//Eintraege fuer Iterationen
		int[] werte = {25, 50, 75, 100, 150, 200, 250, 300, 350, 400, 500, 600,
			800, 1000};

		JMenuItem[] iter = new JMenuItem[werte.length];
		for(int i = 0; i < werte.length; i++) {
			// Eintrag initialisieren
			iter[i] = new JMenuItem(String.valueOf(werte[i]));

			// Einfuegen
			iterMenu.add(iter[i]);

			// ActionListener
			iter[i].addActionListener(this.controller);
		}

		return iterMenu;
	}
	/**
	 * Hilfmethode zum Erzeugen eines Menueeintrages der Menueleiste zur Auswahl
	 * einer Farbe
	 * @return MenueBar Untermenue Farben
	 */
	private JMenu makeMenueFarbe() {
		// Untermenue Farbe
		JMenu farbMenu = new JMenu("Farbe");

		String col = "Farbe: ";
		String[] farben = {col +"Gelb", col +"Rot", col +"Gruen", col +"Blau",
			col +"Violett",	col +"Orange", col +"Gold", col +"Grau", col +"Invertiert"};

		JMenuItem[] farbe = new JMenuItem[farben.length];
		for(int i = 0; i < farben.length; i++) {
			// Eintrag initialisieren
			farbe[i] = new JMenuItem(farben[i]);

			// Einfuegen
			farbMenu.add(farbe[i]);

			// ActionListener
			farbe[i].addActionListener(this.controller);
		}

		return farbMenu;
	}
	/**
	 * Getter fuer untere Bereichsgrenze
	 * @return obeunterere Bereichsgrenze ca
	 */
	public Complex getCa() {
		return this.ca;
	}
	/**
	 * Getter fuer obere Bereichsgrenze
	 * @return obere Bereichsgrenze ce
	 */
	public Complex getCe() {
		return this.ce;
	}
	/**
	 * Getter fuer Bild
	 * @return Bild (erweitert JPanel)
	 */
	public Bild getBild() {
		return this.bild;
	}
	/**
	 * Getter fuer das EingabePanel
	 * @return Eingabenpanel
	 */
	public PanelEingabe getPanel() {
		return this.panelEingabe;
	}
	/**
	 * Getter fuer das Model Julia
	 * @return Julia
	 */
	public Julia getJulia() {
		return this.julia;
	}
	/**
	 * Methode, um das aktuelle Model auf das jeweils im Moment nicht Aktuelle
	 * zu setzen.
	 * @return aktuelles Model
	 */
	Mandelbrot alterModel() {
		if(this.modelAktuell.getClass().equals(this.julia.getClass())) {
			this.modelAktuell = mandelbrot;
		}
		else {
			this.modelAktuell = julia;
		}
		this.bild.setModel(this.modelAktuell);

		return this.modelAktuell;
	}
	/**
	 * Setter fuer das Label zur Anzeige der Iterationenanzahl
	 * @param iterationen Iterationenanzahl
	 */
    public void setLbIter(int iterationen) {
    	this.lbIter.setText("Iterationen: " +iterationen);
    }
	/**
	 * Setter fuer die Bereichsgrenzen ca und ce. Es wird die Validitaet der uebergebenen
	 * Daten geprueft und erst gesetzt, wenn Re(c_a) < Re(c_e) und Im(c_a) < I,(c_e)
	 * @param ca
	 * @param ce
	 */
	public void setGrenzen(Complex ca, Complex ce) {
		if((ca.getReal() < ce.getReal()) && (ca.getImagin() < ce.getImagin())) {
			this.ca = ca;
			this.ce = ce;
		}
	}
	/**
	 * Hilfsmethode, die in den Textfeldern von Bild immer die aktuellen Daten anzeigt
	 */
	public void setFelder() {
		this.setLbIter(this.modelAktuell.getIterationen());

		// Dezimalzahl durch Punkt getrennt!
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#0.0000000000", dfs);

		// Textfelder zeigen gezoomte Intervallgrenzen
		this.panelEingabe.tfCAreal.setText("" +f.format(this.modelAktuell.getCa().getReal()));
		this.panelEingabe.tfCAimag.setText("" +f.format(this.modelAktuell.getCa().getImagin()));
		this.panelEingabe.tfCEreal.setText("" +f.format(this.modelAktuell.getCe().getReal()));
		this.panelEingabe.tfCEimag.setText("" +f.format(this.modelAktuell.getCe().getImagin()));

		this.panelEingabe.tfIter.setText("" +this.modelAktuell.getIterationen());

		this.panelEingabe.tfBreite.setText("" +this.modelAktuell.getBreite());
		this.panelEingabe.tfHoehe.setText("" +this.modelAktuell.getHoehe());
	}
	/**
	 * update loest repaint der Grafik aus und setzt die Iterationenzeige auf
	 * den aktuellen Stand
	 * @param model Observable
	 * @param obj Object
	 */
	public void update(Observable  model, Object obj) {
		this.bild.repaint();
		this.setFelder();
	}
	/**
	 * Loesen von Objekten, Models, JPanels und Controller
	 */
	public void release() {
		this.ca = null;
		this.ce = null;

		this.bild.release();

		this.panelEingabe.release();

		this.mandelbrot.deleteObserver(this);
		this.mandelbrot = null;

		this.julia.deleteObserver(this);
		this.julia = null;

		this.modelAktuell.deleteObserver(this);
		this.modelAktuell = null;

	    this.controller.release();
	    this.controller = null;
	    this.dispose();
	}
}
