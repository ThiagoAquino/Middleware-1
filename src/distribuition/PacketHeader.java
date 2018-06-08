package distribution;

import java.io.Serializable;

public class PacketHeader implements Serializable{
	private PacketType operation;
	
	public PacketHeader(PacketType packetType) {
		this.operation = packetType;
	}

	public PacketType getOperation() {
		return operation;
	}

	public void setOperation(PacketType packetType) {
		this.operation = packetType;
	}
	
}
