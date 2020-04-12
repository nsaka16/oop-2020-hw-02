// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	//These array are used to store filled spots in columns/rows.
	private int[] widths;
	private int[] heights;

	//This is returned upon getMaxHeight() call.
	private int maxHeight;
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;	//Should be committed at the start.
		widths = new int [ height ];	//initializing widths,heights array.
		heights = new int [ width ];
		maxHeight =  0 ;	//Setting max height to zero.
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// YOUR CODE HERE
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		return 0; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if( outOfBounds( x, y ) ) return true;
		else return grid[x][y];
	}

	//Returns true if x,y is out of bounds.
	private boolean outOfBounds( int x, int y )
	{
		return ! xInBounds( x ) || ! yInBounds( y );
	}

	//Checkng x bounds.
	private boolean xInBounds( int x )
	{
		return x >= 0 && x < width;
	}

	//Checking y bounds.
	private boolean yInBounds( int y )
	{
		return y >= 0 && y < height;
	}

	//returns false if grid[x][y] is already filled,
	//meaning if grd[x][y] is already true.
	private boolean isInvalidPoint( int x, int y )
	{
		return grid[x][y];
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

		//Will need to sve memento here.
		int res = checkBody( piece, x, y );
		return res;
	}

	private int checkBody( Piece piece, int x, int y)
	{
		TPoint[] body = piece.getBody();	//Body of a passed piece.
		int result = PLACE_OK;
		for( TPoint point : body )
		{
			int currX = x + point.x;
			int currY = y + point.y;
			//If out of bounds.result=appropriate value.
			if( outOfBounds( currX, currY ) )
			{
				result = PLACE_OUT_BOUNDS;
			}
			//else if x,y in grid=true result=appropriate value.
			else if( isInvalidPoint( currX, currY ) )
			{
				result = PLACE_BAD;
			}
			//This else means that x,y is valid and in bounds.
			else {
				grid[currX][currY] = true;	//set grid point to true.
				updateWidthsAndHeights( currX, currY );	//update heights,widths.
				//This widths array is already updated,use new value.
				if( isRowFilled( y )  )
				{
					result = PLACE_ROW_FILLED;
				}
			}
		}
		return result;
	}
	/*
		Updating heights and widths arrays with new values.
		and update maxHeight if needed.
	 */
	private void updateWidthsAndHeights( int x, int y )
	{
		//if increment heights[x] is bigger then max, set it to be max height.
		if( ++heights[x] > getMaxHeight() )
		{
			maxHeight = heights[x];
		}
		widths[y]++;
	}

	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		int start = getLowerFilledIndex();
		if( start == -1 ) return 0;
		for( int to = start; to < height - 1; to++ )
		{
			if( isRowFilled(to) )
			{
				moveGraphDown(to);
				decreaseHeight();
				rowsCleared++;
				to--;
			}
		}
		sanityCheck();
		return rowsCleared;
	}

	private void moveGraphDown( int to )
	{
		for( int from = to + 1; from < height; from ++ )
		{
			copyGridRow( from - 1 ,from );
			copyWidthValue( from-1, from  );
		}
	}
	private void decreaseHeight()
	{
		maxHeight--;
		for( int i=0; i < width; i++ )
		{
			heights[i]--;
		}
	}

	private void copyGridRow( int to, int from )
	{
		for( int i=0; i < width; i++ )
		{
			grid[i][to] = grid[i][from];
		}
	}

	//Used to move one value of widths array to another index.
	private void copyWidthValue( int to, int from )
	{
		widths[to] = widths[from];
	}

	//returns true if array  y'th row is filled.
	private boolean isRowFilled( int y )
	{
		return widths[y] == width;
	}

	private int getLowerFilledIndex()
	{
		for( int i = 0; i < height; i++ )
		{
			if( isRowFilled( i ) ) return i;
		}
		return -1;
	}
	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		// YOUR CODE HERE
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


