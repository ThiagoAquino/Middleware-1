package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientRequestHandler {
	private String host;
	private int port;
	private boolean expectedReply;

	private int sentMessageSize;
	private int receiveMessageSize;

	private Socket connectionSocket;
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;

	public ClientRequestHandler(String host, int port, boolean expectedReply) throws IOException {
		this.host = host;
		this.port = port;
		this.expectedReply = expectedReply;
		this.connectionSocket = new Socket(host,port);
		this.outToServer = new DataOutputStream(this.connectionSocket.getOutputStream());
		this.inFromServer = new DataInputStream(this.connectionSocket.getInputStream());

	}

	public void send(byte [] message) throws IOException, InterruptedException {
		sentMessageSize = message.length;
		outToServer.writeInt(sentMessageSize);
		outToServer.write(message,0,sentMessageSize);
		outToServer.flush();
	}

	public byte [] receive () throws IOException, InterruptedException, ClassNotFoundException {
		byte [] message = null;
		
		receiveMessageSize = inFromServer.readInt();
		message = new byte[receiveMessageSize];
		inFromServer.read(message,0,receiveMessageSize);

		return message;
	}

	public boolean isExpectedReply() {
		return expectedReply;
	}
}
