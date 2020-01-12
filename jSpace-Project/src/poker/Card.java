package poker;

public class Card {

	Suit suit;
	int number;
	
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
	
}
