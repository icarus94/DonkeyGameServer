package server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

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
			outputClient = new PrintStream(socketForConnection.getOutputStream());
			//objectClientOutput = new ObjectOutputStream(socketForConnection.getOutputStream());
			outputClient.println("cao");
			while(true){
				
				switch(inputClient.readLine()){
					case "refreash": continue;
					case "connectgame": break;
				}
			}
		} catch (IOException e) {
			System.out.println("Connection with client is lost!");
		}
		
	}
	
	

}
