package oberflaeche;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import kenngroessen.Kenngroesse;
import kenngroessen.KenngroesseTyp;
import simulation.Setup;
import simulation.Simulation;
import simulation.Zufallsereignis;

public class SimulationsFenster {
	private JFrame frame = new JFrame();
	private Simulation simulation;

	private JLabel staatsvermoegen;
	private JLabel wirtschaftsleistung;
	private JLabel modernisierungsgrad;
	private JLabel lebensqualitaet;
	private JLabel bildung;
	private JLabel runde;
	private JFileChooser fileChooser = new JFileChooser();

	public SimulationsFenster() {
		// Fenster erstellen
	}

	public void start() {
		// Filechooser Einflussfaktoren
		fileChooser.showOpenDialog(null);
		String einflussfaktorenPath = fileChooser.getSelectedFile().getPath();
		Setup.einflussfaktorenPath = einflussfaktorenPath;

		// Filechooser Semulationsdatei
		fileChooser.showOpenDialog(null);
		String simulationsdateiPath = fileChooser.getSelectedFile().getPath();

		// Simulation erstellen
		simulation = new Simulation(simulationsdateiPath);

		neueRundeSetzen();
	}

	private void neueRundeSetzen() {
		// Alle Elemente löschen
		frame = new JFrame();
		frame.setSize(1500, 800);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().removeAll();

		// Anzeige der aktuellen Runde
		runde = new JLabel("Runde: " + (simulation.getRunden().size()));
		runde.setBounds(5, 5, 200, 20);
		frame.add(runde);

		aktuellenStandSetzen();

		anpassungSetzen();

		if (simulation.isSimulationErfolgreich()) {
			frame.setVisible(true);
			// Simulation Erfolgreich
			JOptionPane.showMessageDialog(null, "Simulation Erfolgreich!");
			simulationsdateiErstellen();
			return;
		} else if (simulation.isSimulationFehlgeschlagen()) {
			// Simulation Fehlgeschlagen
			frame.setVisible(true);
			JOptionPane.showMessageDialog(null, "Simulation fehlgeschlagen...");
			simulationsdateiErstellen();
			return;
		}

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
		btn.setBounds(1200, 300, 200, 100);
		frame.add(btn);

		// Frame anzeigen
		frame.setVisible(true);
	}

	private void aktuellenStandSetzen() {
		// Überschriften setzen
		JLabel ueberschrift = new JLabel("Aktueller Stand:");
		ueberschrift.setBounds(100, 50, 250, 50);
		ueberschrift.setFont(new Font(ueberschrift.getFont().getFontName(), Font.BOLD, 25));
		Font font = ueberschrift.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		ueberschrift.setFont(font.deriveFont(attributes));
		frame.add(ueberschrift);

		int hoehe = 100;

		for (Kenngroesse k : simulation.getKenngroessen().values()) {
			JLabel l = new JLabel(k.getKenngroesseTyp().toString() + ": " + k.getAktuellerWert());
			l.setBounds(100, hoehe, 400, 50);
			hoehe += 50;
			l.setFont(new Font(l.getFont().getFontName(), Font.PLAIN, 15));
			frame.add(l);
		}
	}

	private void anpassungSetzen() {
		// Überschrift
		JLabel ueberschrift = new JLabel("Neue Werte:");
		ueberschrift.setBounds(600, 50, 250, 50);
		ueberschrift.setFont(new Font(ueberschrift.getFont().getFontName(), Font.BOLD, 25));
		Font font = ueberschrift.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		ueberschrift.setFont(font.deriveFont(attributes));
		frame.add(ueberschrift);

		// Aufteilung neu anpassen
		// Staatsvermögen
		staatsvermoegen = new JLabel("Staatsvermögen: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Staatsvermoegen).getAktuellerWert());
		staatsvermoegen.setFont(new Font(staatsvermoegen.getFont().getFontName(), Font.PLAIN, 20));
		staatsvermoegen.setBounds(600, 100, 250, 50);
		// Wirtschaftsleistung
		wirtschaftsleistung = new JLabel("Wirtschaftsleistung: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Wirtschaftsleistung).getAktuellerWert());
		wirtschaftsleistung.setFont(new Font(wirtschaftsleistung.getFont().getFontName(), Font.PLAIN, 20));
		wirtschaftsleistung.setBounds(600, 200, 250, 50);
		JButton wirtschaftsleistungHinzufuegen = new JButton("+");
		wirtschaftsleistungHinzufuegen.setBounds(875, 200, 50, 50);
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
		wirtschaftsleistungEntfernen.setBounds(925, 200, 50, 50);
		// Modernisierungsgrad
		modernisierungsgrad = new JLabel("Modernisierungsgrad: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Modernisierungsgrad).getAktuellerWert());
		modernisierungsgrad.setFont(new Font(modernisierungsgrad.getFont().getFontName(), Font.PLAIN, 20));
		modernisierungsgrad.setBounds(600, 300, 250, 50);
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
		modernisierungsgradHinzufuegen.setBounds(900, 300, 50, 50);
		// Lebensqualität
		lebensqualitaet = new JLabel("Lebensqualität: "
				+ simulation.getKenngroessen().get(KenngroesseTyp.Lebensqualitaet).getAktuellerWert());
		lebensqualitaet.setFont(new Font(lebensqualitaet.getFont().getFontName(), Font.PLAIN, 20));
		lebensqualitaet.setBounds(600, 400, 250, 50);
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
		lebensqualitaetHinzufuegen.setBounds(900, 400, 50, 50);
		// Bildung
		bildung = new JLabel("Bildung: " + simulation.getKenngroessen().get(KenngroesseTyp.Bildung).getAktuellerWert());
		bildung.setFont(new Font(bildung.getFont().getFontName(), Font.PLAIN, 20));
		bildung.setBounds(600, 500, 250, 50);
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
		bildungHinzufuegen.setBounds(900, 500, 50, 50);

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

	private void simulationsdateiErstellen() {
		int dateiErstellen = JOptionPane.showOptionDialog(null,
				"Möchten Sie eine Ergebnisdatei von der Simulation erstellen?", "", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);

		if (dateiErstellen == 0) {
			// Die Datei soll erstellt werden
			fileChooser.showOpenDialog(null);
			simulation.simulationsionsdateiErstellen(fileChooser.getSelectedFile().getPath());
		} else if (dateiErstellen == 1) {
			// Die Datei soll nicht erstellt werden

		}
	}
}