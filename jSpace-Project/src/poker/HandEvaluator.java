package poker;

public class HandEvaluator {

	public static final int HANDSIZE = 7;
	
	
	
	
	
	public static Card[] getFlushHand(Card[] hand) throws Exception {

		Card[] actualHand = new Card[5];

		int numOfClubs, numOfDiamonds, numOfHearts, numOfSpades;
		numOfClubs = 0;
		numOfDiamonds = 0;
		numOfHearts = 0;
		numOfSpades = 0;

		Card[] clubHand = new Card[5];
		Card[] diamondHand = new Card[5];
		Card[] heartHand = new Card[5];
		Card[] spadeHand = new Card[5];

		Suit currSuit;
		Card currCard;

		// return false if one suit does not match
		for (int i = 1; i < HANDSIZE; i++) {

			currCard = hand[i];

			// get suit of current card
			currSuit = currCard.getSuit();

			// increment counter of appropriate suit
			if (currSuit == Suit.CLUBS) {

				if (numOfClubs >= 5)
					clubHand = insertIfLarger(clubHand, currCard);

				else {
					clubHand[numOfClubs] = currCard;
					numOfClubs++;
				}
			}

			else if (currSuit == Suit.DIAMONDS) {

				if (numOfDiamonds >= 5)
					diamondHand = insertIfLarger(diamondHand, currCard);

				else {
					diamondHand[numOfDiamonds] = currCard;
					numOfDiamonds++;
				}
			}

			else if (currSuit == Suit.HEARTS) {

				if (numOfHearts >= 5)
					heartHand = insertIfLarger(heartHand, currCard);

				else {
					heartHand[numOfHearts] = currCard;
					numOfHearts++;
				}
			}

			else if (currSuit == Suit.SPADES) {

				if (numOfSpades >= 5)
					spadeHand = insertIfLarger(spadeHand, currCard);

				else {
					spadeHand[numOfSpades] = currCard;
					numOfSpades++;
				}
			}

			// if no counter can be incremented, throw exception
			else {
				throw new Exception("Invalid suit value: " + currSuit);
			}

		}

		if (numOfClubs >= 5) {
			actualHand = clubHand;
		} else if (numOfDiamonds >= 5) {
			actualHand = diamondHand;
		} else if (numOfHearts >= 5) {
			actualHand = heartHand;
		}

		else if (numOfSpades >= 5) {
			actualHand = spadeHand;
		}

		return actualHand;

	}
	
	
	static Card[] getFullHouse(Card[] hand) {


		int[] multTracker = new int[12];
		Card[] actualHand = new Card[5];
		
		hand = HandEvaluator.sortHand(hand);
		
		for (Card card : hand) {
			multTracker[card.getNumber() - 2] = multTracker[card.getNumber() - 2] + 1;
		}
		boolean tripFound = false;
		// find triplet
		int triplet = -1;
		int pair = -1;
		for (int i = 11; i >= 0; i--) {
			if (multTracker[i] >= 3 && !tripFound) {
				triplet = i + 2;
				tripFound = true;
			} else if (multTracker[i] >= 2) {
				pair = i + 2;
			}
		}
		
		
		int actualIndex = 0;
		
		for (int i = 6; i >= 0; i--) {
			
			Card card = hand[i];
			System.out.println("cardNum: " + card.getNumber() + ", pair: " + pair);
			
			if ((card.getNumber() == pair || card.getNumber() == triplet) && actualIndex < 5) {
				actualHand[actualIndex] = card;
				actualIndex++;
			}
		}

		
		return actualHand;

	}
	
	
	
	public static Card[] getStraightHand(Card[] hand) {


		hand = sortHand(hand);

		// currently considered number
		int currNum = hand[0].getNumber();

		Card[] actualHand = new Card[5];
		actualHand[0] = hand[0];

		// index reached in actual index
		int actualIndex = 1;

		for (int i = 1; i < HANDSIZE; i++) {
			int nextNum = hand[i].getNumber();

			// if nextNum is exactly one greater than currNum, add to actualHand
			if (nextNum == currNum + 1) {

				actualHand[actualIndex % 5] = hand[i];
				actualIndex++;

			}

			else if (nextNum == currNum)
				continue;

			else {

				for (int j = 0; j < 5; j++) {
					actualHand[j] = null;
				}
				actualIndex = 0;
			}

			currNum = nextNum;

		}

		for (int j = 0; j < 5; j++) {
			Card card = actualHand[j];
			if (card != null)
				System.out.print(actualHand[j].toString() + ", ");

		}
		System.out.println();

		return actualHand;

		// return consNums >= 5;

	}
	
	
	
	// used to replace smaller flush cards with greater ones
	public static Card[] insertIfLarger(Card[] actualHand, Card card) {

		int min = 15;
		int currNum;
		Card minCard = null;
		int minIndex = -1;

		for (int i = 0; i < actualHand.length; i++) {

			currNum = actualHand[i].getNumber();

			if (currNum < min) {
				min = currNum;
				minCard = actualHand[i];
				minIndex = i;
			}
		}

		if (minCard.getNumber() < card.getNumber() && minIndex > -1) {
			actualHand[minIndex] = card;
		}

		return actualHand;
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
	
}
