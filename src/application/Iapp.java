package application;

public interface Iapp {
	public void publishTopic(String topicName, String content);
	public void subscribeTopic(String topicName);
	public void unsubscribeTopic(String topicName);
	public void listTopics();
}
