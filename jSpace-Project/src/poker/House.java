package poker;

import org.jspace.RandomSpace;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;

import org.jspace.ActualField;
import org.jspace.FormalField;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;

public class House {

	public static void main(String[] args) throws InterruptedException {
		SpaceRepository spaceRepo = new SpaceRepository();
		RandomSpace deck = new RandomSpace();
		SequentialSpace board = new SequentialSpace();
		SequentialSpace game = new SequentialSpace();
		
		spaceRepo(spaceRepo);
		System.out.println("SpaceRepo created");
		deckSpace(spaceRepo, deck);
		System.out.println("deckSpace created");
		createLobby(spaceRepo);
		createBoard(spaceRepo, board);

	}

	public static void spaceRepo(SpaceRepository spaceRepo) {
		// Set the URI of the chat space
		String url = "tcp://localhost:9003/?keep";

		// Open a gate
		spaceRepo.addGate("tcp://localhost:9003/?keep");
		System.out.println("Space repository opened at: " + url + "...");

	}

	public static void deckSpace(SpaceRepository spaceRepo, RandomSpace deck) {

		spaceRepo.add("deck", deck);
	}

	public static void createLobby(SpaceRepository spaceRepo) throws InterruptedException {

		// creates a lobby for players
		SequentialSpace lobby = new SequentialSpace();
		// Add the space to the repository
		spaceRepo.add("lobby", lobby);

		while (true) {
			System.out.println("get from client");
			Object[] t = lobby.get(new FormalField(String.class), new FormalField(Integer.class),
					new FormalField(Integer.class), new FormalField(Integer.class));
			lobby.put(t[0], t[3]);

			System.out.println(t[0] + ": Balance: " + t[1] + " playerId: " + t[2] + "bet: " + t[3]);
			System.out.println("done");
		}

	}
	
	public static void createBoard(SpaceRepository spaceRepo, SequentialSpace board) {
		spaceRepo.add("board", board);
	}
	
	public void updateBoardAfterPlayerAction(Object[] playerActions, SequentialSpace board) throws InterruptedException {
		board.put(playerActions[0],  	//ID
				playerActions[1], 	 	//NAME
				playerActions[2], 		//Balance
				playerActions[3], 		//Kort 1
				playerActions[4],		//Kort 2
				playerActions[5]);		//Check/raise/fold
	}
	
	public Object[] getCardFromDeck(RandomSpace deck) throws InterruptedException {
		Object[] card = deck.get(new FormalField(Integer.class), new FormalField(Integer.class));
		return card;
	}
	
	public void returnCardToDeck(RandomSpace deck, Object[] ts) throws InterruptedException {
		deck.put(ts);
	}
	
	public void riverCards(RandomSpace deck, SequentialSpace board) throws InterruptedException {
		burnCard(deck);
		Object[] t = board.get(new FormalField(Object.class), 
				new FormalField(Object.class), 
				new FormalField(Object.class),
				new FormalField(Object.class), 
				new FormalField(Object.class));
		t[0] = getCardFromDeck(deck);
		t[1] = getCardFromDeck(deck);
		t[2] = getCardFromDeck(deck);
		board.put(t);
	}
	
	public void turnCard(RandomSpace deck, SequentialSpace board) throws InterruptedException {
		burnCard(deck);
		Object[] t = board.get(new FormalField(Object.class), 
				new FormalField(Object.class), 
				new FormalField(Object.class),
				new FormalField(Object.class), 
				new FormalField(Object.class));
		t[3] = getCardFromDeck(deck);
		board.put(t);
	}
	
	public void flopCard(RandomSpace deck, SequentialSpace board) throws InterruptedException {
		burnCard(deck);
		Object[] t = board.get(new FormalField(Object.class), 
				new FormalField(Object.class), 
				new FormalField(Object.class),
				new FormalField(Object.class), 
				new FormalField(Object.class));
		t[4] = getCardFromDeck(deck);
		board.put(t);
	}
	
	public void burnCard(RandomSpace deck) throws InterruptedException {
		Object[] ts = getCardFromDeck(deck);
		returnCardToDeck(deck, ts);
	}
	
