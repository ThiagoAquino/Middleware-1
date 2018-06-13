package distribution;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

public interface IQueueManager {

	public void send(String function, Map<String,String> msg) throws UnknownHostException, IOException;

	public String receive() throws IOException, ClassNotFoundException;
}
