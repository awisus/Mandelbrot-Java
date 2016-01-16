package aufgabe_4;
import javax.swing.UIManager;
/**
 * Startklasse fuer MandelbrotView von der Konsole. Es wird zuerst eine
 * Mandelbrotmenge, durch Buchstaben als Pixel, ausgedruckt, bevor Angaben zur
 * Grafikerzeugung einer Mandelbrotmenge ausgegeben werden und im Anschluss eine
 * View mit ebendiesen Daten erzeugt wird
 * @author Jens Awisus
 */
public class Main {
	/**
	 * Hilfsmethode zum Ausdruck einer Mandelbrotmenge auf der Konsole
	 */
	private static void ausdruck() {
		int breite = 80;
		int hoehe = 32;
		int iter = 80;

		// Neues Mandelbrot
		Mandelbrot mandelbrot = new Mandelbrot(new Complex(-2, -1),
			new Complex(0.5, 1), iter, breite, hoehe);
		
		mandelbrot.berechnen();

		// Zeilen und Spalten ablaufen
		for(int s = 0; s < hoehe; s++) {

			// Hoehe ablaufen
			for(int r = 0; r < breite; r++) {

				// Buchstaben waehlen
				String zeichen = " ";

				if(mandelbrot.getPunkt(r, s) != iter) {
					int farbe = mandelbrot.getPunkt(r, s) % 10;
					switch(farbe) {
						case 0: zeichen = "m"; break;
						case 1: zeichen = "a"; break;
						case 2: zeichen = "n"; break;
						case 3: zeichen = "d"; break;
						case 4: zeichen = "e"; break;
						case 5: zeichen = "l"; break;
						case 6: zeichen = "b"; break;
						case 7: zeichen = "r"; break;
						case 8: zeichen = "o"; break;
						case 9: zeichen = "t"; break;
					}
				}

				// Jetz malen
				System.out.print(zeichen);
			}
			System.out.println();
		}
	}
	/**
	 * Startklasse, um View fuer Mandelbrotmenge zu erzeugen. Angabe von
	 * Darstellungsdaten C_a, C_e, I und Bildgroesse
	 * @param args Konsolenargumente
	 */
	public static void main(String[] args) {
		// Ausgabe einer Mandelbrotmenge in Form von Buchstaben
		ausdruck();

		System.out.println();

		// daten zur Grafikerzeugung
		Complex ca = new Complex(-2, -1);
		Complex ce = new Complex(0.5, 1);
		int iterationen = 80;
		int breite = 1000;
		int hoehe = 800;

		// Ausdruck dieser Daten
		System.out.println("Erzeuge Grafik.");
		System.out.println("Groesse: " +breite +"x" +hoehe);
		System.out.println();
		System.out.println("Iterationen: " +iterationen);
		System.out.println();
		System.out.println("C_a = " +ca.toString());
		System.out.println("C_e = " +ce.toString());

		// Aussehen an System anpassen
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		// View erzeugen
		new View(breite, hoehe, ca, ce, iterationen);
	}
}
