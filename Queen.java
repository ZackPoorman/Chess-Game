 

/* ---------------------------------------------------------------------------
 * A Queen can move as a Rook or as a Bishop. One way to accomplish this is
 * to define the Queen as an extension to a Rook, along with an encapsulation
 * of a Bishop.
 * ---------------------------------------------------------------------------
 */
public class Queen extends Rook {
	
	private Bishop bishop;

	/* -----------------------------------------------------------------------------
	 * The Queen occupies a single square, whether it moves like a Rook or a Bishop. 
	 * -----------------------------------------------------------------------------
	 */
	public Queen(Player player)
	{
		super(player );
		bishop = new Bishop( player, this.location  );
	}
	
	/* -----------------------------------------------------------------------------
	 * The Queen occupies a single Square, whether it moves like a Rook or a Bishop. 
	 * -----------------------------------------------------------------------------
	 */
	public void setLocation( int row, int column )
	{
		super.setLocation(row, column);
		bishop.setLocation(row, column);
	}
	
	public String type()
	{	
		return "Queen";
	}
	
	/* --------------------------------------------------------------------------------
	 * A Queen can move either as a Rook or as a Bishop.  
	 * ---------------------------------------------------------------------------------
	 */
	public boolean isValidMove(Move move, IChessPiece[][] board)
	{
		return super.isValidMove(move, board) || bishop.isValidMove(move, board);
	}
	
	public String toString()
	{
		return player().toString() + "Q " + location.toString();
	}
}

