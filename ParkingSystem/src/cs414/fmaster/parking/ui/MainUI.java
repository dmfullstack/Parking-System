package cs414.fmaster.parking.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.controller.MainController;
import cs414.fmaster.parking.controller.ParkingOperationsHandler;
import cs414.fmaster.parking.controller.ParkingRate;

/**
 * @author MasterF
 * 
 */
public class MainUI extends JFrame {
	private MainController mainController;
	private EnterParkingUI enterParkingUI;
	private ExitParkingUI exitParkingUI;
	private ViewRatesUI viewRatesUI;
	private LoginUI loginUI;
	public AdminUI adminUI;
	public ConfigRatesUI configRatesUI;
	public ConfigParkingSizeUI configParkingSizeUI;
	public ReportsUI reportsUI;
	public JPanel mainPnl = new JPanel();
	public JPanel mainContentPnl = new JPanel();
	private JLabel messageLbl = new JLabel("");

	private static MainUI instance = null;

	private MainUI(MainController mainController) {
		this.mainController = mainController;
		intitializeChildUI();
		setupUI();
	}

	public static MainUI getInstance(MainController mainController) {
		if (instance == null) {
			instance = new MainUI(mainController);
		}
		return instance;
	}

	private void intitializeChildUI() {
		enterParkingUI = EnterParkingUI.getInstance(this, mainController);
		exitParkingUI = ExitParkingUI.getInstance(this, mainController);
		viewRatesUI = ViewRatesUI.getInstance(this, mainController);
		loginUI = LoginUI.getInstance(this, mainController);
		adminUI = AdminUI.getInstance(this, mainController);
		configRatesUI = ConfigRatesUI.getInstance(this, mainController);
		configParkingSizeUI = ConfigParkingSizeUI.getInstance(this, mainController);
		reportsUI = ReportsUI.getInstance(this, mainController);
	}

	private void setupUI() {
		setupMainUI();
		enterParkingUI.setupUI();
		exitParkingUI.setupUI();
		viewRatesUI.setupUI();
		loginUI.setupUI();
		adminUI.setupUI();
		configRatesUI.setupUI();
		configParkingSizeUI.setupUI();
		reportsUI.setupUI();
	}

	private void setupMainUI() {
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainPnl);
		setVisible(true);

		// Top Portion of Main Panel - Content
		mainContentPnl.setLayout(new GridBagLayout());
		JPanel topLeftPnl = new JPanel();
		JPanel topRightPnl = new JPanel();
		addGridBagComponent(mainContentPnl, topLeftPnl, GridBagConstraints.BOTH, 0, 0);
		addGridBagComponent(mainContentPnl, topRightPnl, GridBagConstraints.BOTH, 1, 0);

		// Left Panel with buttons
		JButton enterParkingBtn = new JButton("Enter Parking");
		JButton exitParkingBtn = new JButton("Exit Parking");
		JButton viewRatesBtn = new JButton("View Parking Rates");
		JButton loginBtn = new JButton("Login");
		topLeftPnl.setLayout(new GridLayout(4, 1));
		topLeftPnl.add(enterParkingBtn);
		topLeftPnl.add(exitParkingBtn);
		topLeftPnl.add(viewRatesBtn);
		topLeftPnl.add(loginBtn);

		enterParkingBtn.addActionListener(new MainUIListener());
		exitParkingBtn.addActionListener(new MainUIListener());
		viewRatesBtn.addActionListener(new MainUIListener());
		loginBtn.addActionListener(new MainUIListener());

		// Right Panel with Image
		ImageIcon mainImageIcon = null;
		java.net.URL imgURL = MainUI.class.getResource("images/mainImage.jpg");
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
		displayWelcomeMessage();

		// pack();
		// Main Panel
		mainPnl.setLayout(new GridBagLayout());
		addGridBagComponent(mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
		addGridBagComponent(mainPnl, messagePnl, GridBagConstraints.BOTH, 0, 1);
	}

	public void addGridBagComponent(JPanel parent, JComponent child, int gridBagFill, int gridx, int gridy) {
		GridBagConstraints localgbc = new GridBagConstraints();
		localgbc.fill = gridBagFill;
		localgbc.gridx = gridx;
		localgbc.gridy = gridy;
		parent.add(child, localgbc);
	}

	public void addGridBagComponent(JPanel parent, JComponent child, int gridBagFill, int gridx, int gridy, int gridWidth, int gridHeight) {
		GridBagConstraints localgbc = new GridBagConstraints();
		localgbc.fill = gridBagFill;
		localgbc.gridx = gridx;
		localgbc.gridy = gridy;
		localgbc.gridwidth = gridWidth;
		localgbc.gridheight = gridHeight;
		parent.add(child, localgbc);
	}

	public void showHideContentPanel(JPanel pnlToShow, JPanel pnlToHide) {
		pnlToHide.setVisible(false);
		pnlToShow.setVisible(true);
		displayMessage("");
	}

	public void displayWelcomeMessage() {
		int availableParking = mainController.parkingOpsHandler.getCurrentAvailability();
		int parkingSize = mainController.parkingOpsHandler.getCurrentParkingSize();
		displayMessage("Welcome to My Parking!! " + availableParking + " out of " + parkingSize + " parking spots available.");
	}

	public void displayMessage(String message) {
		messageLbl.setText(message);
	}

	public void populateParkingRatesInTable(JTable parkingRatesTbl) {
		DefaultTableModel model = (DefaultTableModel) parkingRatesTbl.getModel();
		model.setRowCount(0);
		model.addColumn("Hours");
	    model.addColumn("Rate");
	    List<ParkingRate> parkingRatesList = new ArrayList<ParkingRate>();
		parkingRatesList = mainController.parkingOpsHandler.getParkingRates();
		for (ParkingRate pr : parkingRatesList) {
			model.addRow(new Object[] { pr.getHours(), pr.getRate() });
		}
	}

	public class MainUIListener implements ActionListener {

		MainUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Enter Parking")) {
				boolean isParkingAvailable = mainController.parkingOpsHandler.isParkingAvailable();
				if (isParkingAvailable) {
					showHideContentPanel(enterParkingUI.mainContentPnl, mainContentPnl);
					populateParkingRatesInTable(enterParkingUI.parkingRatesTbl);
				} else {
					displayMessage("Sorry. No parking spot available.");
				}
			}
			if (e.getActionCommand().equals("Exit Parking")) {
				boolean isParkingEmpty = mainController.parkingOpsHandler.isParkingEmpty();
				if (isParkingEmpty) {
					displayMessage("No cars present in parking.");
				} else {
					showHideContentPanel(exitParkingUI.mainContentPnl, mainContentPnl);
					populateParkingRatesInTable(exitParkingUI.parkingRatesTbl);
				}
			}
			if (e.getActionCommand().equals("View Parking Rates")) {
				showHideContentPanel(viewRatesUI.mainContentPnl, mainContentPnl);
				populateParkingRatesInTable(viewRatesUI.parkingRatesTbl);
			}
			if (e.getActionCommand().equals("Login")) {
				showHideContentPanel(loginUI.mainContentPnl, mainContentPnl);
			}
		}
	}
}