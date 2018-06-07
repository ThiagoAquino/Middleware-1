package infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientRequestHandler {
	private String host;
	private int port;
	private boolean expectedReply;

	private int sentMessageSize;
	private int receiveMessageSize;

	private Socket connectionSocket;
	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromServer;

	public ClientRequestHandler(String host, int port, boolean expectedReply) throws IOException {
		this.host = host;
		this.port = port;
		this.expectedReply = expectedReply;
		this.connectionSocket = new Socket(host,port);
		this.outToServer = new ObjectOutputStream(this.connectionSocket.getOutputStream());
		this.inFromServer = new ObjectInputStream(this.connectionSocket.getInputStream());

	}

	public void send(byte [] message) throws IOException, InterruptedException {
		sentMessageSize = message.length;
		outToServer.writeInt(sentMessageSize);
		outToServer.write(message,0,sentMessageSize);
		outToServer.flush();
	}

	public byte [] receive () throws IOException, InterruptedException {
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
