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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs414.fmaster.parking.controller.MainController;

/**
 * @author MasterF
 * 
 */
public class AdminUI {
	private MainUI mainUI;
	private MainController mainController;
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

	private AdminUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static AdminUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new AdminUI(mainUI, mainController);
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

		mainUI.addGridBagComponent(adminOptionsPnl, configRatesBtn, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(adminOptionsPnl, configSizeBtn, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(adminOptionsPnl, viewReportsBtn, GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(adminOptionsPnl, addAccountBtn, GridBagConstraints.BOTH, 0, 3);
		mainUI.addGridBagComponent(adminOptionsPnl, disableAccountBtn, GridBagConstraints.BOTH, 0, 4);
		mainUI.addGridBagComponent(adminOptionsPnl, changePwdBtn, GridBagConstraints.BOTH, 0, 5);

		// Change Password Panel
		mainUI.addGridBagComponent(changePwdPnl, new JLabel("Enter old password: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(changePwdPnl, oldPwdTxt, GridBagConstraints.BOTH, 1, 0);
		mainUI.addGridBagComponent(changePwdPnl, new JLabel("Enter new password: "), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(changePwdPnl, newPwdTxt, GridBagConstraints.BOTH, 1, 1);
		mainUI.addGridBagComponent(changePwdPnl, new JLabel("Re-enter new password: "), GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(changePwdPnl, newPwd2Txt, GridBagConstraints.BOTH, 1, 2);
		JButton confirmNewPwdBtn = new JButton("Confirm Change Password");
		confirmNewPwdBtn.addActionListener(new AdminUIListener());
		mainUI.addGridBagComponent(changePwdPnl, confirmNewPwdBtn, GridBagConstraints.BOTH, 0, 3, 2, 1);

		// Add Account Panel
		mainUI.addGridBagComponent(addAccountPnl, new JLabel("Username: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(addAccountPnl, addUsernameTxt, GridBagConstraints.BOTH, 1, 0);
		mainUI.addGridBagComponent(addAccountPnl, new JLabel("Password: "), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(addAccountPnl, passwordTxt, GridBagConstraints.BOTH, 1, 1);
		mainUI.addGridBagComponent(addAccountPnl, new JLabel("Security Question: "), GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(addAccountPnl, securityQuesTxt, GridBagConstraints.BOTH, 1, 2);
		mainUI.addGridBagComponent(addAccountPnl, new JLabel("Security Answer: "), GridBagConstraints.BOTH, 0, 3);
		mainUI.addGridBagComponent(addAccountPnl, securityAnsTxt, GridBagConstraints.BOTH, 1, 3);
		JButton createAccountBtn = new JButton("Create Account");
		mainUI.addGridBagComponent(addAccountPnl, createAccountBtn, GridBagConstraints.NONE, 0, 4, 2, 1);
		createAccountBtn.addActionListener(new AdminUIListener());

		// Disable Account Panel
		mainUI.addGridBagComponent(disableAccountPnl, new JLabel("Username: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(disableAccountPnl, disableUsernameTxt, GridBagConstraints.BOTH, 1, 0);
		JButton confirmDisableAccountBtn = new JButton("Confirm Disable Account");
		mainUI.addGridBagComponent(disableAccountPnl, confirmDisableAccountBtn, GridBagConstraints.NONE, 0, 1, 2, 1);
		confirmDisableAccountBtn.addActionListener(new AdminUIListener());

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, adminOptionsPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, addAccountPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, disableAccountPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, changePwdPnl, GridBagConstraints.NONE, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.BOTH, 1, 1);
		backBtn.addActionListener(new AdminUIListener());
		logoutBtn.addActionListener(new AdminUIListener());

		addAccountPnl.setVisible(false);
		disableAccountPnl.setVisible(false);
		changePwdPnl.setVisible(false);
		backBtn.setVisible(false);

		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	public void resetUI() {
		adminOptionsPnl.setVisible(true);
		logoutBtn.setVisible(true);
		addAccountPnl.setVisible(false);
		disableAccountPnl.setVisible(false);
		changePwdPnl.setVisible(false);
	}

	private class AdminUIListener implements ActionListener {
		AdminUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Configure Parking Rates")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.configRatesUI.mainContentPnl, mainContentPnl);
				mainUI.populateParkingRatesInTable(mainUI.configRatesUI.parkingRatesTbl);
			}
			if (e.getActionCommand().equals("Configure Parking Size")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.configParkingSizeUI.mainContentPnl, mainContentPnl);
				mainUI.configParkingSizeUI.currentSizeLbl.setText("Current Parking Size: " + mainController.parkingOpsHandler.getCurrentParkingSize());
				mainUI.configParkingSizeUI.currentOccupancyLbl.setText("Current Occupied Slots: " + mainController.parkingOpsHandler.getCurrentParkingOccupancy());
			}
			if (e.getActionCommand().equals("Generate Reports")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.reportsUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Add Account")) {
				adminOptionsPnl.setVisible(false);
				addAccountPnl.setVisible(true);
				backBtn.setVisible(true);
			}
			if (e.getActionCommand().equals("Create Account")) {
				String addUsername = addUsernameTxt.getText();
				String password = passwordTxt.getText();
				String securityQues = securityQuesTxt.getText();
				String securityAns = securityAnsTxt.getText();
				if (addUsername.matches("[a-zA-Z0-9]{1,10}") && password.matches("[a-zA-Z0-9]{1,10}") && securityQues.matches("[a-zA-Z0-9]{1,10}")
						&& securityAns.matches("[a-zA-Z0-9]{1,10}")) {
					boolean isAdminAccountAdded = mainController.adminOpsHandler.addAdminAccount(addUsername, password, securityQues, securityAns);
					if (isAdminAccountAdded) {
						JOptionPane.showMessageDialog(mainUI, "Account added.", "Message", JOptionPane.INFORMATION_MESSAGE);
						resetUI();
					} else {
						JOptionPane.showMessageDialog(mainUI, "Username exists.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Username, Password and Security Question and Answer can be 1-10 alphabets or numbers.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Disable Account")) {
				adminOptionsPnl.setVisible(false);
				disableAccountPnl.setVisible(true);
				backBtn.setVisible(true);
			}
			if (e.getActionCommand().equals("Confirm Disable Account")) {
				String disableUsername = disableUsernameTxt.getText();
				if (disableUsername.matches("[a-zA-Z0-9]{1,10}")) {
					boolean accountDisabled = mainController.adminOpsHandler.disableAccount(disableUsername);
					if(accountDisabled) {
						JOptionPane.showMessageDialog(mainUI, "Account Disabled.",
								"Message", JOptionPane.ERROR_MESSAGE);
						resetUI();
					}
					else {
						JOptionPane.showMessageDialog(mainUI, "Cannot disable account or account does not exist.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}

				} else {
					JOptionPane.showMessageDialog(mainUI, "Username can be 1-10 alphabets or numbers.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Change Password")) {
				adminOptionsPnl.setVisible(false);
				changePwdPnl.setVisible(true);
				backBtn.setVisible(true);
			}
			if (e.getActionCommand().equals("Confirm Change Password")) {
				String oldPwd = oldPwdTxt.getText();
				String newPwd = newPwdTxt.getText();
				String newPwd2 = newPwd2Txt.getText();
				if (oldPwd.matches("[a-zA-Z0-9]{1,10}") && newPwd.matches("[a-zA-Z0-9]{1,10}") && newPwd2.matches("[a-zA-Z0-9]{1,10}")) {
					boolean pwdVerified = mainController.adminOpsHandler.verifyPassword(oldPwd);
					if (pwdVerified) {
						if (newPwd.equals(newPwd2)) {
							mainController.adminOpsHandler.setPassword(newPwd);
							resetUI();
						} else {
							JOptionPane.showMessageDialog(mainUI, "New password and re-entered password do not match.", "Password Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(mainUI, "Incorrect password. Please try again", "Password Error", JOptionPane.ERROR_MESSAGE);

					}

				} else {
					JOptionPane.showMessageDialog(mainUI, "Passwords can be 1-10 alphabets or numbers.", "Password Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
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
