package poker;

import org.jspace.RandomSpace;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.FormalField;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;

public class House {
	final static int BALANCE = 1000; 
	static int playerId = 0;
	final static int dealer = 1;
	final static int[] mainAr = {0,0,0,0,0,0,0};
	public static void main(String[] args) throws InterruptedException {
		House house = new House();
		SpaceRepository spaceRepo = new SpaceRepository();
		RandomSpace deck = new RandomSpace();
		SequentialSpace board = new SequentialSpace();
		SequentialSpace game = new SequentialSpace();
		SequentialSpace lobby = new SequentialSpace();
		

		spaceRepo(spaceRepo);
		System.out.println("SpaceRepo created");
		deckSpace(spaceRepo, deck);
		System.out.println("deckSpace created");
		createBoard(spaceRepo, board);
		System.out.println("board created");
		createGame(spaceRepo, game);
		System.out.println("game created");
		createLobby(spaceRepo, lobby);
		System.out.println("lobby created created");
		new Thread(new canJoin(lobby, spaceRepo, game, board, mainAr)).start();
		//ikke start f�r 2-3 er p� bordet.
		while(countPlayers(mainAr) < 3) {
			System.out.println("Vente vente vente");
		}
		house.turn(game, board, mainAr, deck, dealer);

	}

	public static void spaceRepo(SpaceRepository spaceRepo) {
		// Set the URI of the spaceRepo
		String url = "tcp://localhost:9003/?keep";
		
		
		// Open a gate
		spaceRepo.addGate("tcp://localhost:9003/?keep");
		System.out.println("Space repository opened at: " + url + "...");

	}

	public static void deckSpace(SpaceRepository spaceRepo, RandomSpace deck) {
		spaceRepo.add("deck", deck);
	}

	public static void createLobby(SpaceRepository spaceRepo, SequentialSpace lobby) throws InterruptedException {

		// creates a lobby for players

		// Add the space to the repository
		spaceRepo.add("lobby", lobby);
	}

	public static void createBoard(SpaceRepository spaceRepo, SequentialSpace board) {
		spaceRepo.add("board", board);
	}
	
	public static void createGame(SpaceRepository spaceRepo, SequentialSpace game) {
		spaceRepo.add("game", game);
	}

	public void updateBoardAfterPlayerAction(Object[] playerActions, SequentialSpace board)
			throws InterruptedException {
		board.put(playerActions[0], // ID
				playerActions[1], // NAME
				playerActions[2], // Balance
				playerActions[3], // Card 1
				playerActions[4], // Card 2
				playerActions[5]); // Check/raise/fold
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
//		board.put(t);
	}
	
	public void turnCard(RandomSpace deck, SequentialSpace board) throws InterruptedException {
		burnCard(deck);
		Object[] t = board.get(new FormalField(Object.class), 
				new FormalField(Object.class), 
				new FormalField(Object.class),
				new FormalField(Object.class), 
				new FormalField(Object.class));
		t[3] = getCardFromDeck(deck);
//		board.put(t);
	}
	
	public void flopCard(RandomSpace deck, SequentialSpace board) throws InterruptedException {
		burnCard(deck);
		Object[] t = board.get(new FormalField(Object.class), 
				new FormalField(Object.class), 
				new FormalField(Object.class),
				new FormalField(Object.class), 
				new FormalField(Object.class));
		t[4] = getCardFromDeck(deck);
//		board.put(t);
	}

