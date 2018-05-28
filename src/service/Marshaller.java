package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Marshaller {
	static Encryption encry = new Encryption();
	public static byte[] marshall(Message message) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

		//Criptografando
		Message msg = encry.encrypt(message);
		
		objectStream.writeObject(msg);		
		return byteStream.toByteArray();
	}

	public static Message unmarshall(byte[] message) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(message);
		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		
		//Descriptografando
		Message msg = encry.decrypt((Message) objectStream.readObject());
		
		return msg;
	}
}
