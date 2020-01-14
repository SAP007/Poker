package poker;

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

	private boolean isFullHouse(Card[] hand) {

		// TODO: make work for 7 cards

		int firstVal = hand[0].getNumber();
		int secondVal = 0;
		int currVal;

		// multiples of cards, should have values 3 and 2 for full house
		int firstMult = 1;
		int secondMult = 1;

		// false if second value has not been assigned
		boolean secondValAssigned = false;

		for (int i = 1; i < HANDSIZE; i++) {
			currVal = hand[i].getNumber();

			// increment multiple of first value if the number are equal
			if (currVal == firstVal) {
				firstMult++;
			}

			// assign secondVal, if not yet assigned
			else if (!secondValAssigned) {
				secondVal = currVal;
				secondValAssigned = true;
			}

			// increment multiple of second value if the number are equal
			else if (secondVal == currVal) {
				secondMult++;
			}

		}

		// returns true only if full house
		return (firstMult == 3 && secondMult == 2) || (firstMult == 2 && secondMult == 3);

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

	private static boolean isFlush(Card[] hand) throws Exception {

		// TODO: make work with 7 card hand

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
				clubHand[numOfClubs] = currCard;
				numOfClubs++;
			}

			else if (currSuit == Suit.DIAMONDS) {
				diamondHand[numOfDiamonds] = currCard;
				numOfDiamonds++;
			}

			else if (currSuit == Suit.HEARTS) {
				heartHand[numOfHearts] = currCard;
				numOfHearts++;
			}

			else if (currSuit == Suit.SPADES) {
				spadeHand[numOfSpades] = currCard;
				numOfSpades++;
			}

			// if no counter can be incremented, throw exception
			else {
				throw new Exception("Invalid suit value: " + currSuit);
			}

		}

		// returns true only if there are 5 ore more cards of same suit
		return (numOfClubs >= 5) || (numOfDiamonds >= 5) || (numOfHearts >= 5) || (numOfSpades >= 5);

	}

	// Main for testing purposes
	public static void main(String[] args) throws Exception {

		Card[] hand = new Card[HANDSIZE];

		for (int i = 0; i < HANDSIZE; i++) {
			hand[i] = new Card(Suit.suitFromInt(i % 4), i + 4);
		}
		hand[0] = new Card(Suit.HEARTS, 9);
		hand[1] = new Card(Suit.HEARTS, 11);

		System.out.println(isValidHand(HandEvaluator.getStraightHand(hand)));

		if (isFlush(hand)) {
			System.out.println("is flush");
		} else {
			System.out.println("not flush");
		}

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

	public static boolean isValidHand(Card[] actualHand) {

		for (int i = 0; i < 5; i++) {
			if (actualHand[i] == null)
				return false;
		}
		return true;

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
	

}
