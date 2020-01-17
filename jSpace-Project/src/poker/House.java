package poker;

import org.jspace.RandomSpace;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.FormalField;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;

public class House {
	private final static int BALANCE = 1000; 
	private static int playerId = 0;
	public static void main(String[] args) throws InterruptedException {
		SpaceRepository spaceRepo = new SpaceRepository();
		RandomSpace deck = new RandomSpace();
		SequentialSpace board = new SequentialSpace();

		spaceRepo(spaceRepo);
		System.out.println("SpaceRepo created");
		deckSpace(spaceRepo, deck);
		System.out.println("deckSpace created");
		createLobby(spaceRepo);
		createBoard(spaceRepo, board);

	}

	public static void spaceRepo(SpaceRepository spaceRepo) {
		// Set the URI of the spaceRepo
		String url = "tcp://localhost:9003/?keep";
		
		
		// Open a gate
		spaceRepo.addGate("tcp://localhost:9003/?keep");
		System.out.println("Space repository opened at: " + url + "...");

	}

	public static void deckSpace(SpaceRepository spaceRepo, RandomSpace deck) {
		spaceRepo.add("deck", deck);
	}
	
	public static void createLobby(SpaceRepository spaceRepo) throws InterruptedException {

		// creates a lobby for players
		SequentialSpace lobby = new SequentialSpace();

		// Add the space to the repository
		spaceRepo.add("lobby", lobby);

		while (true) {
			canJoin(lobby, spaceRepo, lobby, lobby);

//			System.out.println("get from client");
			Object[] t = lobby.get(
					new FormalField(Integer.class), // playerID
					new FormalField(String.class), // name
					new FormalField(Integer.class), // balance
					new FormalField(Integer.class) // bet
			);
			lobby.put(t[1], t[3]);

//			System.out.println(
//					"player id: " + t[0] + ": name: " + t[1] + " balance: " + t[2] + "$" + " bet " + t[3] + "$");
//			System.out.println("done");
		}
	}

	public static void createBoard(SpaceRepository spaceRepo, SequentialSpace board) {
		spaceRepo.add("board", board);
	}

	public void updateBoardAfterPlayerAction(Object[] playerActions, SequentialSpace board)
			throws InterruptedException {
		board.put(playerActions[0], // ID
				playerActions[1], // NAME
				playerActions[2], // Balance
				playerActions[3], // Card 1
				playerActions[4], // Card 2
				playerActions[5]); // Check/raise/fold
	}

	public Object[] getCardFromDeck(RandomSpace deck) throws InterruptedException {
		Object[] ts = deck.get(new FormalField(Integer.class), new FormalField(Integer.class));
		return ts;
	}

	public void returnCardToDeck(RandomSpace deck, Object[] ts) throws InterruptedException {
		deck.put(ts[0], ts[1]);
	}

	public void river(RandomSpace deck) throws InterruptedException {
		Object[] firstCard = getCardFromDeck(deck);
		Object[] secondCard = getCardFromDeck(deck);
		Object[] thirdCard = getCardFromDeck(deck);
	}

	public void turn(RandomSpace deck) throws InterruptedException {
		Object[] fourthCard = getCardFromDeck(deck);
	}

	public void flop(RandomSpace deck) throws InterruptedException {
		Object[] fifthCard = getCardFromDeck(deck);
	}

	public void burnCard(RandomSpace deck) throws InterruptedException {
		Object[] ts = getCardFromDeck(deck);
		returnCardToDeck(deck, ts);
	}

	public static boolean canJoin(SequentialSpace lobby,SpaceRepository spaceRepo, SequentialSpace game,SequentialSpace board) throws InterruptedException {
		playerId = playerId + 1;
		List<Object[]> players = lobby.queryAll(new ActualField("join"), new FormalField(String.class));
		if (players.size() < 4 && players.size() > -1) {

			lobby.put(playerId, players.get(1), BALANCE, gameSpace(spaceRepo, game),boardSpace(spaceRepo, board) );
		} else {
			lobby.put("declined");
		}

		return false;
	}

	public static String boardSpace(SpaceRepository spaceRepo, SequentialSpace board) {
		// check if board exists 
		spaceRepo.add("board", board);
		String boardUrl = "tcp://localhost:9003/board?keep";
		return boardUrl;
	}

	public static String gameSpace(SpaceRepository spaceRepo, SequentialSpace game) {
		// check if board exists 
		spaceRepo.add("game", game);
		String gameUrl = "tcp://localhost:9003/game?keep";
		return gameUrl;
	}	
}