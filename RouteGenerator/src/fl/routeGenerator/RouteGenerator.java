package fl.routeGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;

import Pachube.Data;
import Pachube.Feed;
import Pachube.Pachube;
import Pachube.PachubeException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import fl.routeGenerator.cosm.Eeml;
import fl.routeGenerator.cosm.Environment;

public class RouteGenerator extends JFrame {

	private static final long serialVersionUID = -2102475249183458076L;
	private static final String COSM_KEY = "5T64pgQVJiKlfgQU2Q9IvH_UyUKSAKxTNjZma1kyQnFsQT0g";

	public static void main(String[] args) {
		RouteGenerator.init();
	}

	private Set<Waypoint> route;
	private RoutePainter<JXMapViewer> routePainter;
	private CompoundPainter<WaypointPainter<JXMapViewer>> customPainter;
	private JXMapKit maps;
	private JTable routeTable;
	private JTextField routeName;
	private JButton save;
	private JButton clear;
	private List<Environment> cosmRoutes;
	private JComboBox<Environment> routeComboBox;
	private Environment selectedEnvironment;
	private JButton resetButton;
	private Pachube cosm;

	public RouteGenerator() {
		this.cosm = new Pachube(COSM_KEY);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		// Frame
		this.setTitle("Banana Route Generator");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new GridBagLayout());

		// Maps
		c.gridx = 0;
		c.gridy = 0;
		this.maps = new JXMapKit();
		this.maps.setDefaultProvider(DefaultProviders.OpenStreetMaps);
		this.maps.setDataProviderCreditShown(true);
		this.maps.setPreferredSize(new Dimension(600, 600));
		this.maps.setVisible(true);
		this.maps.setBorder(BorderFactory.createLineBorder(Color.black));
		this.maps.getMainMap().addMouseListener(new MapMouseListener(this));
		this.getContentPane().add(maps, c);
		this.customPainter = new CompoundPainter<WaypointPainter<JXMapViewer>>();
		this.routePainter = new RoutePainter<JXMapViewer>();
		this.customPainter.setPainters(routePainter);
		this.customPainter.setCacheable(false);
		this.maps.getMainMap().setOverlayPainter(customPainter);

		// Other UI
		buildOtherUI();

		// Load Data
		this.reloadRoutes();

