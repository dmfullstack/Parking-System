/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.controller.MainController;

/**
 * @author MasterF
 * 
 */
public class ExitParkingUI {
	private MainUI mainUI;
	private MainController mainController;
	JPanel mainContentPnl = new JPanel(new GridBagLayout());
	private JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
	JTable parkingRatesTbl = new JTable();
	private JTextField ticketTxt = new JTextField(10);
	private JPanel ticketPnl = new JPanel(new GridBagLayout());
	private JPanel paymentPnl = new JPanel(new GridBagLayout());
	private JLabel totalPaymentLbl = new JLabel("");
	private JLabel paymentDueLbl = new JLabel("");
	private JPanel paymentCardsPnl = new JPanel(new CardLayout());
	private JPanel noPaymentPnl = new JPanel(new GridBagLayout());
	private JTextField cashTxt = new JTextField(4);
	private JTextField nameTxt = new JTextField(10);
	private JTextField addressTxt = new JTextField(10);
	private JTextField creditCardNoTxt = new JTextField(10);
	private JTextField securityCodeTxt = new JTextField(10);
	private JTextField expDateTxt = new JTextField(10);
	private JTextField name2Txt = new JTextField(10);
	private JTextField licenseTxt = new JTextField(10);
	private double totalCharge;
	private double amountDue;
	private int ticketNumber;
	private JButton backBtn = new JButton("Go Back");

	private static ExitParkingUI instance = null;

