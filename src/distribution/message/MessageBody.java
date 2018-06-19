package distribution.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

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
