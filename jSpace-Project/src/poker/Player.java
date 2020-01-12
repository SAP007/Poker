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
	public Player( String name,int balance, int playerId) {
		this.name = name;
		this.balance = balance;
		this.playerId= playerId + 1;
	}

	public static void main(String[] args) throws InterruptedException {

		try {

			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			// Set the URI of the chat space
			// Default value
			System.out.print("Enter URI of the chat server or press enter for default: ");
			String uri = input.readLine();
			// Default value
			if (uri.isEmpty()) {
				uri = "tcp://127.0.0.1:9001" + "/lobby?keep";
			}

			// Connect to the remote chat space
			System.out.println("Connecting to chat space " + uri + "...");
			RemoteSpace lobby = new RemoteSpace(uri);

			// create your player
			System.out.print("Enter your name: ");
			String name = input.readLine();
			System.out.print("Enter your balance: ");
			int balance = Integer.parseInt(input.readLine());

			Player player = new Player((String) name , (int) balance, Player.playerId);
			

			// Keep sending user name and call, raise fold
			System.out.println("make a bet...");
			while (true) {
				int bet = Integer.parseInt(input.readLine());
				lobby.put(player.name, player.balance, Player.playerId, bet);
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