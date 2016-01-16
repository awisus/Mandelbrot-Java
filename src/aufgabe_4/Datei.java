package aufgabe_4;
import java.io.*;
/**
 * Klasse, die Funktionen zum Einlesen aus und
 * Schreiben in Dateien anbietet
 * @author Jens Awisus
 */
@SuppressWarnings("serial")
public class Datei extends File {
	/**
	 * untere Bereichsgrenze
	 */
	private Complex ca;
	/**
	 * obere Bereichsgrenze
	 */
	private Complex ce;
	/**
	 * Iterationenanzahl
	 */
	private int iterationen;
	/**
	 * Name der Datei
	 */
	private String dateiname;
	/**
	 * Konstruktor, setzt Dateinamen, ca und ce und
	 * liesst ein oder schreibt danach
	 */
	public Datei(String dateiname, boolean richtung, Complex ca, Complex ce,
		int iterationen) {
		
		super(dateiname);

		this.dateiname = dateiname;		
		
		// Entweder die Daten einlesen ...
		if(richtung) {
			lesen();
		}
		// ... oder setzen und schreiben in Ausgabedatei
		else {
			this.ca = ca;
			this.ce = ce;
			this.iterationen = iterationen;
			schreiben();
		}
	}
	/**
	 * Den Dateitext Ausgeben
	 * @return Dateitext
	 */
	public Complex getCa() {
		return this.ca;
	}
	/**
	 * Den Dateitext Ausgeben
	 * @return Dateitext
	 */
	public Complex getCe() {
		return this.ce;
	}
	/**
	 * Den Dateitext Ausgeben
	 * @return Dateitext
	 */
	public int getIterationen() {
		return this.iterationen;
	}
	/**
	 * Methode zum lesen der relevanten Daten aus einer Datei.
	 */
	private void lesen() {
		// Exception abfangen
		try {
			// Buffered Filereader erzeugen
			FileReader fr = new FileReader(this.dateiname);
			BufferedReader br = new BufferedReader(fr);

			// Realteil und Imaginaerteil
			double re, im;

			// Lesen und komplexe Zahlen erzeugen
			re = Double.parseDouble(br.readLine());
			im = Double.parseDouble(br.readLine());
			this.ca = new Complex(re, im);
			re = Double.parseDouble(br.readLine());
			im = Double.parseDouble(br.readLine());
			this.ce = new Complex(re, im);

			// Iterationszahl lesen und speichern
			this.iterationen = Integer.parseInt(br.readLine());

			// Stream schliessen
			br.close();
		} catch(IOException ioe) {
			System.out.println("Fehler! Dateieinlesen nicht moeglich.");
		}
	}
	/**
	 * Methode zum schreiben von Daten in Ausgabedatei
	 */
	private void schreiben() {
		try {
			// Buffered Filereader erzeugen
			FileWriter fw = new FileWriter(this.dateiname);
			BufferedWriter bw = new BufferedWriter(fw);

			// Setzen der Zahlen
			setzen(bw, this.ca.getReal(), false);
			setzen(bw, this.ca.getImagin(), false);
			setzen(bw, this.ce.getReal(), false);
			setzen(bw, this.ce.getImagin(), false);
			setzen(bw, this.iterationen, true);

			// Stream schliessen
			bw.close();
		} catch(IOException ioe) {
			System.out.println("Fehler! Dateischreiben nicht moeglich.");
		}
	}
	/**
	 * Hilfmthode zum setzen eines Zeichens in Ausgabedatei
	 * @param bw BufferedWriter
	 * @param eingabe zu setzende Zahl in des Stream
	 * @param integer true, wenn Zahl Integer, false sonst
	 */
	private void setzen(BufferedWriter bw, double eingabe, boolean integer) {
		String temp;
		if(integer) {
			temp = String.valueOf((int)eingabe);
		}
		else {
			temp = String.valueOf(eingabe);
		}
		try {
			/* eingabe in temp. String setzen und dessen
			 * Zeichen in Stream schreiben
			 */
			for(int i = 0; i < temp.length(); i++) {
				bw.write(temp.charAt(i));
			}
			bw.newLine();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
