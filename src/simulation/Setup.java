package simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kenngroessen.Kenngroesse;
import kenngroessen.KenngroesseTyp;

public class Setup {
	public static String einflussfaktorenPath;

	public static List<Kenngroesse> getAllKenngroessen() {
		List<Kenngroesse> kenngroessen = new ArrayList<>();
		Map<String, Map<Integer, Integer>> einflussfaktoren = getEinflussfaktoren(einflussfaktorenPath);

		// Objekte erstellen
		// Bevölkerungsgröße
		Kenngroesse bevoelkerungsgroesse = new Kenngroesse(KenngroesseTyp.Bevoelkerungsgroesse, new ArrayList<>(), 1,
				50, einflussfaktoren, 10);
		// Bevölkerungswachstum
		Kenngroesse bevoelkerungswachstum = new Kenngroesse(KenngroesseTyp.Bevoelkerungswachstum, new ArrayList<>(), 1,
				30, einflussfaktoren, 1);
		// Bevölkerungswachstumsfaktor
		Kenngroesse bevoelkerungswachstumsfaktor = new Kenngroesse(KenngroesseTyp.Bevoelkerungswachstumsfaktor,
				new ArrayList<>(), 1, 3, einflussfaktoren, 1);
		// Wirtschaftsleistung
		Kenngroesse wirtschaftsleistung = new Kenngroesse(KenngroesseTyp.Wirtschaftsleistung, new ArrayList<>(), 1, 30,
				einflussfaktoren, 10);
		// Modernisierungsgrad
		Kenngroesse modernisierungsgrad = new Kenngroesse(KenngroesseTyp.Modernisierungsgrad, new ArrayList<>(), 1, 30,
				einflussfaktoren, 10);
		// Versorgungslage
		Kenngroesse versorgungslage = new Kenngroesse(KenngroesseTyp.Versorgungslage, new ArrayList<>(), -4, 1,
				einflussfaktoren, 0);
		// Politische Stabilität
		Kenngroesse politischeStabilitaet = new Kenngroesse(KenngroesseTyp.PolitischeStabilitaet, new ArrayList<>(),
				-10, 50, einflussfaktoren, 10);
		// Umweltverschmutzung
		Kenngroesse umweltverschmutzung = new Kenngroesse(KenngroesseTyp.Umweltverschmutzung, new ArrayList<>(), 1, 30,
				einflussfaktoren, 10);
		// Lebensqualität
		Kenngroesse lebensqualitaet = new Kenngroesse(KenngroesseTyp.Lebensqualitaet, new ArrayList<>(), 1, 30,
				einflussfaktoren, 10);
		// Bildung
		Kenngroesse bildung = new Kenngroesse(KenngroesseTyp.Bildung, new ArrayList<>(), 1, 30, einflussfaktoren, 10);
		// Staatsvermögen
		Kenngroesse staatsvermoegen = new Kenngroesse(KenngroesseTyp.Staatsvermoegen, new ArrayList<>(), 0,
				Integer.MAX_VALUE, einflussfaktoren, 10);

		// Abhängige Kenngrößen setzen
		// Bevölkerungsgröße
		bevoelkerungsgroesse.getKenngroessenMitEinfluss().add(bevoelkerungswachstum);
		bevoelkerungsgroesse.getKenngroessenMitEinfluss().add(bevoelkerungswachstumsfaktor);

		// Bevölkerungswachstum
		bevoelkerungswachstum.getKenngroessenMitEinfluss().add(bildung);
		bevoelkerungswachstum.getKenngroessenMitEinfluss().add(lebensqualitaet);

		// Bevölkerungswachstumsfaktor
		bevoelkerungswachstumsfaktor.getKenngroessenMitEinfluss().add(bevoelkerungsgroesse);

		// Wirtschaftsleistung
		wirtschaftsleistung.getKenngroessenMitEinfluss().add(wirtschaftsleistung);

		// Modernisierungsgrad
		modernisierungsgrad.getKenngroessenMitEinfluss().add(modernisierungsgrad);

		// Versorgungslage
		versorgungslage.getKenngroessenMitEinfluss().add(wirtschaftsleistung);

		// Politische Stabilität
		politischeStabilitaet.getKenngroessenMitEinfluss().add(lebensqualitaet);

		// Umweltverschmutzung
		umweltverschmutzung.getKenngroessenMitEinfluss().add(wirtschaftsleistung);
		umweltverschmutzung.getKenngroessenMitEinfluss().add(modernisierungsgrad);
		umweltverschmutzung.getKenngroessenMitEinfluss().add(umweltverschmutzung);

		// Lebensqualität
		lebensqualitaet.getKenngroessenMitEinfluss().add(umweltverschmutzung);
		lebensqualitaet.getKenngroessenMitEinfluss().add(bildung);
		lebensqualitaet.getKenngroessenMitEinfluss().add(bevoelkerungsgroesse);
		lebensqualitaet.getKenngroessenMitEinfluss().add(lebensqualitaet);

		// Bildung
		bildung.getKenngroessenMitEinfluss().add(bildung);

		// Staatsvermögen
		staatsvermoegen.getKenngroessenMitEinfluss().add(bevoelkerungsgroesse);
		staatsvermoegen.getKenngroessenMitEinfluss().add(wirtschaftsleistung);
		staatsvermoegen.getKenngroessenMitEinfluss().add(versorgungslage);
		staatsvermoegen.getKenngroessenMitEinfluss().add(politischeStabilitaet);
		staatsvermoegen.getKenngroessenMitEinfluss().add(lebensqualitaet);

		// KENNGROESSEN HINZUFUEGEN
		// Bevölkerungsgröße
		kenngroessen.add(bevoelkerungsgroesse);
		// Bevölkerungswachstum
		kenngroessen.add(bevoelkerungswachstum);
		// Bevölkerungswachstumsfaktor
		kenngroessen.add(bevoelkerungswachstumsfaktor);
		// Wirtschaftsleistung
		kenngroessen.add(wirtschaftsleistung);
		// Modernisierungsgrad
		kenngroessen.add(modernisierungsgrad);
		// Versorgungslage
		kenngroessen.add(versorgungslage);
		// Politische Stabilität
		kenngroessen.add(politischeStabilitaet);
		// Umweltverschmutzung
		kenngroessen.add(umweltverschmutzung);
		// Lebensqualität
		kenngroessen.add(lebensqualitaet);
		// Bildung
		kenngroessen.add(bildung);
		// Staatsvermögen
		kenngroessen.add(staatsvermoegen);

		return kenngroessen;
	}

