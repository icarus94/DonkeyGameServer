package server.game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Player;

public class HumanPlayer extends Player implements Runnable {
	
	private Socket socketForConnection;
	private ObjectOutputStream objectClientOutput;
	private ServerGameRoom pointerToGameRoom;

	public Socket getSocketForConnection() {
		return socketForConnection;
	}

	public void setSocketForConnection(Socket socketForConnection) {
		this.socketForConnection = socketForConnection;
	}

	public HumanPlayer(String playerName, Socket socketForConnection,ServerGameRoom pointerToGameRoom) {
		super(playerName,socketForConnection);
		this.socketForConnection = socketForConnection;
		this.pointerToGameRoom = pointerToGameRoom;
	}

	@Override
	public void run() {
		while(true){
			try {
				objectClientOutput = new ObjectOutputStream(socketForConnection.getOutputStream());
				while(true){
					objectClientOutput.writeObject(pointerToGameRoom.getListOfPlayers());
					if(this.getPlayerHandCards().size() == 5){
						objectClientOutput.writeObject(this.getPlayerHandCards().getLast());//last card
					}
				}
			} catch (IOException e) {
				System.out.println("Human player:"+this.getPlayerName()+" has left the game");
			}
			
		}
		
	}
	
	
	
}
