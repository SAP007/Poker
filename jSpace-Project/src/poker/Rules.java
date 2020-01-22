package poker;

//Rules governing game
public class Rules {

	public static final int HANDSIZE = 7;

	public static int findHandPower(Card[] hand) throws Exception {

		Card[] actualHand = new Card[5];
		int handPower = 0;
		
		actualHand = HandFinder.findStraightFlush(hand);
		
		if (isValidHand(actualHand)) {
			handPower = 10000 + RulesAux.handSum(actualHand);
			
			System.out.println("Straight Flush: " + handPower);
			
			return handPower;
			
		}
		
		
		//Check if 4 of a kind
		actualHand = HandFinder.findMultOfAKind(hand, 4);
		
		if (isValidHand(actualHand)) {
			handPower = 80000;
			System.out.println("Four of a kind: " + handPower);
			
			//current tie-breaker is sum of hand, mahy not work for edge cases
			handPower += RulesAux.handSum(actualHand);
			
			return handPower;
			
		}
		
		
		//check if full house
		actualHand = HandFinder.findFullHouse(hand);
		
		
		if (isValidHand(actualHand)) {
			handPower = 70000;
			System.out.println("Full House: " + handPower);
			
			return handPower;
		}
		
		actualHand = HandFinder.findFlushHand(hand);

		//check if flush
		if (isValidHand(actualHand)) {
			handPower = 60000;
			System.out.println("Flush: " + handPower);
			
			return handPower;
		}
		
		//check if straight
		actualHand = HandFinder.findStraightHand(hand);
		
		
		if (isValidHand(actualHand)) {

			int bonus = 0;
			bonus = RulesAux.handSum(actualHand);
			
			handPower = 50000 + bonus;
			System.out.println("Straight: " + handPower);
			
			return handPower;
		}
		
		//check if 3 of a kind
		actualHand = HandFinder.findMultOfAKind(hand, 3);
		
		if (isValidHand(actualHand)) {
			
			handPower = 40000 + RulesAux.handSum(actualHand);
			
			System.out.println("3 of a kind, handPower: " + handPower);
			
			return handPower;			
		}
		
		actualHand = HandFinder.findTwoPairs(hand);
		
		if (isValidHand(actualHand)) {
			handPower = 30000 + RulesAux.handSum(actualHand);
			
			System.out.println("two pairs, handPower: " + handPower);
			
			return handPower;	
		}
		
		
		actualHand = HandFinder.findMultOfAKind(hand, 2);
		
		
		if (isValidHand(actualHand)) {
			handPower = 20000 + RulesAux.handSum(actualHand);
			
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

}