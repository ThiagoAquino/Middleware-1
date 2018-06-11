package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import application.Server;

public class ListenClient extends Thread {
	private Socket connectionSocket;
	private DataOutputStream outToClient;
	private DataInputStream inFromClient;
	private int messageSize;

	public ListenClient(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public void run () {
		byte [] message = null;
		try {
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			inFromClient = new DataInputStream(connectionSocket.getInputStream());
			messageSize = inFromClient.readInt();
			message = new byte[messageSize];
			inFromClient.read(message,0,messageSize);


			Server.getQueueServer().receive(message, this.connectionSocket);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}	
}
