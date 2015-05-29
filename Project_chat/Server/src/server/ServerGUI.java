package server;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerGUI {

	private JPanel panel;
	private JTextField commandTextField;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private final Server server;
	private JFrame frame;

	public ServerGUI(final Server server){
		this.server = server;
		frame = new JFrame("Server");

		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.setSize(400, 300);
		frame.setLocation(300, 100);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		textArea.setEditable(false);
		textArea.setText("");
		commandTextField.setText("");
		SwingUtilities.invokeLater(this::runGUI);
}

	private void runGUI(){
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (server.getConnectionList().isEmpty()) {
					server.stop();
				} else {
					showServerMessage("(!)Can't stop server while there are clients in chat");
				}
			}
		});
		commandTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				String command = commandTextField.getText();
				if (KeyEvent.getKeyText(keyCode).equals("Enter") && !commandTextField.equals("")) {
					String[] str = command.split(" ");
					if (str[0].equals("/kick")) {
						server.kickUser(str[1]);
					}
					if (str[0].equals("/showusers")) {
						server.showUsers();
					}
					if (str[0].equals("/help")) {
						showServerMessage("Available commands:\n-/help\n-/kick\n-/showusers");
					}
					////////////////////////////////
					commandTextField.setText("");
				}
			}
		});
	}

	public void stop(){
		frame.dispose();
	}

	public void showServerMessage(String msg){
		SwingUtilities.invokeLater(() -> textArea.setText(textArea.getText() + msg + "\n"));
	}
}
