package server.game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Card;
import server.Player;

public class HumanPlayer extends Player implements Runnable {

	private Socket socketForConnection;
	private ObjectOutputStream objectClientOutput;
	private ServerGameRoom pointerToGameRoom;

	public HumanPlayer(String playerName, Socket socketConnection, ObjectOutputStream obj) {
		super(playerName, socketConnection);
		this.socketForConnection = socketConnection;
		this.objectClientOutput = obj;
	}

	public ServerGameRoom getPointerToGameRoom() {
		return pointerToGameRoom;
	}

	public void setPointerToGameRoom(ServerGameRoom pointerToGameRoom) {
		this.pointerToGameRoom = pointerToGameRoom;
	}

	public Socket getSocketForConnection() {
		return socketForConnection;
	}

	public void setSocketForConnection(Socket socketForConnection) {
		this.socketForConnection = socketForConnection;
	}

	public HumanPlayer(String playerName, Socket socketForConnection, ServerGameRoom pointerToGameRoom) {
		super(playerName, socketForConnection);
		this.socketForConnection = socketForConnection;
		this.pointerToGameRoom = pointerToGameRoom;
	}

	@Override
	public void run() {
		int counter = 0;
		while (true) {
			try {
				
				//objectClientOutput = new ObjectOutputStream(socketForConnection.getOutputStream());
				boolean alreadySendForThisRound = false;
				
				while (true) {
					objectClientOutput.flush();
					objectClientOutput.reset();
					objectClientOutput.writeUnshared(pointerToGameRoom.getListOfPlayersTypePLAYER());
					objectClientOutput.reset();
				//	System.out.println("jbg" + this.getPlayerHandCards().size() + " b " + alreadySendForThisRound);
					if (this.getPlayerHandCards().size() == 5 && !alreadySendForThisRound) {
						objectClientOutput.flush();
						objectClientOutput.reset();
						
						
						Card d = new Card(this.getPlayerHandCards().getLast().getCardNumber(),
								this.getPlayerHandCards().getLast().getSymbolOfCard());
						objectClientOutput.writeUnshared(d);// last card
						objectClientOutput.flush();
						objectClientOutput.reset();
						alreadySendForThisRound = true;
						System.out.println("poslo Igracu" + d.getCardNumber() + " "
								+ d.getSymbolOfCard());
					}
					if (this.getPlayerHandCards().size() == 4)
						alreadySendForThisRound = false;
					counter++;
				}
			} catch (IOException e) {
				System.out.println("Human player:" + this.getPlayerName() + " has left the game a poslo je"+counter);
				// kod za paljenje prvog threada
				Player.ispisSvega(pointerToGameRoom.getListOfPlayers());
				return;
			}

		}

	}
}
