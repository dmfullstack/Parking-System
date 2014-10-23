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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.controller.MainController;
import cs414.fmaster.parking.controller.ParkingRate;
import cs414.fmaster.parking.controller.ParkingOperationsController;

/**
 * @author MasterF
 * 
 */
public class ExitParkingUI {
	MainUI mainUI;
	MainController mainController;
	JPanel mainContentPnl = new JPanel();
	JTextField ticketTxt = new JTextField(10);
	JPanel ticketPanel = new JPanel();
	JPanel paymentPanel = new JPanel();
	JLabel totalPayment = new JLabel("");
	JLabel paymentDue = new JLabel("");
	JPanel paymentCardsPnl = new JPanel(new CardLayout());
	JTextField cashTxt = new JTextField(4);
	JTextField creditCardNoTxt = new JTextField(10);
	JTextField expDateTxt = new JTextField(10);
	double totalCharge;
	double amountDue;
	int ticketNumber;
	JButton backButton = new JButton("Go Back");

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
		mainContentPnl.setLayout(new GridBagLayout());

		JPanel parkingRatesPanel = new JPanel();
		parkingRatesPanel.setLayout(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		mainUI.addGridBagComponent(parkingRatesPanel, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);

		List<ParkingRate> parkingRates = new ArrayList<ParkingRate>();
		parkingRates = mainController.parkingController.getParkingRates();
		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] { "Hours", "Rate" });
		for (ParkingRate pr : parkingRates) {
			model.addRow(new Object[] { pr.getHours(), pr.getRate() });
		}
		JTable parkingRatesTbl = new JTable();
		parkingRatesTbl.setModel(model);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);

		mainUI.addGridBagComponent(parkingRatesPanel, parkingRatesTbl, GridBagConstraints.BOTH, 0, 1);

		ticketPanel.setLayout(new GridBagLayout());
		JLabel emptyLbl1 = new JLabel("  ");
		JLabel enterTicketLbl = new JLabel("Enter Ticket Number: ");
		JButton submitTicket = new JButton("Submit Ticket");
		mainUI.addGridBagComponent(ticketPanel, emptyLbl1, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(ticketPanel, enterTicketLbl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(ticketPanel, ticketTxt, GridBagConstraints.BOTH, 1, 1);
		mainUI.addGridBagComponent(ticketPanel, submitTicket, GridBagConstraints.BOTH, 0, 2, 2, 1);
		submitTicket.addActionListener(new ExitParkingUIListener());

		paymentPanel.setLayout(new GridBagLayout());
		JLabel emptyLbl2 = new JLabel("  ");
		JLabel paymentLbl = new JLabel("Payment Type: ");
		String paymentTypes[] = { "Cash", "Credit" };
		JComboBox paymentComboBox = new JComboBox(paymentTypes);

		paymentComboBox.setEditable(false);
		paymentComboBox.addItemListener(new PaymentItemListener());

		// Create the "cards".
		JPanel payment1 = new JPanel(new GridBagLayout());
		mainUI.addGridBagComponent(payment1, new JLabel("Enter cash: "), GridBagConstraints.BOTH, 0, 0);

		mainUI.addGridBagComponent(payment1, cashTxt, GridBagConstraints.BOTH, 1, 0);
		JButton submitCashBtn = new JButton("Submit Cash");
		submitCashBtn.addActionListener(new ExitParkingUIListener());
		mainUI.addGridBagComponent(payment1, submitCashBtn, GridBagConstraints.BOTH, 0, 1, 2, 1);

		JPanel payment2 = new JPanel(new GridBagLayout());
		mainUI.addGridBagComponent(payment2, new JLabel("Name: "), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(payment2, new JTextField(10), GridBagConstraints.BOTH, 1, 0);
		mainUI.addGridBagComponent(payment2, new JLabel("Address: "), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(payment2, new JTextField(10), GridBagConstraints.BOTH, 1, 1);
		mainUI.addGridBagComponent(payment2, new JLabel("Credit Card No. : "), GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(payment2, creditCardNoTxt, GridBagConstraints.BOTH, 1, 2);
		mainUI.addGridBagComponent(payment2, new JLabel("Expiration Date: "), GridBagConstraints.BOTH, 0, 3);
		expDateTxt.setText("mm/yyyy");
		mainUI.addGridBagComponent(payment2, expDateTxt, GridBagConstraints.BOTH, 1, 3);
		JButton submitCreditBtn = new JButton("Submit Credit Details");
		submitCreditBtn.addActionListener(new ExitParkingUIListener());
		mainUI.addGridBagComponent(payment2, submitCreditBtn, GridBagConstraints.BOTH, 0, 4, 2, 1);

		paymentCardsPnl.add(payment1, "Cash");
		paymentCardsPnl.add(payment2, "Credit");

		mainUI.addGridBagComponent(paymentPanel, emptyLbl2, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(paymentPanel, totalPayment, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(paymentPanel, paymentDue, GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(paymentPanel, paymentLbl, GridBagConstraints.BOTH, 0, 3);
		mainUI.addGridBagComponent(paymentPanel, paymentComboBox, GridBagConstraints.BOTH, 1, 3);
		mainUI.addGridBagComponent(paymentPanel, paymentCardsPnl, GridBagConstraints.BOTH, 0, 4, 2, 1);
		paymentPanel.setVisible(false);

		mainUI.addGridBagComponent(mainContentPnl, parkingRatesPanel, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, ticketPanel, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, paymentPanel, GridBagConstraints.BOTH, 0, 2);
		mainContentPnl.setVisible(false);
		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ExitParkingUIListener implements ActionListener {
		ExitParkingUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Submit Ticket")) {
				String ticket = ticketTxt.getText();
				if (ticket.matches("[0-9]{6}")) {
					ticketNumber = Integer.parseInt(ticket);
					boolean isValid = mainController.parkingController.isTicketValid(ticketNumber);
					if (isValid) {
						totalCharge = mainController.paymentController.calculateTotalPayment(ticketNumber);
						ticketPanel.setVisible(false);
						paymentPanel.setVisible(true);
						totalPayment.setText("Total Charge: " + totalCharge);
						amountDue = totalCharge;
						paymentDue.setText("Amount Due: " + amountDue);
					} else {
						JOptionPane.showMessageDialog(mainUI, "Invalid ticket number.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter six digit ticket number.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Submit Cash")) {
				String cash = cashTxt.getText();
				if (cash.matches("[1-9]{1}[0-9]{0,3}")) {
					int cashAmount = Integer.parseInt(cash);
					if (cashAmount > amountDue) {
						mainController.paymentController.createPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(mainUI, "Amount returned: " + (cashAmount - amountDue), "Payment Received",
								JOptionPane.INFORMATION_MESSAGE);
					}
					if (cashAmount == amountDue) {
						mainController.paymentController.createPayment(ticketNumber, amountDue);
						JOptionPane.showMessageDialog(mainUI, "Payment received in full", "Payment Received", JOptionPane.INFORMATION_MESSAGE);
					}
					if (cashAmount < amountDue) {
						mainController.paymentController.createPayment(ticketNumber, cashAmount);
						amountDue = amountDue - cashAmount;
						paymentDue.setText("Amount Due: " + amountDue);
					}

				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter cash amount between 1 and 9999. No coins allowed.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}

			if (e.getActionCommand().equals("Submit Credit Details")) {
				String creditCard = creditCardNoTxt.getText();
				if (!creditCard.matches("[1-9]{1}[0-9]{15}")) {
					JOptionPane.showMessageDialog(mainUI, "Enter valid credit card number.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {

				}

			}

			if (e.getActionCommand().equals("Go Back")) {
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}

	private class PaymentItemListener implements ItemListener {

		PaymentItemListener() {
		}

		public void itemStateChanged(ItemEvent evt) {
			CardLayout cl = (CardLayout) (paymentCardsPnl.getLayout());
			cl.show(paymentCardsPnl, (String) evt.getItem());
		}
	}
}