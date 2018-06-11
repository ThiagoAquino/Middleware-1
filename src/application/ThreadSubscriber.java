package application;

import java.io.IOException;
import java.net.UnknownHostException;

import distribution.QueueManagerProxy;

public class ThreadSubscriber extends Thread{
	private String topicName;
	private QueueManagerProxy proxy;
	private boolean shouldContinue = true;
	private boolean isSubscribed;

	public ThreadSubscriber(String topicName) throws UnknownHostException, IOException, InterruptedException {
		this.topicName = topicName;
		this.isSubscribed = true;
		proxy = new QueueManagerProxy(topicName);
		proxy.send("subscribe", null);
	}
	
	public String getTopicName() {
		return this.topicName;
	}
	
	public synchronized void setIsSubscribed(boolean b) {
		this.isSubscribed = b;
	}
	

	public void run() {
		String response;
		while(shouldContinue && this.isSubscribed){
			
			try {
				this.sleep(1000);
				if((response = proxy.receive()) != null){
					if(response.equals("This topic is unavaliable.")) {
						System.out.println(response);
						break;
					}
					System.out.println(response);
					
				}
			} catch (ClassNotFoundException e) {
				shouldContinue = false;
			} catch (IOException e) {
				shouldContinue = false;
			} catch (InterruptedException e) {
				shouldContinue = false;
			}
			
			if (!this.isSubscribed) {
				try {
					this.proxy.send("unsubscribe", null);
					String receivedMessage = this.proxy.receive();
					if (!receivedMessage.equals("Unsubscribed Topic!")) {
						this.isSubscribed = true;
					}
					
					System.out.println(receivedMessage);
					
				}catch(Exception e) {
					
				}
				
			}
		}
		
		
	}
	
}
