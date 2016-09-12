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
	private volatile int roundCounter = 0;
	private boolean gameRoomShutingDown = false;
	
		public boolean isGameRoomShutingDown() {
		return gameRoomShutingDown;
	}

		public int getRoundCounter() {
		return roundCounter;
	}

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
		if(player instanceof HumanPlayer){
			HumanPlayer hp = (HumanPlayer) player;
			hp.setPointerToGameRoom(this);
			System.out.println("ID as HumanPlayer");
		}
		if(numberOfBots <= 3){
			for (int i = 1; i <= numberOfBots; i++) {
				this.listOfPlayers.addLast(new AIServer("Robot"+i,this));//Insertion of AI
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
			this.listOfPlayers.get(i).setTime(0);
			this.listOfPlayers.get(i).getPlayerHandCards().clear();
		}
		
	}
	
	public void startHumanPlayerThreadForComm(){
		for (int i = 0; i < listOfPlayers.size(); i++) {
			if(listOfPlayers.get(i) instanceof HumanPlayer){
				HumanPlayer hp = (HumanPlayer) listOfPlayers.get(i);
				new Thread(hp).start();
			}
		}
	}

	public void dealCards(){
		LinkedList<Card> cardDeck = Card.shuffledCardDeck();
		boolean firstHandDealt = false;
		
		for (int i =(roundCounter%4); i < listOfPlayers.size(); i++) {
			if(firstHandDealt == false){
				for (int j=0; j < 5; j++) {
					listOfPlayers.get(i).getPlayerHandCards().addLast(cardDeck.pop());
				}
				firstHandDealt = true;
				listOfPlayers.get(i).setNumberOfCardsInHand(5);
				continue;
			}
			for (int j=0; j < 4; j++) {
				listOfPlayers.get(i).getPlayerHandCards().addLast(cardDeck.pop());
			}
			listOfPlayers.get(i).setNumberOfCardsInHand(4);
		}
		for (int i = 0; i < (roundCounter%4); i++) {
			for (int j=0; j < 4; j++) {
				listOfPlayers.get(i).getPlayerHandCards().addLast(cardDeck.pop());
			}
			listOfPlayers.get(i).setNumberOfCardsInHand(4);
		}
	}
	
	public void startRobotPlayers(){
		for (int i = 0; i < listOfPlayers.size(); i++) {
			if(listOfPlayers.get(i) instanceof AIServer){
				AIServer aiS = (AIServer) listOfPlayers.get(i);
				new Thread(aiS).start();
			}
		}
	}
	public void addLetterToPlayer(Player player){
		switch (player.getDonkeyLetters().length()) {
			case 0:{
				player.setDonkeyLetters("D");
				break;
			}
			case 1:{
				player.setDonkeyLetters("Do");
				break;
			}
			case 2:{
				player.setDonkeyLetters("Don");
				break;
			}
			case 3:{
				player.setDonkeyLetters("Donk");
				break;
			}
			case 4:{
				player.setDonkeyLetters("Donke");
				break;
			}
			case 5:{
				player.setDonkeyLetters("Donkey");
				break;
			}
		}
	}
	
	public Player findPlayerWithHighestTime(){
		Player playerWithHighestTime = listOfPlayers.get(0);
		for (int i = 1; i < listOfPlayers.size(); i++) {
			if(playerWithHighestTime.getTime()<listOfPlayers.get(i).getTime()){
				playerWithHighestTime = listOfPlayers.get(i);
			}
		}
		return playerWithHighestTime;
	}
	
	public boolean isItEndOfTheGame(){
		boolean checkEnd = false;
		for (int i = 0; i < listOfPlayers.size(); i++) {
			if(listOfPlayers.get(i).getDonkeyLetters().length() == 6){
				checkEnd = true;
				break;
			}
		}
		return checkEnd;
	}
	
	public void run() {
		this.startHumanPlayerThreadForComm();
		this.startRobotPlayers();
		while(!(listOfPlayers.size() == 4)){
			
		}
		while(true){
			this.restartPlayersSettings();//restarts possisonTwoOfClubs and areCardsDown
			this.dealCards(); //Deals cards
			
			for (int i = 0; i < listOfPlayers.size(); i++) {
				for (int j = 0; j < listOfPlayers.get(i).getPlayerHandCards().size(); j++) {
					System.out.println(listOfPlayers.get(i).getPlayerHandCards().get(j).getCardNumber()+" "+
							listOfPlayers.get(i).getPlayerHandCards().get(j).getSymbolOfCard());
				}
				System.out.println("for player "+listOfPlayers.get(i).getPlayerName()+"\n");
			}//za ispis
			while(true){
				if(listOfPlayers.get(0).isAreCardsDropped() ==true &&
						listOfPlayers.get(1).isAreCardsDropped() ==true &&
						listOfPlayers.get(2).isAreCardsDropped() ==true &&
						listOfPlayers.get(3).isAreCardsDropped() ==true){
					break;
				}
			}
			addLetterToPlayer(findPlayerWithHighestTime());
			roundCounter++;
			if(isItEndOfTheGame()){
				try {
					wait(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//return all players code
				
				return;
			}
		}
	}
}
