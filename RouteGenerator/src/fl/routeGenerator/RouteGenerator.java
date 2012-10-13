package fl.routeGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

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
		this.maps.setPreferredSize(new Dimension(800, 600));
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
		// Route Tree
		JPanel treePanel = new JPanel();
		treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.Y_AXIS));
		this.routeTree = new JTree();
		this.routeTree.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("Route")));
		this.routeTree.setPreferredSize(new Dimension(250, 300));
		this.routeTree.addKeyListener(new TreeKeyListener(this));
		JScrollPane jsp = new JScrollPane(this.routeTree);
		treePanel.add(jsp);

		// Buttons
		JPanel otherPanel = new JPanel();
		otherPanel.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.routeName = new JTextField();
		otherPanel.add(this.routeName, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		this.clear = new JButton("Clear route");
		this.clear.setName("clear");
		this.clear.addMouseListener(new DefaultMouseListener(this));
		otherPanel.add(this.clear, c);

		c.gridx = 1;
		c.gridy = 1;
		this.save = new JButton("Save route to COSM");
		this.save.setName("save");
		this.save.addMouseListener(new DefaultMouseListener(this));
		otherPanel.add(this.save, c);

		// Split Pane
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		JSplitPane routePanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		routePanel.add(treePanel);
		routePanel.add(otherPanel);
		this.getContentPane().add(routePanel, c);

		// Other
		this.route = new LinkedHashSet<Waypoint>();

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
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(
					getCoordinateString(current));
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
					routePoint.setTag(getCoordinateString(current));
					routeFeed.addData(routePoint);
					j++;
				}

				cosm.createFeed(routeFeed);
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
		this.updateTree();
		this.repaintMap();
	}

	public void deleteRoutePoint() {
		if (this.routeTree.getSelectionPath().getLastPathComponent() != null) {
			String point = ((TreeNode) this.routeTree.getSelectionPath()
					.getLastPathComponent()).toString();

			((DefaultTreeModel) this.routeTree.getModel())
					.removeNodeFromParent((MutableTreeNode) this.routeTree
							.getSelectionPath().getLastPathComponent());

			Iterator<Waypoint> i = this.route.iterator();
			while (i.hasNext()) {
				RoutePoint current = (RoutePoint) i.next();

				if (getCoordinateString(current).equals(point))
					i.remove();
			}

			this.updateMapWithTree();
		}
	}
}