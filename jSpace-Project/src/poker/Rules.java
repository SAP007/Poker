package poker;

//Rules governing game
public class Rules {

	public static final int HANDSIZE = 7;

	public static int findHandPower(Card[] hand) throws Exception {

		// TODO: assign numerical value based on power of a hand
		Card[] actualHand = new Card[5];
		int cardPower = 0;
		
		actualHand = HandFinder.findFlushHand(hand);
				
		//TODO: Straight Flush
		
		actualHand = HandFinder.findMultOfAKind(hand, 4);
		
		System.out.println("act");
		
		
		if (isValidHand(actualHand)) {
			System.out.println("Four of a kind");
			
			cardPower = 800;
			
			//current tie-breaker is sum of hand, mahy not work for edge cases
			cardPower += RulesAux.handSum(actualHand);
			
			return cardPower;
			
		}
		
		actualHand = HandFinder.findFullHouse(hand);
		
		
		if (isValidHand(actualHand)) {
			System.out.println("Full House");
			
			cardPower = 700;
			
			
			return cardPower;
		}
		
		actualHand = HandFinder.findFlushHand(hand);

		if (isValidHand(actualHand)) {
			System.out.println("Flush");
			
			cardPower = 600;
			
			
			return cardPower;
		}
		
		actualHand = HandFinder.findStraightHand(hand);
		
		if (isValidHand(actualHand)) {
			System.out.println("Straight");
			cardPower = 500 + RulesAux.handSum(actualHand);
			
			return cardPower;
		}
		
		return cardPower;
	}

	
	//returns false, if actualHand contains any null cards
	public static boolean isValidHand(Card[] actualHand) {

		for (int i = 0; i < 5; i++) {
			if (actualHand[i] == null)
				return false;
		}
		return true;

	}

	// Main for testing purposes
	public static void main(String[] args) throws Exception {

		Card[] hand = new Card[7];

		hand[0] = new Card(Suit.CLUBS, 6);
		hand[1] = new Card(Suit.HEARTS, 6);
		hand[2] = new Card(Suit.CLUBS, 9);
		hand[3] = new Card(Suit.SPADES, 6);
		hand[4] = new Card(Suit.CLUBS, 4);
		hand[5] = new Card(Suit.CLUBS, 5);
		hand[6] = new Card(Suit.CLUBS, 2);

		Card[] actualHand = new Card[5];

//		actualHand = HandFinder.findMultOfAKind(hand, 4);
//		for (Card card : actualHand) {
//			if (Rules.isValidHand(actualHand))
//				System.out.print(card.toString() + ", ");
//		}
//		System.out.println();
//
//		actualHand = HandFinder.findMultOfAKind(hand, 3);
//		for (Card card : actualHand) {
//			if (Rules.isValidHand(actualHand))
//				System.out.print(card.toString() + ", ");
//		}
//		System.out.println();
//
//		actualHand = HandFinder.findMultOfAKind(hand, 2);
//		for (Card card : actualHand) {
//			if (Rules.isValidHand(actualHand))
//				System.out.print(card.toString() + ", ");
//		}
//		System.out.println();
		
		findHandPower(hand);
		
		
//		Card[] hand = new Card[HANDSIZE];
//
//		for (int i = 2; i < HANDSIZE; i++) {
//			hand[i] = new Card(Suit.suitFromInt(i % 4), 4);
//		}
//		hand[0] = new Card(Suit.HEARTS, 2);
//		hand[1] = new Card(Suit.CLUBS, 2);
//		hand[2] = new Card(Suit.SPADES, 5);
//
//		System.out.println(isValidHand(HandFinder.findFlushHand(hand)));
//
//		System.out.println("Fullhouse: " + isValidHand(HandFinder.findFullHouse(hand)));
//
//		int mult = 0;
//
//		mult = multOfAKind(hand);
//
//		if (mult == 2) {
//			System.out.println("is pair");
//		} else if (mult == 3) {
//			System.out.println("three of a kind");
//		}
//
//		else if (mult == 4) {
//			System.out.println("four of a kind");
//		}
//
//		else {
//			System.out.println("one of a kind");
//		}
	}

}