	public void givePlayerCards(RandomSpace deck, SequentialSpace game) throws InterruptedException {
		
		for(int i = 1; i <= 7; i++) {
			//indsætte if statement der tjekker om værdien i arrayet er 0 eller et id og så udføre nedenstående. ved at sætte id som x
		Object[] playerInView = (Object[]) getPlayerInView(game, x); //Gets the player in question from the lobby
		playerInView[3] = getCardFromDeck(deck); //gives the first card
		playerInView[4] = getCardFromDeck(deck); //gives the second card
		playerInView[5] = -2; //sets it to -2 so the player knows it needs to take the hand
		game.put(playerInView); //returns the player to the game
		}	
	}
	public void turn (SequentialSpace game, SequentialSpace board, int numberFromArray, RandomSpace deck) throws InterruptedException {
		Object[] playerBoard;
		Array testarray;
		givePlayerCards(deck, game); //gives the players their cards	

		checkRaiseFoldSequence(game, board, numberFromArray);//check for c/r/f
		
		riverCards(deck, board);//deal river
		
		checkRaiseFoldSequence(game, board, numberFromArray);//check for c/r/f
		
		turnCard(deck, board);//deal turn card
		
		checkRaiseFoldSequence(game, board, numberFromArray);//check for c/r/f
		
		flopCard(deck, board);//deal flop card
		
		checkRaiseFoldSequence(game, board, numberFromArray);//check for c/r/f
		//find en vinder
		//returner kort? eller bare discard dem, og lav et nyt deck?
	}
	
	public void checkRaiseFoldSequence(SequentialSpace game, SequentialSpace board, int dealer) {
		for(int i = 1; i <= 7; i++) {
			//indsætte if statement der tjekker om værdien i arrayet er 0 eller et id og så udføre nedenstående.
			//skaf første spiller, den der er dealer. Står i arrayet.
			//Derefter køres arrayet igennem med checkplayer action.
			//der skal vær gang hentes spiller ned og ændre "raise" værdien til -2 så spilleren ved den skal gøre noget
			
			
		}
	}
	
	public void checkPlayerAction(SequentialSpace game, SequentialSpace board) throws InterruptedException {
		int numberFromArray = 0;
		Object[] playerInView;
		Object[] playerBoard;
		Object[] waitingForPlayer = (Object[]) waitForPlayer(board, game, numberFromArray);
		
		while((int)waitingForPlayer[5] < 0) {
				//Endless loop to keep quering for the specific player.
			
		}
		
		playerInView = (Object[]) getPlayerInView(game, numberFromArray); //gets the player from the gamespace
		
		if((int)playerInView[5] >= 0) {
			//raises the pot with the value the player entered. also works for just checking.
			playerBoard = (Object[]) getBoard(board); //gets the board into view
			playerBoard[5] = (int)playerBoard[5] + (int)playerInView[5]; //updates the pot with the value the player entered
			playerInView[5] = -3; //sets the player to be in waiting state again.
			board.put(playerBoard); //returns the board object to the boardspace
			game.put(playerInView); //returns the player object to the gamespace
			
		}
		//check = 0
		//fold = -1
		//raise > 0
		//Giv tur til spiller = -2
		//Efter spiller er behandlet = -3
		//Bør være  et sequential space eller et array, hvor spillere kan fjernes fra hvis de ikke længere er tilstede.
		//der kan derefter laves et forloop og køre igennem hvem der er dealer, alt efter turene.
	}
	
	public Object getBoard(SequentialSpace board) throws InterruptedException {
		Object[] playerBoard = board.get(new FormalField(Integer.class),  // first card
				new FormalField(Integer.class),  // second card
				new FormalField(Integer.class),  // third card
				new FormalField(Integer.class),  // fourth card
				new FormalField(Integer.class),  // fifth card
				new FormalField(Integer.class));
		return playerBoard;
	}
	
	public Object waitForPlayer(SequentialSpace board, SequentialSpace game, int number) throws InterruptedException {
		Object[] waitingForPlayer = game.query(new ActualField(number),
				new FormalField(String.class),
				new FormalField(Integer.class),
				new FormalField(Object.class),
				new FormalField(Object.class),
				new FormalField(Integer.class));
		return waitingForPlayer;
	}
	
	public Object getPlayerInView(SequentialSpace game, int number) throws InterruptedException {
		Object[] playerInView = game.get(new ActualField(number),
				new FormalField(String.class),
				new FormalField(Integer.class),
				new FormalField(Object.class),
				new FormalField(Object.class),
				new FormalField(Integer.class));
		return playerInView;
	}
	
	public Object getPlayerOnBoard(SequentialSpace board, SequentialSpace game, int number) throws InterruptedException {
		Object[] playerOnBoard = game.get(new ActualField(number),
				new FormalField(String.class),
				new FormalField(Integer.class),
				new FormalField(Object.class),
				new FormalField(Object.class),
				new FormalField(Integer.class));
		return playerOnBoard;
	}
	
}


