package poker;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class Card {

	Suit suit;
	int number;

	public Card() {
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
			suitChar = "clubs";

		else if (suit == Suit.DIAMONDS)
			suitChar = "diamonds";

		else if (suit == Suit.HEARTS)
			suitChar = "hearts";

		else
			suitChar = "spades";

		return number + " of " + suitChar;

	}

	public void addToDeck() throws InterruptedException, UnknownHostException, IOException {
		// Need to create a check if the space exists.
		String deckUri = "tcp://localhost:9002/deck?keep";
		RemoteSpace deckSpace = new RemoteSpace(deckUri);
		for (int i = 1; i <= 4; i++) {
			for (int j = 2; j <= 14; j++) {
				deckSpace.put(i, j); // tuple,
			}
		}
	}

	public static void main(String[] args) throws IOException, IOException {
		String deckUri = "tcp://localhost:9002/deck?keep";
		RemoteSpace deckSpace = new RemoteSpace(deckUri);
		int numberOfCards = 52;

	}

}
