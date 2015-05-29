package client;

import common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class Network {

	private final Client client;
	private final ArrayBlockingQueue<Message> messageQueue;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean stopped;

	public Network(final Client client){
		this.client = client;
		this.messageQueue = new ArrayBlockingQueue<Message>(150);
		Settings settings = client.getSettings();
		this.stopped = false;
		if (!connect()) client.stop();
	}

	public void start(){
		startSender();
		startReceiver();
	}

	private void startSender(){
		new Thread(() -> {
			while (!isStopped()){
				try {
					Message message = messageQueue.take();
					oos.writeObject(message);
				} catch (IOException | InterruptedException e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void startReceiver(){
		while (!isStopped()){
			try {
				Message message = (Message) ois.readObject();
				client.receiveMessage(message);
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			} catch (IOException e){
				if (!isStopped()){
					client.stop();
				}
			}
		}
	}

	public boolean connect(){
		try {
			this.socket = new Socket(client.getSettings().getIP(), client.getSettings().getPort());
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
			sendInitMessage();
		} catch (IOException e){
			e.printStackTrace();
		}
		return !socket.isClosed();
	}

	public boolean reconnect(){
		try {
			socket.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return connect();
	}

	public void sendAsync(Message message){
		messageQueue.add(message);
	}

	private void sendInitMessage(){
		try {
			oos.writeObject(new Message(client.getSettings().getName(), Message.Sortes.INIT, ""));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private void sendExitMessage(){
		try {
			oos.writeObject(new Message(client.getSettings().getName(), Message.Sortes.EXIT, ""));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void startReceiving(){
		new Thread(this::start).start();
	}

	public void stop(){
		if (isStopped()) return;
		stopped = true;
		try {
			sendExitMessage();
			oos.close();
			ois.close();
			socket.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private boolean isStopped(){
		return stopped;
	}
}
