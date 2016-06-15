package aufgabe_4;
/**
 * Implementation einer Juliamenge. Punkte in einem Bereich [ca, ce], wobei ca,
 * ce aus Complex, werden durchlaufen und dabei betrachtet, ob diese zur
 * Juliamenge gehoeren oder nicht.
 * @author Jens Awisus
 */
public class Julia extends Mandelbrot {
	/**
	 * Konstante für untere Bereichsgrenze
	 */
	public static final Complex CA = new Complex(-2, -2);
	/**
	 * Konstante für obere Bereichsgrenze
	 */
	public static final Complex CE = new Complex(2, 2);
	/**
	 * Konstruktor, setzt Iterationenanzahl, untere und obere Bereichsgrenzen,
	 * sowie Bildbereichsgroessen
	 * @param iterationen Iterationenanzahl
	 * @param breite Breite Darstellungsbereich
	 * @param hoehe Hoehe Darstellungsbereich
	 */
	public Julia(int iterationen, int breite, int hoehe) {
		super(CA, CE, iterationen, breite, hoehe);
	}
	/**
	 * Iterationsanfang vor Pruefung, ob z in Juliamenge
	 */
	protected int iterationsAnfang(Complex z) {
		return inMenge(z);
	}
	/**
	 * Setter fuer Konstante c der Iteration
	 * @param c Konstante c der Iteration
	 */
	protected void setC(Complex c) {
		this.c = c;
	}
}
