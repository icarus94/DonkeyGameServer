 package server.AI;

import java.util.LinkedList;

import server.Card;
import server.Player;

public class AIServer extends Player implements Runnable {
	
	private volatile LinkedList<Card> cardHandRobot;
	private volatile boolean possessionOfTwoOfClubs = false;
	private int playCounter = 0;
	private volatile boolean areCardsDown = false;

	public AIServer(String playerName) {
		super(playerName);
	}
	@Override
	public void run() {
		
		while(!areCardsDown){
			this.transferPlayerHandCardsToCardHandRobot();
			if(cardHandRobot.size() == 5){
				whichCardToForward();
				playCounter++;
			}
			if(!(checkPossessionOfTwoOfClubs()) && isThereARowOfSameCards()){
				areCardsDown = true;
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
	public void triggerWhenCardsAreDown(){
		this.areCardsDown=true;
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
