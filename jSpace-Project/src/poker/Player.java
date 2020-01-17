package poker;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
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

	// Constructor
	public Player(int playerId, String name, int balance) {
		this.playerId = playerId;
		this.name = name;
		this.balance = balance;
	}

	public static void main(String[] args) throws InterruptedException {

		try {

			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			// Set the URL of the lobby space
			String uri = "tcp://localhost:9003/lobby?keep";

			// Connect to the remote chat space
			System.out.println("Connecting to poker lobby wait room " + uri + "...");
			RemoteSpace lobby = new RemoteSpace(uri);

			System.out.print("Enter your name: ");
			String name = input.readLine();

			// put request to join
			lobby.put("join", name);

			// playerid, name ,gameLobby, boardLobby
			Object[] playerinfo = lobby.get(new FormalField(Integer.class), // player
																			// id
					new ActualField(name), // match the name
					new FormalField(Integer.class), // balance
					new FormalField(String.class), // get gamelobby
					new FormalField(String.class) // get boardLobby
			);

			// query on game for turn
			RemoteSpace gameLobby = new RemoteSpace((String) playerinfo[3]);

			// holds what is on the board, 5 cards one pot
			RemoteSpace boardLobby = new RemoteSpace((String) playerinfo[4]);

			// Keep sending username and call, raise fold
			System.out.println("make a bet...");
			while (true) {
				int bet = Integer.parseInt(input.readLine());
				// (id, name, balance, card1, card2, rcf )

				createPlayer(playerinfo, gameLobby, input);

				new Thread(new displayHandler(uri)).start();

			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/*
	 * Creates the player object, and puts information into the gamelobby
	 * */
	public static void createPlayer(Object[] playerInfo, RemoteSpace gameLobby, BufferedReader input)
			throws InterruptedException, NumberFormatException, IOException {
		// send info to gameLobby space
		// userinput for check raise fold

		Player player = new Player((int) playerInfo[0], (String) playerInfo[1], (int) playerInfo[2]);
		System.out.println();
		int userInput = Integer.parseInt(input.readLine());
		Object[] t = { 0, 0 };
		Object[] x = new Object[6];
		gameLobby.put(player.getPlayerId(), // player id
				player.getName(), // name
				player.balance, // balance
				t[0], // empty card
				t[1], // empty card
				userInput); // check raise fold
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	
	/*
	 * raisePot controls that the amount being raised with is legal, 
	 * and changes the balance. 
	 * */
	public int raisePot(int raiseAmount) {
		if (raiseAmount > 0 && raiseAmount <= getBalance()) {
			setBalance(getBalance() - raiseAmount);
			return raiseAmount;
		} else {
			setBalance(0);
			return raiseAmount;
		}
	}
}

class turnHandler implements Runnable {
	private RemoteSpace gameLobby;
	private Player player;
	private int userBet;
	
	// inorder to get input from player
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public void run() {
		while (true) {
			try {
				Object[] info = gameLobby.query(new ActualField(player.getPlayerId()), // PlayerId
						new ActualField(player.getName()), // player name
						new FormalField(Integer.class), // players balance
						new FormalField(Object.class), // first card
						new FormalField(Object.class), // second card
						new ActualField(-2) // Raise, check, Fold
				);
				
				
				//raise check fold 
				userBet =  Integer.parseInt(input.readLine());
				
				/*
				 * put back to the gamelobby the raise fold check 
				 * name and id new balance and cards.
				 * 
				 * */

				gameLobby.put(
						player.getPlayerId(), // playerid
						player.getName(), 	// name
						player.getBalance(), // balance
						info[3],	// fisrt card
						info[4], 	// second card
						userBet		// check raise fold
						);
				
				 
				
				/*
				 * List<Object[]> playerInfo = gameLobby.queryAll(new
				 * FormalField(Integer.class), // Player // Id new
				 * FormalField(String.class), // player name new
				 * FormalField(Integer.class), // players balance new
				 * FormalField(Integer.class), // first card new
				 * FormalField(Integer.class), // second card new
				 * FormalField(Integer.class) // Raise, check, Fold );
				 */

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	
	
	
}

class displayHandler implements Runnable {
	private RemoteSpace lobby;
	private RemoteSpace board;

	public displayHandler(String lobbyUrl) throws UnknownHostException, IOException {
		lobby = new RemoteSpace(lobbyUrl);
	}

	public void run() {
		try {

			// keep updating player info and printing them
			while (true) {
				wipeConsole();
				getboardState(board);
				Thread.sleep(1500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void getboardState(RemoteSpace board) throws InterruptedException {
		List<Object[]> boardInfo = board.queryAll(new FormalField(Object.class), // first
																					// card
				new FormalField(Object.class), // second card
				new FormalField(Object.class), // third card
				new FormalField(Object.class), // fourth card
				new FormalField(Object.class), // fifth card
				new FormalField(Integer.class)); // pot
		for (Object[] boardState : boardInfo) {
			System.out.println(
					"[ " + boardState[0] + " ] " + "[ " + boardState[1] + " ] " + "[ " + boardState[2] + " ] " + "[ "
							+ boardState[3] + " ] " + "[ " + boardState[4] + " ] " + " " + boardState[5] + " $ ");
		}

	}

	public static void wipeConsole() {
		for (int i = 0; i < 15; i++) {
			System.out.println();
		}
	}

}
