package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

import distribution.QueueManagerProxy;

public class Client implements Iapp {

	private String userAddress;
	private int userPort;
	private ArrayList<SubscriberThread> subscriberThreads;

	public Client() throws UnknownHostException{
		userAddress = InetAddress.getLocalHost().getHostAddress();
		this.subscriberThreads = new ArrayList<SubscriberThread>();
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public int getUserPort() {
		return userPort;
	}
	public void setUserPort(int userPort) {
		this.userPort = userPort;
	}


	public static void main(String [] args) throws UnknownHostException, IOException{
		Client user = null;
		try {
			user = new Client();
			System.out.println(user.getUserAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String userHintString = "functions you can invoke:\n\n"
				+ "help : Shows intructions\n"
				+ "quit : Quit application\n"
				+ "publishTopic topicName content  : publishes topic\n"
				+ "subscribeTopic topicName  : subscribes to topic\n"
				+ "unsubscribeTopic TopicName : unsubscribes to topic\n"
				+ "listTopics : lists topics available\n";

		System.out.println(userHintString);
		Scanner in = new Scanner(System.in);

		boolean shouldContinue = true;
		while(shouldContinue) {
			String userChoice = in.nextLine();
			if (userChoice.equals("help")) {
				System.out.println(userHintString);
			} else if (userChoice.equals("quit")) {
				System.out.println("See you!");
				shouldContinue = false;
			} else if (userChoice.startsWith("publishTopic")) {
				handlePublishTopic(user, userChoice);
			} else if (userChoice.startsWith("subscribeTopic")) {
				handleSubscribeTopic(user, userChoice);	
			} else if(userChoice.startsWith("unsubscribeTopic")) {
				handleUnsusbcribeTopic(user, userChoice);
			} else if (userChoice.startsWith("listTopics")) {
				user.listTopics();			
			} else {
				System.out.println("We didn't really catch that");
			}

		}
	}

	private static void handleSubscribeTopic(Client user, String userChoice) {
		String topicName;
		String[] parts = userChoice.split(" ");
		topicName = parts[1];
		user.subscribeTopic(topicName);
		//System.out.println("Subscribed to topic " + topicName);
	}

	private static void handleUnsusbcribeTopic(Client user, String userChoice) {
		String topicName;
		String [] parts = userChoice.split(" ");
		topicName = parts[1];
		user.unsubscribeTopic(topicName);
		//System.out.println("Unsubscribed to topic " + topicName);
	}

	private static void handlePublishTopic(Client user, String userChoice) throws UnknownHostException, IOException {
		String topicName;
		String content;
		String[] parts = userChoice.split(" ");

		topicName = parts[1];
		content = parts[2];
		user.publishTopic(topicName, content);
		//System.out.println("Published to topic " + topicName);

	}
	@Override
	public void publishTopic(String topicName, String content){
		try{
			Map<String,String> parameters = new HashMap<String, String>();
			parameters.put("content",content);
			QueueManagerProxy proxy = new QueueManagerProxy(topicName);
			proxy.send("publish",parameters);
			System.out.println(proxy.receive());
		} catch(Exception e) {
			System.out.println("Wrong case in Client publishTopic");
		}
	}
	@Override
	public void subscribeTopic(String topicName) {
		try{
			SubscriberThread thread = new SubscriberThread(topicName);
			this.subscriberThreads.add(thread);
			thread.start();

		}catch(Exception e){
			System.out.println("Wrong case in Client subscribeTopic");
		}
	}
	public void listTopics() {
		try{
			QueueManagerProxy proxy = new QueueManagerProxy(null);
			proxy.send("listAll", null);
			System.out.println(proxy.receive());

		}catch(Exception e){
			System.out.println("Wrong case in Client listTopics");

		}
	}
	@Override
	public void unsubscribeTopic(String topicName) {
		try {
			System.out.println("Chegou!");
			boolean isSubscribed = false;

			for (int i = 0; i < this.subscriberThreads.size(); i++) {
				if (this.subscriberThreads.get(i).getTopicName().equals(topicName)) {
					isSubscribed = true;
					this.subscriberThreads.get(i).setIsSubscribed(false);
				}
			}

			if (!isSubscribed) {
				System.out.println("User is not subscribed to Topic!");
			}
		} catch (Exception e) {
			System.out.println("Wrong case in Client unsubscribeTopic");
		}
	}

}
