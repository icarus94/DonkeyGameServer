package server.game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

import server.*;
import server.connection.ClientServerConnection;
import server.startup.ServerStartup;


public class ServerControl implements Runnable{
	
	public static volatile LinkedList<ServerGameRoom> listOfGameRooms = new LinkedList<ServerGameRoom>();
	private Socket socketForConnection;
	private ServerGameRoom gameRoomForInputControl;
	private Player playerForControl;
	
	
	@Override
	public void run() {
		for (int i = 0; i < ServerStartup.clientConnectionList.size(); i++) {
			if(ServerStartup.clientConnectionList.get(i).getSocketForConnection().equals(socketForConnection)){
				//PITAJ DA LI SE OVAKO POREDI ILI SA ==
			}
		}
		
		try {
			 BufferedReader inputClient = new BufferedReader(new InputStreamReader(socketForConnection.getInputStream()));
			 while(true){
				 String inputStringFromClient = null;
				 while(inputStringFromClient == null){
					 inputStringFromClient = inputClient.readLine();
				 }
				 if(inputStringFromClient == "drop"){
					 for (int i = 0; i < gameRoomForInputControl.getListOfPlayers().size(); i++) {
						if(playerForControl.equals(gameRoomForInputControl.getListOfPlayers().get(i))){
							gameRoomForInputControl.getListOfPlayers().get(i).setAreCardsDropped(true);
						}
					}
				 }
				 if(inputStringFromClient.equals("quitGameRoom")){//Needs to be implemented on clientSide
					 
				 }
				 //if(convertStringToCard(inputStringFromClient) != null){}
				 
			 }
		} catch (IOException e) {
			System.out.println("Lost connection in ServerControl");
		}
		
	}
	
	/**
	 * Constructor for quickGame
	 * @param conn
	 * @param player
	 */
	public ServerControl(Socket conn, Player player){
		this.socketForConnection=conn;
		for (int i = 0; i <listOfGameRooms.size(); i++) {
			if(listOfGameRooms.get(i).getListOfPlayers().size() == 3){
				listOfGameRooms.get(i).getListOfPlayers().addLast((GamePlayer) player);//mora da ubaci igraca
			}
		}
		for (int i = 0; i <listOfGameRooms.size(); i++) {
			if(listOfGameRooms.get(i).getListOfPlayers().size() == 2){
				listOfGameRooms.get(i).getListOfPlayers().addLast((GamePlayer) player);//mora da ubaci igraca
			}
		}
		for (int i = 0; i <listOfGameRooms.size(); i++) {
			if(listOfGameRooms.get(i).getListOfPlayers().size() == 1){
				listOfGameRooms.get(i).getListOfPlayers().addLast((GamePlayer) player);//mora da ubaci igraca
			}
		}
		String nameOfNewGameRoom = "RandomGeneratedRoom";
		int counter = 1;
		while(true){
			boolean exists = false;
			for (int i = 0; i < listOfGameRooms.size(); i++) {
				if(listOfGameRooms.get(i).getName().equals(nameOfNewGameRoom)){
					exists = true;
					break;
				}
			}
			if(!exists){
				ServerGameRoom sgr = new ServerGameRoom(nameOfNewGameRoom,"",3,player);
				listOfGameRooms.addFirst(sgr);
				new Thread(sgr).start(); //PITAJ SOSKETA ZA OVO !!!!!!!!! da li u linkedLIST ostaje pokazivac na pokrenutu instancu
			}
			nameOfNewGameRoom = nameOfNewGameRoom+counter;
			counter++;
		}	
	}
	
	/**
	 * For the new Game
	 * @param conn
	 * @param nameOfNewGameRoom
	 * @param gamePassword
	 * @param numberOfBots
	 * @param player
	 */
	public ServerControl(Socket conn,String nameOfNewGameRoom,String gamePassword,int numberOfBots,Player player){
		this.socketForConnection = conn;
		listOfGameRooms.addFirst(new ServerGameRoom(nameOfNewGameRoom,gamePassword,numberOfBots,player));
		new Thread(listOfGameRooms.getFirst()).start();
	}
	
	/**
	 * For conneting to existing serverRoom
	 * @param conn
	 * @param nameOfNewGameRoom
	 * @param gamePassword
	 * @param player
	 */
	public ServerControl(Socket conn,String nameOfNewGameRoom,String gamePassword,Player player){
		this.socketForConnection = conn;
		for (int i = 0; i < listOfGameRooms.size(); i++) {
			if(listOfGameRooms.get(i).getName().equals(nameOfNewGameRoom) && listOfGameRooms.get(i).getPassword().equals(gamePassword)
					&& listOfGameRooms.get(i).getListOfPlayers().size() < 4){
				listOfGameRooms.get(i).getPlayers().add(player);
			}
		}
		try {
			ObjectOutputStream objectClientOutput = new ObjectOutputStream(conn.getOutputStream());
			objectClientOutput.writeObject(new String("Room full or done playing"));
		} catch (IOException e) {
			System.out.println("Lost connection while trying to find wanted Gameroom");
		}
	}
	/**
	 * Creates LinkedList of DGame from ServerGameRoom
	 * @return
	 */
	public static LinkedList<DGame> listOfGameRoomsTypeDGame(){
		LinkedList<DGame> listOfRoomsTypeDgame = new LinkedList<>();
		for (int i = 0; i < ServerControl.listOfGameRooms.size(); i++) {
			listOfRoomsTypeDgame.addFirst(new DGame(ServerControl.listOfGameRooms.get(i).getPassword(), ServerControl.listOfGameRooms.get(i).getName()));
			listOfRoomsTypeDgame.getFirst().setPlayers(ServerControl.listOfGameRooms.get(i).getPlayers());
		}
		return listOfRoomsTypeDgame;
	}
	
	
	
}
