package poker;

import org.jspace.RandomSpace;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import java.net.URI;
import java.net.URISyntaxException;
import org.jspace.FormalField;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;


public class House {
	public static void main(String[] args) {
		spaceRepo();
		System.out.println("SpaceRepo created");
		deckSpace();
		System.out.println("deckSpace created");

	}
	static SpaceRepository spaceRepo = new SpaceRepository();

	public static void spaceRepo() {
					// Set the URI of the chat space
					String url = "tcp://localhost:9002/pokerLobby?keep";

					// Open a gate
					spaceRepo.addGate("tcp://localhost:9002/?keep");
					System.out.println("Space repository opened at: " + url + "...");



	}

	public static void deckSpace() {

		RandomSpace deck = new RandomSpace();

		spaceRepo.add("deck", deck);
	}

	public static void createLobby() {
		try {

			// BufferedReader input = new BufferedReader(new
			// InputStreamReader(System.in));
			// Create a local space for the chat messages
			SequentialSpace lobby = new SequentialSpace();

			// Add the space to the repository
			spaceRepo.add("lobby", lobby);

			// Default value
			String uri = "tcp://127.0.0.1:9001/?keep";

			// Open a gate
			URI myUri = new URI(uri);
			String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() + "?keep";
			System.out.println("Opening repository gate at " + gateUri + "...");
			spaceRepo.addGate(gateUri);

			//
			while (true) {
				Object[] t = lobby.get(new FormalField(String.class), new FormalField(Integer.class),
						new FormalField(Integer.class),new FormalField(Integer.class));
				System.out.println(t[0] + ": Balance: " + t[1] + " playerId: " + t[2] + " " + "bet: " + t[3]);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {

			e.printStackTrace();
		}
	}
}
