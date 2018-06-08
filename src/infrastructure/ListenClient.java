package infrastructure;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import application.Server;

public class ListenClient extends Thread {
	private Socket connectionSocket;
	private ObjectOutputStream outToClient;
	private ObjectInputStream inFromClient;
	private int messageSize;
	
	public ListenClient(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}
	
	public void run () {
		byte [] message = null;
		try {
			outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
			inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
			messageSize = inFromClient.readInt();
			message = new byte[messageSize];
			inFromClient.read(message,0,messageSize);
		
			Server.getQueueServer().receive(message, this.connectionSocket);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}	
}
