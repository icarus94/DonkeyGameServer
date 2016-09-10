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
import server.game.ServerGameRoom;

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
			//
			System.out.println(socketForConnection.getInetAddress().toString());
            System.out.println(socketForConnection.getLocalAddress().toString());
            System.out.println(socketForConnection.getPort());
            System.out.println(socketForConnection.getLocalPort());
            System.out.println(socketForConnection.getRemoteSocketAddress().toString());
			//
			inputClient = new BufferedReader(new InputStreamReader(socketForConnection.getInputStream()));
			objectClientOutput = new ObjectOutputStream(socketForConnection.getOutputStream());
			
			ServerGameRoom existingGameForThisPlayer = checkForUnfinishedGame();
			if(existingGameForThisPlayer != null){
				objectClientOutput.writeObject(new String("Do you want to reconnect to:"+existingGameForThisPlayer.getName()+"as"
			+checkForPlayerNameInUnfinishedGame()));
				String input = null;
				while(input == null){
					input = inputClient.readLine();
				}
				if(input.equals("yes")){
					//method for reconnecting
				}
			}
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
								objectClientOutput.writeObject(new String("serverNameUsed")); 
								break mainLoop;
							}
						}
						objectClientOutput.writeObject(new String("ok"));
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
						boolean serverReady = false;
						for (int i = 0; i < ServerControl.listOfGameRooms.size(); i++) {
							if(ServerControl.listOfGameRooms.get(i).getName().equals(serverName) &&
									ServerControl.listOfGameRooms.get(i).getListOfPlayers().size() < 4 &&
									ServerControl.listOfGameRooms.get(i).getPassword().equals(serverPassword)){
								objectClientOutput.writeObject(new String("ok"));
								serverReady = true;
								break;
							}
						}
						if(!serverReady){
							objectClientOutput.writeObject(new String("server done playing or full or inccorect password"));
							System.out.println("server done playing or full or inccorect password for client "+playerName);
							break mainLoop;
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
	public ServerGameRoom checkForUnfinishedGame(){
		for (int i = 0; i < ServerControl.listOfGameRooms.size(); i++) {
			for (int j = 0; j < ServerControl.listOfGameRooms.get(i).getListOfPlayers().size(); j++) {
				if(ServerControl.listOfGameRooms.get(i).getListOfPlayers().get(j).getSocketAddressOfPlayer().equals(socketForConnection.getRemoteSocketAddress()) &&
						ServerControl.listOfGameRooms.get(i).getListOfPlayers().get(j).getPortOfPlayer() == socketForConnection.getPort()){
					return ServerControl.listOfGameRooms.get(i);
				}
			}
		}
		return null;
	}
	public String checkForPlayerNameInUnfinishedGame(){
		for (int i = 0; i < ServerControl.listOfGameRooms.size(); i++) {
			for (int j = 0; j < ServerControl.listOfGameRooms.get(i).getListOfPlayers().size(); j++) {
				if(ServerControl.listOfGameRooms.get(i).getListOfPlayers().get(j).getSocketAddressOfPlayer().equals(socketForConnection.getRemoteSocketAddress()) &&
						ServerControl.listOfGameRooms.get(i).getListOfPlayers().get(j).getPortOfPlayer() == socketForConnection.getPort()){
					return ServerControl.listOfGameRooms.get(i).getListOfPlayers().get(j).getPlayerName();
				}
			}
		}
		return null;
	}

}
