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
	
	/*
		Methods checked: [ getRowWidth(), getColumnHeight() , getMaxHeight(), clearRow(), getGrid(),place() ].
		Methods Naming in sizeTest-s: %_OK_% - adds figures ok,
					    %_OutOfBounds_% adds figures out of bounds,
					    %_Bad_% adds figures badly.
	 */
	public void testSizeOperations() {
		sizeTest0();
		sizeTest1();
		sizeTest2();
		sizeTest3();
	}

	//Size test lvl.: 0
	//Checked Methods: { getWidth(), getHeight(), getGrid() }
	private void sizeTest0()
	{
		b = new Board(3,4);
		assertEquals(3, b.getWidth() );
		assertEquals( 4 , b.getHeight() );
		//Now Checking that board's empty.
		for(int x=0; x< b.getWidth(); x++)
		{
			for(int y=0; y<b.getHeight(); y++)
			{
				assertEquals(false, b.getGrid(x,y));
			}
		}
	}

	//Size test lvl.: 1
	private void sizeTest1()
	{
		b = new Board(4, 6);
		preCheck_sizeTest1();
		placePyramid_OK_sizeTest1();
		placeS1_OK_sizeTest1();
		placeStick_OutOfBounds_sizeTest1();
		placeStick2_Bad_sizeTest1();
	}

	//sizeTest1 -> Emptiness Check before adding figures.
	private void preCheck_sizeTest1()
	{
		//Check for all rows/columns to be 0 in an empty Board.
		for(int i=0; i<b.getWidth(); i++) assertEquals(b.getColumnHeight(i), 0 );
		for(int i=0; i<b.getHeight(); i++) assertEquals(b.getRowWidth(i), 0 );
		//Check for maxHeight to be zero.
		assertEquals(b.getMaxHeight(), 0 );
	}

	//sizeTest1 -> Placing(OK) and Checking pyramid at (0,0).
	private void placePyramid_OK_sizeTest1()
	{
		int result = b.place(pyr1,0,0);
		b.commit();
		b.clearRows();	//This should change nothing.
		b.commit();
		assertEquals( Board.PLACE_OK, result );
		assertEquals( 1, b.getColumnHeight( 0 ) );
		assertEquals(true, b.getGrid(0,0));
		assertEquals(true, b.getGrid(1,0));
		assertEquals(true, b.getGrid(2,0));
		assertEquals(false, b.getGrid(3,0));
		assertEquals(true, b.getGrid(1,1));
	}

	//sizeTest1 -> Placing(OK) and Checking s1_2 at (1,1).
	private void placeS1_OK_sizeTest1()
	{
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
	private void placeStick_OutOfBounds_sizeTest1()
	{
		//stick1.computeNextRotation() == stick2
		int result2 = b.place( stick1.computeNextRotation(),  2, 3 );
		b.commit();
		assertEquals( Board.PLACE_OUT_BOUNDS, result2 );
		System.out.println(b.toString());
		assertEquals( 4 ,b.getColumnHeight(2) );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals(  3 , b.getRowWidth( 3 ));
	}

	//sizeTest1 -> Placing(BAD) and Checking stick1 at (0,0).
	private void placeStick2_Bad_sizeTest1()
	{
		int result3 = b.place( stick1, 0, 0 );
		b.commit();
		assertEquals( Board.PLACE_BAD, result3 );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4 ,b.getColumnHeight(0) );
	}

	//Size test lvl.: 2
	private void sizeTest2()
	{
		b = new Board( 3, 6 );
		placeFigures_RowFilled_sizeTest2();
		placeStick_OK_sizeTest2();
		placeStick_RowsFilled_sizeTest2();
		placeFigures_RowFilled2_sizeTest2();
	}

	//sizeTest2 -> Placing(Row_Filled) and Checking pyramid1 at (0,0).
	private void placeFigures_RowFilled_sizeTest2()
	{
		int result1 = b.place( pyr1,0,0);
		b.commit();
		assertEquals( Board.PLACE_ROW_FILLED, result1 );
		assertEquals( 3, b.getRowWidth( 0 ) );
		assertEquals( 1, b.getRowWidth( 1 ) );
		int rowsCleared = b.clearRows();	//clearing rows.
		b.commit();
		assertEquals( 1 , rowsCleared );
		assertEquals( 1, b.getRowWidth( 0 ) );
		assertEquals( 0, b.getRowWidth( 1 ) );
		assertEquals( 0, b.getColumnHeight( 0 ) );
		assertEquals( 0, b.getColumnHeight( 2 ) );
		assertEquals( 1, b.getMaxHeight() );
	}

	//sizeTest2 -> Placing(OK) and Checking stick1 at (0,0).
	private void placeStick_OK_sizeTest2()
	{
		int res2 = b.place( stick1, 0 , 0 );
		b.commit();
		assertEquals( Board.PLACE_OK, res2 );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 0, b.getColumnHeight(2) );
	}

	//sizeTest2 -> Placing(Row_Filled) and Checking stick1 (2,0).
	private void placeStick_RowsFilled_sizeTest2()
	{
		int res3 = b.place(stick1 , 2, 0);
		b.commit();
		assertEquals( Board.PLACE_ROW_FILLED, res3 );
		assertEquals( 4, b.getMaxHeight() );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 4, b.getColumnHeight(2) );

	}

	//sizeTest2 -> Placing(Row_Filled) and Checking stick1 (1,1).
	private void placeFigures_RowFilled2_sizeTest2()
	{
		int res4 = b.place(stick1, 1 ,1);
		b.commit();
		assertEquals( Board.PLACE_ROW_FILLED, res4 );
		assertEquals( 5, b.getMaxHeight() );
		assertEquals( 5, b.getColumnHeight(1) );
		assertEquals( 4, b.getColumnHeight(0) );
		assertEquals( 4, b.getColumnHeight(2) );
		int clearedRows = b.clearRows();	//Clearing rows.
		b.commit();
		assertEquals( 4, clearedRows );
		assertEquals( 1, b.getMaxHeight() );
		assertEquals( 0, b.getColumnHeight(0 ) );
		assertEquals( 0, b.getColumnHeight(2 ) );
	}

	//Size test lvl.: 3
	private void sizeTest3()
	{
		b = new Board( 4, 6 );
		placeFigures_OK_sizeTest3();
		placeFigures_RowFilled_sizeTest3();
		placeFigures_OutOfBounds_sizeTest3();
	}

	//sizeTest3 -> Placing(OK) and Checking pyr (0,0) and (0,2).
	private void placeFigures_OK_sizeTest3()
	{
		int res1 = b.place(pyr1,0,0);
		b.commit();
		assertEquals( Board.PLACE_OK, res1 );
		int res2 = b.place(pyr1,0,2);
		b.commit();
		assertEquals( Board.PLACE_OK, res2 );
		assertEquals( 4, b.getMaxHeight());
	}

	//sizeTest3 -> Placing(Row_Filled) and Checking stick1 (3,0).
	private void placeFigures_RowFilled_sizeTest3()
	{
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
	}

	//sizeTest3 -> Placing(Out_Of_Bounds) and Checking square (4,0).
	private void placeFigures_OutOfBounds_sizeTest3()
	{
		//Adding piece fully out of bounds.
		int resOutOfBounds = b.place( square, 4, 0 );
		assertEquals( Board.PLACE_OUT_BOUNDS, resOutOfBounds );
	}

	//Methods checked: [ dropHeight(),getRowWidth(), getColumnHeight() , getMaxHeight(), clearRow(), getGrid(), place() ].
	public void testDropHeight()
	{
		dropTest1();
		dropTest2();
		dropTest3();
	}

	//Drop test lvl.: 1
	private void dropTest1()
	{
		b = new Board( 4, 6 );
		dropOnEmptyBoard_test1();
		int res1 = b.place(pyr1,0,0);
		b.commit();
		dropOnFigures1_test1();
		int res2 = b.place( square, 2, 1 );
		dropOnFigures2_test1();
	}

	//DropTest1 -> Dropping and checking pyr1.
	//Entering this method Board has [nothing] in it.
	private void dropOnEmptyBoard_test1()
	{
		int h1 = b.dropHeight( pyr1, 0 );
		assertEquals(0, h1 );
	}

	//DropTest1 -> Dropping and checking s2_1,s2_2.
	//Entering this method Board has [ pyr1(0,0) ] in it.
	private void dropOnFigures1_test1()
	{
		int h1 = b.dropHeight(s2_1.computeNextRotation(), 1);
		assertEquals( 2, h1 );
		int h2 = b.dropHeight(s2_1.computeNextRotation(), 0);
		assertEquals( 1, h2 );
		int h3 = b.dropHeight(s2_1, 1 );
		assertEquals(1 , h3);
	}

	//DropTest1 -> Dropping and checking s2_1,s2_2.
	//Entering this method Board has [ pyr1(0,0) ,square(2,1) ] in it.
	private void dropOnFigures2_test1()
	{
		int h4 = b.dropHeight( square, 2 );
		int h5 = b.dropHeight( square, 1 );
		assertEquals( 3, h4 );
		assertEquals(3 , h5);
	}

	//Drop test lvl.: 2
	private void dropTest2()
	{
		b = new Board( 3, 5 );
		placeFigures_DropTest2();
		dropOnFigures1_DropTest2();
	}

	//DropTest2 -> Placing and checking pyr1.
	//Entering this method Board has [ ] in it.
	private void placeFigures_DropTest2()
	{
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
	private void dropOnFigures1_DropTest2()
	{
		int h1 = b.dropHeight(stick1, 0 );
		assertEquals( 0 , h1 );
		int h2 = b.dropHeight(square, 1);
		assertEquals( 1 , h2 );
		int h3 = b.dropHeight(square, 4);
		assertEquals(-1, h3);
	}

	//Drop test lvl.: 3
	private void dropTest3()
	{
		b = new Board(3, 6);
		b.place( stick1, 0, 0 );
		assertEquals(4,b.dropHeight( square,0 ) );
		assertEquals( -1, b.dropHeight(stick1.computeNextRotation(),1 ) );
	}


	//Testing undo.
	public void testUndo()
	{
		firstUndoTest();
		secondUndoTest();
	}

	//Undo test lvl.: 1
	private void firstUndoTest()
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
	private void secondUndoTest()
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
}
