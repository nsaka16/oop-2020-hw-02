

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

		pieces = Piece.getPieces();	//pieces have first rotations as array members, not original ones.

		/*
			Initialise global variables of tests.
		 */
		pyr1 = new Piece( Piece.PYRAMID_STR);
		pyr2 =  pyr1.fastRotation();
		pyr3 =  pyr2.fastRotation();
		pyr4 =  pyr3.fastRotation();

		s1_1 = new Piece( Piece.S1_STR );
		s1_2 = s1_1.fastRotation();

		s2_1 = new Piece( Piece.S2_STR );
		s2_2 = s2_1.fastRotation();

		square = new Piece( Piece.SQUARE_STR );

		stick1 = new Piece( Piece.STICK_STR );
		stick2 = stick1.fastRotation();
	}
	

	/*
		Tests width/height properties' correctness for each figure.
	 */
	public void testSampleSize() {
		pyramidSizeTest();
		stickSizeTest();
		squareSizeTest();
		lSizeTest();
		sSizeTest();
	}


	/*
		Testing figures' skirt() methods.
	 */
	public void testSampleSkirt()
	{
		pyramidSkirt();
		stickSkirt();
		squareSkirt();
		lSkirt();
		sSkirt();
	}

	/*
		Testing if fast rotations are working properly.
	 	Since fast rotation is implemented by getNextRotation() method,
	 	there's no need to check inner method too.
	 */
	public void testFastRotateEquals()
	{
		//using global S Piece.
		assertTrue( s1_1.equals( s1_1.fastRotation().fastRotation() )  );	//S - figure's second rotation == original one.
		assertTrue( s1_1.fastRotation().equals( pieces[ Piece.S1 ] ) );	//	*pieces contains first rotations.
		assertTrue( s1_1.equals( s1_1 ) );
		assertFalse( s1_1.equals( s1_1.fastRotation() ) );

		//Creating local l1 Piece.
		Piece l1_1 = new Piece( Piece.L1_STR );
		Piece l1_2 = l1_1.fastRotation();
		Piece l1_3 = l1_2.fastRotation();
		Piece l1_4 = l1_3.fastRotation();


		assertTrue( l1_1.fastRotation().fastRotation().fastRotation().fastRotation().equals( l1_1 ) );
		assertTrue( l1_1.equals( l1_4.fastRotation() ) );
		assertTrue( l1_2.equals( pieces[ Piece.L1 ] ) );
		assertTrue( l1_3.equals( pieces[ Piece.L1 ].fastRotation() ) );
		assertTrue( l1_1.fastRotation().fastRotation().fastRotation().equals( l1_4 ) );
		assertFalse( l1_1.equals( l1_4 ) );



		//using global square Piece.
		assertTrue( square.equals( pieces[ Piece.SQUARE ] ) );
		assertTrue( square.equals( square.fastRotation() ) );
		assertTrue( square.equals( square.fastRotation().fastRotation() ) );
		assertTrue( square.equals( square ) );

		//using global stick1,stick2 Pieces.
		assertTrue( stick1.equals( pieces[ Piece.STICK ].fastRotation() ) );
		assertTrue( stick1.equals( stick1.fastRotation().fastRotation() ));
		assertFalse( stick2.equals( stick1 ) );
		assertTrue( stick2.equals( pieces[ Piece.STICK ] ));


		//using global pyramids Pieces.
		assertTrue( pyr4.fastRotation().equals( pyr1 ) );
		assertTrue( pyr1.fastRotation().fastRotation().fastRotation().fastRotation().equals( pyr1 ) );
		assertTrue( pyr2.equals( pieces[ Piece.PYRAMID ] ));
		assertTrue( pyr1.equals( pyr1 ) );
		assertFalse( pyr4.equals( pyr1 ) );
	}

	/*
		This function checks pyramid's width/height correctness.
	 */
	private void pyramidSizeTest()
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
	private void stickSizeTest()
	{
		//using global stick1,stick2 variables.

		assertEquals( 4, stick2.getWidth() );
		assertEquals( 1, stick2.getHeight() );
		assertEquals( pieces[ Piece.STICK ].getWidth() , stick2.getWidth() );

		assertEquals( 1, stick1.getWidth() );
		assertEquals( 4, stick1.getHeight() );
		assertEquals( pieces[ Piece.STICK ].fastRotation().getWidth(), stick1.getWidth() );
	}

	/*
		This function checks Square's width/height correctness.
	 */
	private void squareSizeTest()
	{
		assertEquals( 4, square.getWidth() );
		assertEquals( 4, square.getHeight() );
		assertEquals( pieces[ Piece.SQUARE ].getWidth(), square.getWidth() );
		assertEquals( pieces[ Piece.SQUARE ].getWidth(), square.fastRotation().getWidth() );
	}

	/*
		This function checks L's(both L1,L2) width/height correctness.
	 */
	public void lSizeTest()
	{
		//Creating local l1 Piece.
		Piece l1_1 = new Piece( Piece.L1_STR );
		Piece l1_2 = l1_1.fastRotation();
		Piece l1_3 = l1_2.fastRotation();
		Piece l1_4 = l1_3.fastRotation();

		//original L1 figure.
		assertEquals( 2, l1_1.getWidth() );
		assertEquals( 3, l1_1.getHeight() );

		//first time rotated L1.
		assertEquals( 3, l1_2.getWidth() );
		assertEquals( 2, l1_2.getHeight() );
		assertEquals( pieces[ Piece.L1 ].getWidth(), l1_2.getWidth() );
		assertEquals( pieces[ Piece.L1 ].getHeight(), l1_2.getHeight() );

		//second time rotated L1.
		assertEquals( l1_1.getWidth(), l1_3.getWidth() ); // l1_3's width,height == l1_1's width/height.
		assertEquals( l1_1.getHeight(), l1_3.getHeight() );
		assertEquals( pieces[ Piece.L1 ].fastRotation().getWidth(), l1_3.getWidth() );

		//third time rotated L1.
		assertEquals( 3, l1_4.getWidth() );
		assertEquals( 2, l1_4.getHeight() );
		assertEquals( l1_1.getWidth(), l1_4.fastRotation().getWidth() );


		//Creating local l2 Piece.
		Piece l2_1 = new Piece( Piece.L2_STR );
		Piece l2_2 = l2_1.fastRotation();
		Piece l2_3 = l2_2.fastRotation();
		Piece l2_4 = l2_3.fastRotation();

		//original L2 figure.
		assertEquals( 2, l2_1.getWidth() );
		assertEquals( 3, l2_1.getHeight() );

		//first time rotated L2.
		assertEquals( 3, l2_2.getWidth() );
		assertEquals( 2, l2_2.getHeight() );
		assertEquals( pieces[ Piece.L2 ].getWidth(), l2_2.getWidth() );
		assertEquals( pieces[ Piece.L2 ].getHeight(), l2_2.getHeight() );

		//second time rotated L2.
		assertEquals( l2_1.getWidth(), l2_3.getWidth() ); // l2_3's width,height == l2_1's width/height.
		assertEquals( l2_1.getHeight(), l2_3.getHeight() );
		assertEquals( pieces[ Piece.L2 ].fastRotation().getWidth(), l2_3.getWidth() );

		//third time rotated L2.
		assertEquals( 3, l2_4.getWidth() );
		assertEquals( 2, l2_4.getHeight() );
		assertEquals( l2_1.getWidth(), l2_4.fastRotation().getWidth() );
	}

	/*
		This function checks S's(both S1,S2) width/height correctness.
	 */
	private void sSizeTest()
	{
		//using global s variables.

		//original s1.
		assertEquals( 3, s1_1.getWidth() );
		assertEquals( 2, s1_1.getHeight() );

		//first time rotated s1.
		assertEquals( 2, s1_2.getWidth() );
		assertEquals( 3, s1_2.getHeight() );
		assertEquals( pieces[ Piece.S1 ].getWidth(), s1_2.getWidth() );
		assertEquals( pieces[ Piece.S1 ].getHeight(), s1_2.getHeight() );

		//second/third time rotated s1.
		assertEquals( s1_2.fastRotation().getWidth(), s1_1.getWidth() );
		assertEquals( s1_2.fastRotation().fastRotation().getWidth(), s1_2.getWidth() );


		//original s2.
		assertEquals( 3, s2_1.getWidth() );
		assertEquals( 2, s2_1.getHeight() );

		//first time rotated s2.
		assertEquals( 2, s2_2.getWidth() );
		assertEquals( 3, s2_2.getHeight() );
		assertEquals( pieces[ Piece.S2 ].getWidth(), s2_2.getWidth() );
		assertEquals( pieces[ Piece.S2 ].getHeight(), s2_2.getHeight() );

		//second/third time rotated s2.
		assertEquals( s2_2.fastRotation().getWidth(), s2_1.getWidth() );
		assertEquals( s2_2.fastRotation().fastRotation().getWidth(), s2_2.getWidth() );
	}

	/*
		This function checks Pyramid's skirt's correctness.
	 */
	public void pyramidSkirt()
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
	public void stickSkirt()
	{
		//original stick1.
		assertTrue(Arrays.equals(new int[] {0}, stick1.getSkirt()));

		//first time rotated stick1.
		assertTrue(Arrays.equals(new int[] { 0 , 0 , 0 , 0 }, stick2.getSkirt()));
	}

	/*
		This function checks Square's skirt's correctness.
	 */
	public void squareSkirt()
	{
		//original square.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, square.getSkirt()));

		//first time rotated square.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, square.fastRotation().getSkirt()));	//Checking that nothing changes after rotation.
	}

	/*
		This function checks L's skirt's correctness.
	 */
	public void lSkirt()
	{
		//getting L1 variable, from pieces array.
		Piece l1_2 = pieces[ Piece.L1 ];	//array has 1'st rotations in it.
		Piece l1_3 = l1_2.fastRotation();
		Piece l1_4 = l1_3.fastRotation();
		Piece l1_1 = l1_4.fastRotation();

		//original L1.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, l1_1.getSkirt()));

		//first rotation of L1.
		assertTrue(Arrays.equals(new int[] { 0 , 0 ,0 }, l1_2.getSkirt()));

		//second rotation of L1.
		assertTrue(Arrays.equals(new int[] { 2 , 0 }, l1_3.getSkirt()));

		//third rotation of L1.
		assertTrue(Arrays.equals(new int[] { 0 , 1, 1 }, l1_4.getSkirt()));

		//getting L2 variable, from pieces array.
		Piece l2_2 = pieces[ Piece.L2 ];	//array has 1'st rotations in it.
		Piece l2_3 = l1_2.fastRotation();
		Piece l2_4 = l1_3.fastRotation();
		Piece l2_1 = l1_4.fastRotation();

		//original L2.
		assertTrue(Arrays.equals(new int[] { 0 , 0 }, l2_1.getSkirt()));

		//first rotation of L2.
		assertTrue(Arrays.equals(new int[] { 1 , 1 , 0 }, l2_2.getSkirt()));

		//second rotation of L2.
		assertTrue(Arrays.equals(new int[] { 0 , 2 }, l2_3.getSkirt()));

		//third rotation of L2.
		assertTrue(Arrays.equals(new int[] { 0 , 0 , 0 }, l2_4.getSkirt()));
	}

	/*
    	This function checks S's skirt's correctness.
 	*/
	public void sSkirt()
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
