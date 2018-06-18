package distribution;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import distribution.packet.Packet;
import distribution.packet.PacketBody;
import distribution.packet.PacketHeader;
import distribution.packet.PacketType;
import infrastructure.ClientRequestHandler;

public class QueueManagerProxy implements IQueueManager{
	private String queueName;
	private ClientRequestHandler crh;

	public QueueManagerProxy(String queueName) throws UnknownHostException, IOException {
		this.queueName = queueName;
		crh = new ClientRequestHandler("localhost", 8080, false);
	}

	@Override
	public void send(String function,Map<String,String> parameters) throws UnknownHostException, IOException {
		Encryption encrypt = new Encryption();
		Marshaller marshaller = new Marshaller();
		MessageHeader messageHeader = new MessageHeader(queueName);
		String content = "content";
		if (parameters!=null){
			content = parameters.get("content");
		}
		MessageBody messageBody = new MessageBody(content);
		Message message = new Message(messageHeader, messageBody);
		PacketType packetType = getPacketType(function);
		PacketHeader packetHeader = new PacketHeader(packetType);
		PacketBody packetBody = new PacketBody(message);
		Packet packet = new Packet(packetHeader,packetBody);

		try {
			crh.send(marshaller.marshall(encrypt.encrypt(packet)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String receive() throws IOException, ClassNotFoundException {
		byte[] bytes = null;
		try {
			bytes = crh.receive();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Encryption decrypt = new Encryption();
		Marshaller marshaller = new Marshaller();
		Packet packet = (Packet) marshaller.unmarshall(bytes);
		packet = decrypt.decrypt(packet);
		PacketType packetType = packet.getHeader().getType();
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
		default:
			return null;			
		}
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
