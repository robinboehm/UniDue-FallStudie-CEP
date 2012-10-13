package fl.routeGenerator;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
	private JTree routeTree;
	private JTextField routeName;
	private JButton save;
	private JButton clear;

	public RouteGenerator() {
		this.setTitle("Banana Route Generator");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

		// Maps
		this.maps = new JXMapKit();
		this.maps.setDefaultProvider(DefaultProviders.OpenStreetMaps);
		this.maps.setDataProviderCreditShown(true);
		this.maps.setPreferredSize(new Dimension(800, 600));
		this.maps.setVisible(true);
		this.maps.getMainMap().addMouseListener(new MapMouseListener(this));
		this.getContentPane().add(maps);
		customPainter = new CompoundPainter<WaypointPainter<JXMapViewer>>();
		routePainter = new RoutePainter<JXMapViewer>();
		customPainter.setPainters(routePainter);
		customPainter.setCacheable(false);
		maps.getMainMap().setOverlayPainter(customPainter);

		// Other UI
		JPanel treePanel = new JPanel();
		treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.Y_AXIS));
		this.getContentPane().add(treePanel);

		this.routeTree = new JTree();
		this.routeTree.setModel(null);
		this.routeTree.setPreferredSize(new Dimension(250, 300));
		JScrollPane jsp = new JScrollPane(this.routeTree);
		treePanel.add(jsp);

		this.routeName = new JTextField();
		treePanel.add(this.routeName);

		this.clear = new JButton("Clear route");
		this.clear.setName("clear");
		this.clear.addMouseListener(new DefaultMouseListener(this));
		treePanel.add(this.clear);

		this.save = new JButton("Create new route");
		this.save.setName("save");
		this.save.addMouseListener(new DefaultMouseListener(this));
		treePanel.add(this.save);

		// Other
		this.route = new TreeSet<Waypoint>();

		// Finally
		this.pack();
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

		this.updateTree();
	}

	private void repaintMap() {
		this.routePainter.setWaypoints(this.route);
		this.maps.getMainMap().invalidate();
		this.maps.repaint();
	}

	private void updateTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Route");

		Iterator<Waypoint> i = this.route.iterator();
		while (i.hasNext()) {
			RoutePoint current = (RoutePoint) i.next();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(current
					.getPosition().getLatitude()
					+ ";"
					+ current.getPosition().getLongitude());
			root.add(node);
		}

		DefaultTreeModel dtm = new DefaultTreeModel(root);
		this.routeTree.setModel(dtm);
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
					routePoint.setTag(current.getPosition().getLatitude() + ";"
							+ current.getPosition().getLongitude());
					routeFeed.addData(routePoint);
					j++;
				}

				cosm.createFeed(routeFeed);
			} catch (PachubeException e) {
				e.printStackTrace();
			}
		}
	}

	public void clear() {
		this.route.clear();
		this.routeName.setText("");
		this.updateTree();
		this.repaintMap();
	}
}