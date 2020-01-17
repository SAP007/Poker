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
			
			handPower = 800;
			
			//current tie-breaker is sum of hand, mahy not work for edge cases
			handPower += RulesAux.handSum(actualHand);
			
			return handPower;
			
		}
		
		
		//check if full house
		actualHand = HandFinder.findFullHouse(hand);
		
		
		if (isValidHand(actualHand)) {
			System.out.println("Full House");
			
			handPower = 700;
			
			
			return handPower;
		}
		
		actualHand = HandFinder.findFlushHand(hand);

		//check if flush
		if (isValidHand(actualHand)) {
			System.out.println("Flush");
			
			handPower = 600;
			
			
			return handPower;
		}
		
		//check if straight
		actualHand = HandFinder.findStraightHand(hand);
		
		
		if (isValidHand(actualHand)) {
			System.out.println("Straight");
			handPower = 500 + RulesAux.handSum(actualHand);
			
			return handPower;
		}
		
		//check if 3 of a kind
		actualHand = HandFinder.findMultOfAKind(hand, 3);
		
		if (isValidHand(actualHand)) {
			
			handPower = 400 + RulesAux.handSum(actualHand);
			
			System.out.println("3 of a kind, handPower: " + handPower);
			
			return handPower;			
		}
		
		actualHand = HandFinder.findTwoPairs(hand);
		
		if (isValidHand(actualHand)) {
			handPower = 300 + RulesAux.handSum(actualHand);
			
			System.out.println("two pairs, handPower: " + handPower);
			
			return handPower;	
		}
		
		
		actualHand = HandFinder.findMultOfAKind(hand, 2);
		
		if (isValidHand(actualHand)) {
			handPower = 200 + RulesAux.handSum(actualHand);
			
			System.out.println("one pair, handPower: " + handPower);
			
			return handPower;	
		}
		
		
		//TODO: high card
		
		return handPower;
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

		hand[0] = new Card(Suit.HEARTS, 8);
		hand[1] = new Card(Suit.DIAMONDS, 5);
		hand[2] = new Card(Suit.CLUBS, 3);
		hand[3] = new Card(Suit.SPADES, 9);
		hand[4] = new Card(Suit.CLUBS, 4);
		hand[5] = new Card(Suit.HEARTS, 2);
		hand[6] = new Card(Suit.CLUBS, 2);

		Card[] actualHand = new Card[5];
		
		findHandPower(hand);
		
		
	}

}
