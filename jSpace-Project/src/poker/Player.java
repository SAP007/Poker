package poker;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class Player {
	// fields
	int balance;
	String name;
	int playerId;
	Object[] x1;
	Object[] x2;
	static int state;
	static int lastBet;
	static Player player;
	static int totalBet;

	// Constructor
	public Player(int playerId, String name, int balance, Object[] card1, Object[] card2, int state, int lastBet, int totalBet) {
		this.playerId = playerId;
		this.name = name;
		this.balance = balance;
		this.x1 = card1;
		this.x2 = card2;
		this.state = state;
		this.lastBet = lastBet;
		this.totalBet = totalBet;
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
			System.out.println(playerinfo[3]);
			
			
			
			// query on game for turn
			RemoteSpace gameLobby = new RemoteSpace((String) playerinfo[3]);

			// holds what is on the board, 5 cards one pot
			RemoteSpace boardLobby = new RemoteSpace((String) playerinfo[4]);

			// Keep sending username and call, raise fold
			//System.out.println("make a bet...");
			System.out.println("f�r player");
			player = createPlayer(playerinfo, gameLobby, input);
			
			System.out.println("Player id efter creation :" + player.playerId);
			new Thread(new turnHandler(player, gameLobby)).start();
//			while (true) {
//			//	int bet = Integer.parseInt(input.readLine());
//				// (id, name, balance, card1, card2, rcf )
//				
//				waitforTurn(player, input, gameLobby);
//				System.out.println("efter vent for tur");
//				Thread.sleep(600);
//
//				//new Thread(new displayHandler(uri)).start();
//
//			}

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
	public static Player createPlayer(Object[] playerInfo, RemoteSpace gameLobby, BufferedReader input)
			throws InterruptedException, NumberFormatException, IOException {
		// send info to gameLobby space
		// userinput for check raise fold
		Player player = new Player((int) playerInfo[0], (String) playerInfo[1], (int) playerInfo[2], (Object[]) null, (Object[]) null, (int) state, lastBet, totalBet);
		
		Object[] t = { 0, 0 };
		player.playerId = (int) playerInfo[0];
		player.name = (String) playerInfo[1];
		player.x1 = t;
		player.x2 = t;
		player.balance = 5000;
		player.state = -1;
		player.lastBet = 0;
		player.totalBet = 0;
		gameLobby.put(player.playerId, player.name, player.balance, player.x1, player.x2, player.state, player.lastBet, player.totalBet); // check raise fold
		return player;
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
	public static void waitforTurn(Player player, BufferedReader input, RemoteSpace gameLobby)
			throws InterruptedException, NumberFormatException, IOException {
		Object[] playerinfo = gameLobby.get(new ActualField(player.getPlayerId()), // PlayerId
				new ActualField(player.getName()), // player name
				new FormalField(Integer.class), // players balance
				new FormalField(Object.class), // first card
				new FormalField(Object.class), // second card
				new ActualField(-3), // Raise, check, Fold
				new FormalField(Integer.class),
				new FormalField(Integer.class)
		);
		
		System.out.println("Get player id : " +playerinfo[0]);
		System.out.println("Get name : " +playerinfo[1]);
		System.out.println("Get balance : " +playerinfo[2]);
		System.out.println("Get first card : " +playerinfo[3]);
		System.out.println("Get second card : " +playerinfo[4]);
		System.out.println("Get raise check fold  : " +playerinfo[5]);
		System.out.println("Get last bet : " + playerinfo[6]);
		
		
			player.state = (int) playerinfo[5];
		
			System.out.println("player state = " +  player.state);
			
			
		if ((int) playerinfo[5] == -2) {
			System.out.println("�H NEJ");
			// raise check fold

		}
	}
}

class turnHandler implements Runnable {
	private RemoteSpace gameLobby;
	private Player player;
	private int userBet;
	
	// inorder to get input from player
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	public turnHandler(Player player, RemoteSpace gameLobby) {
		this.player = player;
		this.gameLobby = gameLobby;
	}
	
	@Override
	public void run() {
		System.out.println(player.getName());
		Object[] info = null;
		//get cards in first run.
		System.out.println("first run");
		try {
			Object[] playerPlaceholder = gameLobby.get(new ActualField(player.getPlayerId()), // PlayerId
							new ActualField(player.getName()), // player name
							new FormalField(Integer.class), // players balance
							new FormalField(Object.class), // first card
							new FormalField(Object.class), // second card
							new ActualField(-2), // Raise, check, Fold
							new FormalField(Integer.class),
							new FormalField(Integer.class)
					);
			System.out.println("player id: " + playerPlaceholder[0]);
			player.playerId = (int) playerPlaceholder[0];
			player.name = (String) playerPlaceholder[1];
			player.balance = (int) playerPlaceholder[2];
			player.x1 = (Object[]) playerPlaceholder[3];
			player.x2 = (Object[]) playerPlaceholder[4];
			player.state = -3;
			playerPlaceholder[6] = 1;
			player.lastBet = (int) playerPlaceholder[6];
			player.totalBet = (int) playerPlaceholder[7];
			playerPlaceholder[5] = -3;

			System.out.println("got from game lobby");
			System.out.println("Fetched player to give cards: " + playerPlaceholder);
			
			// test
			
			gameLobby.put(
					playerPlaceholder[0], // playerid
					playerPlaceholder[1], 	// name
					playerPlaceholder[2], // balance
					playerPlaceholder[3],	// fisrt card
					playerPlaceholder[4], 	// second card
					playerPlaceholder[5],	// check raise fold
					playerPlaceholder[6],		//last bet
					playerPlaceholder[7]
					);
			
			System.out.println("lagt mig tilbage");
			
			System.out.println("gave player back: " + playerPlaceholder);
			
			
			
			
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		while (true) {
			try {
				info = gameLobby.get(new ActualField(player.getPlayerId()), // PlayerId
						new ActualField(player.getName()), // player name
						new FormalField(Integer.class), // players balance
						new FormalField(Object.class), // first card
						new FormalField(Object.class), // second card
						new ActualField(-2), // Raise, check, Fold
						new FormalField(Integer.class), // Last bet
						new FormalField(Integer.class)
				);
				
				System.out.println("Fetched player to start turn: " + info);
				
				
				
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		System.out.println("Spiller hentet");
			System.out.println(info[1]);
			System.out.println(info[5]);
				//raise check fold 
				try {
					int bets = 0;
					if((int)info[6] > 0) {
						System.out.println("Call or Raise, the current bet is: " + info[6] + " and you have bet: " + info[7]);
						userBet =  Integer.parseInt(input.readLine());
						bets = userBet + (int)info[7];
						while(bets < (int)info[6] && userBet != -1) {
							System.out.println("Your bid, is lower than the current bid. Please try again");
							System.out.println("Call or Raise, the current bet is: " + info[6]);
							userBet =  Integer.parseInt(input.readLine());
							bets = userBet + (int)info[7];
						}
					}
					else if((int)info[6] == 0 ) {
						System.out.println("Check or Raise");
						userBet =  Integer.parseInt(input.readLine());
						while(userBet <= -2) {
							System.out.println("Your bid, is not valid, please try again");
							System.out.println("Check or Raise");
							userBet =  Integer.parseInt(input.readLine());
						}
					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				/*
				 * put back to the gamelobby the raise fold check 
				 * name and id new balance and cards.
				 * 
				 * */
				info[5] = userBet;
				if(userBet > 0) {
				info[7] = (int)info[7] + userBet;
				}
				try {
					System.out.println("putting player back");
					gameLobby.put(
							info[0], 	// playerid
							info[1], 	// name
							info[2],	 // balance
							info[3],	// fisrt card
							info[4], 	// second card
							info[5],	// check raise fold
							info[6],		//last bet
							info[7]
							);

					System.out.println("Get player id : " +info[0]);
					System.out.println("Get name : " +info[1]);
					System.out.println("Get balance : " +info[2]);
					System.out.println("Get first card : " + info[3]);
					System.out.println("Get second card : " +info[4]);
					System.out.println("Get raise check fold  : " +info[5]);
					System.out.println("Get last bet : " +info[6]);
					
					
					Thread.sleep(60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 
				
				/*
				 * List<Object[]> playerInfo = gameLobby.queryAll(new
				 * FormalField(Integer.class), // Player // Id new
				 * FormalField(String.class), // player name new
				 * FormalField(Integer.class), // players balance new
				 * FormalField(Integer.class), // first card new
				 * FormalField(Integer.class), // second card new
				 * FormalField(Integer.class) // Raise, check, Fold );
				 */
				System.out.println("Done with turn");
			
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
		List<Object[]> boardInfo = board.queryAll(new FormalField(Object.class), // firstcard
				new FormalField(Object.class), // second card
				new FormalField(Object.class), // third card
				new FormalField(Object.class), // fourth card
				new FormalField(Object.class), // fifth card
				new FormalField(Integer.class)); // pot
		
		
		
		
		for (Object[] boardState : boardInfo) {
			System.out.println(
					"[ " + boardState[0] + " ] " + "[ " + boardState[1] + " ] " + "[ " + boardState[2] + " ] " + "[ "
							+ boardState[3] + " ] " + "[ " + boardState[4] + " ] " + " " + boardState[5] + " $ ");
			System.out.println("Get firstcard  : " + boardState[0]);
			System.out.println("Get second : " + boardState[1]);
			System.out.println("Get third : " + boardState[2]);
			System.out.println("Get fourth : " + boardState[3]);
			System.out.println("Get fifth : " + boardState[4]);
			System.out.println("Get pot  : " + boardState[5]);
			
			
		}

	}

	public static void wipeConsole() {
		for (int i = 0; i < 15; i++) {
			System.out.println();
		}
	}

}
