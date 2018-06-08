package distribution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Marshaller {
	static Encryption encry = new Encryption();
	public byte[] marshall(Packet pkt) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

		//Criptografando
		Packet pckt = encry.encrypt(pkt);
		
		objectStream.writeObject(pckt);		
		return byteStream.toByteArray();
	}

	public Packet unmarshall(byte[] pkt) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(pkt);
		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		
		//Descriptografando
		Packet pckt = encry.decrypt((Packet) objectStream.readObject());
		
		return pckt;
	}
}
