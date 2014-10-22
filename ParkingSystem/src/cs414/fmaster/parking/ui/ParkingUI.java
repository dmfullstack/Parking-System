package cs414.fmaster.parking.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import cs414.fmaster.parking.ParkingSystem;

/**
 * @author MasterF
 * 
 */
public class ParkingUI extends JFrame {
	ParkingSystem ps;
	EnterParkingUI enterParkingUI;
	JPanel mainPnl = new JPanel();
	JPanel mainContentPnl = new JPanel();
	JPanel messagePnl = new JPanel();
	JLabel messageLbl = new JLabel("");

	private static String welcomeMsg = "Welcome to My Parking.";
	private static ParkingUI instance = null;

	protected ParkingUI(ParkingSystem ps) {
		super(ps.getName());
		this.ps = ps;
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		generateMainScreen();
		this.enterParkingUI = EnterParkingUI.getInstance(this.ps, this);
	}

	public static ParkingUI getInstance(ParkingSystem ps) {
		if (instance == null) {
			instance = new ParkingUI(ps);
		}
		return instance;
	}

	public void generateMainScreen() {
		setupMainPanel();
		setupMainContent();
		setMessage(welcomeMsg);
		pack();
		setVisible(true);
	}

	public void setupMainPanel() {
		mainPnl.setLayout(new GridBagLayout());
		addComponent(mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
		addComponent(mainPnl, messagePnl, GridBagConstraints.BOTH, 0, 1);
		messagePnl.add(messageLbl);
		add(mainPnl);
	}

	private void setupMainContent() {
		mainContentPnl.setLayout(new GridBagLayout());
		generateAndAddMainMenuOnUI();
		generateAndAddImageOnUI();
	}

	private void generateAndAddMainMenuOnUI() {
		JPanel topLeftPnl = new JPanel();
		JButton enterParkingBtn = new JButton("Enter Parking");
		JButton exitParkingBtn = new JButton("Exit Parking");
		JButton viewRatesBtn = new JButton("View Parking Rates");
		JButton loginBtn = new JButton("Login");
		topLeftPnl.setLayout(new GridLayout(4, 1));
		topLeftPnl.add(enterParkingBtn);
		topLeftPnl.add(exitParkingBtn);
		topLeftPnl.add(viewRatesBtn);
		topLeftPnl.add(loginBtn);

		addComponent(mainContentPnl, topLeftPnl, GridBagConstraints.BOTH, 0, 0);

		enterParkingBtn.addActionListener(new MainButtonListener());
		exitParkingBtn.addActionListener(new MainButtonListener());
		viewRatesBtn.addActionListener(new MainButtonListener());
		loginBtn.addActionListener(new MainButtonListener());
	}

	private void generateAndAddImageOnUI() {
		ImageIcon mainImageIcon = createImageIcon("images/mainImage.jpg");
		JLabel mainImageLbl = new JLabel("");
		mainImageLbl.setIcon(mainImageIcon);

		JPanel topRightPnl = new JPanel();
		addComponent(mainContentPnl, topRightPnl, GridBagConstraints.BOTH, 1, 0);
		topRightPnl.add(mainImageLbl);
	}

	public void setMessage(String message) {
		messageLbl.setText("Message: " + message);
	}

	public void addComponent(JPanel parent, JComponent child, int gridBagFill, int gridx, int gridy) {
		GridBagConstraints localgbc = new GridBagConstraints();
		localgbc.fill = gridBagFill;
		localgbc.gridx = gridx;
		localgbc.gridy = gridy;
		parent.add(child, localgbc);
	}

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ParkingUI.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public void hideContentPanel(JPanel pnl) {
		pnl.setVisible(false);
	}

	public void showContentPanel(JPanel pnl) {
		pnl.setVisible(true);
	}

	public JPanel getMainPanel() {
		return mainPnl;
	}

	public class MainButtonListener implements ActionListener {

		MainButtonListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Enter Parking")) {
				hideContentPanel(mainContentPnl);
				enterParkingUI.generateEnterParkingScreen();
			}
		}
	}
}
