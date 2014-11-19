/**
 * 
 */
package a5.fmaster.src.main.java.client.ui.admin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import a5.fmaster.src.main.java.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class AdminUI {
	private AdminMainUI adminMainUI;
	private ParkingInterface parking;
	public JPanel mainContentPnl = new JPanel();
	private JPanel adminOptionsPnl = new JPanel(new GridBagLayout());
	private JPanel addAccountPnl = new JPanel(new GridBagLayout());
	private JPanel disableAccountPnl = new JPanel(new GridBagLayout());
	private JPanel changePwdPnl = new JPanel(new GridBagLayout());
	private JButton backBtn = new JButton("Go Back");
	private JButton logoutBtn = new JButton("Logout");
	private JTextField addUsernameTxt = new JTextField(10);
	private JTextField passwordTxt = new JTextField(10);
	private JTextField securityQuesTxt = new JTextField(10);
	private JTextField securityAnsTxt = new JTextField(10);
	private JTextField disableUsernameTxt = new JTextField(10);
	private JTextField oldPwdTxt = new JTextField(10);
	private JTextField newPwdTxt = new JTextField(10);
	private JTextField newPwd2Txt = new JTextField(10);

	private static AdminUI instance = null;

	private AdminUI(AdminMainUI adminMainUI, ParkingInterface parking) {
		this.adminMainUI = adminMainUI;
		this.parking = parking;
	}

	public static AdminUI getInstance(AdminMainUI adminMainUI, ParkingInterface parking) {
		if (instance == null) {
			instance = new AdminUI(adminMainUI, parking);
		}
		return instance;
	}

	public void setupUI() {
		mainContentPnl.setLayout(new GridBagLayout());
		mainContentPnl.setVisible(false);

		// Admin Options Panel
		JButton configRatesBtn = new JButton("Configure Parking Rates");
		configRatesBtn.addActionListener(new AdminUIListener());
		JButton configSizeBtn = new JButton("Configure Parking Size");
		configSizeBtn.addActionListener(new AdminUIListener());
		JButton viewReportsBtn = new JButton("Generate Reports");
		viewReportsBtn.addActionListener(new AdminUIListener());
		JButton addAccountBtn = new JButton("Add Account");
		addAccountBtn.addActionListener(new AdminUIListener());
		JButton disableAccountBtn = new JButton("Disable Account");
		disableAccountBtn.addActionListener(new AdminUIListener());
		JButton changePwdBtn = new JButton("Change Password");
		changePwdBtn.addActionListener(new AdminUIListener());

		adminMainUI.addGridBagComponent(adminOptionsPnl, configRatesBtn, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(adminOptionsPnl, configSizeBtn, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(adminOptionsPnl, viewReportsBtn, GridBagConstraints.BOTH, 0, 2);
		adminMainUI.addGridBagComponent(adminOptionsPnl, addAccountBtn, GridBagConstraints.BOTH, 0, 3);
		adminMainUI.addGridBagComponent(adminOptionsPnl, disableAccountBtn, GridBagConstraints.BOTH, 0, 4);
		adminMainUI.addGridBagComponent(adminOptionsPnl, changePwdBtn, GridBagConstraints.BOTH, 0, 5);

		// Change Password Panel
		adminMainUI.addGridBagComponent(changePwdPnl, new JLabel("Enter old password: "), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(changePwdPnl, oldPwdTxt, GridBagConstraints.BOTH, 1, 0);
		adminMainUI.addGridBagComponent(changePwdPnl, new JLabel("Enter new password: "), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(changePwdPnl, newPwdTxt, GridBagConstraints.BOTH, 1, 1);
		adminMainUI.addGridBagComponent(changePwdPnl, new JLabel("Re-enter new password: "), GridBagConstraints.BOTH, 0, 2);
		adminMainUI.addGridBagComponent(changePwdPnl, newPwd2Txt, GridBagConstraints.BOTH, 1, 2);
		JButton confirmNewPwdBtn = new JButton("Confirm Change Password");
		confirmNewPwdBtn.addActionListener(new AdminUIListener());
		adminMainUI.addGridBagComponent(changePwdPnl, confirmNewPwdBtn, GridBagConstraints.BOTH, 0, 3, 2, 1);

		// Add Account Panel
		adminMainUI.addGridBagComponent(addAccountPnl, new JLabel("Username: "), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(addAccountPnl, addUsernameTxt, GridBagConstraints.BOTH, 1, 0);
		adminMainUI.addGridBagComponent(addAccountPnl, new JLabel("Password: "), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(addAccountPnl, passwordTxt, GridBagConstraints.BOTH, 1, 1);
		adminMainUI.addGridBagComponent(addAccountPnl, new JLabel("Security Question: "), GridBagConstraints.BOTH, 0, 2);
		adminMainUI.addGridBagComponent(addAccountPnl, securityQuesTxt, GridBagConstraints.BOTH, 1, 2);
		adminMainUI.addGridBagComponent(addAccountPnl, new JLabel("Security Answer: "), GridBagConstraints.BOTH, 0, 3);
		adminMainUI.addGridBagComponent(addAccountPnl, securityAnsTxt, GridBagConstraints.BOTH, 1, 3);
		JButton createAccountBtn = new JButton("Create Account");
		adminMainUI.addGridBagComponent(addAccountPnl, createAccountBtn, GridBagConstraints.NONE, 0, 4, 2, 1);
		createAccountBtn.addActionListener(new AdminUIListener());

		// Disable Account Panel
		adminMainUI.addGridBagComponent(disableAccountPnl, new JLabel("Username: "), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(disableAccountPnl, disableUsernameTxt, GridBagConstraints.BOTH, 1, 0);
		JButton confirmDisableAccountBtn = new JButton("Confirm Disable Account");
		adminMainUI.addGridBagComponent(disableAccountPnl, confirmDisableAccountBtn, GridBagConstraints.NONE, 0, 1, 2, 1);
		confirmDisableAccountBtn.addActionListener(new AdminUIListener());

		// Main Content Panel
		adminMainUI.addGridBagComponent(mainContentPnl, adminOptionsPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, addAccountPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, disableAccountPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, changePwdPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.BOTH, 1, 1);
		backBtn.addActionListener(new AdminUIListener());
		logoutBtn.addActionListener(new AdminUIListener());

		addAccountPnl.setVisible(false);
		disableAccountPnl.setVisible(false);
		changePwdPnl.setVisible(false);
		backBtn.setVisible(false);

		adminMainUI.addGridBagComponent(adminMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	public void resetUI() {
		adminOptionsPnl.setVisible(true);
		logoutBtn.setVisible(true);
		addAccountPnl.setVisible(false);
		disableAccountPnl.setVisible(false);
		changePwdPnl.setVisible(false);
	}

	private class AdminUIListener implements ActionListener {
		private AdminUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Configure Parking Rates")) {
				configureParkingRates();
			}
			if (e.getActionCommand().equals("Configure Parking Size")) {
				configureParkingSize();
			}
			if (e.getActionCommand().equals("Generate Reports")) {
				generateReports();
			}
			if (e.getActionCommand().equals("Add Account")) {
				addAccount();
			}
			if (e.getActionCommand().equals("Create Account")) {
				createAccount();
			}
			if (e.getActionCommand().equals("Disable Account")) {
				disableAccount();
			}
			if (e.getActionCommand().equals("Confirm Disable Account")) {
				confirmDisableAccount();
			}
			if (e.getActionCommand().equals("Change Password")) {
				changePassword();
			}
			if (e.getActionCommand().equals("Confirm Change Password")) {
				confirmChangePassword();
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
			}
			if (e.getActionCommand().equals("Logout")) {
				logout();
			}
		}

		private void logout() {
			try {
				JOptionPane.showMessageDialog(adminMainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				parking.logout();
				resetUI();
				adminMainUI.showHideContentPanel(adminMainUI.mainContentPnl, mainContentPnl);
				adminMainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void confirmChangePassword() {
			String oldPwd = oldPwdTxt.getText();
			String newPwd = newPwdTxt.getText();
			String newPwd2 = newPwd2Txt.getText();
			try {
				if (oldPwd.matches("[a-zA-Z0-9]{1,10}") && newPwd.matches("[a-zA-Z0-9]{1,10}") && newPwd2.matches("[a-zA-Z0-9]{1,10}")) {
					boolean pwdVerified = parking.verifyPassword(oldPwd);
					if (pwdVerified) {
						if (newPwd.equals(newPwd2)) {
							parking.setPassword(newPwd);
							resetUI();
						} else {
							JOptionPane.showMessageDialog(adminMainUI, "New password and re-entered password do not match.", "Password Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(adminMainUI, "Incorrect password. Please try again", "Password Error", JOptionPane.ERROR_MESSAGE);

					}

				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Passwords can be 1-10 alphabets or numbers.", "Password Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void changePassword() {
			adminOptionsPnl.setVisible(false);
			changePwdPnl.setVisible(true);
			backBtn.setVisible(true);
		}

		private void confirmDisableAccount() {
			String disableUsername = disableUsernameTxt.getText();
			try {
				if (disableUsername.matches("[a-zA-Z0-9]{1,10}")) {
					boolean accountDisabled = parking.disableAccount(disableUsername);
					if (accountDisabled) {
						JOptionPane.showMessageDialog(adminMainUI, "Account successfully disabled.", "Message", JOptionPane.ERROR_MESSAGE);
						resetUI();
					} else {
						JOptionPane
								.showMessageDialog(adminMainUI, "Account cannot be disabled.", "Error", JOptionPane.ERROR_MESSAGE);
					}

				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Username can be 1-10 alphabets or numbers.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void disableAccount() {
			adminOptionsPnl.setVisible(false);
			disableAccountPnl.setVisible(true);
			backBtn.setVisible(true);
		}

		private void createAccount() {
			String addUsername = addUsernameTxt.getText();
			String password = passwordTxt.getText();
			String securityQues = securityQuesTxt.getText();
			String securityAns = securityAnsTxt.getText();
			try {
				if (addUsername.matches("[a-zA-Z0-9]{1,10}") && password.matches("[a-zA-Z0-9]{1,10}") && securityQues.matches("[a-zA-Z0-9]{1,10}")
						&& securityAns.matches("[a-zA-Z0-9]{1,10}")) {
					boolean isAdminAccountAdded = parking.addAdminAccount(addUsername, password, securityQues, securityAns);
					if (isAdminAccountAdded) {
						JOptionPane.showMessageDialog(adminMainUI, "Account successfully added.", "Message", JOptionPane.INFORMATION_MESSAGE);
						resetUI();
					} else {
						JOptionPane.showMessageDialog(adminMainUI, "Username already exists. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Username, Password, Security Question and Answer can be 1-10 alphabets or numbers.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void addAccount() {
			adminOptionsPnl.setVisible(false);
			addAccountPnl.setVisible(true);
			backBtn.setVisible(true);
		}

		private void generateReports() {
			resetUI();
			adminMainUI.showHideContentPanel(adminMainUI.reportsUI.mainContentPnl, mainContentPnl);
		}

		private void configureParkingSize() {
			resetUI();
			adminMainUI.showHideContentPanel(adminMainUI.configParkingSizeUI.mainContentPnl, mainContentPnl);
			try {
				adminMainUI.configParkingSizeUI.currentSizeLbl.setText("Current Parking Size: " + parking.getCurrentParkingSize());
				adminMainUI.configParkingSizeUI.currentOccupancyLbl.setText("Current Occupied Slots: " + parking.getCurrentParkingOccupancy());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void configureParkingRates() {
			resetUI();
			adminMainUI.showHideContentPanel(adminMainUI.configRatesUI.mainContentPnl, mainContentPnl);
			try {
				adminMainUI.populateParkingRatesInTable(adminMainUI.configRatesUI.parkingRatesTbl);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
