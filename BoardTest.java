import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b;

	//These are all the pieces, we'll need.
	Piece pyr1, pyr2, pyr3, pyr4;
	Piece s1_1,s1_2;
	Piece s2_1,s2_2;
	Piece l1_1,l1_2,l1_3,l1_4;
	Piece l2_1,l2_2,l2_3,l2_4;
	Piece stick1, stick2;
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
	
	// Check the basic getRowWidth/getColumnHeight/getMaxHeight.
	public void testSizeOperations() {
		//At this moment board is empty.
		firstSizeTest();
		secondSizeTest();
		thirdSizeTest();
	}

	//Check the dropHeight() method.
	//Should we check out of bounds drops?
	public void testDropHeight()
	{
		firstDropTest();
		secondDropTest();
	}
	/*
		Checking  getColumnHeight()/getRowWidth/place()/getMaxHeight() functionality.
		[Out of bounds, bad, ok ] places are introduced.
	 */
	private void firstSizeTest()
	{
		b = new Board(4, 6);	//Initialising specific test board.

		//Check for all rows/columns to be 0 in an empty Board.
		for(int i=0; i<b.getWidth(); i++) assertEquals(b.getColumnHeight(i), 0 );
		for(int i=0; i<b.getHeight(); i++) assertEquals(b.getRowWidth(i), 0 );

		//Check for maxHeight to be zero.
		assertEquals(b.getMaxHeight(), 0 );

		//Place pyramid on the board.
		int result = b.place(pyr1,0,0);
		b.commit();

		//Checking
		assertEquals( Board.PLACE_OK, result );
		assertEquals(true, b.getGrid(0,0));
		assertEquals(true, b.getGrid(1,0));
		assertEquals(true, b.getGrid(2,0));
		assertEquals(false, b.getGrid(3,0));
		assertEquals(true, b.getGrid(1,1));

		//Put rotated s1 figure(s1_2) on (1,1) starting point.
		int result1 = b.place( s1_1.computeNextRotation(),1,1 );
		b.commit();

		//Check rows/columns.
		assertEquals(Board.PLACE_OK, result1);	//Should have added OK.
		assertEquals( 1, b.getColumnHeight( 0 ) );
		assertEquals( 4, b.getColumnHeight( 1 ) );
		assertEquals( 3, b.getColumnHeight( 2 ) );
		assertEquals( 4, b.getMaxHeight());
		assertEquals( 3, b.getRowWidth(0) );
		assertEquals( 2, b.getRowWidth(1) );
		assertEquals( 2, b.getRowWidth(2) );
		assertEquals( 1, b.getRowWidth(3) );
		assertEquals( 0, b.getRowWidth(4) );

		//Place square on the board, should be out of bounds.
		int result2 = b.place( stick1.computeNextRotation(),  2, 3 );
		b.commit();

		//Check rows/columns.
		assertEquals( Board.PLACE_OUT_BOUNDS, result2 );	//out of bounds.
		assertEquals( 4 ,b.getColumnHeight(2) );
		assertEquals( 4 ,b.getColumnHeight(3) );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals(  3 , b.getRowWidth( 3 ));

		//Place stick on the board, should overlap other figure.
		int result3 = b.place( stick1, 0, 0 );

		//Check rows/columns.
		assertEquals( Board.PLACE_BAD, result3 );
		assertEquals( 4 ,b.getColumnHeight(0) );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4 ,b.getRowWidth(3) );
	}

	/*
		Checking  freeRow for adjacent filled rows.
	 */
	private void secondSizeTest()
	{
		b = new Board( 3, 6 );	//Initialising specific test board.

		//Place pyramid on he board.
		int result1 = b.place( pyr1,0,0);
		b.commit();

		//Check rows/columns.
		assertEquals( Board.PLACE_ROW_FILLED, result1 );
		assertEquals( 3, b.getRowWidth( 0 ) );
		assertEquals( 1, b.getRowWidth( 1 ) );

		//Clear filled rows.
		int rowsCleared = b.clearRows();
		b.commit();

		//Check changes after clearing Rows.
		assertEquals( 1 , rowsCleared );
		assertEquals( 1, b.getRowWidth( 0 ) );
		assertEquals( 0, b.getRowWidth( 1 ) );
		assertEquals( 0, b.getColumnHeight( 0 ) );
		assertEquals( 1, b.getColumnHeight( 1 ) );
		assertEquals( 0, b.getColumnHeight( 2 ) );
		assertEquals( 1, b.getMaxHeight() );

		//Placing stick at (0,0).
		int res2 = b.place( stick1, 0 , 0 );
		b.commit();

		//Check after first stick add.
		assertEquals( Board.PLACE_OK, res2 );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 0, b.getColumnHeight(3) );

		//Placing stick at (2,0).
		int res3 = b.place(stick1 , 2, 0);
		b.commit();

		//Check after second stick add.
		assertEquals( Board.PLACE_OK, res3 );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 4, b.getColumnHeight(3) );

		//Placing stick at (1,1).
		int res4 = b.place(stick1, 1 ,1);
		b.commit();

		//Check after final stick add.
		assertEquals( Board.PLACE_ROW_FILLED, res3 );
		assertEquals( 5, b.getMaxHeight() );
		assertEquals( 5, b.getColumnHeight(1) );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 4, b.getColumnHeight(2) );

		//Clear rows.
		int clearedRows = b.clearRows();
		b.commit();

		//Checking after clearing rows.
		assertEquals( 4, clearedRows );
		assertEquals( 1, b.getMaxHeight() );
		assertEquals( 0, b.getColumnHeight(0 ) );
		assertEquals( 0, b.getColumnHeight(2 ) );
	}

	/*
		Checking clearRows for not adjacent filled rows.
	 */
	private void thirdSizeTest()
	{
		b = new Board( 4, 6 );	//Initialising specific test board.
		int res1 = b.place(pyr1,0,0);
		b.commit();

		//Should get placed ok.
		assertEquals( Board.PLACE_OK, res1 );

		int res2 = b.place(pyr1,0,2);
		b.commit();

		//Should het placed ok.
		assertEquals( Board.PLACE_OK, res2 );
		assertEquals( 4, b.getMaxHeight());

		int res3 = b.place(stick1,3,0);
		//Should we use commit here??

		assertEquals( Board.PLACE_ROW_FILLED, res1 );
		assertEquals( 4, b.getMaxHeight() );

		int res = b.clearRows();
		b.commit();
		assertEquals( 2, res );

		//Check heights/widths after clearing rows.
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(2, b.getColumnHeight(3));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getMaxHeight() );

		//Adding piece fully out of bounds.
		int resOutOfBounds = b.place( square, 4, 0 );
		assertEquals( Board.PLACE_OUT_BOUNDS, resOutOfBounds );

	}

	/*
		First/simple dropHeight() test.
	 */
	private void firstDropTest()
	{
		b = new Board( 4, 6 );	//Initialising specific test board.

		int h1 = b.dropHeight( pyr1, 0 );	//Dropping on an empty board Should get y == 0.
		assertEquals(0, h1 );

		int res1 = b.place(pyr1,0,0);	//Now place pyramid on board.
		b.commit();

		//Checking dropping rotated s2,on two different x coordinates [ 0 , 1 ].
		int h2 = b.dropHeight(s2_1.computeNextRotation(), 1);
		assertEquals( 2, h2 );
		int h3 = b.dropHeight(s2_1.computeNextRotation(), 0);
		assertEquals( 1, h3 );

		int res2 = b.place( square, 2, 1 );	//Placing square at (2,1).

		int h4 = b.dropHeight( square, 2 );	//Checking dropping square on x==2.
		assertEquals( 3, h4 );
	}

	//Checking dropHeight() and introducing getGrid().
	private void secondDropTest()
	{
		b = new Board( 3, 6 );	//Initialising specific test board.
		int res1 = b.place(pyr1,0,0);	//placing pyramid
		b.commit();

		assertEquals(Board.PLACE_ROW_FILLED, res1);	//Should return row filled status.

		b.clearRows();	//Clear filled row(0-th row)
		b.commit();

		//Checking getGrid() method, checking whether cell of the grid is filled.
		assertEquals( true , b.getGrid(1,0));
		assertEquals( false , b.getGrid(0,0));
		assertEquals( false , b.getGrid(2,0));

		//Dropping stick on x=0.
		int h1 = b.dropHeight(stick1, 0 );
		assertEquals( 0 , h1 );

		//Dropping square on x=0.
		int h2 = b.dropHeight(square, 1);
		assertEquals( 1 , h2 );

		//This is special case when x index is out of bounds.
		int h3 = b.dropHeight(square, 4);
		assertEquals(-1, h3);

	}

	
	// Make  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	
}
