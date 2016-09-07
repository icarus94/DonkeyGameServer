package server.startup;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

import server.Player;
import server.connection.ClientServerConnection;
import server.game.ServerControl;
import server.game.ServerGameRoom;

public class ServerStartup {
	
	public static LinkedList<ClientServerConnection> clientConnectionList = new LinkedList<ClientServerConnection>();

	public static void main(String[] args) {
		ServerControl.listOfGameRooms.add(new ServerGameRoom("soba", "", 1, new Player()));
		ServerControl.listOfGameRooms.add(new ServerGameRoom("soba1", "", 1, new Player()));
		ServerControl.listOfGameRooms.add(new ServerGameRoom("soba2", "", 1, new Player()));
		
		
		
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
