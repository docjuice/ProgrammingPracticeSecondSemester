package server;

import common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

public class Connection {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private final Socket socket;
	private Server server;
	ServerGUI serverGUI;
	public String name = "";

	public Connection(Server server, Socket socket){
		this.serverGUI = server.serverGUI;
		this.socket = socket;
		try{
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e){
			e.printStackTrace();
			close();
		}
		this.server = server;
		(new Thread(this::run)).start();
	}

	private void sendInitMessage() throws IOException, ClassNotFoundException{
		name = ((Message) ois.readObject()).getSender();
		Iterator<Connection> iter = server.getConnectionList().iterator();
		serverGUI.showServerMessage(name + " has joined us!");
		while (iter.hasNext()){
			Connection currConnection = iter.next();
			if (!currConnection.socket.isClosed() && !currConnection.equals(this)){
				currConnection.oos.writeObject(new Message("SERVER", Message.Sortes.MSG, name + " has joined us!"));
			} else if (!currConnection.socket.isClosed()){
				currConnection.oos.writeObject(new Message("SERVER", Message.Sortes.INIT, "Welcome!"));
			}
		}
	}

	private void sendMessages() throws IOException, ClassNotFoundException{
		Message msg;
		while (true){
			msg = (Message) ois.readObject();
			if (msg.getSort() == Message.Sortes.EXIT){
				break;
			}
			Iterator<Connection> iter = server.getConnectionList().iterator();
			serverGUI.showServerMessage(msg.toString());
			while (iter.hasNext()){
				Connection currConnection = iter.next();
				if (!currConnection.socket.isClosed() && !currConnection.equals(this)){
					currConnection.oos.writeObject(msg);
				}
			}
		}
	}

	private void sendExitMessage() throws IOException{
		Iterator<Connection> iter = server.getConnectionList().iterator();
		serverGUI.showServerMessage(name + " has left us");
		while (iter.hasNext()){
			Connection currConnection = iter.next();
			if (!currConnection.socket.isClosed() && !currConnection.equals(this)){
				currConnection.oos.writeObject(new Message("SERVER", Message.Sortes.MSG, name + " has left us"));
			}
		}
	}

	private void run(){
		try{
			sendInitMessage();
			sendMessages();
			sendExitMessage();
		} catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void close(){
		try{
			oos.close();
			ois.close();
			socket.close();
			server.getConnectionList().remove(this);
		} catch (Exception e){
			System.err.println("Connection was not closed!");
		}
	}
}
