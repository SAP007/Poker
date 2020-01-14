package poker;

import java.io.ObjectInputStream.GetField;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

//Rules governing game
public class Rules {

	public static final int HANDSIZE = 7;

	public int getHandPower(Card[] hand) {

		// TODO: assign numerical value based on power of a hand

		return 0;
	}

	private static Card[] getFullHouse(Card[] hand) {

		// TODO: make work for 7 cards
		
		hand = HandEvaluator.sortHand(hand);
		Card[] actualHand = new Card[5];
		
		//parts making up a full house
		Card[] triplet = new Card[3];
		Card[] pair = new Card[2];
		
		Card[] temp = new Card[3];
		
		System.out.println("hand sorted: ");
		
		for (int i = 0; i < hand.length; i++) {
			System.out.print(hand[i].toString() + ", ");
		}
		System.out.println();
		
		
		Card currCard = hand[0];
		int count = 1;
		temp[0] = currCard;
		int currVal = currCard.getNumber();
		
		Card nextCard = null;
		int nextVal = 0;

		for (int i = 1; i < HANDSIZE; i++) {
			nextCard = hand[i];
			nextVal = nextCard.getNumber();

			// increment multiple of first value if the number are equal
			if (currVal == nextVal) {
				temp[count - 1] = nextCard;
				count++;
			}
			else {
				count = 1;
				for (int j = 0; j < temp.length; j++) {
					temp[j] = null;
				}
			}
			
			//when two of the same kind are found, add them to pair
			if (count == 2) {
				for (int j = 0; j < pair.length; j++) {
					pair[j] = temp[j];
				}
			}
			else if (count > 2) {
				for (int j = 0; j < triplet.length; j++) {
					triplet[j] = temp[j];
				}
			}

		}
		
		for (int i = 0; i < pair.length; i++) {
			actualHand[i] = pair[i];
		}
		for (int i = 0; i < triplet.length; i++) {
			actualHand[i + 2] = triplet[i];
		}
		
		
		
		// returns true only if full house
		return actualHand;

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


	public static boolean isValidHand(Card[] actualHand) {

		for (int i = 0; i < 5; i++) {
			if (actualHand[i] == null)
				return false;
		}
		return true;

	}

	// Main for testing purposes
	public static void main(String[] args) throws Exception {

		Card[] hand = new Card[HANDSIZE];

		for (int i = 2; i < HANDSIZE; i++) {
			hand[i] = new Card(Suit.suitFromInt(i % 4), 4);
		}
		hand[0] = new Card(Suit.HEARTS, 2);
		hand[1] = new Card(Suit.CLUBS, 2);

		System.out.println(isValidHand(HandEvaluator.getFlushHand(hand)));
		
		System.out.println("Fullhouse: " + isValidHand(getFullHouse(hand)));

		int mult = 0;

		mult = multOfAKind(hand);

		if (mult == 2) {
			System.out.println("is pair");
		} else if (mult == 3) {
			System.out.println("three of a kind");
		}

		else if (mult == 4) {
			System.out.println("four of a kind");
		}

		else {
			System.out.println("one of a kind");
		}
	}

}
