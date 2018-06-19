package application.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import application.Iapp;
import application.SubscriberThread;

import java.util.ArrayList;

import distribution.QueueManagerProxy;

public class TestClientSub implements Iapp {

	private String userAddress;
	private int userPort;
	private ArrayList<SubscriberThread> subscriberThreads;

	public TestClientSub() throws UnknownHostException{
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
		TestClientSub user = null;
		try {
			user = new TestClientSub();
			System.out.println(user.getUserAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Scanner in = new Scanner(System.in);

		boolean shouldContinue = true;
		int i = in.nextInt();

		Long time = System.currentTimeMillis();

		while(shouldContinue) {
			String userChoice = "publishTopic subTest";

			if (userChoice.startsWith("publishTopic")) {
				handlePublishTopic(user, userChoice);
			} else if (userChoice.startsWith("subscribeTopic")) {
				handleSubscribeTopic(user, userChoice);	
			} else if(userChoice.startsWith("unsubscribeTopic")) {
				handleUnsusbcribeTopic(user, userChoice);
			}
			i--;
		}
		shouldContinue = false;
		System.out.println(System.currentTimeMillis() - time);
	}

	private static void handleSubscribeTopic(TestClientSub user, String userChoice) {
		String topicName;
		String[] parts = userChoice.split(" ");
		topicName = parts[1];
		user.subscribeTopic(topicName);
		//System.out.println("Subscribed to topic " + topicName);
	}

	private static void handleUnsusbcribeTopic(TestClientSub user, String userChoice) {
		String topicName;
		String [] parts = userChoice.split(" ");
		topicName = parts[1];
		user.unsubscribeTopic(topicName);
		//System.out.println("Unsubscribed to topic " + topicName);
	}

	private static void handlePublishTopic(TestClientSub user, String userChoice) throws UnknownHostException, IOException {
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
			//System.out.println("Wrong case in Client publishTopic");
		}
	}
	@Override
	public void subscribeTopic(String topicName) {
		try{
			SubscriberThread thread = new SubscriberThread(topicName);
			this.subscriberThreads.add(thread);
			thread.start();

		}catch(Exception e){
			//			System.out.println("Wrong case in Client subscribeTopic");
		}
	}
	public void listTopics() {
		try{
			QueueManagerProxy proxy = new QueueManagerProxy(null);
			proxy.send("listAll", null);
			System.out.println(proxy.receive());

		}catch(Exception e){
			//			System.out.println("Wrong case in Client listTopics");

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
			//			System.out.println("Wrong case in Client unsubscribeTopic");
		}
	}

}
