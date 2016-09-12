 package server.AI;

import java.util.LinkedList;
import java.util.Random;

import com.sun.javafx.scene.paint.GradientUtils.Point;

import server.Card;
import server.Player;
import server.game.ServerGameRoom;

public class AIServer extends Player implements Runnable {
	
	private volatile LinkedList<Card> cardHandRobot = this.getPlayerHandCards();
	private volatile boolean possessionOfTwoOfClubs = false;
	private int playCounter = 0;
	private ServerGameRoom pointerToGameRoom = null;
	
	//private volatile boolean areCardsDown = false;

	public AIServer(String playerName,ServerGameRoom pointerToGameRoom) {
		super(playerName);
		this.pointerToGameRoom = pointerToGameRoom;
	}
	@Override
	public void run() {
		while(true){
			while(cardHandRobot.isEmpty()){
				
			}
			while(!this.isAreCardsDropped()){
				//this.transferPlayerHandCardsToCardHandRobot();
				if(cardHandRobot.size() == 5){
					Card forwardingCard = whichCardToForward();
					this.getPlayerHandCards().remove(forwardingCard);
					this.setNumberOfCardsInHand(4);
					for (int i = 0; i < pointerToGameRoom.getListOfPlayers().size(); i++) {
						if(pointerToGameRoom.getListOfPlayers().get(i).equals(this)){
							pointerToGameRoom.getListOfPlayers().get(i+1).getPlayerHandCards().addLast(forwardingCard);
							pointerToGameRoom.getListOfPlayers().get(i+1).setNumberOfCardsInHand(5);
						}
					}
					playCounter++;
				}
				if(!(checkPossessionOfTwoOfClubs()) && isThereARowOfSameCards()){
					this.setAreCardsDropped(true);
				}
				for (int i = 0; i < pointerToGameRoom.getListOfPlayers().size(); i++) {
					if(pointerToGameRoom.getListOfPlayers().get(i).isAreCardsDropped() &&
							!pointerToGameRoom.getListOfPlayers().get(i).equals(this)){
						//otaseva metoda za tajmer
						Random r = new Random();
						double randomValue = 1 + (8 - 1) * r.nextDouble();
						this.setTime(randomValue);
						this.setAreCardsDropped(true);
						this.cardHandRobot.clear();
					}
				}
			}
		}
	}
	private boolean checkPossessionOfTwoOfClubs(){
		if(cardHandRobot.contains(Card.TWO_OF_CLUBS)){
			possessionOfTwoOfClubs=true;
		}else{
			possessionOfTwoOfClubs=false;
		}
		return possessionOfTwoOfClubs;
	}
	private Card whichCardToForward(){
		Card forwardingCard = null;
		int cardCounter = -1; 
		int startingPoint = 0;
		if(playCounter%3 == 0 && playCounter > 1){
			for (int i = 0; i < cardHandRobot.size(); i++) {
				if(cardHandRobot.get(i).getCardNumber() == 2){
					playCounter++;
					forwardingCard = cardHandRobot.get(i);
					cardHandRobot.remove(i);
					return forwardingCard;
				}
			}
		}
		forwardingCard = cardHandRobot.get(startingPoint);
		if(cardHandRobot.get(startingPoint).getCardNumber() == 2)
			forwardingCard = cardHandRobot.get(++startingPoint);
		mainLoop:
		for (int i = startingPoint; i < cardHandRobot.size(); i++) {
			int innerCounter = 0;
			if(cardHandRobot.get(i).getCardNumber() == 2)
				continue;
			for (int z = i; z >=0 ; z--) {
				if(cardHandRobot.get(i).getCardNumber() == cardHandRobot.get(z).getCardNumber()){
					continue mainLoop;
				}
			}
			for (int j = i+1; j < cardHandRobot.size(); j++) {
				if(cardHandRobot.get(i).getCardNumber() == cardHandRobot.get(j).getCardNumber()){
					innerCounter++;
				}
			}
			if(innerCounter<cardCounter || cardCounter == -1){
				forwardingCard = cardHandRobot.get(i);
			}
		}	
		return forwardingCard;
	}
	private boolean isThereARowOfSameCards(){
		if(checkPossessionOfTwoOfClubs())
			return false;
		Card checker = cardHandRobot.get(0);
		int counter = 0;
		for (int i = 1; i < cardHandRobot.size(); i++) {
			if(checker.getCardNumber() == cardHandRobot.get(i).getCardNumber()){
				counter++;
			}
		}
		if(counter == 0){
			checker = cardHandRobot.get(1);
			for (int i = 2; i < cardHandRobot.size(); i++) {
				if(checker.getCardNumber() == cardHandRobot.get(i).getCardNumber()){
					counter++;
				}
			}
		}
		if(counter == 3)
			return true;
		return false;
}
	
	public void addCard(Card card){
		if(cardHandRobot.size()==4)
			this.cardHandRobot.add(card);
	}

	public void addCards(LinkedList<Card> cardHand){
		if(cardHand.size()<=5)
			this.cardHandRobot = cardHand;
	}
	
	public void transferPlayerHandCardsToCardHandRobot(){
		cardHandRobot.clear();
		cardHandRobot = this.getPlayerHandCards();
	}
}
