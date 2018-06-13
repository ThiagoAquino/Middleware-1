package distribution;

import java.io.Serializable;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import distribution.packet.Packet;

/* Modo de Encriptogração 
 * composto por 2 partes
 * 1. Cifra de César
 * 2. Class Cipher
 */

@SuppressWarnings("serial")
public class Encryption implements Serializable  {

	public Encryption() {}

	private static final byte[] secretKey = new byte[]{'o', 'l', 'u', '4', 'a', 'p', 'o', 'a', 'o', 'j', '2'};
	private static String transformation = "AES"; // AES/CBC/PKCS5Padding...

	//Modelo de Cesar, onde troca uma letra por X letras seguintes
	public Packet encrypt (Packet in) {
		// first step
		String message = in.getBody().getMessage().getBody().getContent();
		String aux = "";
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) != ' ') {
				char a = (char) (message.charAt(i)+3);
				aux = aux+a;
			} else {
				aux = aux+' ';
			}
		}

		//Second step
		//        Key key = generateKey();
		//        Cipher cipher = Cipher.getInstance(this.transformation);
		//        cipher.init(Cipher.ENCRYPT_MODE, key);
		//        byte[] value = cipher.doFinal(aux.getBytes());
		//        Base64.getEncoder().encodeToString(value);

		Packet retorno = in;
		retorno.getBody().getMessage().getBody().setContent(aux);
		return retorno;
	}


	public Packet decrypt(Packet in) {
		String message = in.getBody().getMessage().getBody().getContent();

		//        Key key = generateKey();
		//        Cipher cipher = Cipher.getInstance(this.transformation);
		//        cipher.init(Cipher.DECRYPT_MODE, key);
		//        byte[] value = Base64.getDecoder().decode(message);
		//        byte[] valuePreCesar = cipher.doFinal(value);
		//        String aux = new String(valuePreCesar);

		String aux1 = "";
		for (int i = 0; i < message.length(); i++) {
			if(message.charAt(i) != ' ') {
				char a = (char) (message.charAt(i)-3);
				aux1 = aux1+a;
			} else {
				aux1 = aux1+' ';
			}
		}

		Packet retorno = in;
		retorno.getBody().getMessage().getBody().setContent(aux1);
		return retorno;
	}

	private static Key generateKey() throws Exception {
		return new SecretKeySpec(secretKey, transformation);
	}

}