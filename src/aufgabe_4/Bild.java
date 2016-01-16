package aufgabe_4;
import java.awt.*;
import javax.swing.*;
/**
 * JPanel fuer Darstellung einer Mandelbrotmenge
 * @author Jens Awisus
 */
@SuppressWarnings("serial")
class Bild extends JPanel {
	/**
	 * Konstante Werte fuer einen gelben Farbverlauf
	 */
	public static final float[] FARBE_GELB  = {0.94f, 0.06f, 0.94f, 0.0f, 0.06f, 0.0f};
	/**
	 * Konstante Werte fuer einen roten Farbverlauf
	 */
	public static final float[] FARBE_ROT = {0.94f, 0.06f, 0.25f, 0.0f, 0.0f, 0.0f};
	/**
	 * Konstante Werte fuer einen gruenen Farbverlauf
	 */
	public static final float[] FARBE_GRUEN = {0.0f, 0.0f, 0.94f, 0.06f, 0.10f, 0.0f};
	/**
	 * Konstante Werte fuer einen blauen Farbverlauf
	 */
	public static final float[] FARBE_BLAU  = {0.0f, 0.0f, 0.6f, 0.0f, 0.94f, 0.06f};
	/**
	 * Konstante Werte fuer einen violetten Farbverlauf
	 */
	public static final float[] FARBE_VIOLETT = {0.6f, 0.06f, 0.4f, 0.0f, 0.94f, 0.06f};
	/**
	 * Konstante Werte fuer einen orangen Farbverlauf
	 */
	public static final float[] FARBE_ORANGE = {0.88f, 0.0f, 0.3f, 0.05f, 0.1f, 0.12f};
	/**
	 * Konstante Werte fuer einen gueldenen Farbverlauf
	 */
	public static final float[] FARBE_GOLD = {1.0f, 0.0f, 1.0f, 0.0f, 0.4f, 0.08f};
	/**
	 * Konstante Werte fuer einen grauen Farbverlauf
	 */
	public static final float[] FARBE_GRAU = {0.98f, 0.02f, 0.98f, 0.02f, 0.98f, 0.02f};
	/**
	 * aktuelle Farbverlaufskonstanten
	 */
	private float[] farbeAktuell;
	/**
	 * Farbe der Darstellung in abhaengigkeit zu farbeAktuell
	 */
	private Color farbe;
	/**
	 * Speichern, ob normales oder inverses Farbschema benutzt wird
	 */
	private boolean invertiert;
	/**
	 * Instanz einer Mandelbrotmenge
	 */
	private Mandelbrot model;
	/**
	 * Konstruktor, fuer bevorzugte Groesse des Darstellungsbereiches. Initiiert
	 * model und setzte die aktuelle Farbe auf Violett
	 * @param model Instanz einer Mandelbrotmenge
	 */
	public Bild(Mandelbrot model) {
		this.setModel(model);
		setPreferredSize(new Dimension(this.model.getBreite(), this.model.getHoehe()));

		this.setFarbe(FARBE_VIOLETT);
	}
	/**
	 * Setzen der Farben der Pixel des Darstellungsbereiches. Weiss, wenn in
	 * Mandelbrotmenge, bunt sonst.
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		// Breite ablaufen
		for(int r = 0; r < this.model.getBreite(); r++) {

			// Hoehe ablaufen
			for(int s = 0; s < this.model.getHoehe(); s++) {

				// Punkte in der Menge werden weiss
				g.setColor(Color.BLACK);

				// Alles andere wird bunt
				if(this.model.getPunkt(r, s) != this.model.getIterationen()) {
					// Farbe berechnen
					this.setColor((float)this.model.getPunkt(r, s) / (float)(this.model.getIterationen() - 1));

					g.setColor(this.farbe);
				}

				// Jetz Punkt malen
				g.drawLine(r, s, r, s);
			}
		}
	}
	/**
	 * Setter fuer die Einstellung der Farbwerte der Darstellung
	 * @param farbWerte Konstante mit Farbwerten (z.B. Bild.FARBE_GRUEN)
	 */
	public void setFarbe(float[] farbWerte) {
		this.farbeAktuell = farbWerte;
		this.repaint();
	}
	/**
	 * Hilfmethode zum erzeugen des Farbverlaufes in Abhaengigkeit zur
	 * Iterationstiefe der Bildpunkte
	 * @param faktor Anhaengiger Anstiegsaktor
	 */
	private void setColor(float faktor) {
		// Farbe berechnen
		if(this.invertiert) {
			this.farbe = new Color(
				(1 - this.farbeAktuell[0] * faktor),
				(1 - this.farbeAktuell[2] * faktor),
				(1 - this.farbeAktuell[4] * faktor));
		}
		else {
			this.farbe = new Color(
				(this.farbeAktuell[0] * faktor + this.farbeAktuell[1]),
				(this.farbeAktuell[2] * faktor + this.farbeAktuell[3]),
				(this.farbeAktuell[4] * faktor + this.farbeAktuell[5]));
		}
	}
	/**
	 * Methode um aktuellen Farbverlauf in seinen RGB-Werten zu invertieren
	 */
	public void invertieren() {
		if(this.invertiert) {
			this.invertiert = false;
		}
		else {
			this.invertiert = true;
		}
		this.repaint();
	}
	/**
	 * Setter fuer Referenz auf aktuell relevantes Model. Loest repaint aus
	 * @param model zu setzendes Model
	 */
	void setModel(Mandelbrot model) {
		this.model = model;
		this.repaint();
	}
	/**
	 * Loesen von model
	 */
	public void release() {
		this.farbe = null;
		this.model = null;
	}
}
