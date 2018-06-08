package distribution;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Encryption implements Serializable  {

	public Encryption() {}

	//Modelo de Cesar, onde troca uma letra por X letras seguintes
	public Packet encrypt (Packet in) {
		String message = in.getBody().getMessage().getBody().getContent();
		String aux1="";
		for (int i = 0; i < message.length(); i++) {
			if(message.charAt(i) != ' ') {
				char a = (char) (message.charAt(i)+3);
				aux1 = aux1+a;
			} else {
				aux1 = aux1+' ';
			}
		}
		Packet retorno = in;
		retorno.getBody().getMessage().getBody().setContent(message);
		return retorno;
	}


	public Packet decrypt(Packet in) {
		String message = in.getBody().getMessage().getBody().getContent();
		String aux1="";
		for (int i = 0; i < message.length(); i++) {
			if(message.charAt(i) != ' ') {
				char a = (char) (message.charAt(i)-3);
				aux1 = aux1+a;
			} else {
				aux1 = aux1+' ';
			}
		}
		Packet retorno = in;
		retorno.getBody().getMessage().getBody().setContent(message);
		return retorno;
	}
}
