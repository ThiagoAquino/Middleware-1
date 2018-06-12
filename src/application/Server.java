package application;

import java.io.IOException;
import java.util.Scanner;

import distribution.QueueManager;
import distribution.QueueServer;
import infrastructure.ServerRequestHandler;

public class Server {
	private static boolean shouldContinue = true;
	private static boolean serverIsUp = false;
	private static QueueServer queueServer;
	private static ServerRequestHandler srh; 

	public static void main(String [] args) throws IOException{
		Scanner in = new Scanner(System.in);
		String choice = "";
		String serverHintString = "Welcome to MOM Middleware!\n"
				+ "Here are some functions you can invoke:\n"
				+ "s : Start Server\n"
				+ "h : Shows help intructions\n"
				+ "q : Shutdown server\n";
		System.out.println(serverHintString);
		while(shouldContinue){
			choice = in.nextLine();
			if (choice.equals("h")){
				System.out.println(serverHintString);
			} else if (choice.equals("q")){
				killServer();
			} else if (choice.startsWith("s")){
				startServerThreadOrErrorMessage(choice);
			} else {
				System.out.println("Wrong case in Server Main");
			}
		}
		in.close();
	}

	private static void startServerThreadOrErrorMessage(String choice) {
		if(choice.equals("s")){
			if (!serverIsUp){
				startServerThread();
			}else{
				System.out.println("Server is already UP!");
			}	
		} else{
			System.out.println("Wrong case in startServerThreadOrErrorMessage");
		}
	}

	private static void startServerThread() {
		new Thread(){
			public void run(){
				try {
					startServer();
				} catch (ClassNotFoundException e) {
					System.out.println("Something went wrong");
				} catch (IOException e) {
					System.out.println("Something went wrong");
				}
			}
		}.start();
	}

	public static void startServer() throws ClassNotFoundException, IOException{
		QueueManager queueManager = QueueManager.getInstance();
		srh = new ServerRequestHandler(queueManager.getPort());
		queueServer = new QueueServer();
		System.out.println("Server is UP!\n");
		serverIsUp = true;
		while(shouldContinue){
			srh.receive();

		}
	}

	public static void killServer() throws IOException{
		if (serverIsUp){
			shouldContinue = false;
			serverIsUp = false;
			srh.setServerSocket(null);

			System.out.println("Shutting down the server!");
		}else{
			System.out.println("Server is not UP");
		}
	}

	public static QueueServer getQueueServer(){
		return queueServer;
	}

	public static ServerRequestHandler getSRH(){
		return srh;
	}

	public static boolean getShouldContinue(){
		return shouldContinue;
	}

	public static void setQueueServer(QueueServer queueServer) {
		Server.queueServer = queueServer;
	}


}
