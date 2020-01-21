package poker;

public class RulesAux {

	// find given hand, the card with minimum value
	public static Card findMin(Card[] hand) {
		int min = 15;
		int currNum;
		Card minCard = null;

		for (int i = 0; i < hand.length; i++) {

			currNum = hand[i].getNumber();

			if (currNum < min) {
				min = currNum;
				minCard = hand[i];
			}
		}

		return minCard;
	}

	// find given hand, the card with maximum value
	public static Card findMax(Card[] hand) {
		int max = 1;
		int currNum;
		Card maxCard = null;

		for (int i = 0; i < hand.length; i++) {

			currNum = hand[i].getNumber();

			if (currNum > max) {
				max = currNum;
				maxCard = hand[i];
			}
		}

		return maxCard;
	}

	static int[] makeMultTracker(Card[] hand) {
		int[] multTracker = new int[13];
		hand = sortHand(hand);

		for (Card card : hand) {
			multTracker[card.getNumber() - 2] = multTracker[card.getNumber() - 2] + 1;
		}

		return multTracker;
	}

	// from https://java2blog.com/shell-sort-in-java/
	static Card[] sortHand(Card[] hand) {

		// first part uses the Knuth's interval sequence
		int h = 1;
		while (h <= hand.length / 3) {
			h = 3 * h + 1; // h is equal to highest sequence of h<=length/3
			// (1,4,13,40...)
		}

		// next part
		while (h > 0) { // for array of length 10, h=4

			// This step is similar to insertion sort below
			for (int i = 0; i < hand.length; i++) {

				Card tempCard = hand[i];
				int tempInt = hand[i].getNumber();
				int j;

				for (j = i; j > h - 1 && hand[j - h].getNumber() >= tempInt; j = j - h) {
					hand[j] = hand[j - h];
				}
				hand[j] = tempCard;
			}
			h = (h - 1) / 3;
		}
		return hand;
	}
	
	static boolean handsEqual(Card[] hand1, Card[] hand2) {
		
		if (hand1.length != hand2.length) {
			return false;
		}
		
		for (int i = 0; i < hand1.length; i++) {
			
			if (!cardsEqual(hand1[i], hand2[i]))
				return false;
			
		}
		return true;
		
	}
	
	static boolean cardsEqual(Card card1, Card card2) {
		
		return (card1.getNumber() == card2.getNumber()) && (card1.getSuit() == card2.getSuit());
		
	}
	
	static int handSum(Card[] hand) {
		
		int sum = 0;
		
		for (Card card : hand) {
			sum = sum + card.getNumber();
		}
		
		return sum;
	}
	
	static void printHand(Card[] hand) {
		
		for (Card c : hand) {
			if (c == null) System.out.print("[ null ]");
			else System.out.print(c.toString());
		}
		System.out.println();
		
	}
	
	static Card[] removeLastCard(Card[] hand) {
		
		hand = sortHand(hand);
		
		int newN = hand.length - 1;
		Card[] newHand = new Card[newN];
		
		for (int i = 0; i < newN; i++) {
			newHand[i] = hand[i];
		}
		
		return newHand;
	}

}
