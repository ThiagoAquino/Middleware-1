package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import application.Server;
import distribution.QueueServer;

public class ServerRequestHandler {
	private int sentMessageSize;
	private ServerSocket serverSocket =  null;
	private Socket socket = null;
	
	public ServerRequestHandler(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}
	
	public void setSocket(Socket s) {
		this.socket = s;
	}
	
	public void setServerSocket(ServerSocket serverSocket){
		this.serverSocket = serverSocket;
	}
	
	
	
	public void send(byte [] msg) throws UnknownHostException, IOException{
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());
		sentMessageSize = msg.length;
		out.writeInt(sentMessageSize);
		out.write(msg,0,sentMessageSize);
		out.flush();
		
		//socket.close();
		//out.close();
		//in.close();
	}
	
	public void receive() throws IOException, ClassNotFoundException{
		byte [] msg = null;
		socket = serverSocket.accept();
		if(serverSocket != null){
			new ListenClient(socket).start();
		}
	}
	
	
}