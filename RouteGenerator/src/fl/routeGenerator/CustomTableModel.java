package fl.routeGenerator;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3612702494643909431L;

	public boolean isCellEditable(int row, int column) {
		if (column == 2)
			return true;
		else
			return false;
	}
}
