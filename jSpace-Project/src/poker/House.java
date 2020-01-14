package poker;

import org.jspace.RandomSpace;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import java.net.URI;
import java.net.URISyntaxException;

import org.jspace.ActualField;
import org.jspace.FormalField;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;

public class House {

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
		// Set the URI of the chat space
		String url = "tcp://localhost:9003/?keep";

		// Open a gate
		spaceRepo.addGate("tcp://localhost:9003/?keep");
		System.out.println("Space repository opened at: " + url + "...");

	}

	public static void deckSpace(SpaceRepository spaceRepo, RandomSpace deck) {

		spaceRepo.add("deck", deck);
	}

	public static void createLobby() throws InterruptedException {

		// creates a lobby for players
		SequentialSpace lobby = new SequentialSpace();
		// Add the space to the repository
		spaceRepo.add("lobby", lobby);

		while (true) {
			System.out.println("get from client");
			Object[] t = lobby.get(new FormalField(String.class), new FormalField(Integer.class),
					new FormalField(Integer.class), new FormalField(Integer.class));
			lobby.put(t[0], t[3]);

			System.out.println(t[0] + ": Balance: " + t[1] + " playerId: " + t[2] + "bet: " + t[3]);
			System.out.println("done");
		}

	}
	
	public static void createBoard(SpaceRepository spaceRepo, SequentialSpace board) {
		spaceRepo.add("board", board);
	}
	
	public void updateBoardAfterPlayerAction(Object[] playerActions, SequentialSpace board) throws InterruptedException {
		board.put(playerActions[0],  	//ID
				playerActions[1], 	 	//NAME
				playerActions[2], 		//Balance
				playerActions[3], 		//Kort 1
				playerActions[4],		//Kort 2
				playerActions[5]);		//Check/raise/fold
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
	
}


