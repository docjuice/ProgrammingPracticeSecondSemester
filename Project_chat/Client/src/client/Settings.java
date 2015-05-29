package client;

public class Settings {

	private String IP;
	private int port;
	private String name;

	public Settings(String IP, int port, String name){
		setIP(IP);
		setPort(port);
		setName(name);
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
