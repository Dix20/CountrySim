package simulation;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kenngroessen.Kenngroesse;
import kenngroessen.KenngroesseTyp;

public class Simulation {
	private int rundenAnzahl;
	private boolean mitZufallsereignissen;
	private Map<KenngroesseTyp, Kenngroesse> kenngroessen;
	private Random random = new Random();
	private List<Runde> runden = new ArrayList<>();
	private List<Zufallsereignis> zufallsereignisse = new ArrayList<>();

	public Simulation(int rundenAnzahl, boolean mitZufallsereignissen) {
		this.rundenAnzahl = rundenAnzahl;
		this.mitZufallsereignissen = mitZufallsereignissen;

		// TODO FILE // KENNGRÖ?EN HINZUFÜGEN
		simulationsdateiEinlesen(null);

		// Zufallsereignisse hinzufügen:
		zufallsereignisse
				.add(new Zufallsereignis("Bei einem Erdbeben kommen zahlreiche Menschen um. Bevölkerungsgröße -2", -2,
						KenngroesseTyp.Bevoelkerungsgroesse));
	}

	/**
	 * Simuliert alle runden komplett. TODO
	 */
	public void simulieren() {

	}

	/**
	 * Erstellt eine neue Runde, mit den neuen übergebenen Daten. Überprüft
	 * zusäzlich, ob alle übergebenen Daten mit den Daten der letzten Runde
	 * übereinstimmen.
	 * 
	 * @param staatsvermoegen
	 * @param wirtschaftsleistung
	 * @param modernisierungsgrad
	 * @param lebensqualitaet
	 * @param bildung
	 */
	public void neueRunde(int staatsvermoegen, int wirtschaftsleistung, int modernisierungsgrad, int lebensqualitaet,
			int bildung) {
		// Überprüfen, ob die Übergebenen Daten korrekt sind.
		int verwendeteWerte = ((wirtschaftsleistung
				- kenngroessen.get(KenngroesseTyp.Wirtschaftsleistung).getAktuellerWert())
				+ (modernisierungsgrad - kenngroessen.get(KenngroesseTyp.Modernisierungsgrad).getAktuellerWert())
				+ (lebensqualitaet - kenngroessen.get(KenngroesseTyp.Lebensqualitaet).getAktuellerWert())
				+ (bildung - kenngroessen.get(KenngroesseTyp.Bildung).getAktuellerWert()));

		// Überprüfen, ob Zufallsereignisse letzte Runde Einfluss auf die
		// Kenngrößen hatte
		for (Zufallsereignis z : runden.get(runden.size() - 1).getZufallsereignisse()) {
			if (z.getKenngroesseTyp().equals(KenngroesseTyp.Modernisierungsgrad)
					|| z.getKenngroesseTyp().equals(KenngroesseTyp.Lebensqualitaet)
					|| z.getKenngroesseTyp().equals(KenngroesseTyp.Bildung)
					|| z.getKenngroesseTyp().equals(KenngroesseTyp.Wirtschaftsleistung)) {
				// Dieses Zufallsereignis hatte Einfluss, auf eine der
				// Kenngrößen
				verwendeteWerte += z.getAenderungsWert();
			} else if (z.getKenngroesseTyp().equals(KenngroesseTyp.Staatsvermoegen)) {
				// Das Zufallsereignis hat das Staatsvermögen beeinflusst.
				verwendeteWerte -= z.getAenderungsWert();
			}
		}

		// Passen die neuen Werte zu den alten Werten?
		if ((kenngroessen.get(KenngroesseTyp.Staatsvermoegen).getAktuellerWert() - staatsvermoegen) >= 0
				&& staatsvermoegen == verwendeteWerte) {
			System.err.println("Die Gesamtzahl der neuen Werte, stimmt nicht mit der der alten Werte überein.");
			return;
		}

		// Wurde bedacht, dass einigen Kenngroeßen keine Werte
		// abgezogen werden dürfen?
		if (kenngroessen.get(KenngroesseTyp.Modernisierungsgrad).getAktuellerWert() > modernisierungsgrad
				|| kenngroessen.get(KenngroesseTyp.Lebensqualitaet).getAktuellerWert() > lebensqualitaet
				|| kenngroessen.get(KenngroesseTyp.Bildung).getAktuellerWert() > bildung) {
			System.err.println(
					"Es dürfen keine Werte bei Modernisierungsgrad, Lebensqualität und Bildung abgezogen werden.");
			return;
		}

		// Werte aktuallisieren
		kenngroessen.get(KenngroesseTyp.Staatsvermoegen).setAktuellerWert(staatsvermoegen);
		kenngroessen.get(KenngroesseTyp.Wirtschaftsleistung).setAktuellerWert(wirtschaftsleistung);
		kenngroessen.get(KenngroesseTyp.Modernisierungsgrad).setAktuellerWert(modernisierungsgrad);
		kenngroessen.get(KenngroesseTyp.Lebensqualitaet).setAktuellerWert(lebensqualitaet);
		kenngroessen.get(KenngroesseTyp.Bildung).setAktuellerWert(bildung);
		kenngroessenAktuallisieren();

		// Zufallsereignisse mit einspielen
		List<Zufallsereignis> verwendeteZufallsereignisse = new ArrayList<>();
		if (isMitZufallsereignissen()) {
			for (Zufallsereignis z : zufallsereignisse) {
				if (random.nextInt(zufallsereignisse.size()) == 0) {
					// Zufallsereignis anwenden
					kenngroessen.get(z.getKenngroesseTyp()).setAktuellerWert(
							kenngroessen.get(z.getKenngroesseTyp()).getAktuellerWert() + z.getAenderungsWert());
					verwendeteZufallsereignisse.add(z);
				}
			}
		}

		// Neue Runde hinzufügen
		runden.add(new Runde(runden.size(), getKenngroessenMitMenge(), verwendeteZufallsereignisse));
	}

