import junit.framework.TestCase;


public class BoardTest extends TestCase {
	//Global board variable.
	Board b;

	//These are all the pieces, we'll need.
	Piece pyr1, pyr2, pyr3, pyr4;
	Piece s1_1;
	Piece s2_1;
	Piece l1_1;
	Piece l2_1;
	Piece stick1;
	Piece square;

	/*
		Setting up, different pieces.
	 */
	protected void setUp() throws Exception {

		//Initializing pyramid piece.
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		//Initializing all the needed pieces.
		s1_1 = new Piece( Piece.S1_STR );
		s2_1 = new Piece( Piece.S2_STR );
		l1_1 = new Piece( Piece.L1_STR );
		l2_1 = new Piece( Piece.L2_STR );
		stick1 = new Piece( Piece.STICK_STR );
		square = new Piece( Piece.SQUARE_STR );
	}
	//Placing figures, and then placing figure at the free
	// place below them, so heights dont update.
	public void testSize()
	{
		b = new Board(10,20);
		int res1=b.place(square, 0,0);
		b.commit();
		assertEquals(Board.PLACE_OK,res1 );
		int res2= b.place(stick1.computeNextRotation(),1,2);
		b.commit();
		b.commit();//second commit should change nothing.
		assertEquals(Board.PLACE_OK,res2);
		int res3= b.place(stick1.computeNextRotation(),2,0);
		b.commit();
		b.sanityCheck();
		System.out.println(b.toString());//For branch coverage
		assertEquals(Board.PLACE_OK, res3);
		int res4 = b.place(square, 0 , 100);
		b.commit();
		int res5 = b.place(square, 0 , -100);
		assertEquals(Board.PLACE_OUT_BOUNDS, res4);
		assertEquals(Board.PLACE_OUT_BOUNDS, res5);
		assertEquals(true, b.getGrid(-1,-1));
		assertEquals(true, b.getGrid(-1,0));
		assertEquals(true, b.getGrid(0,-1));
	}

	public void testException()
	{
		b= new Board(3,5);
		try
		{
			b.place(stick1,0,0);
			b.place(square, 0,2);
		}
		catch ( RuntimeException e )
		{
			assertEquals("place commit problem", e.getMessage()); }
	}

	//sizeTest1 -> Emptiness Check before adding figures.
	public void test1_size_1()
	{
		b = new Board(4, 6);
		//Check for all rows/columns to be 0 in an empty Board.
		for(int i=0; i<b.getWidth(); i++) assertEquals(b.getColumnHeight(i), 0 );
		for(int i=0; i<b.getHeight(); i++) assertEquals(b.getRowWidth(i), 0 );
		//Check for maxHeight to be zero.
		assertEquals(b.getMaxHeight(), 0 );
	}

	//sizeTest1 -> Placing(OK) and Checking pyramid at (0,0).
	public void test2_size_1()
	{
		b = new Board(4, 6);
		int result = b.place(pyr1,0,0);
		b.commit();
		b.clearRows();	//This should change nothing.
		b.commit();
		assertEquals( Board.PLACE_OK, result );
		assertEquals( 1, b.getColumnHeight( 0 ) );
		assertEquals(true, b.getGrid(0,0));
		assertEquals(true, b.getGrid(1,0));
		assertEquals(false, b.getGrid(3,0));
		assertEquals(true , b.getGrid(-1,-1));
	}

	//sizeTest1 -> Placing(OK) and Checking s1_2 at (1,1).
	public void test3_size_1()
	{
		b = new Board(4, 6);
		int result = b.place(pyr1,0,0);
		b.commit();
		//s1_1.computeNextRotation() == s1_2
		int result1 = b.place( s1_1.computeNextRotation(),1,1 );
		b.commit();
		assertEquals(Board.PLACE_OK, result1);
		assertEquals( 1, b.getColumnHeight( 0 ) );
		assertEquals( 3, b.getColumnHeight( 2 ) );
		assertEquals( 4, b.getMaxHeight());
		assertEquals( 3, b.getRowWidth(0) );
		assertEquals( 2, b.getRowWidth(1) );
		assertEquals( 0, b.getRowWidth(4) );
	}

