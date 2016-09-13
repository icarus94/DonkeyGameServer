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
		ServerControl.listOfGameRooms.add(new ServerGameRoom("soba", "", 3, new Player()));
		//ServerControl.listOfGameRooms.add(new ServerGameRoom("soba1", "", 3, new Player()));
		//ServerControl.listOfGameRooms.add(new ServerGameRoom("soba2", "", 3, new Player()));
		//new Thread(ServerControl.listOfGameRooms.getFirst()).start();;
		//ServerControl.listOfGameRooms.add(new ServerGameRoom(4));
		//new Thread(ServerControl.listOfGameRooms.getFirst()).start();
		
		
		
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
