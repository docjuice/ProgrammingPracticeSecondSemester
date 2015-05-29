package client;

import common.Message;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientGUI {
	private JPanel panel;
	private JTextArea chatTextArea;
	private JScrollPane scrollPane;
	private JTextField messageField;
	private final Client client;
	private JFrame frame;

	public ClientGUI(final Client client){
		this.client = client;
		frame = new JFrame("Nickname: " + client.getSettings().getName());
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.setSize(400, 300);
		frame.setLocation(300, 100);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		chatTextArea.setEditable(false);
		chatTextArea.setText("");
		messageField.setText("");
		SwingUtilities.invokeLater(this::runGUI);
	}

	private void runGUI(){
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				client.stop();
			}
		});
		messageField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				String text = messageField.getText();
				if (KeyEvent.getKeyText(keyCode).equals("Enter") && !text.equals("")) {
					Message message = new Message(client.getSettings().getName(), Message.Sortes.MSG, text);
					chatTextArea.setText(chatTextArea.getText() + message.toString() + "\n");
					messageField.setText("");
					client.sendMessage(message);
				}
			}
		});
	}

	public void showNewMessage(Message message) {
		SwingUtilities.invokeLater(() -> chatTextArea.setText(chatTextArea.getText() + message.toString() + "\n"));
	}

	public void stop(){
		frame.dispose();
	}
}
