package distribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.net.Socket;

public class QueueManager {
	private static QueueManager queueManager;
	private String host;
	private int port;
	Map <String,Queue> queues = new HashMap<String,Queue>();
	Map <String, ArrayList<Socket>> subscribersQueue = new HashMap<String, ArrayList<Socket>>();

	public static QueueManager getInstance() {
		if (queueManager == null) {
			queueManager = new QueueManager("127.0.0.1", 8080);
		}
		return queueManager;
	}

	public QueueManager(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Map<String, Queue> getQueues() {
		return queues;
	}

	public void setQueues(Map<String, Queue> queues) {
		this.queues = queues;
	}

	public Map<String, ArrayList<Socket>> getSubscribersQueue() {
		return this.subscribersQueue;
	}

	public void setSubscribersQueue(Map<String, ArrayList<Socket>> subscribersQueues) {
		this.subscribersQueue = subscribersQueues;
	}

	public String listQueues(){
		String queueString = "Lista de Tópicos Disponíveis \n";
		Set<String> keyset = queues.keySet();
		if (keyset.size() == 0){
			queueString = "Nenhum tópico disponível\n";
		}else{
			queueString = "Lista de Tópicos disponíveis\n";
			for (String key:keyset) {
				queueString = queueString + key + "\n";
			}
		}
		return queueString;
	}
}
