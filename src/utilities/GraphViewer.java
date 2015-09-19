/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.VerticalLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import project.Project;

public class GraphViewer extends JInternalFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	//private static GpsWindow frame;
	
	private JPanel option;
	private JPanel paneGraphe;
	private XYSeriesCollection dataset = null;
	private ChartPanel graphe;
	private XYSeries series = null;
	private JButton parcourir;
	private JScrollPane scpane;
	private JPanel sensorsList;
	private LinkedList<JCheckBox> sensors;
	private LinkedList<JCheckBox> sensors1;
	private Db db;
	
	public GraphViewer() {
	
		super("Graph Viewer");
		setRootPaneCheckingEnabled(false);
		
		this.setLocation(0, 0);
		this.setVisible(false);
		dataset = new XYSeriesCollection();
		this.db = new Db();
		this.sensors = new LinkedList<JCheckBox>();
		this.sensors1 = new LinkedList<JCheckBox>();

		getContentPane().setLayout(new BorderLayout(0, 0));
		
		this.setName("Graph Viewer");
		setIconifiable(true);
		setClosable(true);
		setBounds(6, 95, 1000, 600);
		
		
		option = new JPanel();
		option.setBackground(new Color(240, 240, 255));
		option.setPreferredSize(new Dimension(200, 500));
		option.setLayout(new BorderLayout(10, 10));
		getContentPane().add(option, BorderLayout.WEST);

		parcourir = new JButton("Add Sensor");
		parcourir.setAlignmentX(Component.CENTER_ALIGNMENT);
		parcourir.addActionListener(this);
		option.add(parcourir, BorderLayout.NORTH);

		this.sensorsList = new JPanel();
		sensorsList.setLayout(new VerticalLayout());
		sensorsList.setBackground(Color.WHITE);

		scpane = new JScrollPane(sensorsList);
		scpane.setBorder(BorderFactory.createLineBorder(Color.black));
		scpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		option.add(scpane, BorderLayout.CENTER);

		paneGraphe = new JPanel();
		paneGraphe.setLayout(new BorderLayout(0, 0));
		graphe = graphe();
		paneGraphe.add(graphe, BorderLayout.CENTER);
		getContentPane().add(paneGraphe);
		
		
	}
	
	public void draw() {
		loadFile();
	}
	
	public void addSensor(String fichier) {

		// ajouter le graphe

		ajouterFichier(fichier);
		// this.paneGraphe.add(graphe());
		graphe.revalidate();

		db.add(this.getSensorName(fichier), fichier);

		// ajouter un element a la list
		JCheckBox chckbxNewCheckBox = new JCheckBox(this.getSensorName(fichier));
		this.sensors.add(chckbxNewCheckBox);
		this.sensors1.add(chckbxNewCheckBox);
		chckbxNewCheckBox.setBackground(new Color(200, 200, 255));
		chckbxNewCheckBox.setBorder(new LineBorder(Color.BLACK));
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.addActionListener(this);
		JLabel l = new JLabel(" ");
		l.setPreferredSize(new Dimension(50, 5));
		this.sensorsList.add(l);
		this.sensorsList.add(chckbxNewCheckBox);
		this.sensorsList.validate();

	}
	
	private ChartPanel graphe() {

		JFreeChart graph = ChartFactory.createXYLineChart("Sensors Energy",
				"Time", "Energy", dataset, PlotOrientation.VERTICAL, true,
				true, false);

		ChartPanel cPanel = new ChartPanel(graph);
		cPanel.setBackground(Color.blue);
		cPanel.setPreferredSize(new Dimension(800, 600));

		XYPlot plot = graph.getXYPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);

		return cPanel;
	}

	private void ajouterFichier(String fichier) {

		series = new XYSeries(this.getSensorName(fichier));
		BufferedReader entree = null;
		try {
			entree = new BufferedReader(new FileReader(fichier));
			String s;
			String[] tab;
			double x, y;
			try {
				s = entree.readLine();
				s = entree.readLine();
				s = entree.readLine();
				s = entree.readLine();
				while (s != null) {
					tab = s.split(" ");
					x = Double.valueOf(tab[0]);
					y = Double.valueOf(tab[3]);
					series.add(x, y);
					s = entree.readLine();
				}
				entree.close();
				this.dataset.addSeries(series);
			} catch (IOException e) {
				//
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			System.err.println("Erreur dans la lecture de fichier");
		}

	}

	public String getSensorName(String fichier) {
		BufferedReader entree = null;
		try {
			entree = new BufferedReader(new FileReader(fichier));
			String s;
			String[] tab;
			try {
				s = entree.readLine();
				s = entree.readLine();
				entree.close();
				tab = s.split(" ");
				return tab[2];

			} catch (IOException e) {
				//
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			System.err.println("Erreur dans la lecture de fichier");
		}
		return null;

	}

	private void loadFile() {
		System.out.println(Project.getProjectResultsPath());
		String spth = Project.getProjectResultsPath();
		File file = new File(spth);
		String[] list = file.list();
		for (int i = 0; i < list.length; i++) {
			if (list[i].charAt(0) != '.')
				addSensor(spth+File.separator+list[i]);
		}

	}

	public void actionPerformed(ActionEvent e) {
		// Evenement button parcourir
		if (e.getSource() == this.parcourir) {

			JFileChooser fileopen = new JFileChooser();
			FiltreSimple filter = new FiltreSimple("Fichier res", "res");
			fileopen.addChoosableFileFilter(filter);
			int ret = fileopen.showDialog(null, "Open file");
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fileopen.getSelectedFile();
				System.out.println(file.getPath());
				this.addSensor(file.getPath());

			}

		} else {

			JCheckBox check;
			for (int i = 0; i < this.sensors.size(); i++) {
				check = sensors.get(i);
				if (e.getSource() == check) {
					if (check.isSelected()) {
						if (!this.isExist(check)) {
							String s = check.getText();
							s = s.substring(1);
							ajouterFichier(this.db.getPath(check.getText()));
							sensors1.addLast(check);
							this.graphe.revalidate();
						}

					} else {
						this.removeSerie(check.getText());
						sensors1.remove(check);
						this.graphe.revalidate();

					}
					break;
				}
			}

		}

	}

	private void removeSerie(String serie) {
		System.out.println(serie + " " + this.getSeriesNum(serie));
		dataset.removeSeries(this.getSeriesNum(serie));
	}

	private int getSeriesNum(String series) {
		String s;

		for (int i = 0; i < sensors1.size(); i++) {
			s = sensors1.get(i).getText();
			if (series.compareTo(s) == 0) {
				return i;
			}
		}

		return -1;
	}

	private boolean isExist(JCheckBox check) {

		for (int i = 0; i < this.sensors1.size(); i++) {
			if (check == sensors1.get(i))
				return true;
		}

		return false;

	}

}
