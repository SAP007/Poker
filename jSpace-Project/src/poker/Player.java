package poker;

import org.jspace.RemoteSpace;
import org.jspace.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class Player {
	// fields
	private int balance;
	private String name;
	private static int playerId = 0;

	// default constructor
	public Player() {
		balance = 0;
		name = "";
		playerId = 0;
	}

	//Constructor
	public Player( String name,int balance) {
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
				System.out.println("name: "+ name.getClass()+ "balance: "+ balance + "bet: " + bet);
				lobby.put(player.name, player.balance, player.playerId,bet);
				System.out.println("byyeeeee ");
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