package distribution.message;

import java.io.Serializable;

public class Message implements Serializable{
	private MessageHeader header;
	private MessageBody body;
	private long timeOfArrival;

	public Message(MessageHeader header, MessageBody body){
		this.header = header;
		this.body = body;
		this.timeOfArrival = 0;
	}

	public MessageHeader getHeader() {
		return header;
	}

	public void setHeader(MessageHeader header) {
		this.header = header;
	}

	public MessageBody getBody() {
		return body;
	}

	public void setBody(MessageBody body) {
		this.body = body;
	}

	public long getTimeOfArrival(){
		return this.timeOfArrival;
	}

	public void setTimeOfArrival(long time){
		this.timeOfArrival = time;
	}

}
