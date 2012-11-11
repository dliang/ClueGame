package clueTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ClueBoard.BadConfigFormatException;
import ClueBoard.Board;
import ClueBoard.BoardCell;
import ClueBoard.Card.CardType;
import ClueBoard.ComputerPlayer;
import ClueBoard.HumanPlayer;
import ClueBoard.Player;
import ClueBoard.Card;
import ClueBoard.Solution;

public class GameSetupTests {	
	private static Board board;
	//person cards
	private static Card profPlumCard, mustardCard, mrsWhiteCard, mrsPeacockCard, mrGreenCard, missScarletCard;
	//room cards
	private static Card blackjackCard, gamesCard, zooCard, parlourCard, bowlingCard, cloakCard, larderCard, dugoutCard, mcdonaldsCard; 
	//weapon cards
	private static Card candlestickCard, revolverCard, wrenchCard, knifeCard, pipeCard, ropeCard;
	
	
	@BeforeClass
	public static void setUp() throws IOException, BadConfigFormatException {
		board = new Board("config.txt", "legend.txt", "players.txt", "cards.txt",1);

		//person cards
		profPlumCard = new Card("Professor Plum", ClueBoard.Card.CardType.PERSON);
        mustardCard = new Card("Colonel Mustard", ClueBoard.Card.CardType.PERSON);
        mrsWhiteCard = new Card("Mrs. White", ClueBoard.Card.CardType.PERSON);
        mrsPeacockCard = new Card("Mrs. Peacock", ClueBoard.Card.CardType.PERSON);
        mrGreenCard = new Card("Mr. Green", ClueBoard.Card.CardType.PERSON);
        missScarletCard = new Card("Miss Scarlet", ClueBoard.Card.CardType.PERSON);
        //room cards
        blackjackCard = new Card("Blackjack Room", ClueBoard.Card.CardType.ROOM);
        gamesCard = new Card("Games Room", ClueBoard.Card.CardType.ROOM);
        zooCard = new Card("Zoo", ClueBoard.Card.CardType.ROOM);
        parlourCard = new Card("Parlour", ClueBoard.Card.CardType.ROOM);
        bowlingCard = new Card("Bowling Alley", ClueBoard.Card.CardType.ROOM);
        cloakCard = new Card("Cloak Room", ClueBoard.Card.CardType.ROOM);
        larderCard = new Card("Larder", ClueBoard.Card.CardType.ROOM);
        dugoutCard = new Card("Dugout", ClueBoard.Card.CardType.ROOM);
        mcdonaldsCard = new Card("McDonalds", ClueBoard.Card.CardType.ROOM);
        //weapon cards
        candlestickCard = new Card("Candlestick", ClueBoard.Card.CardType.WEAPON);
        revolverCard = new Card("Revolver", ClueBoard.Card.CardType.WEAPON);
        wrenchCard = new Card("Wrench", ClueBoard.Card.CardType.WEAPON);
        knifeCard = new Card("Knife", ClueBoard.Card.CardType.WEAPON);
        pipeCard = new Card("Lead Pipe", ClueBoard.Card.CardType.WEAPON);
        ropeCard = new Card("Rope", ClueBoard.Card.CardType.WEAPON);
	}
	
	
	@Test
	public void testLoadPlayer() {
		//test human
		Assert.assertEquals("Professor Plum", board.getPlayer(0).getName());
		Assert.assertEquals("blue", board.getPlayer(0).getColor());
		Assert.assertEquals(board.calcIndex(6, 0), board.getPlayer(0).getLocation());
		
		//test computer player
		Assert.assertEquals("Colonel Mustard", board.getPlayer(1).getName());
		Assert.assertEquals("orange", board.getPlayer(1).getColor());
		Assert.assertEquals(board.calcIndex(7, 24), board.getPlayer(1).getLocation());
	}
	
	@Test
	public void testLoadCards() {
		//test deck size
		Assert.assertEquals(21, board.getAllCards().size());
		
		//check type of cards
		int weapon_cards = 0, person_cards = 0, room_cards = 0;
		for (int i = 0; i < board.getAllCards().size(); i++) {
			if (board.getAllCards().get(i).getCardtype().equals(CardType.ROOM))
				room_cards++;
			else if (board.getAllCards().get(i).getCardtype().equals(CardType.WEAPON))
				weapon_cards++;
			else if (board.getAllCards().get(i).getCardtype().equals(CardType.PERSON))
				person_cards++;
		}
		//check that the deck contains correct number of cards of each type
		Assert.assertEquals(6, person_cards);
		Assert.assertEquals(6, weapon_cards);
		Assert.assertEquals(9, room_cards);
		
		//check that the deck contains one of each card type

		int weapon_cnt = 0;
		int room_cnt = 0;
		int person_cnt = 0;
		
		
		for (int i = 0; i < board.getAllCards().size(); i++) {
			if (board.getAllCards().get(i).equals(knifeCard))
				weapon_cnt++;
			else if (board.getAllCards().get(i).equals(zooCard))
				room_cnt++;
			else if (board.getAllCards().get(i).equals(profPlumCard))
				person_cnt++;	
		}
		Assert.assertEquals(1, weapon_cnt);
		Assert.assertEquals(1, room_cnt);
		Assert.assertEquals(1, person_cnt);
		
	}

