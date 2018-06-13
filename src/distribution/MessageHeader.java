package distribution;

import java.io.Serializable;

public class MessageHeader implements Serializable{
	private String destinationQueue;

	public MessageHeader(String destinationQueue) {
		this.destinationQueue = destinationQueue;
	}

	public String getDestinationQueue() {
		return destinationQueue;
	}

	public void setDestinationQueue(String destinationQueue) {
		this.destinationQueue = destinationQueue;
	}
	
	
}
