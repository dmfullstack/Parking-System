package cs414.fmaster.parking.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cs414.fmaster.parking.controller.MainController;
import cs414.fmaster.parking.controller.ParkingOperationsController;

/**
 * @author MasterF
 * 
 */
public class MainUI extends JFrame {
	MainController mainController;
	EnterParkingUI enterParkingUI;
	ExitParkingUI exitParkingUI;
	ViewRatesUI viewRatesUI;
	LoginUI loginUI;
	JPanel mainPnl = new JPanel();
	JPanel mainContentPnl = new JPanel();
	JPanel messagePnl = new JPanel();
	JLabel messageLbl = new JLabel("");

	private static MainUI instance = null;

	private MainUI() {
	}

	private static MainUI getInstance() {
		if (instance == null) {
			instance = new MainUI();
		}
		return instance;
	}

	private void intitilizeControllers() {
		mainController = MainController.getInstance();
	}

	private void intitializeChildUI() {
		enterParkingUI = EnterParkingUI.getInstance(this, mainController);
		exitParkingUI = ExitParkingUI.getInstance(this, mainController);
		viewRatesUI = ViewRatesUI.getInstance(this, mainController);
		loginUI = LoginUI.getInstance(this, mainController);
	}

	private void setupUI() {
		setupMainUI();
		enterParkingUI.setupUI();
		exitParkingUI.setupUI();
		viewRatesUI.setupUI();
		loginUI.setupUI();
	}

	private void setupMainUI() {
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Main Panel
		mainPnl.setLayout(new GridBagLayout());
		addGridBagComponent(mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
		addGridBagComponent(mainPnl, messagePnl, GridBagConstraints.BOTH, 0, 1);

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
		messagePnl.add(messageLbl);
		displayWelcomeMessage();

		add(mainPnl);
		//pack();
		setVisible(true);
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
		int availableParking = mainController.parkingController.getCurrentAvailability();
		int parkingSize = mainController.parkingController.getCurrentParkingSize();
		displayMessage("Welcome to My Parking!! " + availableParking + " out of " + parkingSize + " parking spots available.");
	}

	public void displayMessage(String message) {
		messageLbl.setText(message);
	}

	public class MainUIListener implements ActionListener {

		MainUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Enter Parking")) {
				boolean isParkingAvailable = mainController.parkingController.isParkingAvailable();
				if (isParkingAvailable) {
					showHideContentPanel(enterParkingUI.mainContentPnl, mainContentPnl);
				} else {
					displayMessage("Sorry. No parking spot available.");
				}
			}
			if (e.getActionCommand().equals("Exit Parking")) {
				showHideContentPanel(exitParkingUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("View Parking Rates")) {
				showHideContentPanel(viewRatesUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Login")) {
				showHideContentPanel(loginUI.mainContentPnl, mainContentPnl);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainUI mainUI = MainUI.getInstance();
				mainUI.intitilizeControllers();
				mainUI.intitializeChildUI();
				mainUI.setupUI();
			}
		});
	}
}