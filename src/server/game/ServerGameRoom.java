package server.game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import server.DGame;
import server.Player;

public class ServerGameRoom extends DGame implements Runnable{
	private LinkedList<GamePlayer> listOfPlayers = new LinkedList<>();
	
	ObjectOutputStream objectClientOutput;

	public LinkedList<GamePlayer> getListOfPlayers() {
		return listOfPlayers;
	}

	public void setListOfPlayers(LinkedList<GamePlayer> listOfPlayers) {
		this.listOfPlayers = listOfPlayers;
	}
	public ServerGameRoom(String nameOfServer,String gamePassword,String numberOfBots,Player player){
		super(nameOfServer,gamePassword); 
	}
	
	@Override
	public void run() {
		while(!(listOfPlayers.size() == 4)){
			for (int i = 0; i < listOfPlayers.size(); i++) {
				if(listOfPlayers.get(i).isItARobot()){
					continue;
				}
				try {
					objectClientOutput = new ObjectOutputStream(
							listOfPlayers.get(i).getSocketForConnection().getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(
							"Izgubljena konekcija u sobi sa igracem "+listOfPlayers.get(i).getPlayerName());
					listOfPlayers.remove(listOfPlayers.get(i));
				}
			}
		}
		
	}
	

	
	
}
