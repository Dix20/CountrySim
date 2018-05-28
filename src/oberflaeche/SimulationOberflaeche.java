package oberflaeche;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import kenngroessen.Kenngroesse;
import kenngroessen.KenngroesseTyp;
import simulation.Runde;
import simulation.Setup;
import simulation.Simulation;
import simulation.Zufallsereignis;

/**
 * Diese Klasse stellt die Oberfläche zur verfügung und bietet so eine
 * Schnittstelle zwischen dem Benutzer und der Simulation.
 * 
 * @author Flo
 * @author Fynn
 * @author Jan
 */
public class SimulationOberflaeche {
	private JFrame frame = new JFrame();
	private Simulation simulation;

	private JLabel staatsvermoegen;
	private JLabel wirtschaftsleistung;
	private JLabel modernisierungsgrad;
	private JLabel lebensqualitaet;
	private JLabel bildung;
	private JLabel runde;
	private JFileChooser fileChooser = new JFileChooser();

	/**
	 * Startet die Simulation, indem die Einflussfaktoren und Simulationsdatei
	 * eingelesen werden und die Daten der erste Runde gesetzt werden.
	 */
	public void start() {
		// Filechooser Einflussfaktoren
		JOptionPane.showMessageDialog(null, "Wählen Sie die Einflussfaktoren Datei aus.");

		fileChooser.setFileFilter(new FileNameExtensionFilter("CSV FILES", "csv"));
		fileChooser.showOpenDialog(null);
		String einflussfaktorenPath = fileChooser.getSelectedFile().getPath();
		Setup.einflussfaktorenPath = einflussfaktorenPath;

		// Filechooser Semulationsdatei
		JOptionPane.showMessageDialog(null, "Wählen Sie die Simulationsdatei aus.");
		fileChooser.setFileFilter(new FileNameExtensionFilter("SIM FILES", "sim"));
		fileChooser.showOpenDialog(null);
		String simulationsdateiPath = fileChooser.getSelectedFile().getPath();

		// Simulation erstellen
		simulation = new Simulation(simulationsdateiPath);

		// Frame erzeugen
		frame = new JFrame();
		frame.setSize(1700, 800);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		neueRundeSetzen();
		frame.setVisible(true);
	}

	/**
	 * Setzt die Daten der Aktuellen Runde aus der Simulation
	 */
	private void neueRundeSetzen() {
		// Alle Elemente löschen
		frame.getContentPane().removeAll();

		// Anzeige der aktuellen Runde
		runde = new JLabel("Runde: " + (simulation.getRunden().size()));
		runde.setBounds(5, 5, 200, 20);
		frame.add(runde);

		diagrammSetzen();

		aktuellenStandSetzen();

		anpassungSetzen();

		if (simulation.isSimulationErfolgreich()) {
			frame.repaint();
			// Simulation Erfolgreich
			JOptionPane.showMessageDialog(null,
					"Simulation Erfolgreich!\nSimulationserfolg: " + simulation.getSimulationserfolg());
			simulationsdateiErstellen();
			return;
		} else if (simulation.isSimulationFehlgeschlagen()) {
			// Simulation Fehlgeschlagen
			frame.repaint();
			JOptionPane.showMessageDialog(null, "Simulation fehlgeschlagen...");
			simulationsdateiErstellen();
			return;
		}

		naechsteRundeButton();

		// Frame aktuallisieren
		frame.repaint();
	}