	public void burnCard(RandomSpace deck) throws InterruptedException {
		Object[] ts = getCardFromDeck(deck);
		returnCardToDeck(deck, ts);
	}

	
	public void givePlayerCards(RandomSpace deck, SequentialSpace game, int dealer) throws InterruptedException {
		int playerTurn = dealer;
		for(int i = 0; i < 7; i++) {
			if(mainAr[i] != 0){
				Object[] playerInView = (Object[]) getPlayerInView(game, playerTurn); //gets the player in view from the gamespace
				System.out.println("kort 1");
				playerInView[3] = getCardFromDeck(deck); //gives the first card
				playerInView[4] = getCardFromDeck(deck); //gives the second card
				System.out.println("kort givet");
				playerInView[5] = -2; //sets it to -2 so the player knows it needs to take the hand
				game.put(playerInView); //returns the player to the game
			}
		}	
	}
	
	
	public void turn (SequentialSpace game, SequentialSpace board, int[] mainAr, RandomSpace deck, int dealer) throws InterruptedException {
		Object[] playerBoard;
		Array testarray;
		System.out.println("Reset player");
		resetPlayers(game, board, dealer, mainAr); //resets players on board.
		System.out.println("Give some cards");
		givePlayerCards(deck, game, dealer); //gives the players their cards	
		System.out.println("CRF sequence");
		checkRaiseFoldSequence(game, board, dealer, mainAr);//check for c/r/f
		System.out.println("River card");
		riverCards(deck, board);//deal river
		System.out.println("CRF sequence");
		checkRaiseFoldSequence(game, board, dealer, mainAr);//check for c/r/f
		System.out.println("Turn Card");
		turnCard(deck, board);//deal turn card
		System.out.println("CRF sequence");
		checkRaiseFoldSequence(game, board, dealer, mainAr);//check for c/r/f
		System.out.println("Flop Card");
		flopCard(deck, board);//deal flop card
		System.out.println("CRF sequence");
		checkRaiseFoldSequence(game, board, dealer, mainAr);//check for c/r/f
		//find en vinder
		//returner kort? eller bare discard dem, og lav et nyt deck?
	}
	
	public void resetPlayers(SequentialSpace game, SequentialSpace board, int dealer, int[] mainAr) throws InterruptedException {
		int playerTurn = dealer; //finds the dealer and sets the playerTurn to that int value matching that id.
		for(int i = 0; i < 7; i++) {
			if(mainAr[i] == playerTurn && mainAr[i] != 0){
				Object[] playerInView = (Object[]) getPlayerInView(game, playerTurn);
				playerInView[5] = -3;
				game.put(playerInView);
			}
			playerTurn = playerTurn + 1; //Changes the value to the next player
		}
	}
	
	
	public void checkRaiseFoldSequence(SequentialSpace game, SequentialSpace board, int dealer, int[] mainAr) throws InterruptedException {
		int playerTurn = dealer; //finds the dealer and sets the playerTurn to that int value matching that id.
		System.out.println("CRF sequence 1");
		for(int i = 0; i < 7; i++) {
			System.out.println("CRF sequence forloop");
			if(mainAr[i] == playerTurn && mainAr[i] != 0){
				System.out.println("CRF sequence if state");
				Object[] playerInView = (Object[]) getPlayerInView(game, playerTurn);
				System.out.println("CRF sequence playerview done");
				if((int)playerInView[5] == -1) {
					System.out.println("CRF sequence playerview break");
					break;
				}
				System.out.println("CRF sequence player set to -2");
				playerInView[5] = -2;
				System.out.println("CRF sequence playerview put object back");
				game.put(playerInView[0],
						playerInView[1],
						playerInView[2],
						playerInView[3],
						playerInView[4],
						playerInView[5]);
				Thread.sleep(60);
				System.out.println("CRF sequence playerview wait for player action");

				checkPlayerAction(game, board, playerTurn);
			}
			System.out.println("CRF sequence playerturn increment");
			playerTurn = playerTurn + 1; //Changes the value to the next player
		}
	}
	
