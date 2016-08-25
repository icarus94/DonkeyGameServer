package server.game;
import java.net.Socket;
import java.util.LinkedList;

import server.*;


public class ServerControl implements Runnable{
	
	public static volatile LinkedList<ServerGameRoom> listOfGameRooms = new LinkedList<ServerGameRoom>();
	private Socket socketForConnection;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public ServerControl(Socket conn, Player player){//for quickgame
		this.socketForConnection=conn;
		for (int i = 0; i <listOfGameRooms.size(); i++) {
			if(listOfGameRooms.get(i).getListOfPlayers().size() == 3){
				listOfGameRooms.get(i).getListOfPlayers().addLast(null);//mora da ubaci igraca
			}
		}
	}
	
	public ServerControl(Socket conn,String nameOfNewGameRoom,String gamePassword,String numberOfBots,Player player){//for newGame
		this.socketForConnection = conn;
		
		listOfGameRooms.addFirst(new ServerGameRoom(nameOfNewGameRoom));
		new Thread(listOfGameRooms.getFirst()).start();
	}
	
	public ServerControl(Socket conn,String nameOfNewGameRoom,String gamePassword,Player player){
		this.socketForConnection = conn;
		
	}
	
	
	
	
}
