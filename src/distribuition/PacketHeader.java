package distribution;

import java.io.Serializable;

public class PacketHeader implements Serializable{
	private String operation;
	
	public PacketHeader(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