	public void checkPlayerAction(SequentialSpace game, SequentialSpace board, int playerNumber) throws InterruptedException {
		System.out.println("wait for player method");
		Object[] playerInView;
		System.out.println("wait for player method");
		Object[] playerBoard;
		System.out.println("wait for player method");
		Object[] waitingForPlayer = (Object[]) waitForPlayer(game, playerNumber);
		System.out.println(waitingForPlayer[5]);
		while((int)waitingForPlayer[5] < 0) {
			System.out.println("aargh jeg i looop");
				//Endless loop to keep quering for the specific player.
		}
		System.out.println("ikke i loop");
		playerInView = waitingForPlayer;
		System.out.println("player in view " + playerInView[1] + " " + playerInView[5]);
		if((int)playerInView[5] >= 0) {
			//raises the pot with the value the player entered. also works for just checking.
			System.out.println("Raises pot");
//			playerBoard = (Object[]) getBoard(board); //gets the board into view
//			System.out.println("got the board");
//			playerBoard[5] = (int)playerBoard[5] + (int)playerInView[5]; //updates the pot with the value the player entered
//			System.out.println("updated the board");
			playerInView[5] = -3; //sets the player to be in waiting state again.
//			board.put(playerBoard); //returns the board object to the boardspace
			game.put(playerInView[0],
					playerInView[1],
					playerInView[2],
					playerInView[3],
					playerInView[4],
					playerInView[5]); //returns the player object to the gamespace
			Thread.sleep(60);
			
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
	
	public Object waitForPlayer(SequentialSpace game, int number) throws InterruptedException {
		System.out.println("waiting for player");
		System.out.println(number);
		Object[] waitingForPlayer;

		waitingForPlayer = game.get(new ActualField(number),
				new FormalField(String.class),
				new FormalField(Integer.class),
				new FormalField(Object.class),
				new FormalField(Object.class),
				new FormalField(Integer.class));
		System.out.println("Spiller hentet");
		
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
	

	
	public static String boardSpace(SpaceRepository spaceRepo, SequentialSpace board) {
		// check if board exists 
		String boardUrl = "tcp://localhost:9003/board?keep";
		return boardUrl;
	}

	public static String gameSpace(SpaceRepository spaceRepo, SequentialSpace game) {
		// check if board exists 
		String gameUrl = "tcp://localhost:9003/game?keep";
		return gameUrl;
	}
	
	// fills the seats.
	public static int fillSeats(int[] ar){
		int place =0;
		for(int i = 0; i < 7; i++){
			if(ar[i] == 0){
				ar[i] =  i+1; // needs to be changed to accommodate changes to the overall code.
				place = ar[i];
				i=7;
			}
		}
		return place;
	}
		
	// takes the seats and rotates them by one
	public static int[] rotateLeft(int[] ar){
		int temp;
		for(int i = 0; i< ar.length-1; i++){
			temp = ar[i];
			ar[i] = ar[i+1];
			ar[i+1] = temp;
		}
		return ar;
	}

	public static int countPlayers(int[] mainAr) {
		int count=0;
		for(int i = 0; i< mainAr.length; i++) {
			if(mainAr[i] != 0) {
				count++;
			}
		}
		return count;
	}
		
}


class canJoin implements Runnable {

	SequentialSpace lobby;
	SpaceRepository spaceRepo;
	SequentialSpace game;
	SequentialSpace board;
	int[] mainAr;
	
	
	public canJoin(SequentialSpace lobby,SpaceRepository spaceRepo, SequentialSpace game,SequentialSpace board, int[] mainAr) {
		this.lobby = lobby;
		this.spaceRepo = spaceRepo;
		this.game = game;
		this.board = board;
		this.mainAr = mainAr;
		
	}
	@Override
	public void run() {
			
			while(true) {
			
			
				Object[] players = null;
				try {
					players = lobby.get(new ActualField("join"), new FormalField(String.class));
				
					House.playerId = House.fillSeats(mainAr);
					if(House.playerId > 0){
							lobby.put(House.playerId, players[1], House.BALANCE, House.gameSpace(spaceRepo, game),House.boardSpace(spaceRepo, board));
					}
					else {
							lobby.put("wait");		
						} //needs more stuff done to work properly. and to put players in loop
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}

	}
}
}
	
