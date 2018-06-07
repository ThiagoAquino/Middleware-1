package distribution;

import java.io.Serializable;

public class MessageBody implements Serializable{
	private String content;

	public MessageBody(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
