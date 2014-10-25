/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import cs414.fmaster.parking.controller.MainController;

/**
 * @author MasterF
 * 
 */
public class ConfigRatesUI {

	private MainUI mainUI;
	private MainController mainController;
	public JPanel mainContentPnl = new JPanel();
	private JButton backBtn = new JButton("Go Back");
	private JButton logoutBtn = new JButton("Logout");
	JTable parkingRatesTbl = new JTable();
	
	private static ConfigRatesUI instance = null;

	private ConfigRatesUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static ConfigRatesUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new ConfigRatesUI(mainUI, mainController);
		}
		return instance;
	}

	public void setupUI() {
		mainContentPnl.setLayout(new GridBagLayout());
		mainContentPnl.setVisible(false);

		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	public void resetUI() {

	}

	private class ConfigRatesUIListener implements ActionListener {
		ConfigRatesUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Configure Parking Rates")) {
				resetUI();
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Logout")) {
				JOptionPane.showMessageDialog(mainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				mainController.adminOpsHandler.logout();
				resetUI();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}

}