	//sizeTest1 -> Placing(OutOfBounds) and Checking stick2 at (2,3).
	public void test4_size_1()
	{
		b = new Board(4, 6);
		int result = b.place(pyr1,0,0);
		b.commit();
		//s1_1.computeNextRotation() == s1_2
		int result1 = b.place( s1_1.computeNextRotation(),1,1 );
		b.commit();
		//stick1.computeNextRotation() == stick2
		int result2 = b.place( stick1.computeNextRotation(),  2, 3 );
		b.commit();
		assertEquals( Board.PLACE_OUT_BOUNDS, result2 );
		assertEquals( 4 ,b.getColumnHeight(2) );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals(  3 , b.getRowWidth( 3 ));
		int result3 = b.place(stick1, -1 ,0);
		b.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, result3 );
		int result4 = b.place( stick1, 0, 0 );
		b.commit();
		assertEquals( Board.PLACE_BAD, result4 );
	}

	//sizeTest2 -> Placing(Row_Filled) and Checking pyramid1 at (0,0).
	public void test1_size_2()
	{
		b = new Board( 3, 6 );
		int result1 = b.place( pyr1,0,0);
		assertEquals( Board.PLACE_ROW_FILLED, result1 );
		assertEquals( 3, b.getRowWidth( 0 ) );
		assertEquals( 1, b.getRowWidth( 1 ) );
		int rowsCleared = b.clearRows();	//clearing rows.
		b.commit();
		assertEquals( 1 , rowsCleared );
		assertEquals( 1, b.getRowWidth( 0 ) );
		assertEquals( 0, b.getColumnHeight( 2 ) );
		assertEquals( 1, b.getMaxHeight() );
	}

	//sizeTest2 -> Placing(OK) and Checking stick1 at (0,0).
	public void test2_size_2()
	{
		b = new Board( 3, 6 );
		int result1 = b.place( pyr1,0,0);
		int rowsCleared = b.clearRows();	//clearing rows.
		b.commit();
		int res2 = b.place( stick1, 0 , 0 );
		b.commit();
		assertEquals( Board.PLACE_OK, res2 );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 0, b.getColumnHeight(2) );
	}

	//sizeTest2 -> Placing(Row_Filled) and Checking stick1 (2,0).
	public void test3_size_2()
	{
		b = new Board( 3, 6 );
		int result1 = b.place( pyr1,0,0);
		int rowsCleared = b.clearRows();	//clearing rows.
		b.commit();
		int res2 = b.place( stick1, 0 , 0 );
		b.commit();
		int res3 = b.place(stick1 , 2, 0);
		b.commit();
		assertEquals( Board.PLACE_ROW_FILLED, res3 );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 4, b.getColumnHeight(2) );

	}

	//sizeTest2 -> Placing(Row_Filled) and Checking stick1 (1,1).
	public void test4_size_2()
	{
		b = new Board( 3, 6 );
		int result1 = b.place( pyr1,0,0);
		int rowsCleared = b.clearRows();	//clearing rows.
		b.commit();
		int res2 = b.place( stick1, 0 , 0 );
		b.commit();
		int res3 = b.place(stick1 , 2, 0);
		b.commit();
		int res4 = b.place(stick1, 1 ,1);
		b.commit();
		assertEquals( Board.PLACE_ROW_FILLED, res4 );
		assertEquals( 5, b.getMaxHeight() );
		assertEquals( 5, b.getColumnHeight(1) );
		assertEquals( 4, b.getColumnHeight(0) );
		int clearedRows = b.clearRows();	//Clearing rows.
		b.commit();
		assertEquals( 4, clearedRows );
		assertEquals( 1, b.getMaxHeight() );
		assertEquals( 0, b.getColumnHeight(0 ) );
	}

	//sizeTest3 -> Placing(OK) and Checking pyr (0,0) and (0,2).
	public void test1_size_3()
	{
		b = new Board( 4, 6 );
		int res1 = b.place(pyr1,0,0);
		b.commit();
		int res2 = b.place(pyr1,0,2);
		b.commit();
		assertEquals( Board.PLACE_OK, res1 );
		assertEquals( Board.PLACE_OK, res2 );
		assertEquals( 4, b.getMaxHeight());
	}

	//sizeTest3 -> Placing(Row_Filled) and Checking stick1 (3,0).
	public void test2_size_3()
	{
		b = new Board( 4, 6 );
		int res1 = b.place(pyr1,0,0);
		b.commit();
		int res2 = b.place(pyr1,0,2);
		b.commit();
		int res3 = b.place(stick1,3,0);
		assertEquals( Board.PLACE_ROW_FILLED, res3 );
		assertEquals( 4, b.getMaxHeight() );
		int res = b.clearRows();
		b.commit();
		assertEquals( 2, res );
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(2, b.getColumnHeight(3));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getMaxHeight() );

		int resOutOfBounds = b.place( square, 4, 0 );
		assertEquals( Board.PLACE_OUT_BOUNDS, resOutOfBounds );
	}

	//DropTest1 -> Dropping and checking pyr1.
	//Entering this method Board has [nothing] in it.
	public void test_dropOnEmptyBoard_dropTest1()
	{
		b = new Board( 4, 6 );
		int h1 = b.dropHeight( pyr1, 0 );
		assertEquals(0, h1 );
		int h2 = b.dropHeight(null, 0);
		assertEquals(-1, h2 );
		int h3 = b.dropHeight(pyr1, 3);
		assertEquals(-1, h3);
		int h4 = b.dropHeight(pyr1, -2);
		assertEquals(-1, h4);
	}

	//DropTest1 -> Dropping and checking s2_1,s2_2.
	//Entering this method Board has [ pyr1(0,0) ] in it.
	public void test_dropOnFigures1_dropTest1()
	{
		b = new Board( 4, 6 );
		int res1 = b.place(pyr1,0,0);
		b.commit();
		int h1 = b.dropHeight(s2_1.computeNextRotation(), 1);
		assertEquals( 2, h1 );
		int h2 = b.dropHeight(s2_1.computeNextRotation(), 0);
		assertEquals( 1, h2 );
		int h3 = b.dropHeight(s2_1, 1 );
		assertEquals(1 , h3);
	}

	//DropTest1 -> Dropping and checking s2_1,s2_2.
	//Entering this method Board has [ pyr1(0,0) ,square(2,1) ] in it.
	public void test_dropOnFigures2_dropTest1()
	{
		b = new Board( 4, 6 );
		int res1 = b.place(pyr1,0,0);
		b.commit();
		int res2 = b.place( square, 2, 1 );
		int h4 = b.dropHeight( square, 2 );
		int h5 = b.dropHeight( square, 1 );
		assertEquals( 3, h4 );
		assertEquals(3 , h5);
	}

	//DropTest2 -> Placing and checking pyr1.
	//Entering this method Board has [ ] in it.
	public void test_placeFigures_DropTest2()
	{
		b = new Board( 3, 5 );
		int res1 = b.place(pyr1,0,0);
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, res1);
		b.clearRows();
		b.commit();
		assertEquals( true , b.getGrid(1,0));
		assertEquals( false , b.getGrid(0,0));
		assertEquals( false , b.getGrid(2,0));
	}


	//DropTest2 -> Dropping and checking stick1,square.
	//Entering this method Board has [ true(filled) at (1,0) ] in it.
	public void test_dropOnFigures1_DropTest2()
	{
		b = new Board( 3, 5 );
		int res1 = b.place(pyr1,0,0);
		b.commit();
		b.clearRows();
		b.commit();
		int h1 = b.dropHeight(stick1, 0 );
		assertEquals( 0 , h1 );
		int h2 = b.dropHeight(square, 1);
		assertEquals( 1 , h2 );
		int h3 = b.dropHeight(square, 4);
		assertEquals(-1, h3);
	}

	//Drop test lvl.: 3
	public void test_testDrop3()
	{
		b = new Board(3, 6);
		b.place( stick1, 0, 0 );
		assertEquals(4,b.dropHeight( square,0 ) );
		assertEquals( -1, b.dropHeight(stick1.computeNextRotation(),1 ) );
	}

	//Undo test lvl.: 1
	public void test_firstUndo()
	{
		b = new Board(5,8);
		int res1 = b.place( pyr1,0,0 );
		b.commit();
		int res2 = b.place(square, 3,0 );
		int rowsCleared = b.clearRows();
		//Checking post clear parameters of board.
		assertEquals(1, rowsCleared);
		assertEquals( 3 , b.getRowWidth(0) );
		assertEquals( true , b.getGrid(1,0) );
		assertEquals( true, b.getGrid( 3, 0 ) );
		assertEquals( true, b.getGrid( 4, 0 ) );
		assertEquals( 0 , b.getRowWidth(1) );
		assertEquals(1 , b.getMaxHeight());

		//Undoing changes.
		b.undo();
		b.undo();	//This second undo should change nothing.
		assertEquals( 3 , b.getRowWidth(0) );
		assertEquals( 1 , b.getRowWidth(1) );
		assertEquals(2 , b.getMaxHeight());
		//Again adding another piece.
		int res3 = b.place( l1_1, 2, 1 );
		assertEquals( Board.PLACE_OK, res3 );
		assertEquals( 3, b.getRowWidth(0) );
		assertEquals( 3, b.getRowWidth(1) );
		assertEquals( 4, b.getColumnHeight(2) );
	}

	//Undo test lvl.: 2
	public void test_secondUndo()
	{
		b = new Board(5,8);
		assertEquals( 0, b.getRowWidth(0) );
		assertEquals( 0, b.getMaxHeight() );
		int res1 = b.place(l2_1,3, 0);
		assertEquals(Board.PLACE_OK, res1);
		assertEquals( 2, b.getRowWidth(0 ));
		assertEquals( 1, b.getRowWidth(2) );
		assertEquals( 3 , b.getMaxHeight() );
		b.undo();	//Undoing changes.
		assertEquals( 0, b.getRowWidth(0) );
		assertEquals( 0, b.getMaxHeight() );
		assertEquals( 0, b.getColumnHeight(3) );
		int res2 = b.place(square,3, 0);
		b.commit();
		assertEquals(Board.PLACE_OK , res2);
		assertEquals(2, b.getRowWidth(0));
		assertEquals( 2, b.getRowWidth(1));
		assertEquals( 2, b.getMaxHeight() );
		int res3 = b.place(pyr1,0,0);
		b.clearRows();
		assertEquals(3 , b.getRowWidth(0));
		assertEquals(1 , b.getMaxHeight());
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(3));
		assertEquals(1, b.getColumnHeight(4));
		//Undoing clearing rows.
		b.undo();
		//Check that there is only square left.
		assertEquals(2, b.getRowWidth(0));
		assertEquals( 2, b.getRowWidth(1));
		assertEquals( 2, b.getColumnHeight(3));
		assertEquals( 2, b.getColumnHeight(4));
		assertEquals( 2, b.getMaxHeight());
	}

	public void testSanity()
	{
		b = new Board(4, 6);
		int result = b.place(pyr1,0,0);
		b.commit();
		b.setMaxHeight(15);
		b.sanityCheck();
		b.setDebug(false);
		b.sanityCheck();
		b.setDebug(true);
		b.setHeights(new int[]{0,0,0,0});
		b.sanityCheck();
		b.setWidths(new int[]{0,0,0,0});
		b.sanityCheck();

	}
}
