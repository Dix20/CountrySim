package simulation;

import java.util.List;
import java.util.Map;

import kenngroessen.KenngroesseTyp;

/**
 * Diese Klasse repräsentiert eine bestimmte 
 * Runde einer Simulation.
 * 
 * @author Flo
 * @author Fynn
 * @author Jan
 */
public class Runde {
	private int runde;
	private Map<KenngroesseTyp, Integer> wertDerKenngroesse;
	private List<Zufallsereignis> zufallsereignisse;
	private Simulation simulation;

	public Runde(int runde, Map<KenngroesseTyp, Integer> wertDerKenngroesse, List<Zufallsereignis> zufallsereignisse,
			Simulation simulation) {
		this.runde = runde;
		this.wertDerKenngroesse = wertDerKenngroesse;
		this.zufallsereignisse = zufallsereignisse;
		this.simulation = simulation;
	}

	public String getInfos() {
		String infos = "";

		infos += "Runde: " + runde + "\nWerte:\n";

		for (KenngroesseTyp kt : wertDerKenngroesse.keySet()) {
			infos += kt.toString() + ": " + wertDerKenngroesse.get(kt) + "\n";
		}

		infos += "Zufällige Ereignisse:\n";

		for (Zufallsereignis z : zufallsereignisse) {
			infos += z.getBeschreibung() + "\n";
		}

		return infos;
	}

	// Getter
	public int getRunde() {
		return runde;
	}

	public Map<KenngroesseTyp, Integer> getWertDerKenngroesse() {
		return wertDerKenngroesse;
	}

	public List<Zufallsereignis> getZufallsereignisse() {
		return zufallsereignisse;
	}

	public Simulation getSimulation() {
		return simulation;
	}
}
