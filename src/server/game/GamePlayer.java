package server.game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Player;

public class GamePlayer extends Player implements Runnable {
	
	private Socket socketForConnection;
	private ObjectOutputStream objectClientOutput;

	public Socket getSocketForConnection() {
		return socketForConnection;
	}

	public void setSocketForConnection(Socket socketForConnection) {
		this.socketForConnection = socketForConnection;
	}

	public GamePlayer(String playerName, Socket socketForConnection) {
		super(playerName);
		this.socketForConnection = socketForConnection;
	}

	@Override
	public void run() {
		while(true){
			try {
				objectClientOutput = new ObjectOutputStream(socketForConnection.getOutputStream());
				//objectClientOutput.writeObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
}
