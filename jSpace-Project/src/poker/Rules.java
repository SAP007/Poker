package poker;


//Rules governing game
public class Rules {
	
	public static final int HANDSIZE = 5;

	public int getHandPower(Card[] hand) {
		
		//TODO: assign numerical value based on power of a hand
		
		
		return 0;
	}
	
	
	
	private boolean isFullHouse(Card[] hand) {
		
		int firstVal = hand[0].getNumber();
		int secondVal = 0;
		int currVal;
		
		//multiples of cards, should have values 3 and 2 for full house
		int firstMult = 1;
		int secondMult = 1;
		
		//false if second value has not been assigned
		boolean secondValAssigned = false;
		
		for (int i = 1; i < HANDSIZE; i++) {
			currVal = hand[i].getNumber();
			
			//increment multiple of first value if the number are equal
			if (currVal == firstVal) {
				firstMult++;
			}
			
			//assign secondVal, if not yet assigned
			else if (!secondValAssigned) {
				secondVal = currVal;
				secondValAssigned = true;
			}
			
			//increment multiple of second value if the number are equal
			else if (secondVal == currVal) {
				secondMult++;
			}
			
			
		}
		
		//returns true only if full house
		return (firstMult == 3 && secondMult == 2) || (firstMult == 2 && secondMult == 3);
		
	}
	
	//used for pair, three of a kind, four of a kind
	private int multOfAKind(Card[] hand) {
		
		int currVal;
		int currMult = 0;
		
		//largest multiple of a kind found
		int largestMult = 0;
		
		//compares each card with all other cards in hand
		for (int i = 0; i < HANDSIZE; i++) {
			
			currMult = 0;
			
			//store value of current card being looked at
			currVal = hand[i].getNumber();
			
			
			for (int j = 0; j < HANDSIZE; j++) {
				
				int nextVal = hand[j].getNumber();
				
				//disregard comparing card to itself
				if (i != j) {
					
					/*if current value is equal to
					 *  another card's value, increment currMult
					 */
					if (nextVal == currVal) currMult++;
				}
				
				//update largestMult, if currMult is greater
				if (currMult > largestMult) largestMult = currMult;
				
			}
		}	
		
		
		return largestMult;
	}
	
	private boolean isFlush(Card[] hand) {
		
		//saves suit of first card in hand
		Suit firstSuit = hand[0].getSuit();
		
		//return false if one suit does not match
		for (int i = 1; i < HANDSIZE; i++) {
			if (hand[i].getSuit() != firstSuit) {
				return false;
			}
		}
		return true;
		
	}
	
	
}
