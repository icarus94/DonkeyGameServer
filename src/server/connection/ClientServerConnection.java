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
	private PrintStream outputClient;
	private ObjectOutputStream objectClientOutput;
	
	
	public ClientServerConnection(Socket conn) {
		this.socketForConnection=conn;
	}
	
	@Override
	public void run() {
		try {
			
			inputClient = new BufferedReader(new InputStreamReader(socketForConnection.getInputStream()));
			//outputClient = new PrintStream(socketForConnection.getOutputStream());
			objectClientOutput = new ObjectOutputStream(socketForConnection.getOutputStream());
			//outputClient.println("cao");
			
			while(true){
				objectClientOutput.writeObject(ServerControl.listOfGameRoomsTypeDGame());
				String inputStringFromClient = null;
				while(inputStringFromClient == null){
					 inputStringFromClient = inputClient.readLine();
				}
				String playerName = null,serverName = null,serverPassword = null,numberOfBots = null;
				mainLoop:
				switch(inputStringFromClient){
					case "refresh": continue;
					case "quickgame": {
						while(playerName == null){
							playerName = inputClient.readLine();
						}
						new Thread(new ServerControl(socketForConnection,new Player(playerName))).start(); 
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
								objectClientOutput.writeObject("serverNameUsed");
								break mainLoop;
							}
						}
						while(serverPassword == null){
							serverPassword = inputClient.readLine();
						}
						while(numberOfBots == null){
							numberOfBots = inputClient.readLine();
						}
						new Thread(new ServerControl(socketForConnection,serverName,serverPassword,numberOfBots,new Player(playerName))).start();
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
						new Thread(new ServerControl(socketForConnection,serverName,serverPassword,new Player(playerName))).start();
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Connection with client is lost!");
		}
		
	} 
	
	

}
