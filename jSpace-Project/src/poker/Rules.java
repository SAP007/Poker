package poker;

import java.util.concurrent.ThreadLocalRandom;

//Rules governing game
public class Rules {

	public static final int HANDSIZE = 7;

	public static int findHandPower(Card[] hand) throws Exception {

		// TODO: assign numerical value based on power of a hand
		Card[] actualHand = new Card[5];
		int handPower = 0;
		
		actualHand = HandFinder.findStraightFlush(hand);
				
		//TODO: Straight Flush
		
		if (isValidHand(actualHand)) {
			handPower = 1000 + RulesAux.handSum(actualHand);
			
			System.out.println("Straight Flush: " + handPower);
			RulesAux.printHand(actualHand);
			
			return handPower;
			
		}
		
		
		//Check if 4 of a kind
		actualHand = HandFinder.findMultOfAKind(hand, 4);
		
		if (isValidHand(actualHand)) {
			handPower = 8000;
			System.out.println("Four of a kind: " + handPower);
			RulesAux.printHand(actualHand);
			
			//current tie-breaker is sum of hand, mahy not work for edge cases
			handPower += RulesAux.handSum(actualHand);
			
			return handPower;
			
		}
		
		
		//check if full house
		actualHand = HandFinder.findFullHouse(hand);
		
		
		if (isValidHand(actualHand)) {
			handPower = 7000;
			System.out.println("Full House: " + handPower);
			
			
			RulesAux.printHand(actualHand);
			
			
			
			return handPower;
		}
		
		actualHand = HandFinder.findFlushHand(hand);

		//check if flush
		if (isValidHand(actualHand)) {
			handPower = 6000;
			System.out.println("Flush: " + handPower);
			
			RulesAux.printHand(actualHand);
			
			
			return handPower;
		}
		
		//check if straight
		actualHand = HandFinder.findStraightHand(hand);
		
		
		if (isValidHand(actualHand)) {

			handPower = 5000 + RulesAux.handSum(actualHand);
			System.out.println("Straight: " + handPower);
			

			
			RulesAux.printHand(actualHand);
			
			return handPower;
		}
		
		//check if 3 of a kind
		actualHand = HandFinder.findMultOfAKind(hand, 3);
		
		if (isValidHand(actualHand)) {
			
			handPower = 4000 + RulesAux.handSum(actualHand);
			
			System.out.println("3 of a kind, handPower: " + handPower);
			
			RulesAux.printHand(actualHand);
			
			return handPower;			
		}
		
		actualHand = HandFinder.findTwoPairs(hand);
		
		if (isValidHand(actualHand)) {
			handPower = 3000 + RulesAux.handSum(actualHand);
			
			System.out.println("two pairs, handPower: " + handPower);
			
			RulesAux.printHand(actualHand);
			
			return handPower;	
		}
		
		
		actualHand = HandFinder.findMultOfAKind(hand, 2);
		
		
		if (isValidHand(actualHand)) {
			handPower = 2000 + RulesAux.handSum(actualHand);
			
			System.out.println("one pair, handPower: " + handPower);
			
			RulesAux.printHand(actualHand);
			
			return handPower;	
		}
		
		else {
			
			handPower = HandFinder.findHighCard(hand);
			
			System.out.println("High card, handpower: " + handPower);
			
			RulesAux.printHand(actualHand);
			
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

		Card[] hand = randomHand();
		

//		hand[0] = new Card(Suit.CLUBS, 10);
//		hand[1] = new Card(Suit.CLUBS, 11);
//		hand[2] = new Card(Suit.SPADES, 10);
//		hand[3] = new Card(Suit.DIAMONDS, 5);
//		hand[4] = new Card(Suit.HEARTS, 3);
//		hand[5] = new Card(Suit.DIAMONDS, 7);
//		hand[6] = new Card(Suit.CLUBS, 2);
		
		hand[0] = new Card(Suit.HEARTS, 2);
		hand[1] = new Card(Suit.HEARTS, 3);
		hand[2] = new Card(Suit.HEARTS, 4);
		hand[3] = new Card(Suit.HEARTS, 5);
		hand[4] = new Card(Suit.HEARTS, 6);
		hand[5] = new Card(Suit.HEARTS, 10);
		hand[6] = new Card(Suit.HEARTS, 11);
		
		RulesAux.printHand(hand);

		Card[] actualHand = new Card[5];
		
		findHandPower(hand);
		
		
	}
	
	private static Card[] randomHand() {
		Card[] hand = new Card[7];
		int minNum = 2;
		int maxNum = 14;
		
		int minSuit = 1;
		int maxSuit = 4;
		
		
		for (int i = 0; i < 7; i++) {
			int randNum = ThreadLocalRandom.current().nextInt(minNum, maxNum + 1);
			int randSuit = ThreadLocalRandom.current().nextInt(minSuit, maxSuit + 1);
			hand[i] = new Card(Suit.suitFromInt(randSuit), randNum);
		}
		return hand;
		
		
	}

}
