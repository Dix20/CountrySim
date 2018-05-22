package simulation;

import kenngroessen.KenngroesseTyp;

/**
 * Diese Klasse repräsentiert ein Zufallsereignis.
 * 
 * @author Flo
 * @author Fynn
 * @author Jan
 */
public class Zufallsereignis {
	private String beschreibung;
	private Integer aenderungsWert;
	private KenngroesseTyp kenngroesseTyp;

	public Zufallsereignis(String beschreibung, Integer aenderungsWert, KenngroesseTyp kenngroesseTyp) {
		this.beschreibung = beschreibung;
		this.aenderungsWert = aenderungsWert;
		this.kenngroesseTyp = kenngroesseTyp;
	}

	// Getter
	public String getBeschreibung() {
		return beschreibung;
	}

	public Integer getAenderungsWert() {
		return aenderungsWert;
	}

	public KenngroesseTyp getKenngroesseTyp() {
		return kenngroesseTyp;
	}
}
