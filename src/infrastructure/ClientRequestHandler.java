package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientRequestHandler {
	private String host;
	private int port;
	private int sentMessageSize;
	private int receiveMessageSize;
	private boolean expectedReply;

	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;

	public ClientRequestHandler(String host, int port, boolean expectedReply) throws UnknownHostException, IOException {
		this.host = host;
		this.port = port;
		this.expectedReply = expectedReply;
		socket = new Socket(host,port);
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
	}

	public void send(byte [] msg) throws IOException  {
		sentMessageSize = msg.length;
		out.writeInt(sentMessageSize);
		out.write(msg,0,sentMessageSize);
		out.flush();

	}

	public byte[] receive() throws IOException{
		byte [] msg = null;

		receiveMessageSize = in.readInt();
		msg = new byte[receiveMessageSize];
		in.read(msg,0,receiveMessageSize);



		//socket.close();
		//out.close();
		//in.close();


		return msg;
	}

	public boolean isExpectedReply(){
		return expectedReply;
	}

}