	@Test
	public void testDealCards() {
		//test all cards are dealt
		//board.deal();
		int total_cards = 0;
		for (int i = 0; i < board.getPlayerList().size(); i++) {
			total_cards += board.getPlayerList().get(i).getMyCards().size();
		}
		Assert.assertEquals(18, total_cards);
		
		//test all players have roughly the same number of cards
		int min_cards = board.getPlayerList().get(0).getMyCards().size();
		int max_cards = 0;
		for (int i = 1; i <board.getPlayerList().size(); i++) {
			if (board.getPlayerList().get(i).getMyCards().size() > max_cards)
				max_cards = board.getPlayerList().get(i).getMyCards().size();
			if (board.getPlayerList().get(i).getMyCards().size() < min_cards)
				min_cards = board.getPlayerList().get(i).getMyCards().size();
		}
		Assert.assertTrue((max_cards - min_cards) < 3);
		
		//Test that one card is not given to two different players
		ArrayList<Card> deckCheck = new ArrayList<Card>();
		
		for (int i = 0; i <board.getPlayerList().size(); i++) {
			for (int j = 0; j < board.getPlayerList().get(i).getMyCards().size(); j++) {
				if (!deckCheck.contains(board.getPlayerList().get(i).getMyCards().get(j)))
					deckCheck.add(board.getPlayerList().get(i).getMyCards().get(j));
			}
		}
		Assert.assertEquals(18, deckCheck.size());
		
	}
	
	@Test
	public void testCheckAccusation() {
		
		board.setSolution("Professor Plum", "Zoo", "Knife");
		
		//Check to ensure the accusation is true
		Assert.assertTrue(board.checkAccusation("Professor Plum", "Zoo", "Knife"));
		//Check to ensure the accusation is false
		Assert.assertFalse(board.checkAccusation("Colonel Mustard", "Parlour", "Rope"));
		
		//Check if accusation is false for person
		Assert.assertFalse(board.checkAccusation("Colonel Mustard", "Zoo", "Knife"));
		
		//Check if accusation is false for room
		Assert.assertFalse(board.checkAccusation("Professor Plum", "Parlour", "Knife"));
		
		//Check if accusation is false for weapon
		Assert.assertFalse(board.checkAccusation("Professor Plum", "Zoo", "Rope"));
	}
	
	
	@Test
	public void testTargetRandomSelection() {
		ComputerPlayer test_player = (ComputerPlayer) board.getPlayer(1);
		// Pick a location with no rooms in target, just three targets
		board.calcTargets(board.calcIndex(6, 0), 2);
		int loc_5_1Tot = 0;
		int loc_4_0Tot = 0;
		int loc_6_2Tot = 0;
		// Run the test 100 times
		for (int i=0; i<100; i++) {
			board.calcTargets(board.calcIndex(6, 0), 2);
			BoardCell selected = (BoardCell) test_player.pickLocation();

			if (selected == board.getCellAt(board.calcIndex(4, 0)))
				loc_4_0Tot++;
			else if (selected == board.getCellAt(board.calcIndex(5, 1)))
				loc_5_1Tot++;
			else if	(selected == board.getCellAt(board.calcIndex(6, 2)))
				loc_6_2Tot++;
			else
				fail("Invalid target selected");
		}
		
		System.out.println("o, 3: " + loc_5_1Tot);
		System.out.println("4, 0: " + loc_4_0Tot);
		System.out.println("6, 2: " + loc_6_2Tot);
		// Ensure we have 100 total selections (fail should also ensure)
		assertEquals(100, loc_5_1Tot + loc_4_0Tot + loc_6_2Tot);
		// Ensure each target was selected more than once
		assertTrue(loc_5_1Tot > 10);
		assertTrue(loc_4_0Tot > 10);	
		assertTrue(loc_6_2Tot > 10);
	}
	
	
		@Test
		public void testDisproveSuggestion1() {
			ComputerPlayer player = new ComputerPlayer("Mrs. White", "WHITE", 5, board.getCellAt(5),board);
			//give player 2 of each card
	        player.addCard(mrsPeacockCard);
	        player.addCard(mustardCard);
	        player.addCard(knifeCard);
	        player.addCard(candlestickCard);
	        player.addCard(zooCard);
	        player.addCard(parlourCard);
	        
	        Assert.assertEquals(mrsPeacockCard, player.disproveSuggestion(mrsPeacockCard, larderCard, wrenchCard));
	        Assert.assertEquals(zooCard, player.disproveSuggestion(profPlumCard, zooCard, wrenchCard));
	        Assert.assertEquals(knifeCard, player.disproveSuggestion(profPlumCard, larderCard, knifeCard));
	        Assert.assertEquals(null, player.disproveSuggestion(profPlumCard, larderCard, wrenchCard));
			
		}
		