	/**
	 * Liest die .csv Datei aus dem übergebenen Path ein und generiert daraus die
	 * Einflusswerte und gibt diese anschließend zurück.
	 * 
	 * @param path
	 * @return
	 */
	private static Map<String, Map<Integer, Integer>> getEinflussfaktoren(String path) {
		Map<String, Map<Integer, Integer>> einflussfaktoren = new HashMap<>();
		List<String[]> allLines = new ArrayList<>();
		// Daten in allLines einlesen
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = br.readLine()) != null) {
				allLines.add(line.split(";"));
			}
		} catch (IOException e) {
			System.out.println("Func '" + "" + "' failed: " + e);
		}

		// Daten in einflussfaktoren formatieren
		for (int j = 1; j < allLines.get(0).length; j++) {
			// Die Werte für den aktuellen Einflussfaktor setzen
			Map<Integer, Integer> werte = new HashMap<>();
			for (int i = 1; i < allLines.size(); i++) {
				if (allLines.get(i)[j] != null && !allLines.get(i)[j].equals("")) {
					int faktor = 0;
					if (allLines.get(i)[j].contains("x BWF")) {
						faktor = Integer.parseInt(allLines.get(i)[j].replaceAll("x BWF", "").replaceAll(" ", ""));
					} else if (allLines.get(i)[j].contains("x VL")) {
						faktor = Integer.parseInt(allLines.get(i)[j].replaceAll("x VL", "").replaceAll(" ", ""));
					} else {
						faktor = Integer.parseInt(allLines.get(i)[j].replaceAll(" ", ""));
					}

					werte.put(Integer.parseInt(allLines.get(i)[0]), faktor);
				}
			}
			einflussfaktoren.put(allLines.get(0)[j].replaceAll(" ", ""), werte);
		}

		return einflussfaktoren;
	}

	public static List<Zufallsereignis> getAllZufallsereignisse() {
		List<Zufallsereignis> zufallsereignisse = new ArrayList<>();

		// Zufallsereignisse der Liste hinzufügen
		zufallsereignisse
				.add(new Zufallsereignis("Bei einem Erdbeben kommen zahlreiche Menschen um. Bevölkerungsgröße -2", -2,
						KenngroesseTyp.Bevoelkerungsgroesse));

		zufallsereignisse
				.add(new Zufallsereignis("HSV steigt ab... Lebensqualitaet -2", -2, KenngroesseTyp.Lebensqualitaet));

		return zufallsereignisse;
	}
}
