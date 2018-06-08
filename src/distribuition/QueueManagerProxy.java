package distribution;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import infrastructure.ClientRequestHandler;

public class QueueManagerProxy {
	private String queueName;
	private ClientRequestHandler crh;
	
	public QueueManagerProxy(String queueName) throws UnknownHostException, IOException {
		this.queueName = queueName;
		crh = new ClientRequestHandler("localhost", 2121, false);
	}

	public void send(String function,Map<String,String> parameters) throws UnknownHostException, IOException, InterruptedException {
		Marshaller marshaller = new Marshaller();
		MessageHeader messageHeader = new MessageHeader(queueName);
		String content = null;
		if (parameters!=null){
			content = parameters.get("content");
		}
		MessageBody messageBody = new MessageBody(content);
		Message message = new Message(messageHeader, messageBody);
		PacketType packetType = getPacketType(function);
		PacketHeader packetHeader = new PacketHeader(packetType);
		PacketBody packetBody = new PacketBody(message);
		Packet packet = new Packet(packetHeader,packetBody);
		
		crh.send(marshaller.marshall(packet));
		
	}

	public String receive() throws IOException, ClassNotFoundException, InterruptedException {
		byte [] bytes = crh.receive();
		Marshaller marshaller = new Marshaller();
		Packet packet = marshaller.unmarshall(bytes);
		PacketType packetType = packet.getHeader().getOperation();
		Message message = packet.getBody().getMessage();
		switch(packetType){
			case PUBLISH:
				return message.getBody().getContent();
			case SUBSCRIBE:
				return message.getBody().getContent();
			case LISTALL:
				return message.getBody().getContent();
			case UNSUBSCRIBE:
				return message.getBody().getContent();
				
		}
		return null;
	}
	
	private PacketType getPacketType(String type){
		switch(type){
			case "publish":
				return PacketType.PUBLISH;
			case "subscribe":
				return PacketType.SUBSCRIBE;
			case "listAll":
				return PacketType.LISTALL;				
			case "unsubscribe":
				return PacketType.UNSUBSCRIBE;
			default:
				return PacketType.UNKNOWN;
		}
	}

}
