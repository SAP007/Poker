package poker;

public class HandEvaluator {

	public static final int HANDSIZE = 7;
	
	
	public static Card[] getStraightHand(Card[] hand) {

		int consNums = 1;

		hand = shellsort(hand);

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

				consNums++;

				actualHand[actualIndex % 5] = hand[i];
				actualIndex++;

			}

			else if (nextNum == currNum)
				continue;

			else {
				consNums = 1;

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
	
	
	
	
	
	
	
	
	// from https://java2blog.com/shell-sort-in-java/
	private static Card[] shellsort(Card[] hand) {

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
