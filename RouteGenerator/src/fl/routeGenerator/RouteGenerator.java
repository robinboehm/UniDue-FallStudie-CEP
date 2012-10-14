package fl.routeGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
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

	public RouteGenerator() {
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
		routeComboBox.addItemListener(new DefaultItemListener(this));
		selectionPanel.add(routeComboBox, c);

		JButton reloadButton = new JButton("Reload");
		reloadButton.setName("reload");
		reloadButton.addMouseListener(new DefaultMouseListener(this));
		c.gridx = 1;
		c.gridy = 1;
		selectionPanel.add(reloadButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		this.routeTable = new JTable();
		this.routeTable.setModel(this.generateTableModel(null));
		this.routeTable.setPreferredSize(new Dimension(300, 300));
		this.routeTable.addKeyListener(new TreeKeyListener(this));

		JScrollPane jsp = new JScrollPane(this.routeTable);
		selectionPanel.add(jsp, c);

		c.gridx = 0;
		c.gridy = 3;
		JLabel routeNameLabel = new JLabel("Route name:");
		selectionPanel.add(routeNameLabel, c);

		c.gridx = 1;
		c.gridy = 3;
		c.fill = GridBagConstraints.NONE;
		this.routeName = new JTextField(20);
		selectionPanel.add(this.routeName, c);

		c.gridx = 1;
		c.gridy = 4;
		this.clear = new JButton("Clear route");
		this.clear.setName("clear");
		this.clear.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(this.clear, c);

		c.gridx = 1;
		c.gridy = 5;
		this.save = new JButton("Save route to COSM");
		this.save.setName("save");
		this.save.addMouseListener(new DefaultMouseListener(this));
		selectionPanel.add(this.save, c);

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

	private void loadRoutesFromCOSM() {
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://api.cosm.com/v2/feeds.xml");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("user", "futurelogistics");

		String s = webResource.queryParams(queryParams)
				.header("X-ApiKey", COSM_KEY).get(String.class);

		try {
			JAXBContext context = JAXBContext.newInstance(Eeml.class);

			Eeml feeds = (Eeml) context.createUnmarshaller().unmarshal(
					new StringReader(s));

			this.cosmRoutes = feeds.getEnvironment();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public JXMapKit getMaps() {
		return maps;
	}

	public static void init() {
		RouteGenerator ui = new RouteGenerator();
		ui.setVisible(true);
	}

	public void addPoint(GeoPosition position) {
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
				Pachube cosm = new Pachube(COSM_KEY);

				Feed routeFeed = new Feed();
				routeFeed.setTitle(this.routeName.getText());

				Iterator<Waypoint> i = this.route.iterator();
				int j = 0;
				while (i.hasNext()) {
					RoutePoint current = (RoutePoint) i.next();

					Data routePoint = new Data();
					routePoint.setId(j);
					routePoint.setMinValue(-30d);
					routePoint.setMaxValue(-50d);
					routePoint.setTag(getCoordinateString(current));
					routeFeed.addData(routePoint);
					j++;
				}

				cosm.createFeed(routeFeed);

				this.reloadRoutes();
			} catch (PachubeException e) {
				e.printStackTrace();
			}
		}
	}

	private String getCoordinateString(RoutePoint current) {
		return current.getPosition().getLatitude() + ";"
				+ current.getPosition().getLongitude();
	}

	public void clear() {
		this.route.clear();
		this.routeName.setText("");
		this.updateMapWithTree();
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

	public void loadExistingRoute(Environment item) {
		if (this.route == null)
			this.route = new LinkedHashSet<Waypoint>();
		else
			this.route.clear();

		Iterator<fl.routeGenerator.cosm.Data> i = item.getData().iterator();
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
}