		// Finally
		this.pack();
	}

	private void buildOtherUI() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(2, 2, 2, 2);

		// Route selection
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		JLabel routesLabel = new JLabel("Routes:");
		selectionPanel.add(routesLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		routeComboBox = new JComboBox<Environment>();
		selectionPanel.add(routeComboBox, c);

		c.gridx = 2;
		c.gridy = 0;
		JButton reloadButton = new JButton("Reload List");
		reloadButton.setName("reload");
		reloadButton.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(reloadButton, c);

		c.gridx = 3;
		c.gridy = 0;
		JButton loadButton = new JButton("Select");
		loadButton.setName("select");
		loadButton.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(loadButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 3;
		this.routeTable = new JTable();
		this.routeTable.setModel(this.generateTableModel(null));
		this.routeTable.setPreferredSize(new Dimension(300, 300));
		this.routeTable.addKeyListener(new TableKeyListener(this));
		JScrollPane jsp = new JScrollPane(this.routeTable);
		selectionPanel.add(jsp, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		JLabel routeNameLabel = new JLabel("Route name:");
		selectionPanel.add(routeNameLabel, c);

		c.gridx = 1;
		c.gridy = 4;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 2;
		this.routeName = new JTextField(20);
		selectionPanel.add(this.routeName, c);

		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 5;
		this.clear = new JButton("Cancel");
		this.clear.setName("clear");
		this.clear.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(this.clear, c);

		c.gridx = 2;
		c.gridy = 5;
		this.resetButton = new JButton("Reset");
		this.resetButton.setVisible(false);
		this.resetButton.setName("reset");
		this.resetButton.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(resetButton, c);

		c.gridx = 1;
		c.gridy = 6;
		this.save = new JButton("Update / Save");
		this.save.setName("save");
		this.save.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(this.save, c);

		c.gridx = 1;
		c.gridy = 8;
		JButton temperatureButton = new JButton("Save temperature");
		temperatureButton.setName("saveTemperature");
		temperatureButton.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(temperatureButton, c);

		c.gridx = 1;
		c.gridy = 0;
		this.getContentPane().add(selectionPanel, c);
	}

	private TableModel generateTableModel(String[][] data) {
		String[] cols = new String[] { "X", "Y", "Temperature" };

		DefaultTableModel dtm = new CustomTableModel();
		dtm.setDataVector(data, cols);

		return dtm;
	}

	public void reloadRoutes() {
		this.loadRoutesFromCOSM();
		this.fillRoutes();
	}

	private void fillRoutes() {
		this.routeComboBox.removeAllItems();

		Iterator<Environment> i = this.cosmRoutes.iterator();
		while (i.hasNext()) {
			this.routeComboBox.addItem(i.next());
		}
	}

	private Environment loadRouteFromCOSM(int id) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		return this.query("https://api.cosm.com/v2/feeds/" + id + ".xml",
				queryParams).get(0);
	}

	private void loadRoutesFromCOSM() {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("user", "futurelogistics");

		this.cosmRoutes = query(queryParams);
	}

	private List<Environment> query(MultivaluedMap<String, String> queryParams) {
		return this.query("http://api.cosm.com/v2/feeds.xml", queryParams);
	}

	private List<Environment> query(String url,
			MultivaluedMap<String, String> queryParams) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);

		String s = webResource.queryParams(queryParams)
				.header("X-ApiKey", COSM_KEY).get(String.class);

		try {
			JAXBContext context = JAXBContext.newInstance(Eeml.class);

			Eeml feeds = (Eeml) context.createUnmarshaller().unmarshal(
					new StringReader(s));

			return feeds.getEnvironment();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return null;
	}

	public JXMapKit getMaps() {
		return maps;
	}

	public static void init() {
		RouteGenerator ui = new RouteGenerator();
		ui.setVisible(true);
	}

	public void addPoint(GeoPosition position) {
		if (this.route == null)
			this.route = new LinkedHashSet<Waypoint>();

		this.route.add(new RoutePoint(this.route.size(), position));

		repaintMap();

		this.updateTable();
	}

	private void repaintMap() {
		this.routePainter.setWaypoints(this.route);
		this.maps.getMainMap().invalidate();
		this.maps.repaint();
	}

	private void updateTable() {
		String[][] values = new String[this.route.size()][3];

		Iterator<Waypoint> i = this.route.iterator();
		int j = 0;
		while (i.hasNext()) {
			RoutePoint current = (RoutePoint) i.next();

			values[j][0] = getCoordinateString(current).split(";")[0];
			values[j][1] = getCoordinateString(current).split(";")[1];

			j++;
		}

		this.routeTable.setModel(this.generateTableModel(values));
	}

	public void saveRoute() {
		if (!this.routeName.getText().equals("") && (this.route.size() >= 2)) {
			try {
				if (this.selectedEnvironment != null) {
					updateExistingFeed();
				} else {
					createFeedFromScratch();
				}

				this.reloadRoutes();
			} catch (PachubeException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateExistingFeed() {
		try {
			Feed routeFeed = this.cosm.getFeed(this.selectedEnvironment.getId()
					.intValue());
			Iterator<Data> i = routeFeed.getData().iterator();
			while (i.hasNext()) {
				Data current = i.next();
				routeFeed.deleteDatastream(current.getId());
			}

			convertRoutesToDatastreams(routeFeed);

		} catch (PachubeException e) {
		}
	}

	private void createFeedFromScratch() throws PachubeException {
		Feed routeFeed;
		routeFeed = new Feed();
		routeFeed.setTitle(this.routeName.getText());

		convertRoutesToDatastreams(routeFeed);

		this.cosm.createFeed(routeFeed);
	}

	private void convertRoutesToDatastreams(Feed routeFeed)
			throws PachubeException {
		Iterator<Waypoint> i = this.route.iterator();
		int j = 0;
		while (i.hasNext()) {
			RoutePoint current = (RoutePoint) i.next();

			Data routePoint = new Data();
			routePoint.setId(j);
			routePoint.setMinValue(-30d);
			routePoint.setMaxValue(-50d);
			routePoint.setTag(getCoordinateString(current));
			routeFeed.createDatastream(routePoint);
			j++;
		}
	}

	private String getCoordinateString(RoutePoint current) {
		return current.getPosition().getLatitude() + ";"
				+ current.getPosition().getLongitude();
	}

	public void clear() {
		this.route.clear();
		this.routeName.setText("");
		this.routeName.setEditable(true);
		this.resetButton.setVisible(false);
		this.routeComboBox.setEnabled(true);

		this.updateMapWithTree();

		this.selectedEnvironment = null;
	}

	private void updateMapWithTree() {
		this.updateTable();
		this.repaintMap();
	}

	public void deleteRoutePoint() {
		if (this.routeTable.getSelectedRow() >= 0) {
			String point = this.routeTable.getModel().getValueAt(
					this.routeTable.getSelectedRow(), 0)
					+ ";"
					+ this.routeTable.getModel().getValueAt(
							this.routeTable.getSelectedRow(), 1);

			Iterator<Waypoint> i = this.route.iterator();
			while (i.hasNext()) {
				RoutePoint current = (RoutePoint) i.next();

				if (getCoordinateString(current).equals(point))
					i.remove();
			}

			this.updateMapWithTree();
		}
	}

	public void selectRoute() {
		if (this.route == null)
			this.route = new LinkedHashSet<Waypoint>();
		else
			this.route.clear();

		this.routeComboBox.setEnabled(false);
		this.resetButton.setVisible(true);
		this.selectedEnvironment = (Environment) this.routeComboBox
				.getSelectedItem();
		this.routeName.setText(this.selectedEnvironment.getTitle());
		this.routeName.setEnabled(false);

		Iterator<fl.routeGenerator.cosm.Data> i = (this.selectedEnvironment)
				.getData().iterator();
		while (i.hasNext()) {
			fl.routeGenerator.cosm.Data current = i.next();

			double latitude = Double
					.parseDouble(current.getTag().split(";")[0]);
			double longitude = Double
					.parseDouble(current.getTag().split(";")[1]);

			this.route.add(new RoutePoint(this.route.size(), latitude,
					longitude));
		}

		this.updateTable();
		this.repaintMap();
	}

	public void resetRoute() {
		this.selectRoute();
	}

	public void saveTemparature() {
		try {
			Feed feed = this.cosm.getFeed(this.selectedEnvironment.getId()
					.intValue());

			Map<String, String> values = new LinkedHashMap<String, String>();

			for (int i = 0; i < this.routeTable.getRowCount(); i++) {
				values.put(this.routeTable.getValueAt(i, 0) + ";"
						+ this.routeTable.getValueAt(i, 1), this.routeTable
						.getValueAt(i, 2).toString());
			}

			Iterator<String> i = values.keySet().iterator();
			int j = 0;
			while (i.hasNext()) {
				String current = i.next();
				String value = values.get(current);

				feed.updateDatastream(j, Double.parseDouble(value));

				j++;
			}
		} catch (PachubeException e) {
		}
	}
}