		@Test
		public void testDisproveSuggestion2() {
	        int tot_Room = 0;
	        int tot_Weapon = 0;
	        int tot_Person = 0;
	        ComputerPlayer player = new ComputerPlayer("Mrs. White", "WHITE", 5, board.getCellAt(5),board);
	        
	        player.addCard(profPlumCard);
	        player.addCard(knifeCard);
	        player.addCard(ropeCard);
	        player.addCard(parlourCard);
	        
	        //test that any of the three cards will be returned
	        for (int i = 0; i < 100; i++) {
	            Card result = player.disproveSuggestion(profPlumCard, parlourCard, knifeCard);
	            if(result.getCardName().equals("Professor Plum"))
	                tot_Person++;
	            else if (result.getCardName().equals("Parlour"))
	                tot_Room++;
	            else if (result.getCardName().equals("Knife"))
	                tot_Weapon++;
	            else
	                Assert.fail();
	        }
	        Assert.assertTrue(tot_Room > 0);
	        Assert.assertTrue(tot_Person > 0);
	        Assert.assertTrue(tot_Weapon > 0);
		}
	
		@Test
		public void testDisproveSuggestion3() {
	        ArrayList players = new ArrayList();
	        
	        HumanPlayer hplayer = new HumanPlayer ("Professor Plum", "PURPLE", 3);
	        hplayer.addCard(mustardCard);
	        hplayer.addCard(bowlingCard);
	        hplayer.addCard(knifeCard);
	        hplayer.addCard(mrsWhiteCard);
	        players.add(hplayer);
	        
	        ComputerPlayer player = new ComputerPlayer("Mrs. White", "WHITE", 0, board.getCellAt(0),board);
	        player.addCard(profPlumCard);
	        player.addCard(dugoutCard);
	        player.addCard(ropeCard);
	        player.addCard(zooCard);
	        players.add(player);
	        
	        player = new ComputerPlayer("Mr. Green", "GREEN", 176, board.getCellAt(176),board);
	        player.addCard(missScarletCard);
	        player.addCard(wrenchCard);
	        player.addCard(revolverCard);
	        player.addCard(parlourCard);
	        players.add(player);
	        
	        player = new ComputerPlayer("Colonel Mustard", "ORANGE", 50, board.getCellAt(50),board);
	        player.addCard(mrsPeacockCard);
	        player.addCard(pipeCard);
	        player.addCard(gamesCard);
	        player.addCard(blackjackCard);
	        players.add(player);
	        
	        board.setPlayers(players);
	        
	        //test suggestion which no players can disprove
	        Assert.assertEquals(null, board.getPlayerList().get(0).disproveSuggestion(mrGreenCard, mcdonaldsCard, candlestickCard));
	        Assert.assertEquals(null, board.getPlayerList().get(1).disproveSuggestion(mrGreenCard, mcdonaldsCard, candlestickCard));
	        Assert.assertEquals(null, board.getPlayerList().get(2).disproveSuggestion(mrGreenCard, mcdonaldsCard, candlestickCard));
	        Assert.assertEquals(null, board.getPlayerList().get(3).disproveSuggestion(mrGreenCard, mcdonaldsCard, candlestickCard));
	        
	        //test suggestion only human player can disprove
	        Assert.assertEquals(mustardCard, board.getPlayerList().get(0).disproveSuggestion(mustardCard, mcdonaldsCard, candlestickCard));
	        Assert.assertEquals(null, board.getPlayerList().get(1).disproveSuggestion(mustardCard, mcdonaldsCard, candlestickCard));
	        Assert.assertEquals(null, board.getPlayerList().get(2).disproveSuggestion(mustardCard, mcdonaldsCard, candlestickCard));
	        Assert.assertEquals(null, board.getPlayerList().get(3).disproveSuggestion(mustardCard, mcdonaldsCard, candlestickCard));
	 
//	        //test that the player who made the suggestion returns null if that player is the only one who can disprove it
//	        Assert.assertEquals(null, board.getPlayerList().get(3).disproveSuggestion(mrsPeacockCard, mcdonaldsCard, candlestickCard));
	        
	        Card return_card = null;
	        for (int i = 0; i < players.size(); i++) {
	        	return_card = ((Player) players.get(i)).disproveSuggestion(mrsPeacockCard, mcdonaldsCard, candlestickCard);
	        	if (!(return_card == null)) {
	        		break;
	        	}
	        }
	        Assert.assertEquals(mrsPeacockCard, return_card);
	        
	        
	        
	        //Ensure that players are not queried in the same order each time
	        int p0count = 0;
	        int p1count = 0;
	        int p2count = 0;
	        int p3count = 0;
	        
	        for (int j = 0; j < 100; j++) {
		        for (int i = 0; i < players.size(); i++) {
		        	switch (i) {
		        	case 0:	if (null != ((Player) players.get(i)).disproveSuggestion(mustardCard, mcdonaldsCard, wrenchCard))
		        			p0count++;
		        			break;
		        	case 1:	if (null != ((Player) players.get(i)).disproveSuggestion(mustardCard, mcdonaldsCard, wrenchCard))
	        				p1count++;
	        				break;
		        	case 2:	if (null != ((Player) players.get(i)).disproveSuggestion(mustardCard, mcdonaldsCard, wrenchCard))
	        				p2count++;
	        				break;
		        	case 3:	if (null != ((Player) players.get(i)).disproveSuggestion(mustardCard, mcdonaldsCard, wrenchCard))
	        				p3count++;
	        				break;
		        	}
		        
		        }
	        }
	        
	        Assert.assertTrue(p0count > 1);
	        Assert.assertTrue(p1count == 0);
	        Assert.assertTrue(p2count > 1);
	        Assert.assertTrue(p3count == 0);
		}
		
		
		
