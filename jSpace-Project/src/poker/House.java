package poker;

import org.jspace.RandomSpace;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

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


}
