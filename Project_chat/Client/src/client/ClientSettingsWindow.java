package client;

import javax.swing.*;

public class ClientSettingsWindow extends JFrame {
	private JPanel panel;
	private JLabel IPLabel;
	private JLabel portLabel;
	private JLabel nameLabel;
	private JTextField IPTextField;
	private JTextField portTextField;
	private JTextField nameTextField;
	private JButton okButton;
	private JFrame frame;

	public ClientSettingsWindow(){
		frame = new JFrame("Enter settings");
		frame.setContentPane(panel);
		frame.setSize(300, 150);
		frame.setLocation(400, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okButton.addActionListener(e -> {
			frame.dispose();
			if (portTextField.getText().equals("")) portTextField.setText("0");
			new Client(IPTextField.getText(), Integer.valueOf(portTextField.getText()), nameTextField.getText());
		});
	}

	public void showErrorDialog(){
		JOptionPane.showMessageDialog(
				frame,
				"Check that IP, port and nickname have form\n" +
						"IP: [0..255].[0..255].[0..255].[0..255]\n" +
						"Port: [1..65535]\n" +
						"Nickname shouldn't contain ' ', should have at least one symbol and shouldn't be empty",
				"Settings Error",
				JOptionPane.WARNING_MESSAGE
		);
	}
}
