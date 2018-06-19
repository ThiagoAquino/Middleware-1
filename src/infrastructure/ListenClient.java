package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import application.Server;

public class ListenClient extends Thread{
	private Socket connectionSocket;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	private int receiveMessageSize;

	public ListenClient(Socket socket){
		this.connectionSocket = socket;
	}

	public void run(){
		byte [] msg = null;
		try{
			out = new DataOutputStream(connectionSocket.getOutputStream());
			in = new DataInputStream(connectionSocket.getInputStream());
			receiveMessageSize = in.readInt();
			msg = new byte[receiveMessageSize];
			in.read(msg,0,receiveMessageSize);


			Server.getQueueServer().receive(msg, this.connectionSocket);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

}
