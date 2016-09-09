package server.connection;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

import server.*;
import server.game.ServerControl;

public class ClientServerConnection implements Runnable {
	
	private Socket socketForConnection;
	private BufferedReader inputClient;
	private ObjectOutputStream objectClientOutput;
	
	/**
	 * Can be used for thread ID
	 * @return
	 */
	public Socket getSocketForConnection() {
		return socketForConnection;
	}
	/**
	 * Constructor
	 * @param conn
	 */
	public ClientServerConnection(Socket conn) {
		this.socketForConnection=conn;
	}
	
	/**
	 * For communication with MenuWindow in Client
	 */
	public void run() {
		try {
			
			inputClient = new BufferedReader(new InputStreamReader(socketForConnection.getInputStream()));
			objectClientOutput = new ObjectOutputStream(socketForConnection.getOutputStream());
			
			while(true){
				objectClientOutput.writeObject(ServerControl.listOfGameRoomsTypeDGame());//sending list of GameRooms instanceof DGame
				
				String inputStringFromClient = null;
				while(inputStringFromClient == null){
					 inputStringFromClient = inputClient.readLine();
				}
				String playerName = null,serverName = null,serverPassword = null,numberOfBots = null;
				mainLoop:
				switch(inputStringFromClient){
					case "refresh": break;
					case "quickgame": {
						while(playerName == null){
							playerName = inputClient.readLine();
						}
						new Thread(new ServerControl(socketForConnection,new Player(playerName, this.socketForConnection))).start();
						break;
					}
					case "newGameRoom": {
						while(playerName == null){
							playerName = inputClient.readLine();
						}
						while(serverName == null){
							serverName = inputClient.readLine();
						}
						for (int i = 0; i < ServerControl.listOfGameRooms.size(); i++) {
							if(ServerControl.listOfGameRooms.get(i).getName() == serverName){
								objectClientOutput.writeObject(new String("serverNameUsed")); // Needs to be implemented on client side
								break mainLoop;
							}
						}
						while(serverPassword == null){
							serverPassword = inputClient.readLine();
						}
						while(numberOfBots == null){
							numberOfBots = inputClient.readLine();
						}
						int numberOfBotsTypeInt = Integer.parseInt(numberOfBots); //Parsing to INT
						new Thread(new ServerControl(socketForConnection,serverName,serverPassword,numberOfBotsTypeInt,
								new Player(playerName, this.socketForConnection))).start();
						break;
					} 
					case "connectToGameRoom":{
						while(playerName == null){
							playerName = inputClient.readLine();
						}
						while(serverName == null){
							serverName = inputClient.readLine();
						}
						while(serverPassword == null){
							serverPassword = inputClient.readLine();
						}
						new Thread(new ServerControl(socketForConnection,serverName,serverPassword,
								new Player(playerName, this.socketForConnection))).start();
						break;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Connection with client is lost!");
		}
		
	} 
	
	

}