	/**
	 * Liest die Simulationsdatei ein und setzt die erste Runde mit den
	 * entsprechenden Daten aus der Datei. // TODO
	 * 
	 * @param simulationsdatei
	 */
	private void simulationsdateiEinlesen(File simulationsdatei) {
		// Bevölkerungsgröße

		// Bevölkerungswachstum

		// Bevölkerungswachstumsfaktor

		// Wirtschaftsleistung

		// Modernisierungsgrad

		// Versorgungslage

		// Politische Stabilität

		// Umweltverschmutzung

		// Lebensqualität

		// Bildung

		// Staatsvermögen
		
		runden.add(new Runde(runden.size(), getKenngroessenMitMenge(), new ArrayList<>()));
	}

	/**
	 * Erstellt eine neue Simulationsdatei aufgrund der bisherigen Runden. // TODO
	 * 
	 * @param simulationsinfodateiPath
	 */
	public void simulationsinfodateiErstellen(Path simulationsinfodateiPath) {

	}

	/**
	 * Überprüft, ob die Simulation erfolgreich ist.
	 * 
	 * @return
	 */
	public boolean isSimulationErfolgreich() {
		return rundenAnzahl == runden.size();
	}

	/**
	 * Üerprüft, ob die bisherige Simulation fehlgeschlagen ist.
	 * 
	 * @return
	 */
	public boolean isSimulationFehlgeschlagen() {
		return kenngroessen.values().stream().anyMatch(k -> k.getAktuellerWert() > k.getWertebereichEnde()
				|| k.getAktuellerWert() < k.getWertebereichAnfang());
	}

	/**
	 * Aktuallsieirt alle in dieser Simulation gespeicherten Kenngroessen
	 */
	private void kenngroessenAktuallisieren() {
		// TODO Kenngroessen in der richtigen Reihenfolge aktuallisieren...
	}

	/**
	 * Aktuelle Werte der Kennzahlen sammeln.
	 * @return
	 */
	private Map<KenngroesseTyp, Integer> getKenngroessenMitMenge() {
		Map<KenngroesseTyp, Integer> wertDerKenngroesse = new HashMap<>();
		for (KenngroesseTyp kt : kenngroessen.keySet()) {
			wertDerKenngroesse.put(kt, kenngroessen.get(kt).getAktuellerWert());
		}
		return wertDerKenngroesse;
	}

	public int getRundenAnzahl() {
		return rundenAnzahl;
	}

	public void setRundenAnzahl(int rundenAnzahl) {
		this.rundenAnzahl = rundenAnzahl;
	}

	public boolean isMitZufallsereignissen() {
		return mitZufallsereignissen;
	}

	public void setMitZufallsereignissen(boolean mitZufallsereignissen) {
		this.mitZufallsereignissen = mitZufallsereignissen;
	}

	public Map<KenngroesseTyp, Kenngroesse> getKenngroessen() {
		return kenngroessen;
	}

	public void setKenngroessen(Map<KenngroesseTyp, Kenngroesse> kenngroessen) {
		this.kenngroessen = kenngroessen;
	}

	public List<Runde> getRunden() {
		return runden;
	}

	public void setRunden(List<Runde> runden) {
		this.runden = runden;
	}

	public List<Zufallsereignis> getZufallsereignisse() {
		return zufallsereignisse;
	}

	public void setZufallsereignisse(List<Zufallsereignis> zufallsereignisse) {
		this.zufallsereignisse = zufallsereignisse;
	}
}