package poker;

import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class Player {
	// fields
	private int balance;
	private String name;
	private int playerId;
	// default constructor
	public Player() {
		balance = 0;
		name = "";
	}

	//Constructor
	public Player(String name, int balance) {
		this.name = name;
		this.balance = balance;
	}

	public static void main(String[] args) throws InterruptedException {

		try {

			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			// Set the URI of the chat space
	
			String uri = "tcp://localhost:9003/lobby?keep";

			// Connect to the remote chat space
			System.out.println("Connecting to poker lobby " + uri + "...");
			RemoteSpace lobby = new RemoteSpace(uri);

			// create your player
			System.out.print("Enter your name: ");
			String name = input.readLine();
			System.out.print("Enter your balance: ");
			int balance = Integer.parseInt(input.readLine());

			Player player = new Player((String) name , (int) balance);
			

			// Keep sending user name and call, raise fold
			System.out.println("make a bet...");
			while (true) {
				int bet = Integer.parseInt(input.readLine());
				lobby.put(player.name, player.balance, player.playerId, bet);				
				
				List<Object[]> info = lobby.queryAll(new FormalField(String.class), new FormalField(Integer.class));
				System.out.println("plays made:");
				for(Object[] playerInfo : info){
		            System.out.println("player: " + playerInfo[0] + " placed: " + playerInfo[1]);

				}
				
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}