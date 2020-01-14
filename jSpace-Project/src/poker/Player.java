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

	// Constructor
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

			// create a player
			System.out.print("Enter your name: ");
			String name = input.readLine();
			System.out.print("Enter your balance: ");
			int balance = Integer.parseInt(input.readLine());

			Player player = new Player((String) name, (int) balance);

			// Keep sending username and call, raise fold
			System.out.println("make a bet...");
			while (true) {
				int bet = Integer.parseInt(input.readLine());
				
				//(id, navn, balance, kort1, kort2, r positive number /c zero /f -1 )
				lobby.put(player.name, player.balance, player.playerId, bet);

				new Thread(new displayHandler(uri, name)).start();
			
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

class displayHandler implements Runnable {
	private RemoteSpace lobby;
	private RemoteSpace board;

	public displayHandler(String lobbyUrl, String boardUrl) throws UnknownHostException, IOException {
		lobby = new RemoteSpace(lobbyUrl);
		board = new RemoteSpace(boardUrl);

	}

	public void run() {
		try {

			// keep updating player info and printing them
			while (true) {
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();

				List<Object[]> boardInfo = board.queryAll(
						new FormalField(Integer.class),  // first card
						new FormalField(Integer.class),  // second card
						new FormalField(Integer.class),  // third card
						new FormalField(Integer.class),  // fourth card
						new FormalField(Integer.class),  // fifth card
						new FormalField(Integer.class)); // pot
				for (Object[] boardState : boardInfo) {
					System.out.println("[ " + boardState[0] + " ] " +"[ " + boardState[1] + " ] " +"[ " + boardState[2] + " ] " +"[ " + boardState[3] + " ] " +"[ " + boardState[4] + " ] " +" " + boardState[5] + " $ " );
				}

				List<Object[]> info = board.queryAll(
						new FormalField(Integer.class), // Player Id 
						new FormalField(String.class),  // player name
						new FormalField(Integer.class), // players balance 
						new FormalField(Integer.class), // first card
						new FormalField(Integer.class), // second card
						new FormalField(Integer.class)  // Raise, check, Fold
						);
				System.out.println("plays made:");
				for (Object[] playerInfo : info) {
					System.out.println("player id: " + playerInfo[0] + " name: " +
							playerInfo[1] + "balance: " + playerInfo[2] + " firstCard: " + 
							playerInfo[3] + " secondCard: " + playerInfo[4] + " R/C/F: " + playerInfo[5] );
				}

				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
