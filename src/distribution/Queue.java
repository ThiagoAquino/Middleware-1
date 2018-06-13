package distribution;

import java.util.ArrayList;
import java.net.Socket;

public class Queue {
	private ArrayList<Message> queue = new ArrayList<Message>();
	private ArrayList<ArrayList<Socket>> usersForMessage = new ArrayList<ArrayList<Socket>>();

	public void enqueue(Message message){
		message.setTimeOfArrival(System.currentTimeMillis());
		queue.add(message);
	}

	public ArrayList<Socket> getUserForMessageAtIndex(int i) {
		return usersForMessage.get(i);
	}

	public void addUserToMessageAtIndex(Socket user, int index) {
		usersForMessage.get(index).add(user);
	}

	public void initNewListOfUsers() {
		usersForMessage.add(new ArrayList<Socket>());
	}

	public void dequeue(int indexOf){
		if (!queue.isEmpty()){
			queue.remove(indexOf);
		}
	}

	public int queueSize(){
		return queue.size();
	}

	public ArrayList<Message> getQueue(){
		return this.queue;
	}
}
