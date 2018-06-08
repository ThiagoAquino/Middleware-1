package distribution;

import java.io.Serializable;

public class PacketBody implements Serializable{
	private Message message;
	
	public PacketBody(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}	
}