		@Test
		public void testComputerSuggestion() throws FileNotFoundException {
	        ArrayList players = new ArrayList();
	        ArrayList<Card> suggest = new ArrayList<Card>();
	        
	        HumanPlayer hplayer = new HumanPlayer ("Professor Plum", "PURPLE", 3);
	        hplayer.addCard(mustardCard);
	        hplayer.addCard(bowlingCard);
	        hplayer.addCard(knifeCard);
	        hplayer.addCard(mrsWhiteCard);
	        players.add(hplayer);
	        
	        ComputerPlayer player = new ComputerPlayer("Mrs. White", "WHITE", 0, board.getCellAt(0),board);
	        player.addCard(profPlumCard);
	        player.addCard(dugoutCard);
	        player.addCard(ropeCard);
	        player.addCard(zooCard);
	        players.add(player);
	        
	        player = new ComputerPlayer("Mr. Green", "GREEN", 176, board.getCellAt(176),board);
	        player.addCard(missScarletCard);
	        player.addCard(wrenchCard);
	        player.addCard(revolverCard);
	        player.addCard(parlourCard);
	        players.add(player);
	        
	        player = new ComputerPlayer("Colonel Mustard", "ORANGE", 50, board.getCellAt(50),board);
	        player.addCard(mrsPeacockCard);
	        player.addCard(pipeCard);
	        player.addCard(gamesCard);
	        player.addCard(blackjackCard);
	        players.add(player);
	        
	        board.setPlayers(players);
			
	        ComputerPlayer p1 = (ComputerPlayer)players.get(1);
	       // p1.createDeck("cards.txt");
	        
	        int mustard = 0;
	        int bowling = 0;
	        int knife = 0;
	        for(int i = 0; i < 100; i++)
	        {
	        	suggest = p1.createSuggestion();
	            Card returnCard = board.getPlayerList().get(0).disproveSuggestion(suggest.get(0), suggest.get(1), suggest.get(2));
	            if (!(returnCard == null)) {
		            if(returnCard.equals(mustardCard))
		                mustard++;
		            else if(returnCard.equals(bowlingCard))
		                bowling++;
		            else if(returnCard.equals(knifeCard))
		                knife++;
	            }
	        }
	        
	        System.out.println ("mustard: " + mustard);
	        System.out.println("knife: " + knife);
	        System.out.println("bowling: " + bowling);

	        Assert.assertTrue(mustard > 1);
	        Assert.assertTrue(knife > 1);
	        Assert.assertTrue(bowling > 1);
		}
//	
}
