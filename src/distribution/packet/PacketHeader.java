package distribution.packet;

import java.io.Serializable;

public class PacketHeader implements Serializable{
	private PacketType type;
	
	public PacketHeader(PacketType type) {
		this.type = type;
	}

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}
	
}
