package server.AI;

import java.util.LinkedList;

import server.Card;

public class AIServer implements Runnable {
	
	private volatile LinkedList<Card> cardHandRobot;
	private volatile boolean possessionOfTwoOfClubs = false;
	private int playCounter = 0;
	private volatile boolean areCardsDown = false;

	@Override
	public void run() {
		
		while(!areCardsDown){
			if(cardHandRobot.size() == 5){
				
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
		int cardCounter = 0; 
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
		
		for (int i = startingPoint+1; i < cardHandRobot.size(); i++) {
			if(cardHandRobot.get(i).getCardNumber() == 2)
				continue;
			if(forwardingCard.getCardNumber() == cardHandRobot.get(i).getCardNumber()){
				cardCounter++;
			}
			for (int j = 0; j < cardHandRobot.size(); j++) {
				
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

}
