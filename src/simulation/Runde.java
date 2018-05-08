package simulation;

import java.util.List;
import java.util.Map;

import kenngroessen.KenngroesseTyp;

public class Runde {
	private int runde;
	private Map<KenngroesseTyp, Integer> wertDerKenngroesse;
	private List<Zufallsereignis> zufallsereignisse;
	
	public Runde(int runde, Map<KenngroesseTyp, Integer> wertDerKenngroesse, List<Zufallsereignis> zufallsereignisse) {
		super();
		this.runde = runde;
		this.wertDerKenngroesse = wertDerKenngroesse;
		this.zufallsereignisse = zufallsereignisse;
	}
	
	public String getInfos() {
		String infos = "";
		
		infos += "Runde: " + runde + "\nWerte:\n";
		
		for(KenngroesseTyp kt : wertDerKenngroesse.keySet()) {
			infos += kt.toString() + ": "+ wertDerKenngroesse.get(kt) + "\n";
		}
		
		infos += "Zufällige Ereignisse:\n";
		
		for(Zufallsereignis z : zufallsereignisse) {
			infos += z.getBeschreibung() + "\n";
		}
		
		return infos;
	}
	
	public int getRunde() {
		return runde;
	}
	public void setRunde(int runde) {
		this.runde = runde;
	}
	public Map<KenngroesseTyp, Integer> getWertDerKenngroesse() {
		return wertDerKenngroesse;
	}
	public void setWertDerKenngroesse(Map<KenngroesseTyp, Integer> wertDerKenngroesse) {
		this.wertDerKenngroesse = wertDerKenngroesse;
	}
	public List<Zufallsereignis> getZufallsereignisse() {
		return zufallsereignisse;
	}
	public void setZufallsereignisse(List<Zufallsereignis> zufallsereignisse) {
		this.zufallsereignisse = zufallsereignisse;
	}
}
