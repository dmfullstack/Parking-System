package cs414.a5.fmaster.main.java.client.ui.admin;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import cs414.a5.fmaster.main.java.client.ui.MainUI;
import cs414.a5.fmaster.main.java.server.ParkingInterface;

public class AdminMainUI extends MainUI {
	private static final long serialVersionUID = 1L;
	public AdminViewRatesUI viewRatesUI;
	private LoginUI loginUI;
	public AdminUI adminUI;
	public ConfigRatesUI configRatesUI;
	public ConfigParkingSizeUI configParkingSizeUI;
	public ReportsUI reportsUI;

	public AdminMainUI(ParkingInterface parking) throws RemoteException {
		this.parking = parking;
		initializeChildUI();
		setupChildUI();
	}

	public void initializeChildUI() {
		viewRatesUI = AdminViewRatesUI.getInstance(this, parking);
		loginUI = LoginUI.getInstance(this, parking);
		adminUI = AdminUI.getInstance(this, parking);
		configRatesUI = ConfigRatesUI.getInstance(this, parking);
		configParkingSizeUI = ConfigParkingSizeUI.getInstance(this, parking);
		reportsUI = ReportsUI.getInstance(this, parking);

	}

	public void setupChildUI() throws RemoteException {
		setupMainUI();
		viewRatesUI.setupUI();
		loginUI.setupUI();
		adminUI.setupUI();
		configRatesUI.setupUI();
		configParkingSizeUI.setupUI();
		reportsUI.setupUI();
	}

	public void setupMainUI() throws RemoteException {
		setSize(500, 700);
		setLocationRelativeTo(null);
		setTitle("Admin Console");  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainPnl);
		setVisible(true);

		// Top Portion of Main Panel - Content
		JPanel topLeftPnl = new JPanel();
		JPanel topRightPnl = new JPanel();
		addGridBagComponent(mainContentPnl, topLeftPnl, GridBagConstraints.BOTH, 0, 0);
		addGridBagComponent(mainContentPnl, topRightPnl, GridBagConstraints.BOTH, 1, 0);

		// Left Panel with buttons
		JButton viewRatesBtn = new JButton("View Parking Rates");
		JButton loginBtn = new JButton("Login");
		topLeftPnl.setLayout(new GridLayout(2, 1));
		topLeftPnl.add(viewRatesBtn);
		topLeftPnl.add(loginBtn);

		viewRatesBtn.addActionListener(new MainUIListener());
		loginBtn.addActionListener(new MainUIListener());

		// Right Panel with Image
		ImageIcon mainImageIcon = null;
		java.net.URL imgURL = AdminMainUI.class.getResource("../resources/mainImage.jpg");
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
			if (e.getActionCommand().equals("View Parking Rates")) {
				viewParkingRates();
			}
			if (e.getActionCommand().equals("Login")) {
				login();
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

		private void login() {
			showHideContentPanel(loginUI.mainContentPnl, mainContentPnl);
		}
	}
}
