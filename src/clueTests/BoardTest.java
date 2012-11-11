package clueTests;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ClueBoard.BadConfigFormatException;
import ClueBoard.Board;
import ClueBoard.BoardCell;
import ClueBoard.RoomCell;
import ClueBoard.RoomCell.DoorDirection;


public class BoardTest {
	private static Board board;
	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 25;
	public static final int NUM_COLUMNS = 25;
	
	@BeforeClass
	public static void initialize() throws FileNotFoundException, BadConfigFormatException {
		board = new Board("config.txt", "legend.txt", "players.txt", "cards.txt",1);
		
	}
		
	@Test
	public void testRooms() {
		Map<Character, String> rooms = board.getRooms();
		assertEquals(rooms.size(), NUM_ROOMS);		
		assertEquals("Blackjack Room", rooms.get('H'));
		assertEquals("Games Room", rooms.get('G'));
		assertEquals("Bowling Alley", rooms.get('B'));
		assertEquals("Zoo", rooms.get('Z'));
	}
	
	@Test
	public void testBoardDimensions() {
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}
	
	@Test
	public void testDoorDirection() {
		RoomCell room;
		room = board.GetRoomCellAt(7, 5);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.DOWN ,room.getDoorDirection());
		room = board.GetRoomCellAt(23, 22);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.LEFT ,room.getDoorDirection());
		room = board.GetRoomCellAt(14, 21);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.RIGHT ,room.getDoorDirection());
		room = board.GetRoomCellAt(14, 0);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.UP ,room.getDoorDirection());
	}
	
	@Test
	public void testNumberDoorways() {
		int numDoors = 0;
		int numCells = board.getNumColumns() * board.getNumRows();
		assertEquals(625, numCells);
		for (int i = 0; i < numCells; ++i) {
			BoardCell cell = board.getCellAt(i);
			if (cell.isDoorway())
				++numDoors;
		}
		assertEquals(12 , numDoors);
	}
	
	@Test
	public void testRoomInitial() {
		assertEquals('H', board.GetRoomCellAt(1, 5).getRoomInitial());
		assertEquals('L', board.GetRoomCellAt(19, 24).getRoomInitial());
		assertEquals('D', board.GetRoomCellAt(0, 24).getRoomInitial());
		assertEquals('Z', board.GetRoomCellAt(16, 3).getRoomInitial());
		assertEquals('P', board.GetRoomCellAt(23, 8).getRoomInitial());
	}
	
	@Test
	public void testCalcIndex() {
		assertEquals(0, board.calcIndex(0, 0));
		assertEquals(25, board.calcIndex(1, 0));
		assertEquals(624, board.calcIndex(NUM_ROWS-1, NUM_COLUMNS-1));
		assertEquals(NUM_COLUMNS-1, board.calcIndex(0, NUM_COLUMNS-1));
		assertEquals(600, board.calcIndex(NUM_ROWS-1, 0));
		assertEquals(312, board.calcIndex(12, 12));
		assertEquals(520, board.calcIndex(20, 20));	
	}
	

}


