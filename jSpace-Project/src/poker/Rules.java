package poker;

//Rules governing game
public class Rules {

	public static final int HANDSIZE = 7;

	public static int findHandPower(Card[] hand) throws Exception {

		// TODO: assign numerical value based on power of a hand
		Card[] actualHand = new Card[5];
		int handPower = 0;
		
		actualHand = HandFinder.findFlushHand(hand);
				
		//TODO: Straight Flush
		
		
		//Check if 4 of a kind
		actualHand = HandFinder.findMultOfAKind(hand, 4);
		
		if (isValidHand(actualHand)) {
			System.out.println("Four of a kind");
			
			handPower = 8000;
			
			//current tie-breaker is sum of hand, mahy not work for edge cases
			handPower += RulesAux.handSum(actualHand);
			
			return handPower;
			
		}
		
		
		//check if full house
		actualHand = HandFinder.findFullHouse(hand);
		
		
		if (isValidHand(actualHand)) {
			System.out.println("Full House");
			
			handPower = 7000;
			
			
			return handPower;
		}
		
		actualHand = HandFinder.findFlushHand(hand);

		//check if flush
		if (isValidHand(actualHand)) {
			System.out.println("Flush");
			
			handPower = 6000;
			
			
			return handPower;
		}
		
		//check if straight
		actualHand = HandFinder.findStraightHand(hand);
		
		
		if (isValidHand(actualHand)) {
			System.out.println("Straight");
			handPower = 5000 + RulesAux.handSum(actualHand);
			
			return handPower;
		}
		
		//check if 3 of a kind
		actualHand = HandFinder.findMultOfAKind(hand, 3);
		
		if (isValidHand(actualHand)) {
			
			handPower = 4000 + RulesAux.handSum(actualHand);
			
			System.out.println("3 of a kind, handPower: " + handPower);
			
			return handPower;			
		}
		
		actualHand = HandFinder.findTwoPairs(hand);
		
		if (isValidHand(actualHand)) {
			handPower = 3000 + RulesAux.handSum(actualHand);
			
			System.out.println("two pairs, handPower: " + handPower);
			
			return handPower;	
		}
		
		
		actualHand = HandFinder.findMultOfAKind(hand, 2);
		
	RulesAux.printHand(hand);
		
		if (isValidHand(actualHand)) {
			handPower = 2000 + RulesAux.handSum(actualHand);
			
			System.out.println("one pair, handPower: " + handPower);
			
			return handPower;	
		}
		
		else {
			
			handPower = HandFinder.findHighCard(hand);
			
			System.out.println("High card, handpower: " + handPower);
			
			return handPower;
		}

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

//		hand[0] = new Card(Suit.HEARTS, 14);
//		hand[1] = new Card(Suit.DIAMONDS, 7);
//		hand[2] = new Card(Suit.CLUBS, 9);
//		hand[3] = new Card(Suit.SPADES, 10);
//		hand[4] = new Card(Suit.CLUBS, 11);
//		hand[5] = new Card(Suit.HEARTS, 12);
//		hand[6] = new Card(Suit.CLUBS, 6);
		
		hand[0] = new Card(Suit.HEARTS, 7);
		hand[1] = new Card(Suit.DIAMONDS, 6);
		hand[2] = new Card(Suit.CLUBS, 5);
		hand[3] = new Card(Suit.SPADES, 4);
		hand[4] = new Card(Suit.CLUBS, 3);
		hand[5] = new Card(Suit.HEARTS, 2);
		hand[6] = new Card(Suit.CLUBS, 8);

		Card[] actualHand = new Card[5];
		
		findHandPower(hand);
		
		
	}

}
