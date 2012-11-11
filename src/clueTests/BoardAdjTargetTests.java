package clueTests;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ClueBoard.BadConfigFormatException;
import ClueBoard.Board;
import ClueBoard.BoardCell;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardAdjTargetTests {
	private static Board board;
	@BeforeClass
	public static void setUp() throws IOException, BadConfigFormatException {
		board = new Board("config.txt", "legend.txt", "players.txt", "cards.txt",1);
	}
	
	
	
	@Test
	public void testAdjacenciesInsideRooms() throws IOException
	{
		
		// Test a corner
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(0, 24));
		Assert.assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(board.calcIndex(6, 20));
		Assert.assertEquals(0, testList.size());
		//Test walkway on all sides but one
		testList = board.getAdjList(board.calcIndex(11, 23));
		Assert.assertEquals(0, testList.size());
		//Test door top and bottom
		testList = board.getAdjList(board.calcIndex(15, 0));
		Assert.assertEquals(0, testList.size());
		//Test walkway on 2 sides, room on 2 sides
		testList = board.getAdjList(board.calcIndex(12, 20));
		Assert.assertEquals(0, testList.size());
	}
	
	
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(0, 10));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(0, 9)));
		// TEST DOORWAY LEFT
		testList = board.getAdjList(board.calcIndex(0, 3));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(0, 2)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(board.calcIndex(22, 14));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(23, 14)));
		//TEST DOORWAY UP
		testList = board.getAdjList(board.calcIndex(16, 18));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(15, 18)));
		
	}
	
	
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just one walkway piece
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(0, 13));
		Assert.assertTrue(testList.contains(14));
		Assert.assertEquals(1, testList.size());
		
		// Test on left edge of board, three walkway pieces
		testList = board.getAdjList(board.calcIndex(12, 0));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 0)));
		Assert.assertTrue(testList.contains(board.calcIndex(13, 0)));
		Assert.assertTrue(testList.contains(board.calcIndex(12, 1)));
		Assert.assertEquals(3, testList.size());

		// Test between two rooms, walkways right and left
		testList = board.getAdjList(board.calcIndex(16, 8));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 7)));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 9)));
		Assert.assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(board.calcIndex(6, 15));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 16)));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 14)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 15)));
		Assert.assertTrue(testList.contains(board.calcIndex(5, 15)));
		Assert.assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(board.calcIndex(24, 17));
		Assert.assertTrue(testList.contains(board.calcIndex(23, 17)));
		Assert.assertTrue(testList.contains(board.calcIndex(24, 16)));
		Assert.assertEquals(2, testList.size());
		
		// Test on ridge edge of board, next to 1 room piece
		testList = board.getAdjList(board.calcIndex(5, 24));
		Assert.assertTrue(testList.contains(board.calcIndex(5, 23)));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 24)));
		Assert.assertEquals(2, testList.size());

	}
	
	
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction UP
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(13, 0));
		Assert.assertTrue(testList.contains(board.calcIndex(12, 0)));
		Assert.assertTrue(testList.contains(board.calcIndex(13, 1)));
		Assert.assertTrue(testList.contains(board.calcIndex(14, 0)));
		Assert.assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(board.calcIndex(23, 14));
		Assert.assertTrue(testList.contains(board.calcIndex(22, 14)));
		Assert.assertTrue(testList.contains(board.calcIndex(24, 14)));
		Assert.assertEquals(2, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(board.calcIndex(0, 9));
		Assert.assertTrue(testList.contains(board.calcIndex(0, 8)));
		Assert.assertTrue(testList.contains(board.calcIndex(0, 10)));
		Assert.assertTrue(testList.contains(board.calcIndex(1, 9)));
		Assert.assertEquals(3, testList.size());
		// Test beside a door direction RIGHT
		testList = board.getAdjList(board.calcIndex(14, 22));
		Assert.assertTrue(testList.contains(board.calcIndex(14, 21)));
		Assert.assertTrue(testList.contains(board.calcIndex(14, 23)));
		Assert.assertTrue(testList.contains(board.calcIndex(13, 22)));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 22)));
		Assert.assertEquals(4, testList.size());
	}
	
	
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(board.calcIndex(20, 24), 1);
		Set<BoardCell> targets2= board.getTargets();
		Assert.assertEquals(1, targets2.size());
		Assert.assertTrue(targets2.contains(board.getCellAt(board.calcIndex(20, 23))));		
		

		board.calcTargets(board.calcIndex(24, 17), 1);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(24, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 17))));	

	
	
			
	}
	
	

	// Tests of just walkways, 2 steps
	@Test
	public void testTargetsTwoSteps() {

		board.clearTargets();
		board.calcTargets(board.calcIndex(24, 17), 2);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(24, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22, 17))));
		
		board.clearTargets();
		board.calcTargets(board.calcIndex(20, 24), 2);
		targets= board.getTargets();
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 22))));			
	}
	
	// Tests of just walkways, 4 steps
	@Test
	public void testTargetsFourSteps() {

		board.clearTargets();
		board.calcTargets(board.calcIndex(20, 24), 4);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 20))));
		board.clearTargets();
		
	

		// Includes a path that doesn't have enough length
		board.calcTargets(board.calcIndex(24, 17), 4);
		targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(21, 16))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22, 17))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(24, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 16))));

	}	
	
	
	
	// Tests of just walkways plus one door, 6 steps
	@Test
	public void testTargetsSixSteps() {
		board.clearTargets();
		board.calcTargets(board.calcIndex(24, 17), 6);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(10, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(21, 16))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 17))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 17))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 16))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 15))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(24, 15))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 14))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 16))));	
	}

	
	@Test 
	public void testTargetsIntoRoom()
	{
		// One room is exactly 2 away
		board.calcTargets(board.calcIndex(2, 16), 2);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(8, targets.size());
		// directly left/right
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 18))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 14))));
		// directly up and down
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 16))));
		// one up/down, one left/right
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(3, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(3, 15))));
	}
	
	
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(board.calcIndex(0, 0), 5);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		// directly down (can't go up)
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(5, 0))));
		// directly right (can't go left)
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 3))));
	}
	
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(board.calcIndex(12, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(12, 2))));
		// Take two steps
		board.calcTargets(board.calcIndex(12, 3), 2);
		targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(12, 1))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(13, 2))));
	}
	
}
