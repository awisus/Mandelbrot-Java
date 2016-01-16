package aufgabe_4;
import java.util.*;
/**
 * Implementation einer Mandelbrotmenge. Punkte in einem Bereich [ca, ce], wobei
 * ca, ce aus Complex, werden durchlaufen und dabei betrachtet, ob diese zur
 * Mandelbrotmenge gehoeren oder nicht.
 * @author Jens Awisus
 */
public class Mandelbrot extends Observable {
	/**
	 * Menge der Punkte im Darstellungsbereich.
	 * Speichert je die erreichte Iterationetiefe des Punktes beim Test mit der
	 * Mandelbrotmenge.
	 */
	protected int[][] punkte;
	/**
	 * Breite des Darstellungsbereiches
	 */
	protected int breite;
	/**
	 * Hoehe des Darstellungsbereiches
	 */
	protected int hoehe;
	/**
	 * Komplexe Konstante c in z_(n+1) = z_n² + c
	 */
	protected Complex c;
	/**
	 * untere Bereichsgrenze
	 */
	protected Complex ca;
	/**
	 * obere Bereichsgrenze
	 */
	protected Complex ce;
	/**
	 * Anzahl der zu durchlaufenden Iterationen
	 */
	protected int iterationen;
	/**
	 * Konstruktor, setzt Iterationenanzahl, untere und obere Bereichsgrenzen,
	 * sowie Bildbereichsgroessen
	 * @param ca untere Bereichsgrenze
	 * @param ce obere Bereichsgrenze
	 * @param iterationen Iterationenanzahl
	 * @param breite Breite Darstellungsbereich
	 * @param hoehe Hoehe Darstellungsbereich
	 */
	public Mandelbrot(Complex ca, Complex ce, int iterationen, int breite,
		int hoehe) {

		this.setDimension(breite, hoehe);
		this.setGrenzen(ca, ce);
		this.setIterationen(iterationen);
	}
	/**
	 * Berechnung der Iterationstiefen der Bildpunkte
	 */
	public void berechnen() {
		this.punkte = new int[this.breite][this.hoehe];

		// Breite ablaufen
		for(int r = 0; r < this.breite; r++) {

			// Hoehe ablaufen
			for(int s = 0; s < this.hoehe; s++) {
				/*
				 * Zu jedem Punkt des Bildbereiches wird nun die Iterationszahl
				 * gespeichert
				 */
				this.punkte[r][s] = this.iterationsAnfang(this.punktZuComplex(r, s));
			}
		}

		// Observer benachrichtigen
		this.setChanged();
		this.notifyObservers();
	}
	/**
	 * Iterationsanfang z_0 = c
	 * @param z zu untersuchende Zahl der komplexen Ebene
	 * @return Iterationstiefe von z bis Iterationsabbruch
	 */
	protected int iterationsAnfang(Complex z) {
		// Iterationsanfang, c = a + bi und z = x + yi
		this.c = z;
		return inMenge(z);
	}
	/**
	 * Prueft, ob ein Punkt z der komplexen Ebene in der Mandelbrotmenge liegt.
	 * Dabei wird ein gegebener Bildunkt z mit z_n = (z_n-1)^2 + c durchiteriert,
	 * wobei z_0 = c.
	 * @param z zu untersuchender Punkt
	 * @return Iterationstiefe bei Abbruch oder iterationen bei vollem Durchlauf
	 * der Schleife
	 */
	protected int inMenge(Complex z) {
		// Merken der Iterationstiefe fuer spaetere Faerbung
		int iteration = 0;

		// Durchiterieren
		for(int i = 0; i < this.iterationen; i++) {

			// aktuelle Iterationstiefe speichern
			iteration = i;

			// Betrag von z auswerten (Darf nicht groesser als 2 sein)
			double drueber = (z.getReal() * z.getReal() + z.getImagin()
				* z.getImagin());

			// Falls Betrag zu gross, so gib Iterationstiefe zurueck
			if(drueber > 4) {
				return iteration;
			}
			
			// Iterationsschritt
			// Neuer Realteil: x_neu = x^2 - y^2 + a
			double x = (z.getReal() * z.getReal() - z.getImagin()
				* z.getImagin()) + this.c.getReal();

			// Neuer Imaginaerteil: y_neu = 2xy + b
			double y = (2 * z.getReal() * z.getImagin()
				+ this.c.getImagin());

			// Werte validieren
			z = new Complex(x, y);
		}
		return this.iterationen;
	}
	/**
	 * Umformung eines Bildpunktes in eine komplexe Zahl
	 * @param r Realteil des Bildpunktes
	 * @param s Imaginaerteil des Bildpunktes
	 * @return zum Punkt korrespondierende komplexe Zahl
	 */
	public Complex punktZuComplex(int r, int s) {
		// Uebersetzung in Zahl der komplexen Ebene
		double a = ((this.ce.getReal() - this.ca.getReal()) / this.breite) * r
			+ this.ca.getReal();

		double b = ((this.ce.getImagin() - this.ca.getImagin()) / this.hoehe)
			* (this.hoehe - s) + this.ca.getImagin();
		Complex c = new Complex(a, b);

		return c;
	}
	/**
	 * Setter fuer Bildbreite
	 * @param breite gewunschte Bildbreite
	 * @param hoehe gewunschte Bildhoehe
	 */
	public void setDimension(int breite, int hoehe) {
		if(breite > 0 && hoehe > 0) {
			this.breite = breite;
			this.hoehe = hoehe;
		}
	}
	/**
	 * Setter fuer die Bereichsgrenzen ca und ce. Es wird die Validitaet der uebergebenen
	 * Daten geprueft und erst gesetzt, wenn Re(c_a) < Re(c_e) und Im(c_a) < I,(c_e)
	 * @param ca untere Bereichsgrenze
	 * @param ce obere Bereichsgrenze
	 */
	public void setGrenzen(Complex ca, Complex ce) {
		if((ca.getReal() < ce.getReal()) && (ca.getImagin() < ce.getImagin())) {
			this.ca = ca;
			this.ce = ce;
		}
	}
	/**
	 * Setter fuer Iterationenanzahl
	 * @param iterationen Iterationenanzahl
	 * @return true, wenn gueltiger Wert (> 0), false sonst
	 */
	public void setIterationen(int iterationen) {
		// Es koennen nur positive Werte an Iterationen genutzt werden
		if(iterationen > 0) {
			// Wenn Eingabe ok, dann gib true zurueck ...
			this.iterationen = iterationen;
		}
	}
	/**
	 * Getter fuer Bereichsanfang
	 * @return ca Bereichsanfang
	 */
	public Complex getCa() {
		return this.ca;
	}
	/**
	 * Getter fuer Bereichsende
	 * return ca Bereichsende
	 */
	public Complex getCe() {
		return this.ce;
	}
	/**
	 * Getter fuer Iterationenanzahl
	 * @return Iterationenanzahl
	 */
	public int getIterationen() {
		return this.iterationen;
	}
	/**
	 * Getter fuer Bildbreite
	 * @return Bildbreite
	 */
	public int getBreite() {
		return this.breite;
	}
	/**
	 * Getter fuer Bildhoehe
	 * @return Bildhoehe
	 */
	public int getHoehe() {
		return this.hoehe;
	}
	/**
	 * Rueckgabe des Punktes (i, j)
	 * @param i
	 * @param j
	 * @return Punkt an Stelle (i, j)
	 */
	public int getPunkt(int i, int j) {
		return this.punkte[i][j];
	}
}
