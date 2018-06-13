package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRequestHandler {

	private ServerSocket serverSocket;
	private Socket connectionSocket;

	private DataOutputStream outToClient;
	private DataInputStream inFromClient;


	private ListenClient thread;

	public ServerRequestHandler(final int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	public void send(final byte[] message) throws IOException {

		inFromClient = new DataInputStream(connectionSocket.getInputStream());
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		outToClient.writeInt(message.length);
		outToClient.write(message, 0, message.length);
		outToClient.flush();

	}

	public void receive() throws IOException, ClassNotFoundException {
		connectionSocket = serverSocket.accept();
		if(connectionSocket != null && serverSocket != null) {
			thread = new ListenClient(connectionSocket);
			thread.start();
		}
	}


	public void close() throws IOException {
		this.connectionSocket.close();
		this.outToClient.close();
		this.inFromClient.close();
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Socket getConnectionSocket() {
		return connectionSocket;
	}

	public void setConnectionSocket(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public void setOutToClient(DataOutputStream outToClient) {
		this.outToClient = outToClient;
	}

	public DataInputStream getInFromClient() {
		return inFromClient;
	}

	public void setInFromClient(DataInputStream inFromClient) {
		this.inFromClient = inFromClient;
	}

	public DataOutputStream getOutToClient() {
		return outToClient;
	}

}
