package intBoard;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class IntBoardTests {

	IntBoard board;
	
	@Before
	public void initialize(){
		board = new IntBoard();
	}
	

	@Test
	public void testCalcIndex0(){
		Assert.assertEquals(0, board.calcIndex(0,0));
	}
	
	@Test
	public void testCalcIndex1(){
		Assert.assertEquals(1, board.calcIndex(0,1));
	}
	
	@Test
	public void testCalcIndex10(){
		Assert.assertEquals(10, board.calcIndex(2,2));
	}
	
	@Test
	public void testAdjacency0()
	{
		LinkedList<Integer> testList = board.getAdjList(0,0);

		Assert.assertTrue(testList.contains(4));
		Assert.assertTrue(testList.contains(1));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacency15()
	{
		
		LinkedList<Integer> testList = board.getAdjList(3, 3);
		
		
		Assert.assertTrue(testList.contains(11));
		Assert.assertTrue(testList.contains(14));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacency7()
	{
		LinkedList<Integer> testList = board.getAdjList(1, 3);
		
		Assert.assertTrue(testList.contains(11));
		Assert.assertTrue(testList.contains(3));
		Assert.assertTrue(testList.contains(6));
		Assert.assertEquals(3, testList.size());
	}
	@Test
	public void testAdjacency8()
	{
		
		LinkedList<Integer> testList = board.getAdjList(2, 0);
		
		Assert.assertTrue(testList.contains(12));
		Assert.assertTrue(testList.contains(9));
		Assert.assertTrue(testList.contains(4));
		Assert.assertEquals(3, testList.size());
	}
	@Test
	public void testAdjacency5()
	{

		LinkedList<Integer> testList = board.getAdjList(1, 1);

		Assert.assertTrue(testList.contains(4));
		Assert.assertTrue(testList.contains(9));
		Assert.assertTrue(testList.contains(6));
		Assert.assertTrue(testList.contains(1));
		Assert.assertEquals(4, testList.size());
	}
	@Test
	public void testAdjacency10()
	{
		LinkedList<Integer> testList = board.getAdjList(2, 2);
		Assert.assertTrue(testList.contains(14));
		Assert.assertTrue(testList.contains(11));
		Assert.assertTrue(testList.contains(6));
		Assert.assertTrue(testList.contains(9));
		Assert.assertEquals(4, testList.size());
	}
	@Test
	public void testAdjacency3()
	{
		LinkedList<Integer> testList = board.getAdjList(0, 3);
		Assert.assertTrue(testList.contains(2));
		Assert.assertTrue(testList.contains(7));
		Assert.assertEquals(2, testList.size());
	}
	@Test
	public void testAdjacency12()
	{
		LinkedList<Integer> testList = board.getAdjList(3, 0);
		Assert.assertTrue(testList.contains(8));
		Assert.assertTrue(testList.contains(13));
		Assert.assertEquals(2, testList.size());
	}
	
	
	@Test
	public void testTargets0_1()
	{
		board.calcTargets(0, 1);
		HashSet targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(4));
	}
	
	
	@Test
	public void testTargets1_3()
	{
		board.calcTargets(1, 2);
		HashSet targets= board.getTargets();
		
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(4));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(9));
		
	}
	
	
	@Test
	public void testTargets2_3()
	{
		board.calcTargets(2, 3);
		HashSet targets= board.getTargets();
		
		Assert.assertEquals(7, targets.size());
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(4));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(11));
		Assert.assertTrue(targets.contains(14));
	
	}
	
	
	
	@Test
	public void testTargets3_3()
	{
		board.calcTargets(3, 3);
		HashSet targets= board.getTargets();
		
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(0));
		Assert.assertTrue(targets.contains(2));
		Assert.assertTrue(targets.contains(5));
		Assert.assertTrue(targets.contains(7));
		Assert.assertTrue(targets.contains(10));
		Assert.assertTrue(targets.contains(15));
	}
	
	
	@Test
	public void testTargets4_6()
	{
		board.calcTargets(4, 6);
		HashSet targets= board.getTargets();
		Assert.assertEquals(7, targets.size());
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(11));
		Assert.assertTrue(targets.contains(12));
		Assert.assertTrue(targets.contains(14));
	}
	
	
	@Test
	public void testTargets5_5()
	{
		board.calcTargets(5, 5);
		HashSet targets= board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(4));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(11));
		Assert.assertTrue(targets.contains(12));
		Assert.assertTrue(targets.contains(14));
	}
	
}
