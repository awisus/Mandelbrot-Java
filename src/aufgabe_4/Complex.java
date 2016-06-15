package aufgabe_4;
import java.text.DecimalFormat;
/**
 * Implementation einer komplexen Zahl mit Real- und Imaginaerteil
 * @author Jens Awisus
 */
public class Complex {
	/**
	 * Realteil
	 */
	private double real;
	/**
	 * Imaginaerteil
	 */
	private double imagin;
	/**
	 * Konstruktur, setzt Real- und Imaginaerteil
	 * @param real Realteil
	 * @param imagin Imaginaerteil
	 */
	public Complex(double real, double imagin) {
		this.real = real;
		this.imagin = imagin;
	}
	/**
	 * Getter fuer Realteil
	 * @return Realteil
	 */
	public double getReal() {
		return this.real;
	}
	/**
	 * Getter fuer Imaginaerteil
	 * @return Imaginaerteil
	 */
	public double getImagin() {
		return this.imagin;
	}
	/**
	 * Methode zur Ausgabe einer komplexen Zahl als String im Format c = a + bi
	 */
	@Override
	public String toString() {
		String zeichen;
		// Rechenzeichen nur, wenn noetig
		double a = getReal(), b = 0.;
		if(this.getImagin() < 0.0) {
			b = -getImagin();
			zeichen = " - ";
		}
		else {
			b = getImagin();
			zeichen = " + ";
		}

		// Nur bestimmtes Zahlenformat
		DecimalFormat f = new DecimalFormat("#0.000000000");

		return f.format(a) +zeichen +f.format(b) +"i";
	}
}

