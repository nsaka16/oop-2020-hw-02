

import junit.framework.TestCase;

import java.util.*;

/*
	Tests for Piece class.
 */
public class PieceTest extends TestCase {

	/*
		These variables represent rotations of each Piece.
	 */
	private Piece pyr1, pyr2, pyr3, pyr4;	//Pyramid has 4 distinct states.
	private Piece s1_1, s1_2;	//S-Figures have 2 distinct states.
	private Piece s2_1, s2_2;
	private Piece square;	//Square has only 1 distinct state.
	private Piece stick1, stick2;	//Stick has 2 distinct states.

	private Piece[] pieces;  // This array is filled during setUp() method.

	/*
		I set up some global Piece objects using constructors() in setUp(),
		but i'll also have some local(in some methods) Piece objects.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		/*
			!Attention! remember that method fastRotation() is only used for pieces[] array.
		 */
		pieces = Piece.getPieces();	//pieces have first rotations as array members, not original ones.

		/*
			Initialise global variables of tests.
		 */
		pyr1 = pieces[ Piece.PYRAMID ];
		pyr2 =  pyr1.fastRotation();
		pyr3 =  pyr2.fastRotation();
		pyr4 =  pyr3.fastRotation();

		s1_1 = new Piece( Piece.S1_STR );
		s1_2 = s1_1.computeNextRotation();

		s2_1 = new Piece( Piece.S2_STR );
		s2_2 = s2_1.computeNextRotation();

		square = new Piece( Piece.SQUARE_STR );

