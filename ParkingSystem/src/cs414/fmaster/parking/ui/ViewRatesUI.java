/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.controller.MainController;

/**
 * @author MasterF
 * 
 */
public class ViewRatesUI {
	private MainUI mainUI;
	private MainController mainController;
	JPanel mainContentPnl = new JPanel();
	JTable parkingRatesTbl = new JTable();

	private static ViewRatesUI instance = null;

	private ViewRatesUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static ViewRatesUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new ViewRatesUI(mainUI, mainController);
		}
		return instance;
	}

	public void setupUI() {
		mainContentPnl.setLayout(new GridBagLayout());

		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] {"Hours", "Rate"});
		parkingRatesTbl.setModel(parkingRatesModel);
		mainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		mainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Back button
		JButton backBtn = new JButton("Go Back");
		backBtn.addActionListener(new ViewRatesListener());

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 1);

		mainContentPnl.setVisible(false);
		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ViewRatesListener implements ActionListener {

		ViewRatesListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Go Back")) {
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}
}