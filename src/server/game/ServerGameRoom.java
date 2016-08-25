package server.game;

import java.net.Socket;
import java.util.LinkedList;

import server.DGame;
import server.Player;

public class ServerGameRoom extends DGame implements Runnable{
	private LinkedList<GamePlayer> listOfPlayers = new LinkedList<>();

	public LinkedList<GamePlayer> getListOfPlayers() {
		return listOfPlayers;
	}

	public void setListOfPlayers(LinkedList<GamePlayer> listOfPlayers) {
		this.listOfPlayers = listOfPlayers;
	}
	public ServerGameRoom(String nameOfServer){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	

	
	
}
