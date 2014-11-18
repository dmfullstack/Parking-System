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

import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class LoginUI {
	private AdminMainUI adminMainUI;
	private ParkingServerInterface parking;
	JPanel mainContentPnl = new JPanel(new GridBagLayout());
	private JPanel loginPnl = new JPanel(new GridBagLayout());
	private JPanel securityPnl = new JPanel(new GridBagLayout());
	private JPanel newPwdPnl = new JPanel(new GridBagLayout());
	private JTextField usernameTxt = new JTextField(10);
	private JTextField pwdTxt = new JTextField(10);
	private JLabel securityQuesLbl = new JLabel("");
	private JTextField securityAnsTxt = new JTextField(10);
	private JTextField newPwdTxt = new JTextField(10);
	private JButton backBtn = new JButton("Go Back");
	private String username;

	private static LoginUI instance = null;

	private LoginUI(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		this.adminMainUI = adminMainUI;
		this.parking = parking;
	}

	public static LoginUI getInstance(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new LoginUI(adminMainUI, parking);
		}
		return instance;
	}

	public void setupUI() {
		// Login Panel
		adminMainUI.addGridBagComponent(loginPnl, new JLabel("Username: "), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(loginPnl, usernameTxt, GridBagConstraints.BOTH, 1, 0);
		adminMainUI.addGridBagComponent(loginPnl, new JLabel("Password: "), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(loginPnl, pwdTxt, GridBagConstraints.BOTH, 1, 1);
		JButton loginBtn = new JButton("Login");
		adminMainUI.addGridBagComponent(loginPnl, loginBtn, GridBagConstraints.BOTH, 0, 2);
		JButton forgotPwdBtn = new JButton("Forgot Password");
		adminMainUI.addGridBagComponent(loginPnl, forgotPwdBtn, GridBagConstraints.BOTH, 1, 2);
		loginBtn.addActionListener(new LoginUIListener());
		forgotPwdBtn.addActionListener(new LoginUIListener());

		// Security Question and Answer Panel
		adminMainUI.addGridBagComponent(securityPnl, new JLabel("Security Question: "), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(securityPnl, securityQuesLbl, GridBagConstraints.BOTH, 1, 0);
		adminMainUI.addGridBagComponent(securityPnl, new JLabel("Security Answer: "), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(securityPnl, securityAnsTxt, GridBagConstraints.BOTH, 1, 1);
		JButton verifyBtn = new JButton("Verify");
		adminMainUI.addGridBagComponent(securityPnl, verifyBtn, GridBagConstraints.BOTH, 0, 2, 2, 1);
		verifyBtn.addActionListener(new LoginUIListener());

		// Setting New Password Panel
		adminMainUI.addGridBagComponent(newPwdPnl, new JLabel("New Password: "), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(newPwdPnl, newPwdTxt, GridBagConstraints.BOTH, 1, 0);
		JButton changePwdBtn = new JButton("Change Password");
		adminMainUI.addGridBagComponent(newPwdPnl, changePwdBtn, GridBagConstraints.BOTH, 0, 1);
		changePwdBtn.addActionListener(new LoginUIListener());
		adminMainUI.addGridBagComponent(newPwdPnl, new JLabel(" "), GridBagConstraints.BOTH, 0, 2);

		// Main Content Panel
		adminMainUI.addGridBagComponent(mainContentPnl, loginPnl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(mainContentPnl, securityPnl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(mainContentPnl, newPwdPnl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 1);
		backBtn.addActionListener(new LoginUIListener());
		securityPnl.setVisible(false);
		newPwdPnl.setVisible(false);

		mainContentPnl.setVisible(false);
		adminMainUI.addGridBagComponent(adminMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private void resetUI() {
		loginPnl.setVisible(true);
		securityPnl.setVisible(false);
		newPwdPnl.setVisible(false);
		backBtn.setVisible(true);
		usernameTxt.setText("");
		pwdTxt.setText("");
		securityAnsTxt.setText("");
		newPwdTxt.setText("");
		username = "";
	}

	private class LoginUIListener implements ActionListener {
		private LoginUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Login")) {
				login();
			}
			if (e.getActionCommand().equals("Forgot Password")) {
				forgotPassword();
			}
			if (e.getActionCommand().equals("Verify")) {
				verify();
			}
			if (e.getActionCommand().equals("Change Password")) {
				changePassword();
			}
			if (e.getActionCommand().equals("Go Back")) {
				goBack();
			}
		}

		private void goBack() {
			resetUI();
			adminMainUI.showHideContentPanel(adminMainUI.mainContentPnl, mainContentPnl);
			try {
				adminMainUI.updateWelcomeMessage();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		private void changePassword() {
			String newPwd = newPwdTxt.getText();
			if (newPwd.matches("[a-zA-Z0-9]{1,10}")) {
				try {
					parking.setPassword(username, newPwd);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				resetUI();
			} else {
				JOptionPane.showMessageDialog(adminMainUI, "Please enter new password in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		private void verify() {
			String securityAns = securityAnsTxt.getText();
			if (securityAns.matches("[a-zA-Z0-9]{1,10}")) {
				boolean securityAnswerVerified;
				try {
					securityAnswerVerified = parking.verifySecurityAnswer(username, securityAns);
					if (securityAnswerVerified) {
						securityPnl.setVisible(false);
						newPwdPnl.setVisible(true);
						backBtn.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(adminMainUI, "Incorrect Answer.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(adminMainUI, "Please enter security answer in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		private void forgotPassword() {
			username = usernameTxt.getText();
			try {
				if (username.matches("[a-zA-Z0-9]{1,10}")) {
					boolean activeUserExist = parking.isActiveUserExist(username);
					if (activeUserExist) {
						String securityQues = parking.getSecurityQuestion(username);
						securityQuesLbl.setText(securityQues);
						loginPnl.setVisible(false);
						securityPnl.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(adminMainUI, "Invalid user.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Please enter username in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		private void login() {
			username = usernameTxt.getText();
			String password = pwdTxt.getText();
			try {
				if (username.matches("[a-zA-Z0-9]{1,10}") && password.matches("[a-zA-Z0-9]{1,10}")) {
					boolean isLoginSuccessful = parking.login(username, password);
					if (isLoginSuccessful) {
						resetUI();
						adminMainUI.showHideContentPanel(adminMainUI.adminUI.mainContentPnl, mainContentPnl);
					} else {
						JOptionPane.showMessageDialog(adminMainUI, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane
							.showMessageDialog(adminMainUI, "Please enter username and password in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}