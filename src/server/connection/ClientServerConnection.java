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
				objectClientOutput.writeObject(new LinkedList<DGame>(ServerControl.listOfGameRooms));
				switch(inputClient.readLine()){
					case "refreash": continue;
					case "quickgame": new Thread(new ServerControl(socketForConnection)) ;
					case "newGameRoom": new Thread(new ServerControl(socketForConnection,inputClient.readLine(),"new"));
					case "connectToGameRoom":new Thread(new ServerControl(socketForConnection,inputClient.readLine(),"existing"));
				}
			}
		} catch (IOException e) {
			System.out.println("Connection with client is lost!");
		}
		
	}
	
	

}
