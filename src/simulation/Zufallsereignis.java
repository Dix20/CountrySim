package simulation;

import kenngroessen.KenngroesseTyp;

public class Zufallsereignis {
	private String beschreibung;
	private Integer aenderungsWert;
	private KenngroesseTyp kenngroesseTyp;

	public Zufallsereignis(String beschreibung, Integer aenderungsWert, KenngroesseTyp kenngroesseTyp) {
		super();
		this.beschreibung = beschreibung;
		this.aenderungsWert = aenderungsWert;
		this.kenngroesseTyp = kenngroesseTyp;
	}
	
	/**
	 * Gibt alle Infos in einem String in entsprechender Form zurück.
	 * @return
	 */
	public String getInfos() {
		return null;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Integer getAenderungsWert() {
		return aenderungsWert;
	}

	public void setAenderungsWert(Integer aenderungsWert) {
		this.aenderungsWert = aenderungsWert;
	}

	public KenngroesseTyp getKenngroesseTyp() {
		return kenngroesseTyp;
	}

	public void setKenngroesseTyp(KenngroesseTyp kenngroesseTyp) {
		this.kenngroesseTyp = kenngroesseTyp;
	}
}
