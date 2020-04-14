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


	private boolean[][] xGrid;
	private int[] xWidths;
	private int[] xHeights;
	private int xMaxHeight;
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		widths = new int [ height ];
		heights = new int [ width ];
		maxHeight =  0 ;
		//BackUp variables.
		xWidths = new int[ height ];
		xHeights = new int[ width ];
		xGrid = new boolean[width][height];
		xMaxHeight = 0;
		committed = true;
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
			try{
				int currentMaxHeight = 0;
				int currentWidths[] = new int[height];
				int currentHeights[] = new int[width];
				for(int x=0; x<width; x++)
				{
					for(int y=0; y<height; y++)
					{
						if(grid[x][y])
						{
							if(y+1>currentHeights[x]){
								currentHeights[x]=y+1;
								if(currentHeights[x] > currentMaxHeight)
								{
									currentMaxHeight = currentHeights[x];
								}
							}
							currentWidths[y]++;
						}
					}
				}
				if(!Arrays.equals(currentWidths, widths)) throw new RuntimeException("Widths not sane.");
				if(!Arrays.equals(currentHeights, heights)) throw new RuntimeException("Heights not sane.");
				if(currentMaxHeight != maxHeight ) throw new RuntimeException("Max. height not sane.");
			}
			catch (RuntimeException e)
			{
				System.out.println(e);
			}

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
		if( !xInBounds(x) || piece == null || x + piece.getWidth() > width ) return -1;
		else {
			int[] skirt = piece.getSkirt();
			int res= 0;
			for(int i=0; i<skirt.length; i++)
			{
				int xHeight = heights[x+i];
				int xSkirt = skirt[i];
				if( xHeight - xSkirt > res)
				{
					res = xHeight - xSkirt;
				}
			}
			return res;
		}
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

	//returns true if grid[x][y] is already filled.
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
		committed = false;
		copy();
		int res = checkBody( piece, x, y );
		sanityCheck();
		return res;
	}

	private int checkBody( Piece piece, int x, int y)
	{
		TPoint[] body = piece.getBody();
		int result = PLACE_OK;
		for( TPoint point : body )
		{
			int currX = x + point.x;
			int currY = y + point.y;
			if( outOfBounds( currX, currY ) )
			{
				result = PLACE_OUT_BOUNDS;
			}
			else if( isInvalidPoint( currX, currY ) )
			{
				result = PLACE_BAD;
			}
			else {
				grid[currX][currY] = true;
				updateWidthsAndHeights( currX, currY );
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
		if( y+1 > heights[x] ) heights[x] = y + 1;
		//if increment heights[x] is bigger then max, set it to be max height.
		if( heights[x] > getMaxHeight() )
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
		if(committed)
		{
			copy();
			committed = false;
		}
		committed = false;
		int rowsCleared = 0;
		int start = getLowerFilledIndex();	//find lowest filled row.
		if( start == -1 ) return 0;
		for( int to = start; to < height - 1; to++ )
		{
			//Only do process for filled rows.
			if( isRowFilled(to) )
			{
				maxHeight = 0;
				moveGraphDown(to);	//Move everything upper than filled row one row down.
				changeHeights();	//for every filled row decrease all heights.
				rowsCleared++;
				to--;	//This is needed, as two filled rows can be adjacent.
			}
		}
		if(DEBUG)sanityCheck();
		return rowsCleared;
	}

	/*
		Copy  every row upper than passed row one row down.
	 */
	private void moveGraphDown( int to )
	{
		for( int from = to + 1; from < height; from ++ )
		{
			copyGridRow( from - 1 ,from );	//Should move grid down.
			copyWidthValue( from-1, from  );	//Should move widths down.
		}
	}

	//Decrease all heights in heights array.
	private void changeHeights()
	{
		for( int i=0; i < width; i++ )
		{
			heights[i] = calculateHeight(i);
		}
	}

	/*
		When decreasing current height,
		next height is not necessary one below it,
		and we should find next height below current one.
	 */
	private int calculateHeight(int x)
	{
		int res = 0;
		for(int i=0; i<height; i++)
		{
			if(grid[x][i])
			{
				res = i + 1;
			}
		}
		if( res > maxHeight ) maxHeight = res;
		return res;
	}

	//Copies row from into row to of grid.
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

	//Find lowest index of filled row,
	//-1 returned if no filled rows found.
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
		if(committed == true) return;
		swap();
		commit();
		if(DEBUG)sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}

	//This function is used upon commit(), to save the state.
	private void copy()
	{
		System.arraycopy(widths,0,xWidths,0,widths.length);
		System.arraycopy(heights,0,xHeights,0,heights.length);
		for(int i=0; i<grid.length; i++)
		{
			System.arraycopy(grid[i],0,xGrid[i],0,grid[i].length);
		}
		xMaxHeight = maxHeight;
	}

	//Retrieve last saved state.
	private void swap()
	{
		//Save widths values.
		int[] temp = widths;
		widths = xWidths;
		xWidths = temp;

		//Save heights values.
		int[] temp2 = heights;
		heights = xHeights;
		xHeights = temp2;

		//Save grid values.
		boolean[][] temp3 = grid;
		grid = xGrid;
		xGrid = temp3;

		//Save maxHeight values.
		int temp4 = maxHeight;
		maxHeight = xMaxHeight;
		xMaxHeight = temp4;
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


