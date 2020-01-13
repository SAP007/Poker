package poker;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class Card {
	
	public static void main(String[] args) throws IOException, IOException {
	String deckUri = "tcp://localhost:9002/deck?keep";
	RemoteSpace deckSpace = new RemoteSpace(deckUri);
	int numberOfCards = 52;
	


	}		
	public Card(){
		String suit;
		int value;
		}
	
	public void addToDeck() throws InterruptedException, UnknownHostException, IOException {
		//Need to create a check if the space exists.
		String deckUri = "tcp://localhost:9002/deck?keep";
		RemoteSpace deckSpace = new RemoteSpace(deckUri);
	for (int i=1; i<=4; i++) {
		for(int j=2; j<=14; j++) {
			deckSpace.put(i, j); //tuple,
		}
	}
	}		
				
				
}

