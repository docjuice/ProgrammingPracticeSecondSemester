package client;

import common.Message;

public class Client {

	private Settings settings;
	private boolean stopped;
	private ClientGUI clientGUI;
	private Network network;

	public Client(String IP, int port, String name){
		if (!isIPCorrect(IP) || !isPortCorrect(port) || !isNameCorrect(name)){
			new ClientSettingsWindow().showErrorDialog();
		} else{
			this.settings = new Settings(IP, port, name);
			this.network = new Network(this);
			this.clientGUI = new ClientGUI(this);
			this.stopped = false;
			this.network.startReceiving();
		}
	}

	public void sendMessage(Message message) {
		network.sendAsync(message);
	}

	public void receiveMessage(Message message){
		clientGUI.showNewMessage(message);
	}

	public Settings getSettings() {
		return settings;
	}

	public boolean isStopped(){
		return stopped;
	}

	public void stop(){
		if (isStopped()) return;
		this.stopped = true;
		network.stop();
		clientGUI.stop();
	}

	private boolean isIPCorrect(String IP){
		if (IP.equals("")) return false;
		IPValidator validator = new IPValidator();
		return validator.validate(IP);
	}

	private boolean isPortCorrect(int port){
		return (port >= 1 && port <= 65535);
	}

	private boolean isNameCorrect(String name){
		if (name == null || name.equals("")) return false;
		return (name.split(" ").length == 1);
	}
}
