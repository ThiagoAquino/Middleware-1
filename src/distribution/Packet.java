package distribution;

import java.io.Serializable;

public class Packet implements Serializable{
	private PacketHeader header;
	private PacketBody body;

	public Packet(PacketHeader header, PacketBody body){
		this.header = header;
		this.body = body;

	}

	public PacketHeader getHeader() {
		return header;
	}

	public void setHeader(PacketHeader header) {
		this.header = header;
	}

	public PacketBody getBody() {
		return body;
	}

	public void setBody(PacketBody body) {
		this.body = body;
	}


}
