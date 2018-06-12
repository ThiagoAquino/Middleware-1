package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

import distribution.QueueManagerProxy;

public class Client implements Iapp{
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
	@Override
	public void publishTopic(String topicName, String content){
		try{
			Map<String,String> parameters = new HashMap<String, String>();
			parameters.put("content",content);
			QueueManagerProxy proxy = new QueueManagerProxy(topicName);
			proxy.send("publish",parameters);
			System.out.println(proxy.receive());
		}catch(Exception e){
			System.out.println("Something went wrong :T");
		}
	}
	@Override
	public void subscribeTopic(String topicName) {
		try{
			SubscriberThread thread = new SubscriberThread(topicName);
			this.subscriberThreads.add(thread);
			thread.start();
			
		}catch(Exception e){
			System.out.println("Something went wrong :T");
		}
	}
	public void listTopics() {
		try{
			QueueManagerProxy proxy = new QueueManagerProxy(null);
			proxy.send("listAll", null);
			System.out.println(proxy.receive());
			
		}catch(Exception e){
			System.out.println("Something went wrong :T");
			
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
		}catch (Exception e){
			System.out.println("Something went wrong :T");
		}
	}
	
	public static void main(String [] args) throws UnknownHostException, IOException{
		Client user = null;
		try {
			user = new Client();
			System.out.println(user.getUserAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		boolean shouldContinue = true;
		String userChoice;
		int index;
		String functionName = "";
		String userHintString = "Welcome to MOM Middleware!\n"
								+ "Here are some functions you can invoke:\n"
								+ "h : Shows intructions\n"
								+ "q : Quit application\n"
								+ "publishTopic(topicName,content) : publishes topic\n"
								+ "subscribeTopic(topicName) : subscribes to topic\n"
								+ "unsubscribeTopic(TopicName) : unsubscribes to topic\n"
								+ "listTopics() : lists topics available\n";
								
								
		System.out.println(userHintString);
		while(shouldContinue){
			Scanner in = new Scanner(System.in);
			userChoice = in.nextLine();
			index = userChoice.indexOf("(");
			if(index != -1){
				functionName = userChoice.substring(0, index);
			} 
			if (userChoice.equals("h")){
				System.out.println(userHintString);
			}else if (userChoice.equals("q")){
				System.out.println("See you!");
				shouldContinue = false;
			}else if (functionName.equals("publishTopic")){
				handlePublishTopic(user, userChoice);
			}else if (functionName.equals("subscribeTopic")){
				handleSubscribeTopic(user, userChoice);	
			}else if(functionName.equals("unsubscribeTopic")) {
				handleUnsusbcribeTopic(user, userChoice);
			}else if (userChoice.equals("listTopics()")){
				user.listTopics();			
			}else{
				System.out.println("We didn't really catch that :T");
			}
			
		}
	}
	private static void handleSubscribeTopic(Client user, String userChoice) {
		String topicName;
		String[] parts = userChoice.split("[\\(\\)]");
		if(parts.length==2 &&  parts[1].indexOf(',')==-1){
			topicName = parts[1];
			user.subscribeTopic(topicName);
		}else{
			System.out.println("We didn't really catch that :T");
		}
	}
	
	private static void handleUnsusbcribeTopic(Client user, String userChoice) {
		String topicName;
		String [] parts = userChoice.split("[\\(\\)]");
		if (parts.length == 2 && parts[1].indexOf(',') == -1) {
			topicName = parts[1];
			user.unsubscribeTopic(topicName);
		}else {
			System.out.println("We didn't really catch that :T");
		}
	}
	
	private static void handlePublishTopic(Client user, String userChoice) throws UnknownHostException, IOException {
		String topicName;
		String content;
		String[] parts = userChoice.split("[\\(,\\)]");
		if(parts.length == 3) {
			topicName = parts[1];
			content = parts[2];
			user.publishTopic(topicName, content);
			
		}else{
			System.out.println("We didn't really catch that :T");
		}
	}
	

}
