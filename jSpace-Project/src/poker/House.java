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
		spaceRepo();
		System.out.println("SpaceRepo created");
		deckSpace();
		System.out.println("deckSpace created");
		createLobby();
	}

	static SpaceRepository spaceRepo = new SpaceRepository();

	public static void spaceRepo() {
		// Set the URI of the chat space
		String url = "tcp://localhost:9003/?keep";

		// Open a gate
		spaceRepo.addGate("tcp://localhost:9003/?keep");
		System.out.println("Space repository opened at: " + url + "...");

	}

	public static void deckSpace() {

		RandomSpace deck = new RandomSpace();

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
}
