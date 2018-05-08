package kenngroessen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public void aktuallisiereWert() {
		for (Kenngroesse k : kenngroessenMitEinfluss) {
			aktuellerWert += einflussfaktoren.get(this.kenngroesseTyp.toString() + k.getKenngroesseTyp().toString())
					.get(k.getAktuellerWert());
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