	/**
	 * Setzt das Diagramm für die vergangenen Werte jeder bisherigen Runde.
	 */
	private void diagrammSetzen() {
		// Dataset speichert die einzelnen Series
		XYSeriesCollection dataset = new XYSeriesCollection();

		// Erstellt für jede runde Serie, welche alle bisherigen Runden beinhaltet.
		for (KenngroesseTyp k : simulation.getKenngroessen().keySet()) {
			XYSeries series = new XYSeries(k.toString());
			for (Runde r : simulation.getRunden()) {
				series.add(r.getRunde(), r.getWertDerKenngroesse().get(k));
			}
			dataset.addSeries(series);			
		}

		// Fügt die Daten einem Chart hinzu.
		JFreeChart chart = ChartFactory.createXYLineChart("Entwicklung Kenngrößen", "Runde", "Anzahl", dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(10, 150, 550, 500);

		// Fügt das Panel, in welchem das Chart gespeichert ist, dem Frame hinzu.
		frame.add(chartPanel);
	}

	/**
	 * Setzt die Daten der Aktuellen Werte.
	 */
	private void aktuellenStandSetzen() {
		// Überschriften setzen
		JLabel ueberschrift = new JLabel("Aktueller Stand:");
		ueberschrift.setBounds(600, 50, 250, 50);
		ueberschrift.setFont(new Font(ueberschrift.getFont().getFontName(), Font.BOLD, 25));
		Font font = ueberschrift.getFont();
		Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		ueberschrift.setFont(font.deriveFont(attributes));
		frame.add(ueberschrift);

		int hoehe = 100;

		for (Kenngroesse k : simulation.getKenngroessen().values()) {
			JLabel l = new JLabel(k.getKenngroesseTyp().toString() + ": " + k.getAktuellerWert());
			l.setBounds(600, hoehe, 400, 50);
			hoehe += 50;
			l.setFont(new Font(l.getFont().getFontName(), Font.PLAIN, 15));
			frame.add(l);
		}
	}

	/**
	 * Setzt die Elemente um die Werte der Staatsvermögen, Wirtschaftsleistung,
	 * Modernisierungsgrad, Lebensqualität und Bildung zu ändern.
	 */
	private void anpassungSetzen() {
		// Überschrift
		JLabel ueberschrift = new JLabel("Neue Werte:");
		ueberschrift.setBounds(950, 50, 250, 50);
		ueberschrift.setFont(new Font(ueberschrift.getFont().getFontName(), Font.BOLD, 25));
		Font font = ueberschrift.getFont();
		Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		ueberschrift.setFont(font.deriveFont(attributes));
		frame.add(ueberschrift);

		// Aufteilung neu anpassen
		// Staatsvermögen
		staatsvermoegen = new JLabel("Staatsvermögen: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Staatsvermoegen).getAktuellerWert());
		staatsvermoegen.setFont(new Font(staatsvermoegen.getFont().getFontName(), Font.PLAIN, 20));
		staatsvermoegen.setBounds(950, 100, 250, 50);
		// Wirtschaftsleistung
		wirtschaftsleistung = new JLabel("Wirtschaftsleistung: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Wirtschaftsleistung).getAktuellerWert());
		wirtschaftsleistung.setFont(new Font(wirtschaftsleistung.getFont().getFontName(), Font.PLAIN, 20));
		wirtschaftsleistung.setBounds(950, 200, 250, 50);
		JButton wirtschaftsleistungHinzufuegen = new JButton("+");
		wirtschaftsleistungHinzufuegen.setBounds(1225, 200, 50, 50);
		wirtschaftsleistungHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) > 0
						&& Integer.parseInt(wirtschaftsleistung.getText().split(" ")[1]) < simulation.getKenngroessen()
								.get(KenngroesseTyp.Wirtschaftsleistung).getWertebereichEnde()) {
					staatsvermoegen.setText(
							"Staatsvermögen: " + (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) - 1));
					wirtschaftsleistung.setText("Wirtschaftsleistung: "
							+ (Integer.parseInt(wirtschaftsleistung.getText().split(" ")[1]) + 1));
				}
			}
		});
		JButton wirtschaftsleistungEntfernen = new JButton("-");
		wirtschaftsleistungEntfernen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(wirtschaftsleistung.getText().split(" ")[1]) > simulation.getKenngroessen()
						.get(KenngroesseTyp.Wirtschaftsleistung).getWertebereichAnfang()) {
					staatsvermoegen.setText(
							"Staatsvermögen: " + (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) + 1));
					wirtschaftsleistung.setText("Wirtschaftsleistung: "
							+ (Integer.parseInt(wirtschaftsleistung.getText().split(" ")[1]) - 1));
				}
			}
		});
		wirtschaftsleistungEntfernen.setBounds(1275, 200, 50, 50);
		// Modernisierungsgrad
		modernisierungsgrad = new JLabel("Modernisierungsgrad: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Modernisierungsgrad).getAktuellerWert());
		modernisierungsgrad.setFont(new Font(modernisierungsgrad.getFont().getFontName(), Font.PLAIN, 20));
		modernisierungsgrad.setBounds(950, 300, 250, 50);
		JButton modernisierungsgradHinzufuegen = new JButton("+");
		modernisierungsgradHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) > 0
						&& Integer.parseInt(modernisierungsgrad.getText().split(" ")[1]) < simulation.getKenngroessen()
								.get(KenngroesseTyp.Modernisierungsgrad).getWertebereichEnde()) {
					staatsvermoegen.setText(
							"Staatsvermögen: " + (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) - 1));
					modernisierungsgrad.setText("Modernisierungsgrad: "
							+ (Integer.parseInt(modernisierungsgrad.getText().split(" ")[1]) + 1));
				}
			}
		});
		modernisierungsgradHinzufuegen.setBounds(1250, 300, 50, 50);
		// Lebensqualität
		lebensqualitaet = new JLabel("Lebensqualität: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Lebensqualitaet).getAktuellerWert());
		lebensqualitaet.setFont(new Font(lebensqualitaet.getFont().getFontName(), Font.PLAIN, 20));
		lebensqualitaet.setBounds(950, 400, 250, 50);
		JButton lebensqualitaetHinzufuegen = new JButton("+");
		lebensqualitaetHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) > 0
						&& Integer.parseInt(lebensqualitaet.getText().split(" ")[1]) < simulation.getKenngroessen()
								.get(KenngroesseTyp.Lebensqualitaet).getWertebereichEnde()) {
					staatsvermoegen.setText(
							"Staatsvermögen: " + (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) - 1));
					lebensqualitaet.setText(
							"Lebensqualitaet: " + (Integer.parseInt(lebensqualitaet.getText().split(" ")[1]) + 1));
				}
			}
		});
		lebensqualitaetHinzufuegen.setBounds(1250, 400, 50, 50);
		// Bildung
		bildung = new JLabel("Bildung: " + simulation.getKenngroessen().get(KenngroesseTyp.Bildung).getAktuellerWert());
		bildung.setFont(new Font(bildung.getFont().getFontName(), Font.PLAIN, 20));
		bildung.setBounds(950, 500, 250, 50);
		JButton bildungHinzufuegen = new JButton("+");
		bildungHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) > 0
						&& Integer.parseInt(bildung.getText().split(" ")[1]) < simulation.getKenngroessen()
								.get(KenngroesseTyp.Bildung).getWertebereichEnde()) {
					staatsvermoegen.setText(
							"Staatsvermögen: " + (Integer.parseInt(staatsvermoegen.getText().split(" ")[1]) - 1));
					bildung.setText("Bildung: " + (Integer.parseInt(bildung.getText().split(" ")[1]) + 1));
				}
			}
		});
		bildungHinzufuegen.setBounds(1250, 500, 50, 50);

		frame.add(staatsvermoegen);
		frame.add(wirtschaftsleistung);
		frame.add(wirtschaftsleistungHinzufuegen);
		frame.add(wirtschaftsleistungEntfernen);
		frame.add(modernisierungsgrad);
		frame.add(modernisierungsgradHinzufuegen);
		frame.add(lebensqualitaet);
		frame.add(lebensqualitaetHinzufuegen);
		frame.add(bildung);
		frame.add(bildungHinzufuegen);
	}

	/**
	 * Erstellt den 'nächste Runde' Button.
	 */
	private void naechsteRundeButton() {
		// nächste Runde Button
		JButton btn = new JButton("Nächste Runde");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulation.neueRunde(Integer.parseInt(staatsvermoegen.getText().split(" ")[1]),
						Integer.parseInt(wirtschaftsleistung.getText().split(" ")[1]),
						Integer.parseInt(modernisierungsgrad.getText().split(" ")[1]),
						Integer.parseInt(lebensqualitaet.getText().split(" ")[1]),
						Integer.parseInt(bildung.getText().split(" ")[1]));
				// Dialog mit den Zufallsereignissen anzeigen:
				if (simulation.isMitZufallsereignissen()) {
					String zufallsereignisseString = "Folgende Zufallsereignisse sind letzte Runde eingetreten:\n";
					for (Zufallsereignis z : simulation.getRunden().get(simulation.getRunden().size() - 1)
							.getZufallsereignisse()) {
						zufallsereignisseString += z.getBeschreibung() + "\n";
					}
					JOptionPane.showMessageDialog(null, zufallsereignisseString);
				}

				// Fenster akuallisieren
				neueRundeSetzen();
			}
		});
		btn.setBounds(1450, 300, 200, 100);
		frame.add(btn);
	}

	/**
	 * Startet eine Abfrage, ob eine Simulationsdatei erstellt werden soll und
	 * erstellt diese ggfs.
	 */
	private void simulationsdateiErstellen() {
		int dateiErstellen = JOptionPane.showOptionDialog(null,
				"Möchten Sie eine Ergebnisdatei von der Simulation erstellen?", "", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);

		if (dateiErstellen == 0) {
			// Die Datei soll erstellt werden
			fileChooser.setFileFilter(new FileNameExtensionFilter("RES FILES", "res"));
			fileChooser.showOpenDialog(null);
			simulation.simulationsionsinfosErstellen(fileChooser.getSelectedFile().getPath());
		}
	}

	/**
	 * Startet die Oberfläche
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SimulationOberflaeche sf = new SimulationOberflaeche();
		sf.start();
	}
}