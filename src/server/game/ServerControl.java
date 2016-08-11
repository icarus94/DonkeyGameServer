package server.game;
import java.net.Socket;
import java.util.LinkedList;

import server.*;


public class ServerControl implements Runnable{
	
	public static LinkedList<ServerGameRoom> listOfGameRooms = new LinkedList<ServerGameRoom>();
	private Socket socketForConnection;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public ServerControl(Socket conn){
		this.socketForConnection=conn;
	}
	
	public ServerControl(Socket conn,String nameOfNewGameRoom,String type){
		this.socketForConnection = conn;
		//Create new game room useing parameter for name
	}
	
	
	
	
}
