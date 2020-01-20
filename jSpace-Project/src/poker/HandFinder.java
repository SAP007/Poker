
package poker;

public class HandFinder {

	public static final int HANDSIZE = 7;
	
	
	
	
//	static Card[] findStraightFlush(Card[] hand) {
//		
//		Card[] actualHand = new Card[5];
//		
//		actualHand = findStraightHand(hand);
//		
//	}
	
	static Card[] findTwoPairs(Card[] hand) {
		
		Card[] actualHand = new Card[5];
		hand = RulesAux.sortHand(hand);
		
		boolean kickerFound = false;
		
		int pairVal = -1;
		int pairsFound = 0;
		int actualIndex = 0;
		
		Card currCard = hand[hand.length - 1];
		
		for (int i = hand.length - 2; i >= 0; i--) {
			Card nextCard = hand[i];
			
			int currVal = currCard.getNumber();
			int nextVal = nextCard.getNumber();
			
			/*currCard and nextCard are equal, save them as pair and exclude that 
			 * value from being classified as pair later
			 */
			if (currVal == nextVal && currVal != pairVal && actualIndex < 5 && pairsFound < 2) {
				actualHand[actualIndex] = currCard;
				actualHand[actualIndex + 1] = nextCard;
				
				pairVal = currVal;
				actualIndex += 2;
				pairsFound++;
				
			}
			/* for two pairs there is room for one kicker,
			 * first one found will be the highest and is saved
			 */
			else if (!kickerFound && actualIndex < 5) {
				actualHand[actualIndex] = currCard;
				actualIndex++;
				kickerFound = true;
			}
			currCard = nextCard;
			
		}
		
		return actualHand;
		
	}
	
	static Card[] findMultOfAKind(Card[] hand, int mult) {

		Card[] actualHand = new Card[5];

		int[] multTracker = RulesAux.makeMultTracker(hand);
		
		int bestVal = -1;
		
		for (int i = multTracker.length - 1; i >= 0; i--) {
			int currMult = multTracker[i];
			if (currMult >= mult) {
				
				bestVal = i + 2;
				
				break;
			}

		}

		//if no better value is found, return invalid hand
		if (bestVal == -1)
			return actualHand;

		//indicates number of cards in hand that are not part of combo
		int remainder = actualHand.length - mult;
		
		int actualIndex = actualHand.length - 1;
		for (int i = hand.length - 1; i >= 0; i--) {
			int currVal = hand[i].getNumber();
			if (currVal == bestVal && actualIndex >= 0) {
				actualHand[actualIndex] = hand[i];
				actualIndex--;
			} else if (remainder > 0 && actualIndex >= 0) {
				actualHand[actualIndex] = hand[i];
				actualIndex--;
				remainder--;
			}

		}

		return actualHand;
	}
	
	
	public static Card[] findFlushHand(Card[] hand) throws Exception {

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
		for (int i = 0; i < HANDSIZE; i++) {

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
	
	
	static Card[] findFullHouse(Card[] hand) {


		int[] multTracker = new int[12];
		Card[] actualHand = new Card[5];
		
		hand = RulesAux.sortHand(hand);
		
		multTracker = RulesAux.makeMultTracker(hand);
		
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
			
			if ((card.getNumber() == pair || card.getNumber() == triplet) && actualIndex < 5) {
				actualHand[actualIndex] = card;
				actualIndex++;
			}
		}

		
		return actualHand;

	}
	
	
	
	public static Card[] findStraightHand(Card[] hand) {


		hand = RulesAux.sortHand(hand);

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

		return actualHand;

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

	public static int findHighCard(Card[] hand) {
		
		hand = RulesAux.sortHand(hand);
		int sum = 0;
		
		for (int i = hand.length - 1; i > 1; i--) {
			sum = sum + (hand[i].getNumber() * i * 2);
		}
		
		
		
		return sum;
	}
	
}
