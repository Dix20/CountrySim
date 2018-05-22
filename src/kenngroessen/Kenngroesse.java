package kenngroessen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Diese Klasse repräsentiert eine beliebige Kenngröße.
 * 
 * @author Flo
 * @author Fynn
 * @author Jan
 */
public class Kenngroesse {
	private KenngroesseTyp kenngroesseTyp;
	private List<Kenngroesse> kenngroessenMitEinfluss = new ArrayList<>();
	private int wertebereichAnfang;
	private int wertebereichEnde;
	private int aktuellerWert;
	private Map<String, Map<Integer, Integer>> einflussfaktoren;

	public Kenngroesse(KenngroesseTyp kenngroesseTyp, List<Kenngroesse> kenngroessenMitEinfluss, int wertebereichAnfang,
			int wertebereichEnde, Map<String, Map<Integer, Integer>> einflussfaktoren, int aktuellerWert) {
		this.kenngroesseTyp = kenngroesseTyp;
		this.kenngroessenMitEinfluss = kenngroessenMitEinfluss;
		this.wertebereichAnfang = wertebereichAnfang;
		this.wertebereichEnde = wertebereichEnde;
		this.aktuellerWert = aktuellerWert;
		this.einflussfaktoren = einflussfaktoren;
	}

	/**
	 * Aktuallisiert 'aktuellerWert' aufgrund der kenngroessen mit Einfluss. Der
	 * Übergebene Wert bestimmt den multiplikationsfaktor, falls einer der beiden
	 * Einflussfaktoren 'Bevoelkerungswachstum auf Bevoelkerungsgroesse' oder
	 * 'Bevoelkerungsgroesse auf Staatsvermoegen' mit einfliesst.
	 * 
	 * @param multiplikatorFaktor
	 */
	public void aktuallisiereWert(int multiplikatorFaktor) {
		if (aktuellerWert > wertebereichEnde || aktuellerWert < wertebereichAnfang) {
			// Wenn der aktuelle Wert schon ausserhalbe des Wertebereiches ist, wird die
			// Aktuallisierung abgebrochen
			return;
		}

		if (this.kenngroesseTyp.equals(KenngroesseTyp.Bevoelkerungswachstumsfaktor)
				|| this.kenngroesseTyp.equals(KenngroesseTyp.Versorgungslage)) {
			// Setzt die Werte aus den Einflussfaktoren direkt.
			aktuellerWert = einflussfaktoren.get(kenngroessenMitEinfluss.get(0).getKenngroesseTyp().toString() + "auf"
					+ this.kenngroesseTyp.toString()).get(kenngroessenMitEinfluss.get(0).getAktuellerWert());
		} else {
			// Addiert die Werte der Einflussfaktoren aller einflussnehmender Kenngrößen
			for (Kenngroesse k : kenngroessenMitEinfluss) {
				if (aktuellerWert > wertebereichEnde || aktuellerWert < wertebereichAnfang) {
					// Wenn der aktuelle Wert schon ausserhalbe des Wertebereiches ist, wird die
					// Aktuallisierung abgebrochen
					return;
				}

				int faktor = 0;
				if (k.getKenngroesseTyp().equals(KenngroesseTyp.Bevoelkerungswachstumsfaktor)
						|| k.getKenngroesseTyp().equals(KenngroesseTyp.Versorgungslage)) {
					faktor = k.getAktuellerWert();
				} else {
					faktor = einflussfaktoren
							.get(k.getKenngroesseTyp().toString() + "auf" + this.kenngroesseTyp.toString())
							.get(k.getAktuellerWert());

					if ((k.getKenngroesseTyp().equals(KenngroesseTyp.Bevoelkerungswachstum)
							&& this.kenngroesseTyp.equals(KenngroesseTyp.Bevoelkerungsgroesse))
							|| k.getKenngroesseTyp().equals(KenngroesseTyp.Bevoelkerungsgroesse)
									&& this.kenngroesseTyp.equals(KenngroesseTyp.Staatsvermoegen)) {
						faktor *= multiplikatorFaktor;
					}
				}
				aktuellerWert += faktor;
			}
		}
	}

	// Getter und Setter
	public KenngroesseTyp getKenngroesseTyp() {
		return kenngroesseTyp;
	}

	public List<Kenngroesse> getKenngroessenMitEinfluss() {
		return kenngroessenMitEinfluss;
	}

	public int getWertebereichAnfang() {
		return wertebereichAnfang;
	}

	public int getWertebereichEnde() {
		return wertebereichEnde;
	}

	public int getAktuellerWert() {
		return aktuellerWert;
	}

	public void setAktuellerWert(int aktuellerWert) {
		this.aktuellerWert = aktuellerWert;
	}
}