		stick1 = new Piece( Piece.STICK_STR );
		stick2 = stick1.computeNextRotation();
	}

	/*
		Writing tests for TPoint class.
	 */
	public void testTPoint()
	{
		TPoint point1 = new TPoint(1,2);
		TPoint point2 = new TPoint(point1);

		assertTrue( point1.equals(point2) );
		assertTrue( point1.toString().equals("(1,2)") );
		assertFalse( point1.equals( null ) );
		assertTrue( point1.equals( point1 ) );
	}


	//This is used as in one case Piece class'es parsePoint()
	// should throw an exception, for 100% line coverage.
	public void testException()
	{
		try
		{
			Piece piece = new Piece("blabla");
		}
		catch ( RuntimeException e )
		{
			assertEquals("Could not parse x,y string:blabla", e.getMessage()); }
	}

	public void testS1FastRotate()
	{
		//using global S Piece.
		assertTrue( s1_1.equals( s1_1.computeNextRotation().computeNextRotation() )  );	//S - figure's second rotation == original one.
		assertTrue( s1_1.computeNextRotation().equals( pieces[ Piece.S1 ].fastRotation() ) );	//	*pieces contains first rotations.
		assertTrue( s1_1.equals( s1_1 ) );
		assertFalse( s1_1.equals( s1_1.computeNextRotation() ) );
		assertFalse( s1_1.equals(null) );
	}

	public void testL1FastRotate()
	{
		//Creating local l1 Piece.
		Piece l1_1 = pieces[ Piece.L1 ];
		Piece l1_2 = l1_1.fastRotation();
		Piece l1_3 = l1_2.fastRotation();
		Piece l1_4 = l1_3.fastRotation();


		assertTrue( l1_1.fastRotation().fastRotation().fastRotation().fastRotation().equals( l1_1 ) );
		assertTrue( l1_1.equals( l1_4.fastRotation() ) );
		assertTrue( l1_2.equals( pieces[ Piece.L1 ].fastRotation() ) );
		assertTrue( l1_3.equals( pieces[ Piece.L1 ].fastRotation().fastRotation() ) );
		assertTrue( l1_1.fastRotation().fastRotation().fastRotation().equals( l1_4 ) );
		assertFalse( l1_1.equals( l1_4 ) );
	}

	public void testSquareFastRotate()
	{
		//using global square Piece.
		assertTrue( square.equals( pieces[ Piece.SQUARE ] ) );
		assertTrue( square.equals( square.computeNextRotation() ) );
		assertTrue( square.equals( square.computeNextRotation().computeNextRotation() ) );
		assertTrue( square.equals( square ) );
	}

	public void testStickFastRotate()
	{
		//using global stick1,stick2 Pieces.
		assertTrue( stick1.equals( pieces[ Piece.STICK ].fastRotation().fastRotation() ) );
		assertTrue( stick1.equals( stick1.computeNextRotation().computeNextRotation() ));
		assertFalse( stick2.equals( stick1 ) );
		assertTrue( stick2.equals( pieces[ Piece.STICK ].fastRotation() ));
	}

	public void testPyrFastRotate()
	{
		//using global pyramids Pieces.
		assertTrue( pyr4.fastRotation().equals( pyr1 ) );
		assertTrue( pyr1.fastRotation().fastRotation().fastRotation().fastRotation().equals( pyr1 ) );
		assertTrue( pyr2.equals( pieces[ Piece.PYRAMID ].fastRotation() ));
		assertTrue( pyr1.equals( pyr1 ) );
		assertFalse( pyr4.equals( pyr1 ) );
	}

	/*
		This function checks pyramid's width/height correctness.
	 */
	public void testPyramidSize()
	{
		//Using global pyr1,pyr2,pyr3,pyr4 Pieces.

		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());

		assertEquals(3, pyr3.getWidth());
		assertEquals(2, pyr3.getHeight());

		//pyr4's width,height == pyr2's width,height.
		assertEquals( pyr2.getWidth() , pyr4.getWidth() );
		assertEquals( pyr2.getHeight() , pyr4.getHeight() );

		//pyr3's width,height == pyr1's width,height.
		assertEquals( pyr1.getWidth(), pyr3.getWidth() );
		assertEquals( pyr1.getHeight(), pyr3.getHeight() );
	}

	/*
		This function checks Stick's width/height correctness.
	 */
	public void testStickSize()
	{
		//using global stick1,stick2 variables.

		assertEquals( 4, stick2.getWidth() );
		assertEquals( 1, stick2.getHeight() );
		assertEquals( pieces[ Piece.STICK ].fastRotation().getWidth() , stick2.getWidth() );

		assertEquals( 1, stick1.getWidth() );
		assertEquals( 4, stick1.getHeight() );
		assertEquals( pieces[ Piece.STICK ].fastRotation().fastRotation().getWidth(), stick1.getWidth() );
	}

	/*
		This function checks Square's width/height correctness.
	 */
	public void testSquareSize()
	{
		assertEquals( 2, square.getWidth() );
		assertEquals( 2, square.getHeight() );
		assertEquals( pieces[ Piece.SQUARE ].fastRotation().getWidth(), square.getWidth() );
		assertEquals( pieces[ Piece.SQUARE ].fastRotation().getWidth(), square.computeNextRotation().getWidth() );
	}


	public void testL1Size()
	{
		//Creating local l1 Piece.
		Piece l1_1 = pieces[ Piece.L1 ];
		Piece l1_2 = l1_1.fastRotation();
		Piece l1_3 = l1_2.fastRotation();
		Piece l1_4 = l1_3.fastRotation();

		//original L1 figure.
		assertEquals( 2, l1_1.getWidth() );
		assertEquals( 3, l1_1.getHeight() );

		//first time rotated L1.
		assertEquals( 3, l1_2.getWidth() );
		assertEquals( 2, l1_2.getHeight() );
		assertEquals( pieces[ Piece.L1 ].fastRotation().getWidth(), l1_2.getWidth() );
		assertEquals( pieces[ Piece.L1 ].fastRotation().getHeight(), l1_2.getHeight() );

		//second time rotated L1.
		assertEquals( l1_1.getWidth(), l1_3.getWidth() ); // l1_3's width,height == l1_1's width/height.
		assertEquals( l1_1.getHeight(), l1_3.getHeight() );
		assertEquals( pieces[ Piece.L1 ].fastRotation().fastRotation().getWidth(), l1_3.getWidth() );

		//third time rotated L1.
		assertEquals( 3, l1_4.getWidth() );
		assertEquals( 2, l1_4.getHeight() );
		assertEquals( l1_1.getWidth(), l1_4.fastRotation().getWidth() );
	}

	public void testL2Size()
	{
		//Creating local l2 Piece.
		Piece l2_1 = pieces[ Piece.L2 ];
		Piece l2_2 = l2_1.fastRotation();
		Piece l2_3 = l2_2.fastRotation();
		Piece l2_4 = l2_3.fastRotation();

		//original L2 figure.
		assertEquals( 2, l2_1.getWidth() );
		assertEquals( 3, l2_1.getHeight() );

		//first time rotated L2.
		assertEquals( 3, l2_2.getWidth() );
		assertEquals( 2, l2_2.getHeight() );
		assertEquals( pieces[ Piece.L2 ].fastRotation().getWidth(), l2_2.getWidth() );
		assertEquals( pieces[ Piece.L2 ].fastRotation().getHeight(), l2_2.getHeight() );

		//second time rotated L2.
		assertEquals( l2_1.getWidth(), l2_3.getWidth() ); // l2_3's width,height == l2_1's width/height.
		assertEquals( l2_1.getHeight(), l2_3.getHeight() );
		assertEquals( pieces[ Piece.L2 ].fastRotation().fastRotation().getWidth(), l2_3.getWidth() );

		//third time rotated L2.
		assertEquals( 3, l2_4.getWidth() );
		assertEquals( 2, l2_4.getHeight() );
		assertEquals( l2_1.getWidth(), l2_4.fastRotation().getWidth() );
	}


	public void tests1Size()
	{
		//original s1.
		assertEquals( 3, s1_1.getWidth() );
		assertEquals( 2, s1_1.getHeight() );

		//first time rotated s1.
		assertEquals( 2, s1_2.getWidth() );
		assertEquals( 3, s1_2.getHeight() );
		assertEquals( pieces[ Piece.S1 ].fastRotation().getWidth(), s1_2.getWidth() );
		assertEquals( pieces[ Piece.S1 ].fastRotation().getHeight(), s1_2.getHeight() );

		//second/third time rotated s1.
		assertEquals( s1_2.computeNextRotation().getWidth(), s1_1.getWidth() );
		assertEquals( s1_2.computeNextRotation().computeNextRotation().getWidth(), s1_2.getWidth() );
	}

	public void tests2Size()
	{
		//original s2.
		assertEquals( 3, s2_1.getWidth() );
		assertEquals( 2, s2_1.getHeight() );

		//first time rotated s2.
		assertEquals( 2, s2_2.getWidth() );
		assertEquals( 3, s2_2.getHeight() );
		assertEquals( pieces[ Piece.S2 ].fastRotation().getWidth(), s2_2.getWidth() );
		assertEquals( pieces[ Piece.S2 ].fastRotation().getHeight(), s2_2.getHeight() );

		//second/third time rotated s2.
		assertEquals( s2_2.computeNextRotation().getWidth(), s2_1.getWidth() );
		assertEquals( s2_2.computeNextRotation().computeNextRotation().getWidth(), s2_2.getWidth() );
	}

	/*
		This function checks Pyramid's skirt's correctness.
	 */
	public void testPyramidSkirt()
	{
		//original pyr1.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));

		//first time rotated pyr1.
		assertTrue(Arrays.equals(new int[] {1, 0}, pyr2.getSkirt()));

		//second time rotated pyr1.
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

		//third time rotated pyr1.
		assertTrue(Arrays.equals(new int[] {0, 1}, pyr4.getSkirt()));
	}

	/*
		This function checks Stick's skirt's correctness.
	 */
	public void testStickSkirt()
	{
		//original stick1.
		assertTrue(Arrays.equals(new int[] {0}, stick1.getSkirt()));

		//first time rotated stick1.
		assertTrue(Arrays.equals(new int[] { 0 , 0 , 0 , 0 }, stick2.getSkirt()));
	}

	/*
		This function checks Square's skirt's correctness.
	 */
	public void testSquareSkirt()
	{
		//original square.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, square.getSkirt()));

		//first time rotated square.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, square.computeNextRotation().getSkirt()));	//Checking that nothing changes after rotation.
	}

	public void testL1Skirt()
	{
		//getting L1 variable, from pieces array.
		Piece l1_1 = pieces[ Piece.L1 ];	//array has 1'st rotations in it.

		//original L1.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, l1_1.getSkirt()));

		//first rotation of L1.
		assertTrue(Arrays.equals(new int[] { 0 , 0 ,0 }, l1_1.fastRotation().getSkirt()));

		//second rotation of L1.
		assertTrue(Arrays.equals(new int[] { 2 , 0 }, l1_1.fastRotation().fastRotation().getSkirt()));

		//third rotation of L1.
		assertTrue(Arrays.equals(new int[] { 0 , 1, 1 }, l1_1.fastRotation().fastRotation().fastRotation().getSkirt()));
	}

	public void testL2Skirt()
	{
		//getting L2 variable, from pieces array.
		Piece l2_1 = pieces[ Piece.L2 ];	//array has 1'st rotations in it.

		//original L2.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, l2_1.getSkirt()));

		//first rotation of L2.
		assertTrue(Arrays.equals(new int[] { 1 , 1 , 0 }, l2_1.fastRotation().getSkirt()));

		//second rotation of L2.
		assertTrue(Arrays.equals(new int[] { 0 , 2 }, l2_1.fastRotation().fastRotation().getSkirt()));

		//third rotation of L2.
		assertTrue(Arrays.equals(new int[] { 0 , 0 , 0 }, l2_1.fastRotation().fastRotation().fastRotation().getSkirt()));
	}

	/*
    	This function checks S's skirt's correctness.
 	*/
	public void testSSkirt()
	{
		//original s1.
		assertTrue(Arrays.equals(new int[] { 0 , 0 , 1	 }, s1_1.getSkirt()));

		//first rotation of s1.
		assertTrue(Arrays.equals(new int[] { 1 , 0 }, s1_2.getSkirt()));

		//original s2.
		assertTrue(Arrays.equals(new int[] { 1 , 0 , 0	 }, s2_1.getSkirt()));

		//first rotation of s2.
		assertTrue(Arrays.equals(new int[] { 0 , 1 }, s2_2.getSkirt()));

	}



}
