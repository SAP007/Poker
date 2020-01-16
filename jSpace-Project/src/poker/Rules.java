package poker;

//Rules governing game
public class Rules {

	public static final int HANDSIZE = 7;

	public int getHandPower(Card[] hand) {

		// TODO: assign numerical value based on power of a hand
		

		return 0;
	}

	// used for pair, three of a kind, four of a kind
	private static int multOfAKind(Card[] hand) throws Exception {

		int currVal;
		int currMult = 1;

		// largest multiple of a kind found
		int largestMult = 0;

		// compares each card with all other cards in hand
		for (int i = 0; i < HANDSIZE; i++) {

			currMult = 1;

			// store value of current card being looked at
			currVal = hand[i].getNumber();

			for (int j = 0; j < HANDSIZE; j++) {

				int nextVal = hand[j].getNumber();

				// disregard comparing card to itself
				if (i != j) {

					/*
					 * if current value is equal to another card's value, increment currMult
					 */
					if (nextVal == currVal)
						currMult++;
				}

				// update largestMult, if currMult is greater
				if (currMult > largestMult)
					largestMult = currMult;

				if (largestMult > 4) {
					throw new Exception("Illegal. More than four cards of a kind (same number)");
				}

			}
		}

		return largestMult;
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
		hand[1] = new Card(Suit.CLUBS, 7);
		hand[2] = new Card(Suit.CLUBS, 7);
		hand[3] = new Card(Suit.CLUBS, 13);
		hand[4] = new Card(Suit.CLUBS, 7);
		hand[5] = new Card(Suit.CLUBS, 6);
		hand[6] = new Card(Suit.CLUBS, 13);

		Card[] actualHand = new Card[5];

		actualHand = HandFinder.findMultOfAKind(hand, 4);
		for (Card card : actualHand) {
			if (Rules.isValidHand(actualHand))
				System.out.print(card.toString() + ", ");
		}
		System.out.println();

		actualHand = HandFinder.findMultOfAKind(hand, 3);
		for (Card card : actualHand) {
			if (Rules.isValidHand(actualHand))
				System.out.print(card.toString() + ", ");
		}
		System.out.println();

		actualHand = HandFinder.findMultOfAKind(hand, 2);
		for (Card card : actualHand) {
			if (Rules.isValidHand(actualHand))
				System.out.print(card.toString() + ", ");
		}
		System.out.println();
		
		
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
