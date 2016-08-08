package server.startup;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import server.connection.ClientServerConnection;

public class ServerStartup {
	
	static LinkedList<ClientServerConnection> clientConnectionList = new LinkedList<ClientServerConnection>();

	public static void main(String[] args) {
		
		
		try {
			ServerSocket serverSocketForConnection = new ServerSocket(13413);
			while(true){
				clientConnectionList.addFirst((new ClientServerConnection(serverSocketForConnection.accept())));
				new Thread(clientConnectionList.getFirst()).start();
			}
		} catch (IOException e) {
			
			System.out.println("Server offline/error!");
		}
		

	}

}
