package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Server {

	private List<Connection> connectionList = new ArrayList<Connection>();
	private ServerSocket serverSocket;
	public ServerGUI serverGUI;
	private boolean isStopped;

	public List<Connection> getConnectionList(){
		return connectionList;
	}

	public Server(){
		try{
			isStopped = false;
			serverGUI = new ServerGUI(this);
			serverSocket = new ServerSocket(0);
			String computerName = InetAddress.getLocalHost().getHostName();
			InetAddress serverIP = InetAddress.getByName(computerName);
			serverGUI.showServerMessage("Server started: " + serverIP.getHostAddress() + ":" + serverSocket.getLocalPort());
			serverGUI.showServerMessage("Chat log:");

			while (!isStopped){
				Socket socket = serverSocket.accept();
				Connection connection = new Connection(this, socket);
				connectionList.add(connection);
			}
		} catch (SocketException se) {
			System.err.println("SocketException: closing ServerSocket while accept() blocks!");
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			closeAll();
		}
}

	private void closeAll(){
		try {
			serverSocket.close();
			connectionList.forEach(Connection::close);
			serverGUI.stop();
		} catch (Exception e){
			System.err.println("Streams was not closed!");
		}
	}

	public void stop(){
		try {
			isStopped = true;
			String computerName = InetAddress.getLocalHost().getHostName();
			InetAddress serverIP = InetAddress.getByName(computerName);
			Socket lastSocket = new Socket(serverIP.getHostAddress(), serverSocket.getLocalPort());
			lastSocket.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void kickUser(String name){
		Iterator<Connection> iter = connectionList.iterator();
		while (iter.hasNext()){
			Connection connection = iter.next();
			if (connection.name.equals(name)){
				connection.close();
				serverGUI.showServerMessage("User " + name + " has been kicked");
				return;
			}
		}
		serverGUI.showServerMessage("(!)There is no users with this name");
	}

	public void showUsers(){
		if (connectionList.isEmpty()){
			serverGUI.showServerMessage("(!)No active users");
		} else {
			serverGUI.showServerMessage("----------------");
			serverGUI.showServerMessage("Active users:");
			Iterator<Connection> iter = connectionList.iterator();
			while (iter.hasNext()){
				Connection connection = iter.next();
				serverGUI.showServerMessage("[" + connection.name + "]");
			}
			serverGUI.showServerMessage("----------------");
		}
	}
}
