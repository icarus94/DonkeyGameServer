package server.game;
import java.net.Socket;
import java.util.LinkedList;

import server.*;


public class ServerControl implements Runnable{
	
	private static LinkedList<ServerGameRoom> listOfGameRooms = new LinkedList<ServerGameRoom>();
	private Socket socketForConnection;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public ServerControl(Socket conn){
		this.socketForConnection=conn;
	}
	
	
	
}
