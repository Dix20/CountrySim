package simulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kenngroessen.Kenngroesse;
import kenngroessen.KenngroesseTyp;

/**
 * Diese Klasse repräsentiert eine Simulation.
 * 
 * @author Flo
 * @author Fynn
 * @author Jan
 */
public class Simulation {
	private int rundenAnzahl;
	private boolean mitZufallsereignissen;
	private Map<KenngroesseTyp, Kenngroesse> kenngroessen = new HashMap<>();
	private Random random = new Random();
	private List<Runde> runden = new ArrayList<>();
	private List<Zufallsereignis> zufallsereignisse = new ArrayList<>();

	public Simulation(String simulationsdateiPath) {
		// Kenngrößen hinzufügen
		kenngroessen = Setup.getAllKenngroessen();

		simulationsdateiEinlesen(simulationsdateiPath);

		// Zufallsereignisse hinzufügen:
		zufallsereignisse = Setup.getAllZufallsereignisse();
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
		// Überprüfen, ob die Simulation schon beendet wurde
		if (isSimulationFehlgeschlagen() || isSimulationErfolgreich()) {
			System.err.println("Die Simulation ist bereits beendet. Es kann keine neue Runde simuliert werden.");
			return;
		}
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
		runden.add(new Runde(runden.size(), getKenngroessenMitMenge(), verwendeteZufallsereignisse, this));
	}

	/**
	 * Liest die Simulationsdatei ein und setzt die erste Runde mit den
	 * entsprechenden Daten aus der Datei. // TODO
	 * 
	 * @param simulationsdateiPath
	 */
	private void simulationsdateiEinlesen(String simulationsdateiPath) {
		if (simulationsdateiPath.endsWith(".sim")) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(simulationsdateiPath),
						StandardCharsets.UTF_8.name()));// new
				String line;
				boolean ausgangslage = false;
				while ((line = br.readLine()) != null) {
					if (!line.equals("")) {
						String[] infos = line.split(" ");
						if (ausgangslage) {
							// Überprüfen, ob die Daten der Ausgagngslage fertig sind.
							if (line.equals("+++ Simulationsablauf +++")) {
								ausgangslage = false;
								continue;
							}

							// KenngroesseTyp und Startwert herausfinden
							String typ = infos[0];
							String wert = infos[2];
							if (infos.length == 4) {
								typ += infos[1];
								wert = infos[3];
							}

							// Wert für den KenngroessenTyp setzen
							kenngroessen.get(KenngroesseTyp.valueOf(typ)).setAktuellerWert(Integer.parseInt(wert));
						} else if (line.equals("+++ Ausgangslage +++")) {
							// Überprüfen, ob die Daten der Ausgangslage als nächstes kommen
							ausgangslage = true;
						} else {
							// Überprüfen, ob die Zeile die Rundenzahl oder Zufallsereignisse anzeigt
							// und ggfs. die Werte dafür setzen.
							if (infos[0].equals("Rundenzahl")) {
								rundenAnzahl = Integer.parseInt(infos[2]);
							} else if (infos[0].equals("Zufallsereignisse") && infos[2].equals("ja")) {
								mitZufallsereignissen = true;
							} else if (infos[0].equals("Zufallsereignisse") && infos[2].equals("ja")) {
								mitZufallsereignissen = false;
							}
						}
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			kenngroessen.get(KenngroesseTyp.Versorgungslage).aktuallisiereWert(1);
			kenngroessen.get(KenngroesseTyp.Bevoelkerungswachstumsfaktor).aktuallisiereWert(1);

			runden.add(new Runde(runden.size(), getKenngroessenMitMenge(), new ArrayList<>(), this));
		} else {
			System.err.println("Die Simulationsdatei muss die Endung '.sim' haben.");
		}
	}

	/**
	 * Erstellt eine neue Simulationsdatei aufgrund der bisherigen Runden. // TODO
	 * 
	 * @param simulationsinfodateiPath
	 */
	public void simulationsionsinfosErstellen(String simulationsionsdateiPath) {
		if (!simulationsionsdateiPath.endsWith(".res")) {
			simulationsionsdateiPath += ".res";
		}

		String text = "";

		// Überschrift, welche das Ergebnis der Simulation beschreibt.
		if (isSimulationErfolgreich()) {
			text += "Simulation war Erfolgreich\n";
		} else if (isSimulationFehlgeschlagen()) {
			text += "Simulation ist Fehlgeschlagen...\n";
		} else {
			text += "Simulation wurde noch nicht beendet.\n";
		}

		// Beschreibung jeder Runde
		for (Runde r : runden) {
			text += r.getInfos() + "\n";
		}

		// Datei erstellen und speichern
		BufferedWriter writer = null;
		try {
			// create a temporary file
			File logFile = new File(simulationsionsdateiPath);
			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Überprüft, ob die Simulation erfolgreich ist.
	 * 
	 * @return
	 */
	public boolean isSimulationErfolgreich() {
		return !isSimulationFehlgeschlagen() && rundenAnzahl < runden.size();
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
	 * 
	 * @return
	 */
	private void kenngroessenAktuallisieren() {
		kenngroessen.get(KenngroesseTyp.Wirtschaftsleistung).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Versorgungslage).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Modernisierungsgrad).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Bildung).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Umweltverschmutzung).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Lebensqualitaet).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Bevoelkerungswachstum).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Bevoelkerungswachstumsfaktor).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Bevoelkerungsgroesse)
				.aktuallisiereWert(kenngroessen.get(KenngroesseTyp.Bevoelkerungswachstumsfaktor).getAktuellerWert());
		kenngroessen.get(KenngroesseTyp.PolitischeStabilitaet).aktuallisiereWert(1);
		kenngroessen.get(KenngroesseTyp.Staatsvermoegen)
				.aktuallisiereWert(kenngroessen.get(KenngroesseTyp.Versorgungslage).getAktuellerWert());
	}

	/**
	 * Aktuelle Werte der Kennzahlen sammeln.
	 * 
	 * @return
	 */
	private Map<KenngroesseTyp, Integer> getKenngroessenMitMenge() {
		Map<KenngroesseTyp, Integer> wertDerKenngroesse = new HashMap<>();
		for (KenngroesseTyp kt : kenngroessen.keySet()) {
			wertDerKenngroesse.put(kt, kenngroessen.get(kt).getAktuellerWert());
		}
		return wertDerKenngroesse;
	}

	// Getter
	public int getRundenAnzahl() {
		return rundenAnzahl;
	}

	public boolean isMitZufallsereignissen() {
		return mitZufallsereignissen;
	}

	public Map<KenngroesseTyp, Kenngroesse> getKenngroessen() {
		return kenngroessen;
	}

	public List<Runde> getRunden() {
		return runden;
	}

	public List<Zufallsereignis> getZufallsereignisse() {
		return zufallsereignisse;
	}
}
