/**
 * 
 */
package cs414.a5.fmaster.main.java.client.ui.exitparking;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import cs414.a5.fmaster.main.java.server.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class ExitParkingUI {
	private ExitParkingMainUI exitParkingMainUI;
	private ParkingInterface parking;
	JPanel mainContentPnl = new JPanel(new GridBagLayout());
	private JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
	public JTable parkingRatesTbl = new JTable();
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

	private ExitParkingUI(ExitParkingMainUI exitParkingMainUI, ParkingInterface parking) {
		this.exitParkingMainUI = exitParkingMainUI;
		this.parking = parking;
	}

	public static ExitParkingUI getInstance(ExitParkingMainUI exitParkingMainUI, ParkingInterface parking) {
		if (instance == null) {
			instance = new ExitParkingUI(exitParkingMainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		// Parking Rates Panel
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] { "Hours", "Rate" });
		parkingRatesTbl.setModel(parkingRatesModel);
		exitParkingMainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		exitParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		exitParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Enter Ticket Panel
		JLabel emptyLbl1 = new JLabel("  ");
		JLabel enterTicketLbl = new JLabel("Enter Ticket Number: ");
		JButton submitTicket = new JButton("Submit");
		JButton lostTicket = new JButton("Lost");
		exitParkingMainUI.addGridBagComponent(ticketPnl, emptyLbl1, GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(ticketPnl, enterTicketLbl, GridBagConstraints.BOTH, 0, 1);
		exitParkingMainUI.addGridBagComponent(ticketPnl, ticketTxt, GridBagConstraints.BOTH, 1, 1);
		exitParkingMainUI.addGridBagComponent(ticketPnl, submitTicket, GridBagConstraints.BOTH, 0, 2);
		exitParkingMainUI.addGridBagComponent(ticketPnl, lostTicket, GridBagConstraints.BOTH, 1, 2);
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
		exitParkingMainUI.addGridBagComponent(payment1, new JLabel("Enter cash: "), GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(payment1, cashTxt, GridBagConstraints.BOTH, 1, 0);
		JButton submitCashBtn = new JButton("Submit Cash");
		submitCashBtn.addActionListener(new ExitParkingUIListener());
		exitParkingMainUI.addGridBagComponent(payment1, submitCashBtn, GridBagConstraints.BOTH, 0, 1, 2, 1);

		JPanel payment2 = new JPanel(new GridBagLayout());
		exitParkingMainUI.addGridBagComponent(payment2, new JLabel("Name: "), GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(payment2, nameTxt, GridBagConstraints.BOTH, 1, 0);
		exitParkingMainUI.addGridBagComponent(payment2, new JLabel("Address: "), GridBagConstraints.BOTH, 0, 1);
		exitParkingMainUI.addGridBagComponent(payment2, addressTxt, GridBagConstraints.BOTH, 1, 1);
		exitParkingMainUI.addGridBagComponent(payment2, new JLabel("Credit Card No. : "), GridBagConstraints.BOTH, 0, 2);
		exitParkingMainUI.addGridBagComponent(payment2, creditCardNoTxt, GridBagConstraints.BOTH, 1, 2);
		exitParkingMainUI.addGridBagComponent(payment2, new JLabel("Security Code : "), GridBagConstraints.BOTH, 0, 3);
		exitParkingMainUI.addGridBagComponent(payment2, securityCodeTxt, GridBagConstraints.BOTH, 1, 3);
		exitParkingMainUI.addGridBagComponent(payment2, new JLabel("Exparkingration Date: "), GridBagConstraints.BOTH, 0, 4);
		expDateTxt.setText("mm-yyyy");
		exitParkingMainUI.addGridBagComponent(payment2, expDateTxt, GridBagConstraints.BOTH, 1, 4);
		JButton submitCreditBtn = new JButton("Submit Credit Details");
		submitCreditBtn.addActionListener(new ExitParkingUIListener());
		exitParkingMainUI.addGridBagComponent(payment2, submitCreditBtn, GridBagConstraints.BOTH, 0, 5, 2, 1);

		paymentCardsPnl.add(payment1, "Cash");
		paymentCardsPnl.add(payment2, "Credit");

		JButton noPaymentBtn = new JButton("No Payment");
		noPaymentBtn.addActionListener(new ExitParkingUIListener());

		exitParkingMainUI.addGridBagComponent(paymentPnl, emptyLbl2, GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(paymentPnl, totalPaymentLbl, GridBagConstraints.BOTH, 0, 1);
		exitParkingMainUI.addGridBagComponent(paymentPnl, paymentDueLbl, GridBagConstraints.BOTH, 0, 2);
		exitParkingMainUI.addGridBagComponent(paymentPnl, paymentLbl, GridBagConstraints.BOTH, 0, 3);
		exitParkingMainUI.addGridBagComponent(paymentPnl, paymentComboBox, GridBagConstraints.BOTH, 1, 3);
		exitParkingMainUI.addGridBagComponent(paymentPnl, paymentCardsPnl, GridBagConstraints.BOTH, 0, 4, 2, 1);
		exitParkingMainUI.addGridBagComponent(paymentPnl, noPaymentBtn, GridBagConstraints.NONE, 0, 5, 2, 1);
		paymentPnl.setVisible(false);

		// No Payment Exception Panel
		exitParkingMainUI.addGridBagComponent(noPaymentPnl, new JLabel("Name: "), GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(noPaymentPnl, name2Txt, GridBagConstraints.BOTH, 1, 0);
		exitParkingMainUI.addGridBagComponent(noPaymentPnl, new JLabel("License: "), GridBagConstraints.BOTH, 0, 1);
		exitParkingMainUI.addGridBagComponent(noPaymentPnl, licenseTxt, GridBagConstraints.BOTH, 1, 1);
		JButton submitExceptionBtn = new JButton("Submit Exception");
		submitExceptionBtn.addActionListener(new ExitParkingUIListener());
		exitParkingMainUI.addGridBagComponent(noPaymentPnl, submitExceptionBtn, GridBagConstraints.BOTH, 0, 2, 2, 1);
		noPaymentPnl.setVisible(false);

		// Back button
		backBtn.addActionListener(new ExitParkingUIListener());

		// Main Content Panel
		exitParkingMainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(mainContentPnl, ticketPnl, GridBagConstraints.BOTH, 0, 1);
		exitParkingMainUI.addGridBagComponent(mainContentPnl, paymentPnl, GridBagConstraints.BOTH, 0, 2);
		exitParkingMainUI.addGridBagComponent(mainContentPnl, noPaymentPnl, GridBagConstraints.BOTH, 0, 3);
		exitParkingMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 3);

		mainContentPnl.setVisible(false);
		exitParkingMainUI.addGridBagComponent(exitParkingMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
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

				submitTicket();

			}
			if (e.getActionCommand().equals("Lost")) {

				lostTicket();

			}
			if (e.getActionCommand().equals("Submit Cash")) {

				submitCash();

			}
			if (e.getActionCommand().equals("Submit Credit Details")) {

				submitCreditDetails();

			}
			if (e.getActionCommand().equals("No Payment")) {
				noPayment();
			}
			if (e.getActionCommand().equals("Submit Exception")) {

				submitException();

			}
			if (e.getActionCommand().equals("Go Back")) {
				goBack();
			}
		}

		private void goBack() {
			resetUI();
			exitParkingMainUI.showHideContentPanel(exitParkingMainUI.mainContentPnl, mainContentPnl);
			try {
				exitParkingMainUI.updateWelcomeMessage();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private void submitException() {
			String name = name2Txt.getText();
			String license = licenseTxt.getText();
			try {
				if (name.matches("[a-zA-Z]{1,20}") && license.matches("[a-zA-Z0-9]{1,20}")) {
					parking.enterPaymentException(name, license, amountDue);
					JOptionPane.showMessageDialog(exitParkingMainUI, "Payment exception created for " + name + ".\nLicense: " + license
							+ "\nAmount: " + amountDue + "\nYou will be contacted soon for payments due.", "Payment Exception",
							JOptionPane.WARNING_MESSAGE);

					parking.openExitGate();
					JOptionPane.showMessageDialog(exitParkingMainUI, "Exit Gate Opened. Click OK to close it.", "Exit Gate opened.",
							JOptionPane.INFORMATION_MESSAGE);
					parking.closeExitGate();
					goBack();
				} else {
					JOptionPane.showMessageDialog(exitParkingMainUI, "Only 1-20 alphabets or numbers allowed for name and license number.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void noPayment() {
			parkingRatesPnl.setVisible(false);
			ticketPnl.setVisible(false);
			paymentPnl.setVisible(false);
			noPaymentPnl.setVisible(true);
		}

		private void submitCreditDetails() {
			String name = nameTxt.getText();
			String address = addressTxt.getText();
			String creditCard = creditCardNoTxt.getText();
			String securityCode = securityCodeTxt.getText();
			String expDate = expDateTxt.getText();
			try {
				if (name.matches("[a-zA-Z]+") && address.matches(".+") && creditCard.matches("[1-9]{1}[0-9]{15}") && securityCode.matches("[0-9]{3}")
						&& expDate.matches("[0-9]{2}-[0-9]{4}")) {
					boolean paymentAuthorized = parking.validateCreditPayment(name, address, creditCard, securityCode, expDate);
					if (paymentAuthorized) {
						parking.enterPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(exitParkingMainUI, "Payment received in full", "Payment Received",
								JOptionPane.INFORMATION_MESSAGE);
						amountDue = 0;
						paymentDueLbl.setText("Amount Due: " + amountDue);

						parking.openExitGate();
						JOptionPane.showMessageDialog(exitParkingMainUI, "Exit Gate Opened. Click OK to close it.", "Exit Gate opened.",
								JOptionPane.INFORMATION_MESSAGE);
						parking.closeExitGate();
						goBack();
					} else {
						JOptionPane.showMessageDialog(exitParkingMainUI, "Payment not authorized. Please try again.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane
							.showMessageDialog(exitParkingMainUI, "Enter valid credit card details:" + "\nAlphabets allowed for name."
									+ "\nCredit card number must be 16 digits." + "\nSecurity Code must be 3 digits.", "Error",
									JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void submitCash() {
			String cash = cashTxt.getText();
			try {
				if (cash.matches("[1-9]{1}[0-9]{0,3}")) {
					int cashAmount = Integer.parseInt(cash);
					if (cashAmount > amountDue) {
						parking.enterPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(exitParkingMainUI, "Amount returned: " + (cashAmount - amountDue), "Payment Received",
								JOptionPane.INFORMATION_MESSAGE);
						amountDue = 0;
					}
					if (cashAmount == amountDue) {
						parking.enterPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(exitParkingMainUI, "Payment received in full.", "Payment Received",
								JOptionPane.INFORMATION_MESSAGE);
						amountDue = 0;
					}
					if (cashAmount < amountDue) {
						parking.enterPayment(ticketNumber, cashAmount);
						amountDue = amountDue - cashAmount;
					}
					paymentDueLbl.setText("Amount Due: " + amountDue);
					if (amountDue == 0) {
						parking.openExitGate();
						JOptionPane.showMessageDialog(exitParkingMainUI, "Exit Gate Opened. Click OK to close it.", "Exit Gate opened.",
								JOptionPane.INFORMATION_MESSAGE);
						parking.closeExitGate();
						goBack();
					}
				} else {
					JOptionPane.showMessageDialog(exitParkingMainUI, "Enter cash amount between 1 and 9999. No coins allowed.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void lostTicket() {
			try {
				totalCharge = parking.getMaximumPayment();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			totalPaymentLbl.setText("Total Charge: " + totalCharge);
			amountDue = totalCharge;
			paymentDueLbl.setText("Amount Due: " + amountDue);
			ticketNumber = 0; // Means no ticket

			ticketPnl.setVisible(false);
			paymentPnl.setVisible(true);
			backBtn.setVisible(false);
		}

		private void submitTicket() {
			String ticket = ticketTxt.getText();
			try {
				if (ticket.matches("[0-9]{6}")) {
					ticketNumber = Integer.parseInt(ticket);
					boolean isValid = parking.isTicketValid(ticketNumber);
					if (isValid) {
						parking.submitTicket(ticketNumber);

						totalCharge = parking.calculateTotalPayment(ticketNumber);
						totalPaymentLbl.setText("Total Charge: " + totalCharge);
						amountDue = totalCharge;
						paymentDueLbl.setText("Amount Due: " + amountDue);

						ticketPnl.setVisible(false);
						paymentPnl.setVisible(true);
						backBtn.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(exitParkingMainUI, "Invalid ticket number. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(exitParkingMainUI, "Ticket number must be six digits.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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