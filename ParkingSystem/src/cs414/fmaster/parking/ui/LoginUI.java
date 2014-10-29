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
public class LoginUI {
	private MainUI mainUI;
	private MainController mainController;
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
		// Login Panel
		mainUI.addGridBagComponent(loginPnl, new JLabel("Username: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(loginPnl, usernameTxt, GridBagConstraints.BOTH, 1, 0);
		mainUI.addGridBagComponent(loginPnl, new JLabel("Password: "), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(loginPnl, pwdTxt, GridBagConstraints.BOTH, 1, 1);
		JButton loginBtn = new JButton("Login");
		mainUI.addGridBagComponent(loginPnl, loginBtn, GridBagConstraints.BOTH, 0, 2);
		JButton forgotPwdBtn = new JButton("Forgot Password");
		mainUI.addGridBagComponent(loginPnl, forgotPwdBtn, GridBagConstraints.BOTH, 1, 2);
		loginBtn.addActionListener(new LoginUIListener());
		forgotPwdBtn.addActionListener(new LoginUIListener());

		// Security Question and Answer Panel
		mainUI.addGridBagComponent(securityPnl, new JLabel("Security Question: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(securityPnl, securityQuesLbl, GridBagConstraints.BOTH, 1, 0);
		mainUI.addGridBagComponent(securityPnl, new JLabel("Security Answer: "), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(securityPnl, securityAnsTxt, GridBagConstraints.BOTH, 1, 1);
		JButton verifyBtn = new JButton("Verify");
		mainUI.addGridBagComponent(securityPnl, verifyBtn, GridBagConstraints.BOTH, 0, 2, 2, 1);
		verifyBtn.addActionListener(new LoginUIListener());

		// Setting New Password Panel
		mainUI.addGridBagComponent(newPwdPnl, new JLabel("New Password: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(newPwdPnl, newPwdTxt, GridBagConstraints.BOTH, 1, 0);
		JButton changePwdBtn = new JButton("Change Password");
		mainUI.addGridBagComponent(newPwdPnl, changePwdBtn, GridBagConstraints.BOTH, 0, 1);
		changePwdBtn.addActionListener(new LoginUIListener());
		mainUI.addGridBagComponent(newPwdPnl, new JLabel(" "), GridBagConstraints.BOTH, 0, 2);

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, loginPnl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, securityPnl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, newPwdPnl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 1);
		backBtn.addActionListener(new LoginUIListener());
		securityPnl.setVisible(false);
		newPwdPnl.setVisible(false);

		mainContentPnl.setVisible(false);
		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
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
				username = usernameTxt.getText();
				String password = pwdTxt.getText();
				if (username.matches("[a-zA-Z0-9]{1,10}") && password.matches("[a-zA-Z0-9]{1,10}")) {
					boolean isLoginSuccessful = mainController.adminOpsHandler.login(username, password);
					if (isLoginSuccessful) {
						resetUI();
						mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
					} else {
						JOptionPane.showMessageDialog(mainUI, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane
							.showMessageDialog(mainUI, "Please enter username and password in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Forgot Password")) {
				username = usernameTxt.getText();
				if (username.matches("[a-zA-Z0-9]{1,10}")) {
					boolean activeUserExist = mainController.adminOpsHandler.isActiveUserExist(username);
					if (activeUserExist) {
						String securityQues = mainController.adminOpsHandler.getSecurityQuestion(username);
						securityQuesLbl.setText(securityQues);
						loginPnl.setVisible(false);
						securityPnl.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(mainUI, "Invalid user.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Please enter username in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Verify")) {
				String securityAns = securityAnsTxt.getText();
				if (securityAns.matches("[a-zA-Z0-9]{1,10}")) {
					boolean securityAnswerVerified = mainController.adminOpsHandler.verifySecurityAnswer(username, securityAns);
					if (securityAnswerVerified) {
						securityPnl.setVisible(false);
						newPwdPnl.setVisible(true);
						backBtn.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(mainUI, "Incorrect Answer.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Please enter security answer in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Change Password")) {
				String newPwd = newPwdTxt.getText();
				if (newPwd.matches("[a-zA-Z0-9]{1,10}")) {
					mainController.adminOpsHandler.setPassword(username, newPwd);
					resetUI();
				} else {
					JOptionPane.showMessageDialog(mainUI, "Please enter new password in correct format.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}
}