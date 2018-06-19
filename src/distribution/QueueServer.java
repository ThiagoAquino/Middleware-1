package distribution;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import application.Server;
import distribution.message.Message;
import distribution.message.MessageBody;
import distribution.message.MessageHeader;
import distribution.packet.Packet;
import distribution.packet.PacketBody;
import distribution.packet.PacketHeader;
import distribution.packet.PacketType;

public class QueueServer { 


	public String receive (byte[] bytes, Socket socket) throws ClassNotFoundException, IOException{

		Marshaller marshaller = new Marshaller();
		Packet packet = marshaller.unmarshall(bytes);
		PacketType packetType = packet.getHeader().getType();
		Message message = packet.getBody().getMessage();
		String destinationQueue = message.getHeader().getDestinationQueue();
		QueueManager queueManager = QueueManager.getInstance();
		Map<String, Queue> queuesHM = queueManager.getQueues();

		switch(packetType) {
		case PUBLISH:
			try {
				Queue queue;
				if (queuesHM.containsKey(destinationQueue)) {
					queue = queuesHM.get(destinationQueue);

				} else {
					queue = new Queue();
				}
				queue.enqueue(message);
				queue.initNewListOfUsers();

				queuesHM.put(destinationQueue, queue);
				System.out.println("Message '"+message.getBody().getContent() +"' published at topic " +destinationQueue);
				send(packetType, "Message published!");

				//send message to all subscribers
				this.broadcastMessageToAllSubscribers(destinationQueue);

			} catch(Exception e) {
				System.out.println(e.getMessage());
				send(packetType, "Something went wrong!");	
			}
			break;
		case SUBSCRIBE:
			try {	
				if (queuesHM.containsKey(destinationQueue)) {
					ArrayList<Socket> socketList = QueueManager.getInstance().subscribersQueue.get(destinationQueue);

					if (socketList == null) {
						socketList = new ArrayList<Socket>();
					}

					socketList.add(socket);
					QueueManager.getInstance().subscribersQueue.put(destinationQueue, socketList);
					this.broadcastMessageToAllSubscribers(destinationQueue);
				} else {
					System.out.println("Unavaliable Topic!");
					send(packetType, "This topic is unavaliable.");
				}

			} catch(Exception e) {
				System.out.println(e.getMessage());
				send(packetType, "Something went wrong!");
			}

			break;
		case LISTALL:
			String queueString = queueManager.listQueues();
			send(packetType,queueString);
			break;

		case UNSUBSCRIBE:
			try {
				if (queuesHM.containsKey(destinationQueue)) {
					ArrayList<Socket> socketList = QueueManager.getInstance().subscribersQueue.get(destinationQueue);

					if (socketList != null && socketList.contains(socket)) {

						int index = socketList.indexOf(socket);
						socketList.remove(index);
						send(packetType, "Unsubscribed Topic!");
					}

				} else {
					System.out.println("Unavaliable Topic!");
					send(packetType, "This topic is unavaliable.");
				}
			} catch( Exception e) {
				System.out.println(e.getMessage());
				send(packetType, "Something went wrong!");
			}

			break;
		default:
			break;
		}
		
		return "";
	}

	private void send(PacketType packetType, String content) throws IOException {
		Marshaller marshaller = new Marshaller();
		MessageHeader messageHeader = new MessageHeader(null);
		MessageBody messageBody = new MessageBody(content);
		Message message = new Message(messageHeader, messageBody);
		PacketType type = packetType;
		PacketHeader packetHeader = new PacketHeader(type);
		PacketBody packetBody = new PacketBody(message);
		Packet packet = new Packet(packetHeader,packetBody);
		Server.getSRH().send(marshaller.marshall(packet));
	}

	private void broadcastMessageToAllSubscribers(String topicName) {
		Map<String, Queue> topicToMessageQueue = QueueManager.getInstance().getQueues();
		Map<String, ArrayList<Socket>> topicToSubscribersConnection = QueueManager.getInstance().getSubscribersQueue();


		Queue messageQueue = topicToMessageQueue.get(topicName);
		ArrayList<Socket> connections = topicToSubscribersConnection.get(topicName);

		System.out.println("connections attached to topic " + connections.size());
		for (int i=0; i < connections.size(); i++) {
			Server.getSRH().setConnectionSocket(connections.get(i));

			for(int j=0; j < messageQueue.queueSize(); j++) {
				try {
					ArrayList<Socket> userForEachMessage = messageQueue.getUserForMessageAtIndex(j);

					if (!userForEachMessage.contains(connections.get(i))) {
						this.send(PacketType.SUBSCRIBE, messageQueue.getQueue().get(j).getBody().getContent());
						messageQueue.addUserToMessageAtIndex(connections.get(i), j);
					}

				} catch(IOException e) {
					e.getStackTrace();
				}

			}
		}

	}

	public void sentMessageThread(){
		new Thread(){
			public void run(){
				try {
					while(Server.getShouldContinue()){
						sleep(1000);
						sentMessage();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void sentMessage(){
		Map<String, Queue> mapQueue = QueueManager.getInstance().getQueues();
		Set<String> keys = mapQueue.keySet();
		for (String chave : keys) {
			ArrayList<Message> queue = mapQueue.get(chave).getQueue();
			for(int i = 0; i < queue.size();i++){
				Queue q = mapQueue.get(chave);
				q.dequeue(i);
			}
		}
	}
}
