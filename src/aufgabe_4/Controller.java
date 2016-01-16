package aufgabe_4;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
/**
 * Controller fuer Mandelbrotview und -model.
 * @author Jens Awisus
 */
public class Controller implements ActionListener, ItemListener, MouseListener,
	MouseWheelListener, MouseMotionListener {
	/**
	 * Modell Mandelbrotmenge
	 */
	private Mandelbrot model;
	/**
	 * View fuer Mandelbrotmenge
	 */
	private View view;
	/**
	 * Konstruktor. initiiert model und view
	 * @param model Instanz einer Mandelbrotmenge
	 * @param view Instanz einer View
	 */
	public Controller(Mandelbrot model, View view) {
		this.model = model;
		this.view = view;
	}
	/**
	 * Hoeren auf die Aktionen der View (Aus der Menueleiste, den Eingabebutton und der
	 * Navigations- und Zoombutton)
	 * @param ae ActionEvent
	 */
	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		this.menueKontrolle(command);
		this.eingabeKontrolle(command);
		this.naviKontrolle(command);
    }
	/**
	 * Kontrolle der Eingabe aus der Menueleiste
	 * @param command ActionCommand
	 */
	private void menueKontrolle(String command) {
		// Aufruf Speichern der Startdaten
		if(command.equals(View.ACTION_SPEICHERN)) {
			this.speichern();
		}

		// Aufruf des png-Exports
		if(command.equals(View.ACTION_EXPORTIEREN)) {
			this.exportieren(this.view.getBild());
		}

		/* Wahl einer Zahl aus JMenu ITERATIONEN setzt im Model neue
		 * Iterationenanzahl
		 */
		if(this.istZahl(command)) {
			this.setIter(command);
		}

		// Ausloesen eines Farbwechsels der Darstellung
		if(command.contains("Farbe: ")) {
			this.setBildFarbe(command);
		}

		// Beenden der aktuellen View
		if(command.equals(View.ACTION_BEENDEN)) {
			this.view.release();
		}
	}
	/**
	 * Kontrolle der Eingaben aus den Textfelder nebst zugehoeriger Button
	 * @param command ActionCommand
	 */
	private void eingabeKontrolle(String command) {
		// Verkleinern der Iterationenanzahl mit Button -
		if(command.equals(PanelEingabe.ACTION_KLEINER)) {
			int iter;
			if(this.view.getPanel().tfIter.getText().equals("")) {
				iter = 0;
			} else {
				iter = Integer.parseInt(this.view.getPanel().tfIter.getText());
			}
			if(iter > 0) {
				this.view.getPanel().tfIter.setText("" +(iter - 5));
			}
		}

		// Vergroessern der Iterationenanzahl mit Button +
		if(command.equals(PanelEingabe.ACTION_GROESSER)) {
			int iter;
			if(this.view.getPanel().tfIter.getText().equals("")) {
				iter = 0;
			} else {
				iter = Integer.parseInt(this.view.getPanel().tfIter.getText());
			}
			this.view.getPanel().tfIter.setText("" +(iter + 5));
		}
		
		// Reset wenn NEU geklickt
		if(command.equals(PanelEingabe.ACTION_NEU)) {
			this.view.getPanel().neu();
		}

		// Druecken des Button Starten loest Dateneinlesen und neues zeichnen aus
		if(command.equals(PanelEingabe.ACTION_STARTEN)) {
			if(!(this.model.getClass().equals(this.view.getJulia().getClass()))) {
				try {
					this.starten();
				} catch(Exception e) {
					System.out.println("Fehler! Ein Eingabefeld ist leer.");
				}
			}
		}
	}
	/**
	 * Kontrolle der Eingaben der Navigations- und Zoombutton
	 * @param command ActionCommand
	 */
	private void naviKontrolle(String command) {
		// Zentrierenbutton stellt Ausgangsgrafik wieder her
		if(command.equals(PanelEingabe.ACTION_ZENTRIERT)) {
			this.zentrieren();
		}

		// Button Hoch verschiebt Bild nach oben
		if(command.equals(PanelEingabe.ACTION_HOCH)) {
			this.verschieben("hoch");
		}

		// Button Runter verschiebt Bild nach unten
		if(command.equals(PanelEingabe.ACTION_RUNTER)) {
			this.verschieben("runter");
		}

		// Button Links verschiebt Bild nach links
		if(command.equals(PanelEingabe.ACTION_LINKS)) {
			this.verschieben("links");
		}

		// Button Rechts verschiebt Bild nach rechts
		if(command.equals(PanelEingabe.ACTION_RECHTS)) {
			this.verschieben("rechts");
		}

		// Button Rechts verschiebt Bild nach rechts
		if(command.equals(PanelEingabe.ACTION_ZOOMEIN)) {
			int r = (this.model.getBreite()) / 2;
			int s = (this.model.getHoehe()) / 2;
			this.zoom(r, s, true);
		}

		// Button Rechts verschiebt Bild nach rechts
		if(command.equals(PanelEingabe.ACTION_ZOOMAUS)) {
			int r = (this.model.getBreite()) / 2;
			int s = (this.model.getHoehe()) / 2;
			this.zoom(r, s, false);
		}
	}
	/**
	 * Speichern der Startdaten der aktuellen Darstellung auf eine Datei
	 */
	private void speichern() {
		// Dateiname zufaellig erstellen
		String dateiname = "Sav_" +System.currentTimeMillis() +".out";

		// Iterationenanzahl aus tfIter
		int iterationen = this.model.getIterationen();

		// Ausgabedatei anlegen
		Datei datei = new Datei(dateiname, false, this.model.getCa(),
			this.model.getCe(), iterationen);

		this.view.getPanel().tfDatei.setText(dateiname);

		try {
			Desktop.getDesktop().open(datei);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Speichern des Darstellungsbereiches Bild in eine Bilddatei
	 */
	private void exportieren(Bild bild) {
		BufferedImage image = new BufferedImage(bild.getWidth(),
			bild.getHeight(), BufferedImage.TYPE_INT_ARGB);

		bild.paint(image.getGraphics());

		try {
			File ausgabe = new File("Set_" +this.model.getIterationen()
				+"_" +bild.getWidth() +"x" +bild.getHeight()
				+"_" +System.currentTimeMillis() +".png");
			ImageIO.write(image, "png", ausgabe);

			// Anzeige des Bildes
			Desktop.getDesktop().open(ausgabe);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Hilfmethode mit der Moeglichkeit, zur Laufzeit die Farbdarstellung zu
	 * aendern. Hierbei werden Events aus dem JMenu Farbe abgefragt und im Bild
	 * die korrespindierend Farkonstante gesetzt, was auch einen rapaint
	 * ausloest
	 * @param command ActionCommand
	 */
	private void setBildFarbe(String command) {
		switch(command) {
		case "Farbe: Gelb": this.view.getBild().setFarbe(Bild.FARBE_GELB);
			break;
		case "Farbe: Rot": this.view.getBild().setFarbe(Bild.FARBE_ROT);
			break;
		case "Farbe: Gruen": this.view.getBild().setFarbe(Bild.FARBE_GRUEN);
			break;
		case "Farbe: Blau": this.view.getBild().setFarbe(Bild.FARBE_BLAU);
			break;
		case "Farbe: Violett": this.view.getBild().setFarbe(Bild.FARBE_VIOLETT);
			break;
		case "Farbe: Orange": this.view.getBild().setFarbe(Bild.FARBE_ORANGE);
			break;
		case "Farbe: Gold": this.view.getBild().setFarbe(Bild.FARBE_GOLD);
			break;
		case "Farbe: Grau": this.view.getBild().setFarbe(Bild.FARBE_GRAU);
			break;
		case "Farbe: Invertiert": this.view.getBild().invertieren();
			break;
		}
		
	}
	/**
	 * Hilfmethode, welche den Status der Checkbox abgleicht und dann das
	 * einlesen der notwendigen Daten anleitet (Entweder aus einer Datei oder
	 * aus Textfeldern, je nach Status der Checkbox). Beim lesen einer Datei
	 * werden ihre eingelesenen Werte in die Textfelder eingetragen. Zum Schluss
	 * werden dem Modell die Daten uebergeben und eine Neuberechnung ausgeloest
	 */
	private void starten() {
		// Deklarationen
		String dateiname;
		Datei datei;
		Complex ca;
		Complex ce;
		int iterationen;

		// Wenn Checkbox ausgewaehlt
		if(this.view.getPanel().cbDatei.isSelected()) {
				/* Aus Datei werden die zahlen gelesen und
				 * entsprechend gesetzt
				 */

				// Dateiname aus Textfeld lesen
				dateiname = this.view.getPanel().tfDatei.getText();
	
				// Dateiinput lesen
				datei = new Datei(dateiname, true, null, null, 0);
				ca = datei.getCa();
				ce = datei.getCe();
				iterationen = datei.getIterationen();
		}
		// Wenn Checkbox nicht ausgewaehlt
		else {
			/* Aus den oberen 5 Textfelder werden die Strings gelesen und in
			 * relevante Zahlen geparsed.
			 */

			// C_a aus tfCAreal und tfCAimag
			double x = Double.parseDouble(this.view.getPanel().tfCAreal.getText());
			double y = Double.parseDouble(this.view.getPanel().tfCAimag.getText());
			ca = new Complex(x, y);

			// C_e aus tfCEreal und tfCEimag
			x = Double.parseDouble(this.view.getPanel().tfCEreal.getText());
			y = Double.parseDouble(this.view.getPanel().tfCEimag.getText());
			ce = new Complex(x, y);

			// Iterationenanzahl aus tfIter
			iterationen = Integer.parseInt(this.view.getPanel().tfIter.getText());
		}

		// Breite und Hoehe der Ausgabe
		int	breite = Integer.parseInt(this.view.getPanel().tfBreite.getText());
		int	hoehe = Integer.parseInt(this.view.getPanel().tfHoehe.getText());

		// Bild erneuern
		this.model.setDimension(breite, hoehe);
		this.model.setIterationen(iterationen);
		this.view.setLbIter(iterationen);
		this.model.setGrenzen(ca, ce);

		// Aenderungen im Model anwenden
		this.model.berechnen();
		
		// Groessenaenderungen anwenden
		this.view.getBild().setPreferredSize(new Dimension(breite, hoehe));
		this.view.setGrenzen(ca, ce);
		this.view.pack();
	}
	/**
	 * Pruefen eines Strings, ob er Zahl ist. Das ist genau dann der Fall, wenn
	 * beim parsen keine Exception auftritt
	 * @param str Teststring
	 * @return true, wenn Zahl, false sonst
	 */
	private boolean istZahl(String str) {
		try {
			int zahl = Integer.parseInt(str);
		    return zahl <= 1000;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	/**
	 * Hilfsmethode um aus dem Iterationenmenue die korrespondierende Zahl ins
	 * aktuelle Model zu setzen
	 * @param command ActionCommand
	 */
	private void setIter(String command) {
		// Auswahl einer Iterationszahl mit MenueIterationen
		int iterationen;
		if(this.view != null) {
			iterationen = this.model.getIterationen();
		}

		iterationen = Integer.parseInt(command);

		// Bild mit neuen Iterationen zeichnen
		this.model.setIterationen(iterationen);
		this.model.berechnen();

		// Label umsetzen
		this.view.setLbIter(iterationen);
	}
	/**
	 * Hilfmethode, welche die urspruenglich geladene Ansicht wiederherstellt
	 */
	private void zentrieren() {
		/* Unterschied, ob Mandelbrotmenge oder Juliamenge
		 * d.h. bei Juliamenge soll Bereich [(-2,-2),(2,2)] wiederhergestellt
		 * werden
		 */
		if(!(this.model.getClass().equals(this.view.getJulia().getClass())))
		{
			this.model.setGrenzen(this.view.getCa(), this.view.getCe());
		}
		else {
			this.model.setGrenzen(Julia.CA, Julia.CE);
		}

		// Neues Bild um den Punkt herum
		this.model.berechnen();
		this.view.setLbIter(this.model.getIterationen());
	}
	/**
	 * Hilfsmthode, um Ansicht verschieben zu koennen.
	 * Alle 4 Richtungen waehlbar.
	 * @param richtung Verschieberichtung im Stile von z.B. "hoch"
	 */
	private void verschieben(String richtung) {
		// Verschieben um ein Zenhtel des Bildbereiches
		double dif = (this.model.getCe().getImagin() -
			this.model.getCa().getImagin()) * 0.10;

		// Fallunterscheidung Hoch/Runter/Links/Rechts
		switch(richtung) {
		case "hoch":
			// Imaginaerteile erhoehen
			this.model.setGrenzen(
				new Complex(this.model.getCa().getReal(),
					this.model.getCa().getImagin() + dif),
				new Complex(this.model.getCe().getReal(),
					this.model.getCe().getImagin() + dif));
			break;

		case "runter":
			// Imaginaerteile erhoehen
			this.model.setGrenzen(
				new Complex(this.model.getCa().getReal(),
					this.model.getCa().getImagin() - dif),
				new Complex(this.model.getCe().getReal(),
					this.model.getCe().getImagin() - dif));
			break;

		case "links":
			// Realteile senken
			this.model.setGrenzen(
				new Complex(this.model.getCa().getReal() - dif,
					this.model.getCa().getImagin()),
				new Complex(this.model.getCe().getReal() - dif,
					this.model.getCe().getImagin()));
			break;

		case "rechts":
			// Realteile erhoehen
			this.model.setGrenzen(
				new Complex(this.model.getCa().getReal() + dif,
					this.model.getCa().getImagin()),
				new Complex(this.model.getCe().getReal() + dif,
					this.model.getCe().getImagin()));
			break;
		}

		// Neuberechnung im Model ausloesen
		this.model.berechnen();
	}
	/**
	 * Eine Methode, die einen Zoom in oder aus der Dargestellten Menge erlaubt.
	 * Hierbei wird ein Punkt (r,s) des Bildbereiches zuerst in eine
	 * korrespondierende komplexe Zahl umgewandelt, bevor um diesen Punkt herum
	 * ein neuer Bildbereich berechnet wird. boolean ein bestimmt Zoomfaktor
	 * (Vergroesserung oder Verkleinerung).
	 * @param r x-Koordinate der einzuzoomenden Punktes
	 * @param s y-Koordinate der einzuzoomenden Punktes
	 * @param ein true, wenn eingezoomt werden soll, false sonst
	 */
	private void zoom(int r, int s, boolean ein) {
		// uebersetzen des Punktes in Zahl der komplexen Ebene
		Complex punkt = this.model.punktZuComplex(r, s);

		// Bereich verkleinern um Punkt herum
		double faktor = 0.6;
		
		double difRe;
		double difIm;
		if(ein) {
			difRe = (faktor * (this.model.getCe().getReal() - this.model.getCa().getReal())) * 0.5;
			difIm = (faktor * (this.model.getCe().getImagin() - this.model.getCa().getImagin())) * 0.5;
		}
		else {
			difRe = ((this.model.getCe().getReal() - this.model.getCa().getReal()) / faktor) * 0.5;
			difIm = ((this.model.getCe().getImagin() - this.model.getCa().getImagin())  / faktor) * 0.5;
		}

		// Neues Bild um den Punkt herum
		this.model.setGrenzen(
			new Complex(punkt.getReal() - difRe, punkt.getImagin() - difIm), 
			new Complex(punkt.getReal() + difRe, punkt.getImagin() + difIm));

		this.model.berechnen();
	}
	/**
	 * Ueberpruefen de Selektionsstatus der Checkbox. Aktiviert oder Deaktiviert
	 * Textfelder.
	 * @param ie ItemEvent
	 */
	@Override
	public void itemStateChanged(ItemEvent ie) {
		// Aktivieren oder Deaktivieren von Textfeldern
		if(this.view.getPanel().cbDatei.isSelected()) {
			this.view.getPanel().tfCAreal.setEnabled(false);
			this.view.getPanel().tfCAimag.setEnabled(false);
			this.view.getPanel().tfCEreal.setEnabled(false);
			this.view.getPanel().tfCEimag.setEnabled(false);
			this.view.getPanel().tfIter.setEnabled(false);
			this.view.getPanel().tfDatei.setEnabled(true);
		}
		else {
			this.view.getPanel().tfCAreal.setEnabled(true);
			this.view.getPanel().tfCAimag.setEnabled(true);
			this.view.getPanel().tfCEreal.setEnabled(true);
			this.view.getPanel().tfCEimag.setEnabled(true);
			this.view.getPanel().tfIter.setEnabled(true);
			this.view.getPanel().tfDatei.setEnabled(false);
		}
	}
    /**
     * Linksklick fuer Zoom auf den geklickten Pukt, Rechtsklick loest
     * Wechseln zwischen Julia- und Mandelbrotmenge aus
     * @param e MouseEvent
     */
	@Override
	public void mouseClicked(MouseEvent e) {
		// Koordinaten Punkt
		int r = e.getX();
		int s = e.getY();

		if(e.getButton() == 1) {
			this.zoom(r, s, true);
		}

		if(e.getButton() == 3) {
			if(this.model.getClass().equals(this.view.getJulia().getClass())) {
				this.view.getPanel().lbMenge.setText("Mandelbrotmenge");
			}
			else {
				this.view.getJulia().setC(this.model.punktZuComplex(r, s));
				this.view.getJulia().setDimension(this.model.getBreite(), this.model.getHoehe());
				this.view.getJulia().setIterationen(this.model.getIterationen());

				this.view.getJulia().berechnen();

				this.view.getPanel().lbMenge.setText("Juliamenge mit c = "
						+this.model.punktZuComplex(r, s));
			}
			this.model = this.view.alterModel();
		}
		
		this.view.setFelder();
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	/**
	 * Wenn sich Maus ueber das Bild bewegt, so wird die zum dem Pixel gehoerige
	 * komplexe Zahl im Label der Menuezeile angezeigt
	 * @param e MouseEvent
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		// Koordinaten Punkt
		int r = e.getX();
		int s = e.getY();

		Complex punkt = this.model.punktZuComplex(r, s);
		this.view.lbPunkt.setText("Punkt: " +punkt.toString());
	}
	/**
	 * Erhoehen oder Verringern der Iterationen mit Mausrad
	 * @param e MouseWheelEvent
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int iterationen = this.model.getIterationen();
			if(e.getWheelRotation() < 0) {
				iterationen += 5;
			}
			// Zahl der Iterationen senken
			else {
				if(iterationen > 4) {
					iterationen -= 5;
				}
			}
		// Bild mit neuen Iterationen zeichnen
		this.model.setIterationen(iterationen);

		this.model.berechnen();

		// Label umsetzen
		this.view.setLbIter(iterationen);
	}
	/**
	 * release. view und model werden freigegeben
	 */
    public void release() {
    	this.model = null;
		this.view = null;
	}
}
