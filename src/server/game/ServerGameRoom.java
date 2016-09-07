package server.game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import server.Card;
import server.DGame;
import server.Player;

public class ServerGameRoom extends DGame implements Runnable{
	private volatile LinkedList<Player> listOfPlayers = new LinkedList<>();
	int roundCounter = 0;
	
	ObjectOutputStream objectClientOutput;

	public LinkedList<Player> getListOfPlayers() {
		return listOfPlayers;
	}

	public void setListOfPlayers(LinkedList<Player> listOfPlayers) {
		this.listOfPlayers = listOfPlayers;
	}
	public ServerGameRoom(String nameOfServer,String gamePassword,int numberOfBots,Player player){
		super(gamePassword,nameOfServer);
		this.listOfPlayers.addLast(player);
		if(numberOfBots <= 3){
			for (int i = 1; i <= numberOfBots; i++) {
				this.listOfPlayers.addLast(new Player("Robot"+i));
				this.listOfPlayers.getLast().setItARobot(true);
			}
		}
	}
	
	@Override
	public void run() {
		while(!(listOfPlayers.size() == 4)){
			for (int i = 0; i < listOfPlayers.size(); i++) {
				if(listOfPlayers.get(i).isItARobot()){
					continue;
				}
				
			}
		}
		while(true){
			LinkedList<Card> cardDeck = Card.shuffledCardDeck();
			for (int i = 0+(roundCounter%4); i < listOfPlayers.size(); i++) {
				// nlistOfPlayers.get(i).setPlayerHandCards();
			}
			for (int i = 0; i < (roundCounter%4); i++) {
				
			}
		}
		
	}
	

	
	
}
