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
				this.listOfPlayers.addLast(new Player("Robot"+i,null));
				this.listOfPlayers.getLast().setItARobot(true);
			}
		}
	}
	
	public void restartPlayersSettings(){
		for (int i = 0; i < this.listOfPlayers.size(); i++) {
			this.listOfPlayers.get(i).setAreCardsDropped(false);
			this.listOfPlayers.get(i).setPossesionTwoOfClubs(false);
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
			this.restartPlayersSettings();//restarts possisonTwoOfClubs and areCardsDown
			LinkedList<Card> cardDeck = Card.shuffledCardDeck();
			/*Card[] cardDeckArray = new Card[cardDeck.size()];
			for (int i = 0; i < cardDeck.size(); i++) {
				cardDeckArray[i] = cardDeck.get(i);
			}*/
			//cardDeck = null;
			int indexOfPlayerForDealingCards = 0, counter = 0;
			
			for (int i = 1+(roundCounter%4); i < listOfPlayers.size(); i++) {
				for (int j = 0; j < cardDeck.size(); j++) {
					
				}
				listOfPlayers.get(indexOfPlayerForDealingCards++).setPlayerHandCards(null);
			}
			for (int i = 0; i < (roundCounter%4); i++) {
				listOfPlayers.get(indexOfPlayerForDealingCards++).setPlayerHandCards(null);
			}
			while(true){
				if(listOfPlayers.get(0).isAreCardsDropped() !=true &&
						listOfPlayers.get(1).isAreCardsDropped() !=true &&
						listOfPlayers.get(2).isAreCardsDropped() !=true &&
						listOfPlayers.get(3).isAreCardsDropped() !=true){
					break;
				}
			}
		}
		
	}
	

	
	
}
