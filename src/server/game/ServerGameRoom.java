package server.game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import server.Card;
import server.DGame;
import server.Player;
import server.AI.AIServer;

public class ServerGameRoom extends DGame implements Runnable{
	private volatile LinkedList<Player> listOfPlayers = new LinkedList<>();
	int roundCounter = 0;
	
	ObjectOutputStream objectClientOutput;
	
	public LinkedList<Player> getListOfPlayersTypePLAYER(){
		LinkedList<Player> playerList = new LinkedList<>();
		for (int i = 0; i < listOfPlayers.size(); i++) {
			playerList.addLast(new Player(listOfPlayers.get(i).getPlayerName()));
			playerList.getLast().setAreCardsDropped(listOfPlayers.get(i).isAreCardsDropped());
			playerList.getLast().setDonkeyLetters(listOfPlayers.get(i).getDonkeyLetters());
			playerList.getLast().setItARobot(listOfPlayers.get(i).isItARobot());
			playerList.getLast().setNumberOfCardsInHand(listOfPlayers.get(i).getNumberOfCardsInHand());
			playerList.getLast().setPlayerHandCards(listOfPlayers.get(i).getPlayerHandCards());
			playerList.getLast().setPortOfPlayer(listOfPlayers.get(i).getPortOfPlayer());
			playerList.getLast().setPossesionTwoOfClubs(listOfPlayers.get(i).isPossesionTwoOfClubs());
			playerList.getLast().setSocketAddressOfPlayer(listOfPlayers.get(i).getSocketAddressOfPlayer());
		}
		return playerList;
	}

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
				this.listOfPlayers.addLast(new AIServer("Robot"+i));//Insertion of AI
				this.listOfPlayers.getLast().setItARobot(true);
			}
		}
	}
	
	public void addLetterToPlayerWhoLostTheRound(){
		
	}
	
	/**
	 * Resets possesionTwoOfClubs and areCardsDropped
	 */
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
			LinkedList<Card> oneHand = new LinkedList<>();
			
			for (int i = 0+(roundCounter%4); i < listOfPlayers.size(); i++) {
				if(counter == 0){
					for (; counter < counter+5; counter++) {
						oneHand.add(cardDeck.get(counter));
					}
					listOfPlayers.get(indexOfPlayerForDealingCards++).setPlayerHandCards(oneHand);
					continue;
				}
				for (; counter < counter+4; counter++) {
					oneHand.add(cardDeck.get(counter));
				}
				listOfPlayers.get(indexOfPlayerForDealingCards++).setPlayerHandCards(oneHand);
			}
			for (int i = 0; i < (roundCounter%4); i++) {
				for (; counter < counter+4; counter++) {
					oneHand.add(cardDeck.get(counter));
				}
				listOfPlayers.get(indexOfPlayerForDealingCards++).setPlayerHandCards(oneHand);
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
