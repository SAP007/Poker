package poker;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class Card {	

	private Suit suit;
	private int number;
  
	public Card(){
		String suit;
		int value;
		}
	
	public Card(Suit suit, int number) {
		this.suit = suit;
		this.number = number;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public int getNumber() {
		return number;
	}
  
  public String toString() {
		String suitChar;
		// TODO: change suitChar to unicode for suits

		if (suit == Suit.CLUBS)
			suitChar = "\u2663";

		else if (suit == Suit.DIAMONDS)
			suitChar = "\u2662";

		else if (suit == Suit.HEARTS)
			suitChar = "\u2661";

		else
			suitChar = "\u2660";

		return "[ " + number + suitChar + " ]";

	}
	
	
	public static void addToDeck() throws InterruptedException, UnknownHostException, IOException {
		//Need to create a check if the space exists.
		String deckUri = "tcp://localhost:9002/deck?keep";
		RemoteSpace deckSpace = new RemoteSpace(deckUri);
	for (int i=1; i<=4; i++) {
		for(int j=2; j<=14; j++) {
			deckSpace.put(i, j); //tuple,
		}
	}
	Thread.sleep(600);
	Object[] t = deckSpace.query(new FormalField(Integer.class), new FormalField(Integer.class));
	System.out.println(deckSpace.size());
	Object[] ts = deckSpace.get(new FormalField(Integer.class), new FormalField(Integer.class));
	System.out.println(ts[0]);
	System.out.println(ts[1]);
	}	
	
	
	public static void main(String[] args) throws IOException, IOException, InterruptedException {
	String deckUri = "tcp://localhost:9002/deck?keep";
	RemoteSpace deckSpace = new RemoteSpace(deckUri);
	int numberOfCards = 52;
	
	addToDeck();
	
	}	
			
				
}
