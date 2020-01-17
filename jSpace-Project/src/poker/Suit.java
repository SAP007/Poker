package poker;

public enum Suit {
	CLUBS, DIAMONDS, HEARTS, SPADES;
	
	
	public static Suit suitFromInt(int i) {
		
		if (i == 1) return CLUBS;
		
		else if (i == 2) return DIAMONDS;
		
		else if (i == 3) return HEARTS;
		
		else return SPADES;
	}
}