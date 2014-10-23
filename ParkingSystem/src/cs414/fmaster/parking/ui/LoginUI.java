/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.controller.MainController;
import cs414.fmaster.parking.controller.ParkingRate;
import cs414.fmaster.parking.controller.ParkingOperationsController;

/**
 * @author MasterF
 * 
 */
public class LoginUI {
	MainUI mainUI;
	MainController mainController;
	JPanel mainContentPnl = new JPanel();

	private static LoginUI instance = null;

	private LoginUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static LoginUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new LoginUI(mainUI, mainController);
		}
		return instance;
	}

	public void setupUI() {
		mainContentPnl.setLayout(new GridBagLayout());

		mainContentPnl.setVisible(false);

		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class LoginUIListener implements ActionListener {

		LoginUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Go Back")) {
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}
}