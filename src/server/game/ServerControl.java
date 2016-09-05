package server.game;
import java.net.Socket;
import java.util.LinkedList;

import server.*;


public class ServerControl implements Runnable{
	
	public static volatile LinkedList<ServerGameRoom> listOfGameRooms = new LinkedList<ServerGameRoom>();
	private Socket socketForConnection;
	
	@Override
	public void run() {
		
		
	}
	public ServerControl(Socket conn, Player player){//for quickgame
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
		//napravi novu sobu ako nista ne prodje
	}
	
	public ServerControl(Socket conn,String nameOfNewGameRoom,String gamePassword,String numberOfBots,Player player){//for newGame
		this.socketForConnection = conn;
		listOfGameRooms.addFirst(new ServerGameRoom(nameOfNewGameRoom,gamePassword,numberOfBots,player));
		new Thread(listOfGameRooms.getFirst()).start();
	}
	
	public ServerControl(Socket conn,String nameOfNewGameRoom,String gamePassword,Player player){//for conneting to existing serverRoom
		this.socketForConnection = conn;
		for (int i = 0; i < listOfGameRooms.size(); i++) {
			if(listOfGameRooms.get(i).getName().equals(nameOfNewGameRoom) && listOfGameRooms.get(i).getPassword().equals(gamePassword)
					&& listOfGameRooms.get(i).getListOfPlayers().size() < 4){
				listOfGameRooms.get(i).getPlayers().add(player);
			}
		}
		
	}
	
	public static LinkedList<DGame> listOfGameRoomsTypeDGame(){
		LinkedList<DGame> listOfRoomsTypeDgame = new LinkedList<>();
		for (int i = 0; i < ServerControl.listOfGameRooms.size(); i++) {
			listOfRoomsTypeDgame.addFirst(new DGame(ServerControl.listOfGameRooms.get(i).getPassword(), ServerControl.listOfGameRooms.get(i).getName()));
			listOfRoomsTypeDgame.getFirst().setPlayers(ServerControl.listOfGameRooms.get(i).getPlayers());
		}
		return listOfRoomsTypeDgame;
	}
	
	
	
}
