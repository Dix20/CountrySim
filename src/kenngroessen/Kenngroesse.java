package kenngroessen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Repräsentiert eine beliebige Kenngröße.
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
	 * Aktuallisiert 'aktuellerWert' aufgrund der kenngroessen mit Einfluss.
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
					System.out.println(k.getKenngroesseTyp().toString() + "auf" + this.kenngroesseTyp.toString() + ".. "
							+ k.getAktuellerWert());
					faktor = einflussfaktoren
							.get(k.getKenngroesseTyp().toString() + "auf" + this.kenngroesseTyp.toString())
							.get(k.getAktuellerWert());

					if (k.getKenngroesseTyp().equals(KenngroesseTyp.Bevoelkerungswachstumsfaktor)
							|| k.getKenngroesseTyp().equals(KenngroesseTyp.Versorgungslage)) {
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

	public void setKenngroesseTyp(KenngroesseTyp kenngroesseTyp) {
		this.kenngroesseTyp = kenngroesseTyp;
	}

	public List<Kenngroesse> getKenngroessenMitEinfluss() {
		return kenngroessenMitEinfluss;
	}

	public void setKenngroessenMitEinfluss(List<Kenngroesse> kenngroessenMitEinfluss) {
		this.kenngroessenMitEinfluss = kenngroessenMitEinfluss;
	}

	public int getWertebereichAnfang() {
		return wertebereichAnfang;
	}

	public void setWertebereichAnfang(int wertebereichAnfang) {
		this.wertebereichAnfang = wertebereichAnfang;
	}

	public int getWertebereichEnde() {
		return wertebereichEnde;
	}

	public void setWertebereichEnde(int wertebereichEnde) {
		this.wertebereichEnde = wertebereichEnde;
	}

	public int getAktuellerWert() {
		return aktuellerWert;
	}

	public void setAktuellerWert(int aktuellerWert) {
		this.aktuellerWert = aktuellerWert;
	}

	public Map<String, Map<Integer, Integer>> getEinflussfaktoren() {
		return einflussfaktoren;
	}

	public void setEinflussfaktoren(Map<String, Map<Integer, Integer>> einflussfaktoren) {
		this.einflussfaktoren = einflussfaktoren;
	}
}