	private ExitParkingUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static ExitParkingUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new ExitParkingUI(mainUI, mainController);
		}
		return instance;
	}

	public void setupUI() {
		// Parking Rates Panel
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] {"Hours", "Rate"});		
		parkingRatesTbl.setModel(parkingRatesModel);
		mainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);
		
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);
				
		// Enter Ticket Panel
		JLabel emptyLbl1 = new JLabel("  ");
		JLabel enterTicketLbl = new JLabel("Enter Ticket Number: ");
		JButton submitTicket = new JButton("Submit");
		JButton lostTicket = new JButton("Lost");
		mainUI.addGridBagComponent(ticketPnl, emptyLbl1, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(ticketPnl, enterTicketLbl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(ticketPnl, ticketTxt, GridBagConstraints.BOTH, 1, 1);
		mainUI.addGridBagComponent(ticketPnl, submitTicket, GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(ticketPnl, lostTicket, GridBagConstraints.BOTH, 1, 2);
		submitTicket.addActionListener(new ExitParkingUIListener());
		lostTicket.addActionListener(new ExitParkingUIListener());

		// Payment Panel
		JLabel emptyLbl2 = new JLabel("  ");
		JLabel paymentLbl = new JLabel("Payment Type: ");
		String paymentTypes[] = { "Cash", "Credit" };
		JComboBox paymentComboBox = new JComboBox(paymentTypes);
		paymentComboBox.setEditable(false);
		paymentComboBox.addItemListener(new PaymentItemListener());

		// Create the payment "cards".
		JPanel payment1 = new JPanel(new GridBagLayout());
		mainUI.addGridBagComponent(payment1, new JLabel("Enter cash: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(payment1, cashTxt, GridBagConstraints.BOTH, 1, 0);
		JButton submitCashBtn = new JButton("Submit Cash");
		submitCashBtn.addActionListener(new ExitParkingUIListener());
		mainUI.addGridBagComponent(payment1, submitCashBtn, GridBagConstraints.BOTH, 0, 1, 2, 1);

		JPanel payment2 = new JPanel(new GridBagLayout());
		mainUI.addGridBagComponent(payment2, new JLabel("Name: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(payment2, nameTxt, GridBagConstraints.BOTH, 1, 0);
		mainUI.addGridBagComponent(payment2, new JLabel("Address: "), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(payment2, addressTxt, GridBagConstraints.BOTH, 1, 1);
		mainUI.addGridBagComponent(payment2, new JLabel("Credit Card No. : "), GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(payment2, creditCardNoTxt, GridBagConstraints.BOTH, 1, 2);
		mainUI.addGridBagComponent(payment2, new JLabel("Security Code : "), GridBagConstraints.BOTH, 0, 3);
		mainUI.addGridBagComponent(payment2, securityCodeTxt, GridBagConstraints.BOTH, 1, 3);
		mainUI.addGridBagComponent(payment2, new JLabel("Expiration Date: "), GridBagConstraints.BOTH, 0, 4);
		expDateTxt.setText("mm-yyyy");
		mainUI.addGridBagComponent(payment2, expDateTxt, GridBagConstraints.BOTH, 1, 4);
		JButton submitCreditBtn = new JButton("Submit Credit Details");
		submitCreditBtn.addActionListener(new ExitParkingUIListener());
		mainUI.addGridBagComponent(payment2, submitCreditBtn, GridBagConstraints.BOTH, 0, 5, 2, 1);

		paymentCardsPnl.add(payment1, "Cash");
		paymentCardsPnl.add(payment2, "Credit");

		JButton noPaymentBtn = new JButton("No Payment");
		noPaymentBtn.addActionListener(new ExitParkingUIListener());

		mainUI.addGridBagComponent(paymentPnl, emptyLbl2, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(paymentPnl, totalPaymentLbl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(paymentPnl, paymentDueLbl, GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(paymentPnl, paymentLbl, GridBagConstraints.BOTH, 0, 3);
		mainUI.addGridBagComponent(paymentPnl, paymentComboBox, GridBagConstraints.BOTH, 1, 3);
		mainUI.addGridBagComponent(paymentPnl, paymentCardsPnl, GridBagConstraints.BOTH, 0, 4, 2, 1);
		mainUI.addGridBagComponent(paymentPnl, noPaymentBtn, GridBagConstraints.NONE, 0, 5, 2, 1);
		paymentPnl.setVisible(false);

		// No Payment Exception Panel
		mainUI.addGridBagComponent(noPaymentPnl, new JLabel("Name: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(noPaymentPnl, name2Txt, GridBagConstraints.BOTH, 1, 0);
		mainUI.addGridBagComponent(noPaymentPnl, new JLabel("License: "), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(noPaymentPnl, licenseTxt, GridBagConstraints.BOTH, 1, 1);
		JButton submitExceptionBtn = new JButton("Submit Exception");
		submitExceptionBtn.addActionListener(new ExitParkingUIListener());
		mainUI.addGridBagComponent(noPaymentPnl, submitExceptionBtn, GridBagConstraints.BOTH, 0, 2, 2, 1);
		noPaymentPnl.setVisible(false);

		// Back button
		backBtn.addActionListener(new ExitParkingUIListener());

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, ticketPnl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, paymentPnl, GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(mainContentPnl, noPaymentPnl, GridBagConstraints.BOTH, 0, 3);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 3);

		mainContentPnl.setVisible(false);
		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private void resetUI() {
		ticketTxt.setText("");
		cashTxt.setText("");
		nameTxt.setText("");
		addressTxt.setText("");
		creditCardNoTxt.setText("");
		securityCodeTxt.setText("");
		expDateTxt.setText("mm-yyyy");
		name2Txt.setText("");
		licenseTxt.setText("");
		parkingRatesPnl.setVisible(true);
		ticketPnl.setVisible(true);
		paymentPnl.setVisible(false);
		noPaymentPnl.setVisible(false);
		backBtn.setVisible(true);
	}

	private class ExitParkingUIListener implements ActionListener {
		private ExitParkingUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Submit")) {
				String ticket = ticketTxt.getText();
				if (ticket.matches("[0-9]{6}")) {
					ticketNumber = Integer.parseInt(ticket);
					boolean isValid = mainController.parkingOpsHandler.isTicketValid(ticketNumber);
					if (isValid) {
						mainController.paymentHandler.submitTicket(ticketNumber);

						totalCharge = mainController.paymentHandler.calculateTotalPayment(ticketNumber);
						totalPaymentLbl.setText("Total Charge: " + totalCharge);
						amountDue = totalCharge;
						paymentDueLbl.setText("Amount Due: " + amountDue);

						ticketPnl.setVisible(false);
						paymentPnl.setVisible(true);
						backBtn.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(mainUI, "Invalid ticket number.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter six digit ticket number.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Lost")) {
				totalCharge = mainController.paymentHandler.getMaximumPayment();
				totalPaymentLbl.setText("Total Charge: " + totalCharge);
				amountDue = totalCharge;
				paymentDueLbl.setText("Amount Due: " + amountDue);
				ticketNumber = 0; // Means no ticket

				ticketPnl.setVisible(false);
				paymentPnl.setVisible(true);
				backBtn.setVisible(false);
			}
			if (e.getActionCommand().equals("Submit Cash")) {
				String cash = cashTxt.getText();
				if (cash.matches("[1-9]{1}[0-9]{0,3}")) {
					int cashAmount = Integer.parseInt(cash);
					if (cashAmount > amountDue) {
						mainController.paymentHandler.enterPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(mainUI, "Amount returned: " + (cashAmount - amountDue), "Payment Received",
								JOptionPane.INFORMATION_MESSAGE);
						amountDue = 0;
					}
					if (cashAmount == amountDue) {
						mainController.paymentHandler.enterPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(mainUI, "Payment received in full", "Payment Received", JOptionPane.INFORMATION_MESSAGE);
						amountDue = 0;
					}
					if (cashAmount < amountDue) {
						mainController.paymentHandler.enterPayment(ticketNumber, cashAmount);
						amountDue = amountDue - cashAmount;
					}
					paymentDueLbl.setText("Amount Due: " + amountDue);
					if (amountDue == 0) {
						mainController.parkingOpsHandler.openExitGate();
						JOptionPane.showMessageDialog(mainUI, "Exit Gate Opened. Click OK to close it.", "Exit Gate opened.",
								JOptionPane.INFORMATION_MESSAGE);
						mainController.parkingOpsHandler.closeExitGate();
						resetUI();
						mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
						mainUI.displayWelcomeMessage();
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter cash amount between 1 and 9999. No coins allowed.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Submit Credit Details")) {
				String name = nameTxt.getText();
				String address = addressTxt.getText();
				String creditCard = creditCardNoTxt.getText();
				String securityCode = securityCodeTxt.getText();
				String expDate = expDateTxt.getText();
				if (name.matches("[a-zA-Z]+") && address.matches(".+") && creditCard.matches("[1-9]{1}[0-9]{15}") && securityCode.matches("[0-9]{3}")
						&& expDate.matches("[0-9]{2}-[0-9]{4}")) {
					boolean paymentAuthorized = mainController.paymentHandler.validateCreditPayment(name, address, creditCard, securityCode, expDate);
					if (paymentAuthorized) {
						mainController.paymentHandler.enterPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(mainUI, "Payment received in full", "Payment Received", JOptionPane.INFORMATION_MESSAGE);
						amountDue = 0;
						paymentDueLbl.setText("Amount Due: " + amountDue);

						mainController.parkingOpsHandler.openExitGate();
						JOptionPane.showMessageDialog(mainUI, "Exit Gate Opened. Click OK to close it.", "Exit Gate opened.",
								JOptionPane.INFORMATION_MESSAGE);
						mainController.parkingOpsHandler.closeExitGate();
						resetUI();
						mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
						mainUI.displayWelcomeMessage();
					} else {
						JOptionPane.showMessageDialog(mainUI, "Payment not authorized. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter valid credit card details.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("No Payment")) {
				parkingRatesPnl.setVisible(false);
				ticketPnl.setVisible(false);
				paymentPnl.setVisible(false);
				noPaymentPnl.setVisible(true);
			}
			if (e.getActionCommand().equals("Submit Exception")) {
				String name = name2Txt.getText();
				String license = licenseTxt.getText();
				if (name.matches("[a-zA-Z]{1,20}") && license.matches("[a-zA-Z0-9]{1,20}")) {
					mainController.paymentHandler.enterPaymentException(name, license, amountDue);
					JOptionPane.showMessageDialog(mainUI, "Payment exception created for " + name + "\nLicense: " + license + "\nAmount: "
							+ amountDue, "Payment Exception", JOptionPane.WARNING_MESSAGE);

					mainController.parkingOpsHandler.openExitGate();
					JOptionPane.showMessageDialog(mainUI, "Exit Gate Opened. Click OK to close it.", "Exit Gate opened.",
							JOptionPane.INFORMATION_MESSAGE);
					mainController.parkingOpsHandler.closeExitGate();
					resetUI();
					mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
					mainUI.displayWelcomeMessage();
				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter valid details.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}

	private class PaymentItemListener implements ItemListener {

		private PaymentItemListener() {
		}

		public void itemStateChanged(ItemEvent evt) {
			CardLayout cl = (CardLayout) (paymentCardsPnl.getLayout());
			cl.show(paymentCardsPnl, (String) evt.getItem());
		}
	}
}