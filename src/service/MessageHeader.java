package service;

import java.io.Serializable;

public class MessageHeader implements Serializable {
	private String destination;

	public MessageHeader(String destination) {
		this.destination = destination;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}


}

