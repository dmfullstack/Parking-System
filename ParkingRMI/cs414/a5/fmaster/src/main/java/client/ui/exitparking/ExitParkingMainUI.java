package a5.fmaster.src.main.java.client.ui.exitparking;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.client.ui.MainUI;
import a5.fmaster.src.main.java.client.ui.ViewRatesUIInterface;
import a5.fmaster.src.main.java.client.ui.admin.AdminUI;
import a5.fmaster.src.main.java.client.ui.admin.ConfigParkingSizeUI;
import a5.fmaster.src.main.java.client.ui.admin.ConfigRatesUI;
import a5.fmaster.src.main.java.client.ui.admin.LoginUI;
import a5.fmaster.src.main.java.client.ui.admin.ReportsUI;
import a5.fmaster.src.main.java.client.ui.enterparking.EnterParkingUI;
import a5.fmaster.src.main.java.client.ui.enterparking.EnterParkingViewRatesUI;
import a5.fmaster.src.main.java.common.ParkingInterface;
import a5.fmaster.src.main.java.server.domain.ParkingRate;

public class ExitParkingMainUI extends MainUI {
	public ExitParkingUI exitParkingUI;
	public ExitParkingViewRatesUI viewRatesUI;

	public ExitParkingMainUI(ParkingInterface parking) throws RemoteException {
		this.parking = parking;
		initializeChildUI();
		setupChildUI();
	}

	public void initializeChildUI() {
		exitParkingUI = ExitParkingUI.getInstance(this, parking);
		viewRatesUI = ExitParkingViewRatesUI.getInstance(this, parking);
	}

	public void setupChildUI() throws RemoteException {
		setupMainUI();
		exitParkingUI.setupUI();
		viewRatesUI.setupUI();
	}

	public void setupMainUI() throws RemoteException {
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainPnl);
		setVisible(true);

		// Top Portion of Main Panel - Content
		JPanel topLeftPnl = new JPanel();
		JPanel topRightPnl = new JPanel();
		addGridBagComponent(mainContentPnl, topLeftPnl, GridBagConstraints.BOTH, 0, 0);
		addGridBagComponent(mainContentPnl, topRightPnl, GridBagConstraints.BOTH, 1, 0);

		// Left Panel with buttons
		JButton exitParkingBtn = new JButton("Exit Parking");
		JButton viewRatesBtn = new JButton("View Parking Rates");
		topLeftPnl.setLayout(new GridLayout(2, 1));
		topLeftPnl.add(exitParkingBtn);
		topLeftPnl.add(viewRatesBtn);

		exitParkingBtn.addActionListener(new MainUIListener());
		viewRatesBtn.addActionListener(new MainUIListener());

		// Right Panel with Image
		ImageIcon mainImageIcon = null;
		java.net.URL imgURL = ExitParkingMainUI.class.getResource("../resources/mainImage.jpg");
		if (imgURL != null) {
			mainImageIcon = new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find image file.");
		}
		JLabel mainImageLbl = new JLabel("");
		mainImageLbl.setIcon(mainImageIcon);
		topRightPnl.add(mainImageLbl);

		// Bottom Portion of Main Panel - Messages and Parking Availability
		JPanel messagePnl = new JPanel();
		messagePnl.add(messageLbl);
		updateWelcomeMessage();

		// pack();
		// Main Panel
		addGridBagComponent(mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
		addGridBagComponent(mainPnl, messagePnl, GridBagConstraints.BOTH, 0, 1);
	}

	private class MainUIListener implements ActionListener {

		private MainUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Exit Parking")) {
				exitParking();
			}
			if (e.getActionCommand().equals("View Parking Rates")) {
				viewParkingRates();
			}
		}

		private void exitParking() {
			boolean isParkingEmpty;
			try {
				isParkingEmpty = parking.isParkingEmpty();
				if (isParkingEmpty) {
					displayMessage("No cars present in parking.");
				} else {
					showHideContentPanel(exitParkingUI.mainContentPnl, mainContentPnl);
					populateParkingRatesInTable(exitParkingUI.parkingRatesTbl);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void viewParkingRates() {
			showHideContentPanel(viewRatesUI.mainContentPnl, mainContentPnl);
			try {
				populateParkingRatesInTable(viewRatesUI.parkingRatesTbl);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

		}
	}
}
